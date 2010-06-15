/**
 * Created January 14, 2007.
 */
package org.sa.rainbow.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.sa.rainbow.RainbowConstants;
import org.sa.rainbow.adaptation.IGenericArchOperators;
import org.sa.rainbow.core.error.RainbowAbortException;
import org.sa.rainbow.event.EventServiceManager;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.model.EffectorDescription;
import org.sa.rainbow.model.GaugeDescription;
import org.sa.rainbow.model.Model;
import org.sa.rainbow.model.ProbeDescription;
import org.sa.rainbow.model.UtilityPreferenceDescription;
import org.sa.rainbow.service.IServiceRegistrant;
import org.sa.rainbow.translator.gauges.IGauge;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.Util;
import org.sa.rainbow.util.YamlUtil;

/**
 * The Main Rainbow class, a singleton object that contains references to
 * important services in the Rainbow Framework.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class Rainbow implements RainbowConstants {
	/**
	 * States used to track the target deployment environment of Rainbow component. 
	 *
	 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
	 */
	public static enum Environment {
		/** We don't yet know what deployment environment. */
		UNKNOWN,
		/** We're in a Linux environment. */
		LINUX,
		/** We're in a Cygwin environment. */
		CYGWIN,
		/** We're in a Mac FreeBSD environment. */
		MAC,
		/** We're in a Windows environment without Cygwin. */
		WINDOWS
	};

	/**
	 * States used to help the Rainbow daemon process determine what to do
	 * after this Rainbow component exits.
	 *
	 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
	 */
	public static enum ExitState {
		/** Completely clear out (daemon dies) after the Rainbow component exits (default). */
		DESTRUCT,
		/** Restart the Rainbow component after exits. */
		RESTART,
		/** After the Rainbow component exits, sleep and await awake command to restart Rainbow. */
		SLEEP,
		/** Abort of operation. */
		ABORT;

		public static ExitState parseState (int val) {
			ExitState st = ExitState.DESTRUCT;
			switch (val) {
			case EXIT_VALUE_DESTRUCT:
				st = ExitState.DESTRUCT;
				break;
			case EXIT_VALUE_RESTART:
				st = ExitState.RESTART;
				break;
			case EXIT_VALUE_SLEEP:
				st = ExitState.SLEEP;
				break;
			case EXIT_VALUE_ABORT:
				st = ExitState.ABORT;
				break;
			}
			return st;
		}

		public int exitValue () {
			int ev = 0;
			switch (this) {
			case DESTRUCT:
				ev = EXIT_VALUE_DESTRUCT;
				break;
			case RESTART:
				ev = EXIT_VALUE_RESTART;
				break;
			case SLEEP:
				ev = EXIT_VALUE_SLEEP;
				break;
			case ABORT:
				ev = EXIT_VALUE_ABORT;
				break;
			}
			return ev;
		}
	}

	/** The thread name */
	public static final String NAME = "Rainbow Runtime Infrastructure";
	/** Property name for the property to explicitly set so Rainbow knows it
	 *  can invoke System.exit when done */
	public static final String PROPKEY_EXEC_STANDALONE = "rainbow.exec.standalone";

	/** Singleton instance of Rainbow */
	private static Rainbow m_instance = null;
	/** Exit status that Rainbow would report when it exits, default to sleeping. */
	private static ExitState m_exitState = ExitState.SLEEP;

	private Environment m_env = Environment.UNKNOWN;
	private boolean m_shouldTerminate = false;
	private boolean m_isDone = false;
	private boolean m_isMaster = false;
	private boolean m_predictionEnabled = false;
	private Properties m_props = null;
	private File m_basePath = null;
	private File m_targetPath = null;

	private ThreadGroup m_threadGroup = null;
	private Thread m_thread = null;
	private Map<String,IGauge> m_id2Gauge = null;
	private RainbowServiceManager m_serviceMgr = null;
	private int m_errorCnt = 0;  // a basic internal error tracker
	private IGenericArchOperators m_sysOpProvider = null;
	private UtilityPreferenceDescription m_prefDesc = null;
	private GaugeDescription m_gaugeDesc = null;
	private EffectorDescription m_effectorDesc = null;
	private ProbeDescription m_probeDesc = null;


	/**
	 * Not for instantiation
	 */
	private Rainbow() {
		m_threadGroup = new ThreadGroup(NAME);
		// so that group would be auto-destroyed when last thread stops
		m_threadGroup.setDaemon(true);
		m_id2Gauge = new HashMap<String,IGauge>();
		m_props = new Properties();

		establishPaths();
		loadConfigFiles();
		canonicalizeHost2IPs();
		evalPropertySubstitution();  // do prop value substitutions AFTER all loaded
		// set booleans from config file
		if (m_props.containsKey(PROPKEY_ENABLE_PREDICTION)) {
			m_predictionEnabled = Boolean.valueOf((String )m_props.get(PROPKEY_ENABLE_PREDICTION));
		}

		Runnable checkDoneRunnable = new Runnable() {
			private Beacon memTimer = new Beacon(10*IRainbowRunnable.LONG_SLEEP_TIME);
			public void run() {
				final boolean simulation = m_props.get(PROPKEY_SIM_PATH) != null;
				memTimer.mark();
				Thread currentThread = Thread.currentThread();
				while (m_thread == currentThread) {
					try {
						Thread.sleep(IRainbowRunnable.LONG_SLEEP_TIME);
					} catch (InterruptedException e) {
						// intentional ignore
					}
					if (isDone()) {
						dispose();
					} else {
						if (memTimer.periodElapsed()) {
							Util.reportMemUsage();
							memTimer.mark();
						}
					}
				}
				if (isProcessStandalone() && !simulation) {
					// wait a little before exiting
					Util.pause(3*IRainbowRunnable.LONG_SLEEP_TIME);
					System.exit(Rainbow.exitValue());
				}
			}
		};
		m_thread = new Thread(checkDoneRunnable, NAME);
		m_thread.start();
	}

	public static Rainbow instance () {
		if (m_instance == null) {
			m_instance = new Rainbow();
		}
		return m_instance;
	}
	public static boolean isInstantiated () {
		return m_instance != null;
	}

	public static boolean isDone () {
		return instance().m_isDone;
	}
	/**
	 * Notifies the Rainbow Runtime object itself to dispose.
	 * Rainbow parts know to terminate
	 */
	public static void done () {
		instance().m_isDone = true;
	}
	/**
	 * Returns whether the Rainbow runtime infrastructure should terminate. 
	 * @return <code>true</code> if Rainbow should terminate, <code>false</code> otherwise.
	 */
	public static boolean shouldTerminate () {
		return instance().m_shouldTerminate;
	}

	/**
	 * Sets the shouldTerminate flag so that the Rainbow Runtime Infrastructure
	 * parts know to terminate.  This method is intended primarily for
	 * {@link org.sa.rainbow.core.Oracle <code>Oracle</code>}, but may be used
	 * by the UpdateService to signal termination.
	 */
	public static void signalTerminate () {
		if (!instance().m_shouldTerminate) {  // log once the signalling to terminate
			RainbowLoggerFactory.logger(Rainbow.class).info("*** Signalling Terminate ***");
		}
		instance().m_shouldTerminate = true;
	}
	public static void signalTerminate (ExitState exitState) {
		setExitState(exitState);
		signalTerminate();
	}

	public static Environment environment () {
		if (instance().m_env == Environment.UNKNOWN) {
			instance().m_env = Environment.valueOf(property(PROPKEY_DEPLOYMENT_ENVIRONMENT).toUpperCase());
		}
		return instance().m_env;
	}
	public static boolean isMaster () {
		return instance().m_isMaster;
	}
	/**
	 * Sets whether the current running Rainbow is the master, which is <code>false</code>
	 * by default.  Only the {@link org.sa.rainbow.core.Oracle <code>Oracle</code>}
	 * gets to affect this state.
	 * @param b  the new state of whether this Rainbow is master
	 */
	/*package*/ static void setIsMaster (boolean b) {
		instance().m_isMaster = b;
	}
	public static boolean isProcessStandalone () {
		return System.getProperty(PROPKEY_EXEC_STANDALONE) != null;
	}
	/**
	 * Returns whether target system is simulated.
	 * @return boolean  <code>true</code> if target system is simulated,
	 *     as indicated by PROKKEY_SIM_PATH property; <code>false</code> otherwise.
	 */
	public static boolean inSimulation () {
		return Rainbow.property(PROPKEY_SIM_PATH) != null;
	}
	
	public static boolean predictionEnabled () {
		return instance().m_predictionEnabled;
	}
	public static long utilityPredictionDuration () {
		String vStr = Rainbow.property(PROPKEY_UTILITY_PREDICTION_DURATION);
		if (vStr == null) {
			return 0L;
		} else {
			return Long.valueOf(vStr);
		}
	}

	/**
	 * Returns the exit value given the Rainbow exit state; this is stored
	 * statically since it would only be used once.
	 * @return int  the exit value to return on System exit.
	 */
	public static int exitValue () {
		return m_exitState.exitValue();
	}
	public static void setExitState (ExitState state) {
		m_exitState = state;
	}

	public static IEventService eventService () {
		return EventServiceManager.instance();
	}

	/**
	 * Retrieves a property identified by key.
	 * @param key  key of property to retrieve
	 * @return String  value of property requested
	 */
	public static String property (String key) {
		return instance().m_props.getProperty(key);
	}
	/**
	 * Retrieves a property identified by key, or return the supplied default value.
	 * @param key  key of property to retrieve
	 * @param defaultVal  the default value to return if property is null
	 * @return String  value of property requested
	 */
	public static String property (String key, String defaultVal) {
		String val = property(key);
		return (val == null) ? defaultVal : val;
	}

	public static void registerGauge(IGauge gauge) {
		instance().m_id2Gauge.put(gauge.id(), gauge);
	}
	public static IGauge lookupGauge(String id) {
		return instance().m_id2Gauge.get(id);
	}


	private void dispose () {
		EventServiceManager.dispose();
		if (m_serviceMgr != null && !m_serviceMgr.isDisposed()) {
			m_serviceMgr.dispose();
		}

		Util.dataLogger().info(IRainbowHealthProtocol.DATA_RAINBOW_TERM);
		if (!m_isMaster && exitValue() != EXIT_VALUE_DESTRUCT
				&& exitValue() != EXIT_VALUE_SLEEP) {
			RainbowLoggerFactory.logger(getClass()).info("Delegate waiting a little to restart...");
			Util.pause(1000);
		}
		String roleStr = " - " + (m_isMaster ? "Master" : "Delegate");
		RainbowLoggerFactory.logger(getClass()).fatal("****** " + NAME + roleStr + " terminated! ******");

		m_props.clear();

		// null-out the data members
		m_thread = null;
		m_threadGroup = null;
		m_serviceMgr = null;
		m_props = null;
		m_prefDesc = null;
		m_gaugeDesc = null;
		m_effectorDesc = null;
		// finally, eliminate singleton instance
		m_isDone = false;
		m_instance = null;
	}

	public File getBasePath () {
		return m_basePath;
	}
	public File getTargetPath () {
		return m_targetPath;
	}

	public ThreadGroup getThreadGroup () {
		return m_threadGroup;
	}

	public void loadProperties (InputStream in) {
		try {
			m_props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setProperty (String key, String val) {
		m_props.setProperty(key, val);
	}
	/**
	 * Substitute property values containing the pattern ${x} with the value
	 * that is mapped to the key "x."
	 */
	public void evalPropertySubstitution () {
		for (Object kObj : m_props.keySet()) {
			String key = (String )kObj;
			String val = m_props.getProperty(key);
			while (val.contains(Util.TOKEN_BEGIN)) {
				m_props.setProperty(key, Util.evalTokens(val, m_props));
				val = m_props.getProperty(key);
			}
		}
	}

	public void registerService (String cmd, IServiceRegistrant reg) {
		serviceManager().registerCommand(cmd, reg, false);
	}
	public void registerAuthService (String cmd, IServiceRegistrant reg) {
		serviceManager().registerCommand(cmd, reg, true);
	}
	public RainbowServiceManager serviceManager () {
		if (m_serviceMgr == null) {
			m_serviceMgr = new RainbowServiceManager();
			m_serviceMgr.start();
		}
		return m_serviceMgr;
	}

	public void registerSysOpProvider (IGenericArchOperators opProvider) {
		m_sysOpProvider = opProvider;
	}
	public IGenericArchOperators sysOpProvider () {
		if (m_sysOpProvider == null) return IGenericArchOperators.NULL_OP;
		return m_sysOpProvider;
	}

	/**
	 * Returns the utility preference description parsed from file by Yaml,
	 * lazily initialized.
	 * @return UtilityPreferenceDescription  the parsed utility preference description
	 */
	public UtilityPreferenceDescription preferenceDesc () {
		if (m_prefDesc == null) {
			m_prefDesc = YamlUtil.loadUtilityPrefs();
		}
		return m_prefDesc;
	}
	/**
	 * Returns the gauge specification parsed from file by Yaml, lazily initialized.
	 * @param model  the RainbowModel to use for property lookup when acquiring Gauge description
	 * @return GaugeDescription  the parsed gauge spec
	 */
	public GaugeDescription gaugeDesc () {
		if (m_gaugeDesc == null) {
			m_gaugeDesc = YamlUtil.loadGaugeSpecs(null);
		}
		return m_gaugeDesc;
	}
	public void loadGaugeDesc (Model model) {
		if (m_gaugeDesc == null) {
			m_gaugeDesc = YamlUtil.loadGaugeSpecs(model);
		}
	}
	/**
	 * Returns the effector description parsed from file by Yaml, lazily initialized.
	 * @return EffectorDescription  the parsed effector description
	 */
	public EffectorDescription effectorDesc () {
		if (m_effectorDesc == null) {
			m_effectorDesc = YamlUtil.loadEffectorDesc();
		}
		return m_effectorDesc;
	}
	/**
	 * Returns the probe description parsed from file by Yaml, lazily initialized.
	 * @return ProbeDescription  the parsed probe description
	 */
	public ProbeDescription probeDesc () {
		if (m_probeDesc == null) {
			m_probeDesc = YamlUtil.loadProbeDesc();
		}
		return m_probeDesc;
	}

	/**
	 * Increments the Rainbow error counter.  Above a error-count threshold,
	 * Rainbow will terminate.
	 * TODO: Before terminating due to exceeding error threshold, should alarm User somehow!
	 */
	public void tallyError () {
		++m_errorCnt;

		if (m_errorCnt > MAX_ERROR_CNT) {  // self-destruct
			System.err.println(NAME + " has experienced " + m_errorCnt + " internal errors, which is more than it can take... self-destruct!!");
			signalTerminate(ExitState.ABORT);
			m_shouldTerminate = true;  // in case signalTerminate doesn't succeed
		}
	}
	/**
	 * Resets the error count, useful for indicating that we're in a known good state.
	 */
	public void resetErrorCount () {
		m_errorCnt = 0;
	}

	/**
	 * Determines and configure the paths to the Rainbow base installation and
	 * the target configuration files.
	 */
	private void establishPaths() {
		String binPath = System.getProperty(PROPKEY_BIN_PATH, RAINBOW_BIN_DIR);
		String cfgPath = System.getProperty(PROPKEY_CONFIG_PATH, RAINBOW_CONFIG_PATH);
		String tgtPath = System.getProperty(PROPKEY_TARGET_NAME, DEFAULT_TARGET_NAME);
		m_props.setProperty(PROPKEY_BIN_PATH, binPath);
		m_props.setProperty(PROPKEY_CONFIG_PATH, cfgPath);
		m_props.setProperty(PROPKEY_TARGET_NAME, tgtPath);
		m_basePath = Util.computeBasePath(cfgPath);
		if (m_basePath == null) {
			throw new RainbowAbortException("Configuration path " + cfgPath + " NOT found, bailing!");
		}
		// establish config target path based on supplied target, store it
		m_targetPath = Util.getRelativeToPath(m_basePath, tgtPath);
		try {
			m_props.setProperty(PROPKEY_TARGET_PATH, Util.unifyPath(m_targetPath.getCanonicalPath()));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			if (m_targetPath == null) {
				throw new RainbowAbortException("Target configuration '" + tgtPath + "' NOT found, bailing!");
			}
		}
	}

	/**
	 * Determine and load the appropriate sequence of Rainbow's primary configs.
	 */
	private void loadConfigFiles() {
		System.out.println("Rainbow config path: " + m_targetPath);
		computeHostSpecificConfig();
		String cfgFile = m_props.getProperty(PROPKEY_CONFIG_FILE, DEFAULT_CONFIG_FILE);
		List<String> cfgFiles = new ArrayList<String>();
		if (!cfgFile.equals(DEFAULT_CONFIG_FILE)) {  // common config loads first
			cfgFiles.add(DEFAULT_CONFIG_FILE);
		}
		cfgFiles.add(cfgFile);
		System.out.println("Loading Rainbow config file: " + Arrays.toString(cfgFiles.toArray()));
		// load sequence of target rainbow config file(s)
		for (String cfg : cfgFiles) {
			try {
				FileInputStream pfIn = new FileInputStream(Util.getRelativeToPath(m_targetPath, cfg));
				m_props.load(pfIn);
				pfIn.close();  // make sure to close file
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Sanitizes the master, deployment, and all target location hostnames to
	 * their IP addresses. This method does value substitution for the master
	 * and deployment hosts.
	 */
	private void canonicalizeHost2IPs() {
		String masterLoc = m_props.getProperty(PROPKEY_MASTER_LOCATION);
		masterLoc = Util.evalTokens(masterLoc, m_props);
		try {
			masterLoc = InetAddress.getByName(masterLoc).getHostAddress();
			m_props.setProperty(PROPKEY_MASTER_LOCATION, masterLoc);
		} catch (UnknownHostException e) {  // complain, but leave master IP as is
			System.err.println("Master Location " + masterLoc + " could NOT be resolved to IP, using the given name!");
			e.printStackTrace();
		}
		String deployLoc = m_props.getProperty(PROPKEY_DEPLOYMENT_LOCATION);
		deployLoc = Util.evalTokens(deployLoc, m_props);
		try {
			deployLoc = InetAddress.getByName(deployLoc).getHostAddress();
			m_props.setProperty(PROPKEY_DEPLOYMENT_LOCATION, deployLoc);
		} catch (UnknownHostException e) {
			System.err.println("Deployment Location " + masterLoc + " could NOT be resolved to IP, using the given name!");
			e.printStackTrace();
		}
		if (m_props.getProperty(PROPKEY_SIM_PATH) == null) {  // must NOT call inSimulation
			int cnt = Integer.parseInt(m_props.getProperty(Rainbow.PROPKEY_TARGET_LOCATION + Util.SIZE_SFX));
			for (int i=0; i < cnt; ++i) {
				String propName = Rainbow.PROPKEY_TARGET_LOCATION + Util.DOT + i;
				String hostLoc = m_props.getProperty(propName);
				hostLoc = Util.evalTokens(hostLoc, m_props);
				try {
					hostLoc = InetAddress.getByName(hostLoc).getHostAddress();
					m_props.setProperty(propName, hostLoc);
				} catch (UnknownHostException e) {
					System.err.println("Target Host Location " + hostLoc + " could NOT be resolved to IP, using the given name!");
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Uses hostname on which this Rainbow component resides to determine
	 * if a host-specific Rainbow config file exists.  Sets the config file name
	 * to point to that file if yes.  The attempts, in order, are:
	 * <ol>
	 * <li> lowercased OS-known (remebered) localhost name
	 * <li> first segment (before first dot) of the remebered hostname
	 * <li> IP number
	 * <li> lowercased canonical hostname
	 * <li> first segment of the canonical
	 * </ol>
	 */
	private void computeHostSpecificConfig () {
		List<String> triedHosts = new ArrayList<String>();
		try {
			InetAddress ia = InetAddress.getLocalHost();
			// check with the remembered hostname (preferred)
			String hostname = ia.getHostName().toLowerCase();
			triedHosts.add(hostname);
			if (checkSetConfig(hostname)) return;
			// try part before first dot
			int dotIdx = hostname.indexOf(Util.DOT);
			if (dotIdx > -1) {
				hostname = hostname.substring(0, dotIdx);
				if (! triedHosts.contains(hostname)) {
					triedHosts.add(hostname);
					if (checkSetConfig(hostname)) return;
				}
			}
			// then try IP number
			hostname = ia.getHostAddress();
			if (! triedHosts.contains(hostname)) {
				triedHosts.add(hostname);
				if (checkSetConfig(hostname)) return;
			}
			// otherwise try canonical hostname
			hostname = ia.getCanonicalHostName().toLowerCase();
			if (! triedHosts.contains(hostname)) {
				triedHosts.add(hostname);
				if (checkSetConfig(hostname)) return;
			}
			// finally, try first part of canonical
			dotIdx = hostname.indexOf(Util.DOT);
			if (dotIdx > -1) {
				hostname = hostname.substring(0, dotIdx);
				if (! triedHosts.contains(hostname)) {
					triedHosts.add(hostname);
					if (checkSetConfig(hostname)) return;
				}
			}
			System.out.println("N.B.: Unable to find host-specific property file! tried: " + Arrays.toString(triedHosts.toArray()));
		} catch (UnknownHostException e) {
			// leave config file as is
		}
	}

	/**
	 * @param hostname
	 */
	private boolean checkSetConfig (String hostname) {
		boolean good = false;
		String cfgFileName = Rainbow.CONFIG_FILE_TEMPLATE.replace(CONFIG_FILE_STUB_NAME, hostname);
		if (Util.getRelativeToPath(m_targetPath, cfgFileName).exists()) {
			m_props.setProperty(Rainbow.PROPKEY_CONFIG_FILE, cfgFileName);
			good = true;
		}
		return good;
	}

}
