/**
 * Created April 7, 2006
 */
package org.sa.rainbow.core;

import org.acmestudio.standalone.environment.StandaloneEnvironment;
import org.sa.rainbow.adaptation.AdaptationManagerWithScenarios;
import org.sa.rainbow.adaptation.executor.Executor;
import org.sa.rainbow.event.EventTypeFilter;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.event.IProtocolAction;
import org.sa.rainbow.event.IRainbowEventListener;
import org.sa.rainbow.event.IRainbowMessage;
import org.sa.rainbow.event.RainbowEventAdapter;
import org.sa.rainbow.gui.RainbowGUI;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.model.evaluator.ArchEvaluatorWithScenarios;
import org.sa.rainbow.model.manager.ModelManagerWithScenarios;
import org.sa.rainbow.monitor.SystemDelegate;
import org.sa.rainbow.monitor.TargetSystem;
import org.sa.rainbow.monitor.sim.SimulationRunner;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.stitch.Ohana;
import org.sa.rainbow.util.ARainbowLoggerFactory;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.Util;

/**
 * The Oracle class is a singleton class that coordinates the active components of Rainbow and provides a control GUI.
 */
public class Oracle implements IDisposable {

	public static final String NAME = "The Rainbow Oracle With Scenarios";
	public static final String JAR_NAME = "app.rainbow.oracle.jar";

	public static void main(String[] args) {
		boolean showHelp = false;
		int numTrials = 1;
		int lastIdx = args.length - 1; // cached value to avoid recomputation
		for (int i = 0; i <= lastIdx; i++) {
			if (args[i].equals("-h")) {
				showHelp = true;
			} else if (args[i].equals("-n") && i < lastIdx) { // get trial count
				numTrials = Integer.parseInt(args[++i]);
			} else if (args[i].equals("-nogui")) { // turn off GUI
				m_showGui = false;
			} else { // we don't know what this option is
				System.err.println("Unrecognized or incomplete argument " + args[i]);
				showHelp = true;
			}
			if (showHelp) {
				System.out.println("Usage:\n" + "  java [-D{key}={val}] -jar " + JAR_NAME + " [options]\n" + "\n"
						+ "  system property options {default}:\n"
						+ "    rainbow.target    name of target configuration {default}\n"
						+ "    rainbow.config    top config directory (org.sa.rainbow.config)\n" + "  options: \n"
						+ "    -h          Show this help message\n" + "    -n <num>    Number of trials to run\n"
						+ "    -nogui      Don't show the Rainbow GUI\n" + "\n"
						+ "Option defaults are defined in <rainbow.target>/rainbow.properties");
				System.exit(Rainbow.EXIT_VALUE_ABORT);
			}
		}

		// establish that we're standalone
		System.setProperty(Rainbow.PROPKEY_EXEC_STANDALONE, "true");
		// run trials (usually only once)
		for (int i = 0; i < numTrials; i++) {
			if (i > 0) {
				// wait a bit to be sure, before restarting loop
				Util.pause(IRainbowRunnable.LONG_SLEEP_TIME);
			}
			instance().m_logger.info("/========================/");
			instance().m_logger.info("|  Trial run " + (i + 1) + " of " + numTrials + "...");
			instance().m_logger.info("\\========================\\");
			Util.pause(IRainbowRunnable.LONG_SLEEP_TIME); // wait a second for GUI to start
			instance().run();
			// at this point, system terminated, scrap everything
			instance().dispose();
			Ohana.cleanup();
		}
		Util.pause(3 * IRainbowRunnable.LONG_SLEEP_TIME);
		System.exit(Rainbow.exitValue());
	}

	// Singleton instance
	private static Oracle m_instance = null;
	static boolean m_showGui = true; // allows package-level change

	public static Oracle instance() {
		if (m_instance == null) {
			m_instance = new Oracle();
		}
		return m_instance;
	}

	/** Intentional package-level visibility for testing access. */
	RainbowLogger m_logger = null;
	private RainbowGUI m_gui = null;
	private RainbowModelWithScenarios m_model = null;
	private TargetSystem m_system = null;

