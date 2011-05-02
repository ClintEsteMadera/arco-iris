/**
 * Created April 7, 2006
 */
package org.sa.rainbow.monitor.sim;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.sa.rainbow.core.AbstractRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.core.error.NotImplementedException;
import org.sa.rainbow.monitor.TargetSystem;
import org.sa.rainbow.monitor.sim.ISimulation.PredictionMode;
import org.sa.rainbow.stitch.util.Tool;
import org.sa.rainbow.translator.effectors.IEffector;
import org.sa.rainbow.util.Util;

/**
 * A simulation system to test the Rainbow infrastructures. TargetSystem configuration comes via a configuration file.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class SimulationRunner extends AbstractRainbowRunnable implements TargetSystem, ISimulatedTargetSystem {

	/**
	 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
	 * 
	 * A simulation point gathered from the simulation config file.
	 */
	public static class SimPoint {
		public boolean visited = false;
		public int index = 0;
		public long simTime = 0L;
		public String identifier = null;
		public String value = null;
	}

	public static final String NAME = "TargetSystem Simulation Runner";

	/**
	 * Set to <code>true</code> to auto-simulate, or <code>false</code> if sim points are advanced externally.
	 */
	public static final boolean TIME_SIMULATION = true;

	private ISimulation m_sim = null;
	private int m_simindex = 0;
	private int m_simcnt = 0;
	private long m_simStartTime = 0L;
	private long m_accumTimeout = 0L;
	private long m_lastTimeout = 0L;
	private List<SimPoint> m_simpoints = null;
	private Properties m_props = null;
	private List<String> m_changeProps = null;

	/**
	 * Default constructor.
	 */
	public SimulationRunner() {
		super(NAME);
		setSleepTime(500/* ms */);

		m_simpoints = new ArrayList<SimPoint>();
		m_props = new Properties();
		m_changeProps = new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose() {
		m_simpoints.clear();
		m_props.clear();
		m_changeProps.clear();

		// null-out data members
		m_sim = null;
		m_simpoints = null;
		m_props = null;
		m_changeProps = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.TargetSystem#connect()
	 */
	public void connect() {
		// start the simulation runner
		start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.TargetSystem#getProperties()
	 */
	public Properties getProperties() {
		return m_props;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.TargetSystem#getChangedProperties()
	 */
	public Map<String, Object> getChangedProperties() {
		Map<String, Object> propMap = new HashMap<String, Object>();
		List<String> changedProps = new ArrayList<String>(m_changeProps);
		m_changeProps.clear(); // clear list of props first
		for (String prop : changedProps) {
			propMap.put(prop, m_props.get(prop));
		}
		return propMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.TargetSystem#queryProperty(java.lang.String)
	 */
	public Object queryProperty(String iden) {
		return m_props.getProperty(iden);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.TargetSystem#predictProperty(java.lang.String, long,
	 *      org.sa.rainbow.monitor.TargetSystem.StatType)
	 */
	public Object predictProperty(String iden, long dur, StatType type) {
		// if not time simulation, timed prediction doesn't have a meaning
		if (!TIME_SIMULATION) {
			return null;
		}
		// if prediction not enabled, return using current conditions
		if (!Rainbow.predictionEnabled()) {
			return Double.valueOf((String) queryProperty(iden));
		}
		Double rv = null;
		switch (type) {
		case SINGLE:
			// stop();
			// roll time forward from now to future duration and determine property
			// value by keeping track of the last set value
			long now = elapsedTime();
			long future = now + dur;
			List<SimPoint> simpts = new ArrayList<SimPoint>();
			for (int i = m_simcnt; i < m_simpoints.size() && m_simpoints.get(i).simTime <= future; ++i) {
				simpts.add(m_simpoints.get(i));
			}
			// generate prediction properties into future time and obtain property of interest
			Properties props = m_sim.predict(simpts, dur, PredictionMode.IMPERFECT);
			rv = Double.valueOf(props.getProperty(iden));
			if (m_logger.isInfoEnabled()) {
				m_logger.info(now + "ms + " + dur + ": " + iden + " = " + rv);
			}
			// start();
			return rv;
		case MEAN:
			// roll time forward from now and accumulate property value across
			// duration to derive the mean
			throw new NotImplementedException("Sorry, AVG predictedProperty() function not yet implemented.");
		case MAX: // seek for max of property in time toward future duration
			throw new NotImplementedException("Sorry, MAX predictedProperty() function not yet implemented.");
		case MIN: // seek for min of property in time toward future duration
			throw new NotImplementedException("Sorry, MIN predictedProperty() function not yet implemented.");
		}
		return rv;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.TargetSystem#findServices(java.lang.String, java.util.Map)
	 */
	public Set<?> findServices(String type, Map<String, String> filters) {
		// let's return a set of strings
		Set<String> svcNames = new HashSet<String>();

		// seek elements for which a "spare" attribute exists and states true!
		StringTokenizer eTokens = new StringTokenizer(m_props.getProperty(ISimulation.KEY_ELEMENTS), ",");
		while (eTokens.hasMoreTokens()) {
			String name = eTokens.nextToken().trim();
			String spareProp = name + ".spare";
			String typeProp = name + ".type";
			if (m_props.containsKey(spareProp) && m_props.containsKey(typeProp)) {
				if (m_props.getProperty(typeProp).equals(type) && Boolean.parseBoolean(m_props.getProperty(spareProp))) {
					// spare element encountered! and type matches
					svcNames.add(name);
				}
			}
		}
		return svcNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.TargetSystem#getEffector(java.lang.String, java.lang.String)
	 */
	public IEffector getEffector(String name, String target) {
		return IEffector.NULL_EFFECTOR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.sim.ISimulatedTargetSystem#changeProperty(java.lang.String, java.lang.Object)
	 */
	public boolean changeProperty(String iden, Object value) {
		if (m_props.containsKey(iden)) {
			try {
				//Double newV = Double.valueOf(value.toString());
				//Double oldV = Double.valueOf((String) m_props.get(iden));
				//Double diff = Math.abs(newV - oldV) / oldV;
				// if (diff > 0.005) { // report only if delta greater than 0.5%
				log(Tool.TAB + Tool.TAB + "Change prop: " + iden + " = " + value);
				// }
			} catch (NumberFormatException e) {
				log(Tool.TAB + Tool.TAB + "Change prop: " + iden + " = " + value);
			}
			m_props.setProperty(iden, value.toString());
			m_changeProps.add(iden);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.sim.ISimulatedTargetSystem#effectProperty(java.lang.String, java.lang.Object)
	 */
	public boolean effectProperty(String iden, Object value) {
		/*
		 * TODO if we can get ahold of "system response time", can use that to inform the effector time delay to
		 * simulate resource limitations
		 */
		// pause for random amount of time between 0.5 to 3 seconds
		double pause = Math.random() * 2500.0 + 501L;
		log(Tool.TAB + Tool.TAB + "Effector time delay to simulate resource limitations: " + pause);
		Util.pause((long) (pause));
		return changeProperty(iden, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#start()
	 */
	@Override
	public void start() {
		if (activeThread() == null || isTerminated()) {
			return;
		}

		switch (state()) {
		case RAW: // not yet started
			m_simStartTime = System.currentTimeMillis();
			break;
		case STOPPED:
			m_accumTimeout += System.currentTimeMillis() - m_lastTimeout;
			m_lastTimeout = 0L;
			break;
		}

		super.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#doStop()
	 */
	@Override
	protected void doStop() {
		if (activeThread() == null || state() == State.RAW || isTerminated()) {
			return;
		}

		m_lastTimeout = System.currentTimeMillis();

		super.doStop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#doTerminate()
	 */
	@Override
	protected void doTerminate() {
		if (m_sim != null) {
			m_sim.terminate();
			// we're terminating in the runnable's thread, safe to signalTerminate.
			// Rainbow.signalTerminate();
		}

		super.doTerminate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#log(java.lang.String)
	 */
	@Override
	protected void log(String txt) {
		Oracle.instance().writeSystemPanel(m_logger, txt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#runAction()
	 */
	@Override
	protected void runAction() {
		m_logger.info("------------- Running ZNewsSim at " + elapsedTime() + "------------");

		if (m_simcnt >= m_simpoints.size()) {
			return;
		}

		SimPoint simpoint = m_simpoints.get(m_simcnt);
		if (TIME_SIMULATION && elapsedTime() > simpoint.simTime && m_simindex < simpoint.index) {
			++m_simindex; // advance sim index automatically in TIME simulation
		}
		while (simpoint.index == m_simindex) {
			if (simpoint.visited) {
				continue;
			}
			if (TIME_SIMULATION && elapsedTime() < simpoint.simTime) {
				break; // not yet time
			}

			String msg = "[" + elapsedTime() + ":sim." + simpoint.index + "] ";
			simpoint.visited = true; // mark sim point as processed
			if (simpoint.identifier.equals("sim") && simpoint.value.equals("terminate")) {
				// done with simulation
				msg += "Simulation terminated!";
				terminate();
				Rainbow.signalTerminate();
				break;
			} else if (simpoint.identifier.equals("sim") && simpoint.value.equals("reset")) {
				m_sim.reset();
			} else {
				msg += "changing property " + simpoint.identifier + " == " + simpoint.value;
				changeProperty(simpoint.identifier, simpoint.value);
			}
			++m_simcnt;
			log(msg);
			simpoint = m_simpoints.get(m_simcnt);
		}
		// finally, simulate 1 step for TIME_SIMULATION, and 3 otherwise
		if (TIME_SIMULATION) {
			m_sim.simulate(1);
		} else {
			m_sim.simulate(3);
		}
	}

	public void setSimFile(String filename) {
		try {
			File simulationFileFullPath = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), filename);
			
			m_logger.info("Loading simulation file from " + simulationFileFullPath.getCanonicalPath());
			
			// store for later reloading, if needed
			m_props.load(new FileInputStream(simulationFileFullPath));
			String incFile = m_props.getProperty("sim.include");
			if (incFile != null) {
				// let's grab the include file and then reload props
				m_props.load(new FileInputStream(Util.getRelativeToPath(Rainbow.instance().getTargetPath(), incFile)));
				// then reload the original property file to supersede values
				m_props.load(new FileInputStream(simulationFileFullPath));
			}
		} catch (FileNotFoundException e) {
			m_logger.error("Sim property loading failed!", e);
			return;
		} catch (IOException e) {
			m_logger.error("Sim property loading failed!", e);
			return;
		}

		// check for rainbow property settings
		final String RBKEY = "rainbow.";
		for (Map.Entry<Object, Object> entry : m_props.entrySet()) {
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			if (key.startsWith(RBKEY)) { // set rainbow property
				String rainbowKey = key.substring(RBKEY.length());
				Rainbow.instance().setProperty(rainbowKey, val);
			}
		}

		// instantiate the sim class
		String simClazz = m_props.getProperty("sim.class");
		Throwable et = null;
		try {
			Class<?> simClass = Class.forName(simClazz);
			Class<?>[] constParams = new Class[2];
			constParams[0] = ISimulatedTargetSystem.class;
			constParams[1] = Properties.class;
			Object[] constArgs = new Object[2];
			constArgs[0] = this;
			constArgs[1] = m_props;
			Constructor<?> cons = simClass.getConstructor(constParams);
			m_sim = (ISimulation) cons.newInstance(constArgs);
		} catch (ClassNotFoundException e) {
			et = e;
		} catch (InstantiationException e) {
			et = e;
		} catch (IllegalAccessException e) {
			et = e;
		} catch (SecurityException e) {
			et = e;
		} catch (NoSuchMethodException e) {
			et = e;
		} catch (IllegalArgumentException e) {
			et = e;
		} catch (InvocationTargetException e) {
			et = e;
		} finally {
			if (et != null) {
				m_logger.error("Sim class instantiation failed!", et);
				return;
			} else {
				if (m_sim == null) {
					m_logger.error("Could not instantiate simulation class " + simClazz, et);
					return;
				}
			}
		}

		// extract the sim points
		int simSize = Integer.parseInt(m_props.getProperty("sim.size"));
		for (int i = 0; i < simSize; i++) {
			String sims = m_props.getProperty("sim." + i);
			if (sims == null) {
				continue; // this sim point wasn't defined
			}
			StringTokenizer tokens = new StringTokenizer(sims, ",");
			while (tokens.hasMoreTokens()) {
				String simToken = tokens.nextToken().trim();
				StringTokenizer simParts = new StringTokenizer(simToken, ":");
				SimPoint simpoint = new SimPoint();
				simpoint.index = i;
				simpoint.simTime = Long.parseLong(simParts.nextToken().trim());
				simpoint.identifier = simParts.nextToken().trim();
				simpoint.value = simParts.nextToken().trim();
				m_simpoints.add(simpoint);
			}
		}
	}

	public long elapsedTime() {
		return System.currentTimeMillis() - m_simStartTime - m_accumTimeout;
	}

	public void nextSimPoint() {
		if (m_simcnt >= m_simpoints.size()) {
			return;
		}

		if (state() == State.STARTED) {
			doStop(); // pause simulation
		}

		SimPoint simpoint = m_simpoints.get(m_simcnt);
		if (simpoint != null && m_simindex < simpoint.index) {
			// only advance if simpoints have been processed
			m_simindex++;
			// refresh states
			m_sim.refresh();
		}

		if (state() == State.STOPPED) {
			start(); // resume simulation
		}
	}

}