	private IRainbowRunnable m_modelmgr = null;
	private IRainbowRunnable m_evaluator = null;
	private IRainbowRunnable m_executor = null;
	private IRainbowRunnable m_adaptmgr = null;

	// private ILearner m_learner = null;

	/**
	 * Not for instantiation.
	 */
	private Oracle() {
		if (m_showGui)
			m_gui = new RainbowGUI();
		Rainbow.setIsMaster(true); // declare this to be the master Rainbow component
		ARainbowLoggerFactory.reset(); // activate the relevant logger factory
		Rainbow.instance().serviceManager(); // activate the RainbowServiceManager
		m_logger = RainbowLoggerFactory.logger(getClass());

		// mark the init of Rainbow
		Util.dataLogger().info(IRainbowHealthProtocol.DATA_RAINBOW_INIT);

		if (m_showGui) {
			// display GUI panel
			m_gui.display();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose() {
		m_logger.info(NAME + " terminated!");

		StandaloneEnvironment.instance().dispose(); // dispose Acme environment
		Rainbow.done();
		if (m_showGui) {
			m_gui.dispose(); // make window disappear last
			m_gui = null;
		}

		m_system = null;
		m_model = null;
		m_modelmgr = null;
		m_evaluator = null;
		m_adaptmgr = null;
		m_executor = null;
		m_instance = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#isDisposed()
	 */
	public boolean isDisposed() {
		// not strictly true (i.e., unintialized == disposed), but close enough
		return m_instance == null;
	}

	public RainbowModelWithScenarios rainbowModel() {
		if (m_model == null) {
			m_model = new RainbowModelWithScenarios();
			// initialize the model as a repository to the language module
			Ohana.instance().setModelRepository(m_model);
		}
		return m_model;
	}

	public TargetSystem targetSystem() {
		if (m_system == null) {
			if (Rainbow.inSimulation()) {
				m_system = new SimulationRunner();
			} else {
				m_system = new SystemDelegate();
			}
		}
		return m_system;
	}

	public IRainbowRunnable modelManager() {
		if ((m_modelmgr == null || m_modelmgr.isDisposed()) && !Rainbow.shouldTerminate()) {
			m_modelmgr = new ModelManagerWithScenarios();
		}
		return m_modelmgr;
	}

	public IRainbowRunnable archEvaluator() {
		if ((m_evaluator == null || m_evaluator.isDisposed()) && !Rainbow.shouldTerminate()) {
			m_evaluator = new ArchEvaluatorWithScenarios();
		}
		return m_evaluator;
	}

	public IRainbowRunnable adaptationManager() {
		if ((m_adaptmgr == null || m_adaptmgr.isDisposed()) && !Rainbow.shouldTerminate()) {
			m_adaptmgr = new AdaptationManagerWithScenarios();
		}
		return m_adaptmgr;
	}

	public IRainbowRunnable strategyExecutor() {
		if ((m_executor == null || m_executor.isDisposed()) && !Rainbow.shouldTerminate()) {
			Executor executor = new Executor();
			// register Executor with Rainbow
			Rainbow.instance().registerSysOpProvider(executor);
			Ohana.instance().setModelOperator(executor);
			m_executor = executor;
		}
		return m_executor;
	}

	/*
	 * public ILearner learner () { if (m_learner == null) { m_learner = new Learner((IRainbowLearner
	 * )adaptationEngine()); } return m_learner; }
	 */

	/**
	 * Check to make sure all threads are terminated. Used to determine whether to contineu next trial run.
	 */
	public boolean allTerminated() {
		return targetSystem().isTerminated() && modelManager().isTerminated() && archEvaluator().isTerminated()
				&& adaptationManager().isTerminated()
				// && ((IRainbowRunnable )learner()).isTerminated()
				&& strategyExecutor().isTerminated();
	}

	public void writeManagerPanel(RainbowLogger logger, String text) {
		if (m_showGui)
			m_gui.writeText(RainbowGUI.ID_MODEL_MANAGER, text);
		logger.info(text);
	}

	public void writeEvaluatorPanelSL(RainbowLogger logger, String text) {
		if (m_showGui)
			m_gui.writeTextSL(RainbowGUI.ID_ARCH_EVALUATOR, text);
		logger.info(text);
	}

	public void writeEvaluatorPanel(RainbowLogger logger, String text) {
		if (m_showGui)
			m_gui.writeText(RainbowGUI.ID_ARCH_EVALUATOR, text);
		logger.info(text);
	}

	public void writeEnginePanel(RainbowLogger logger, String text) {
		if (m_showGui)
			m_gui.writeText(RainbowGUI.ID_ADAPTATION_MANAGER, text);
		logger.info(text);
	}

	public void writeExecutorPanel(RainbowLogger logger, String text) {
		if (m_showGui)
			m_gui.writeText(RainbowGUI.ID_EXECUTOR, text);
		logger.info(text);
	}

	public void writeSystemPanel(RainbowLogger logger, String text) {
		if (m_showGui)
			m_gui.writeText(RainbowGUI.ID_TARGET_SYSTEM, text);
		logger.info(text);
	}

	public void writeTranslatorPanel(RainbowLogger logger, String text) {
		if (m_showGui)
			m_gui.writeText(RainbowGUI.ID_TRANSLATOR, text);
		logger.info(text);
	}

	public void writeEventPanel(RainbowLogger logger, String text) {
		if (m_showGui)
			m_gui.writeText(RainbowGUI.ID_EVENT_BUSES, text);
		logger.info(text);
	}

	public void writeOraclePanel(RainbowLogger logger, String text) {
		if (m_showGui)
			m_gui.writeText(RainbowGUI.ID_ORACLE_MESSAGE, text);
		logger.info(text);
	}

	/**
	 * Allows invoking run from a package-level test class
	 */
	void _runFromTester() {
		run();
	}

	private void run() {
		// - setup health listener
		IRainbowEventListener healthListener = healthListener(Util.genID(getClass()));
		Rainbow.eventService().listen(IEventService.TOPIC_RAINBOW_HEALTH, healthListener);
		// - initialize target system, but don't start yet
		TargetSystem sys = targetSystem();
		if (sys instanceof SimulationRunner) {
			((SimulationRunner) sys).setSimFile(Rainbow.property(Rainbow.PROPKEY_SIM_PATH));
		}
		// - start the executor, adaptation manager, model manager, evaluator
		strategyExecutor().start();
		adaptationManager().start();
		modelManager().start();
		archEvaluator().start();
		// ((IRainbowRunnable )learner()).start();
		// - lastly, connect to the target system OR start simulation
		sys.connect();

		// Oracle monitoring loop, checking for Rainbow termination conditions
		while (!(Rainbow.shouldTerminate() && allTerminated() && Rainbow.eventService().readyForTermination())) {
			Util.pause(IRainbowRunnable.LONG_SLEEP_TIME); // sleep a bit and check termination
			if (Rainbow.shouldTerminate() && !Ohana.isDisposed()) {
				// Stop the Stitch backend first, so that any strategy execution eventually stops
				Ohana.instance().dispose();
			}
		}
		if (!Ohana.isDisposed()) { // make sure to stop Stitch
			Ohana.instance().dispose();
		}
		// - deregister listening
		Rainbow.eventService().unlisten(healthListener);
	}

	private IRainbowEventListener healthListener(String id) {
		return new RainbowEventAdapter(id) {
			@Override
			@EventTypeFilter(eventTypes = { IProtocolAction.LOG_EVENT, IProtocolAction.LOG_TRANSLATOR })
			public void onMessage(IRainbowMessage msg) {
				IProtocolAction action = IProtocolAction.valueOf((String) msg.getProperty(IProtocolAction.ACTION));
				switch (action) {
				case LOG_EVENT:
					String txt = (String) msg.getProperty(IRainbowHealthProtocol.TEXT);
					Oracle.instance().writeEventPanel(m_logger, txt);
					break;
				case LOG_TRANSLATOR:
					txt = (String) msg.getProperty(IRainbowHealthProtocol.TEXT);
					Oracle.instance().writeTranslatorPanel(m_logger, txt);
					break;
				}
			}
		};
	}

}
