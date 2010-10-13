package org.sa.rainbow.adaptation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Level;
import org.sa.rainbow.adaptation.executor.Executor;
import org.sa.rainbow.core.AbstractRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.model.UtilityPreferenceDescription.UtilityAttributes;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.stitch.core.Strategy;
import org.sa.rainbow.stitch.core.UtilityFunction;
import org.sa.rainbow.stitch.visitor.Stitch;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.StopWatch;
import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.scenario.SelfHealingConfigurationManager;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.selfhealing.ScenarioBrokenDetector;
import ar.uba.dc.thesis.selfhealing.ScenarioBrokenDetector4CurrentSystemState;
import ar.uba.dc.thesis.selfhealing.ScenarioBrokenDetector4StrategyScoring;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

/**
 * The Rainbow Adaptation Engine... <br>
 * NOTE: This class is based on the original <code>AdaptationManager</code> code since it is final and thus, it cannot
 * be extended.<br>
 */
public class AdaptationManagerWithScenarios extends AbstractRainbowRunnable {

	public enum Mode {
		SERIAL, MULTI_PRONE
	};

	private static final int RAINBOW_SOLUTION_WEIGHT = NumberUtils.INTEGER_ZERO;

	private static final int SCENARIOS_BASED_SOLUTION_WEIGHT = NumberUtils.INTEGER_ONE;

	public static final String NAME = "Rainbow Adaptation Manager With Scenarios";

	public static final double FAILURE_RATE_THRESHOLD = 0.95;

	public static final double MIN_UTILITY_THRESHOLD = 0.40;

	private static double m_minUtilityThreshold = 0.0;

	public static final long FAILURE_EFFECTIVE_WINDOW = 2000 /* ms */;

	public static final long FAILURE_WINDOW_CHUNK = 1000 /* ms */;

	/**
	 * The prefix to be used by the strategy which takes a "leap" by achieving a similar adaptation that would have
	 * taken multiple increments of the non-leap version, but at a potential "cost" in non-dire scenarios.
	 */
	public static final String LEAP_STRATEGY_PREFIX = "Leap-";
	/**
	 * The prefix to represent the corresponding multi-step strategy of the leap-version strategy.
	 */
	public static final String MULTI_STRATEGY_PREFIX = "Multi-";

	protected static RainbowLogger m_logger = null;

	private static List<SelfHealingScenario> currentBrokenScenarios;

	private static ScenarioBrokenDetector4CurrentSystemState scenarioBrokenDetector4CurrentSystemState;

	private final SelfHealingConfigurationManager selfHealingConfigurationManager;

	private final Mode m_mode = Mode.SERIAL;

	private RainbowModelWithScenarios m_model;

	private boolean m_adaptNeeded = false; // treat as synonymous with constraint being violated

	private boolean m_adaptEnabled = true; // by default, we adapt

	private List<Stitch> m_repertoire;

	private SortedMap<String, UtilityFunction> m_utils;

	private List<Strategy> m_pendingStrategies;

	// track history
	private String m_historyTrackUtilName;

	private Map<String, int[]> m_historyCnt;

	private Map<String, Beacon> m_failTimer;

	/**
	 * Default constructor.
	 */
	public AdaptationManagerWithScenarios(SelfHealingConfigurationManager selfHealingConfigurationManager) {
		super(NAME);
		m_logger = RainbowLoggerFactory.logger(getClass());
		scenarioBrokenDetector4CurrentSystemState = Oracle.instance().scenarioBrokenDetector4CurrentSystemState();
		currentBrokenScenarios = new ArrayList<SelfHealingScenario>();
		this.selfHealingConfigurationManager = selfHealingConfigurationManager;
		this.m_model = (RainbowModelWithScenarios) Oracle.instance().rainbowModel();
		this.m_utils = new TreeMap<String, UtilityFunction>();
		this.m_pendingStrategies = new ArrayList<Strategy>();
		this.m_historyTrackUtilName = Rainbow.property(Rainbow.PROPKEY_TRACK_STRATEGY);
		if (this.m_historyTrackUtilName != null) {
			this.m_historyCnt = new HashMap<String, int[]>();
			this.m_failTimer = new HashMap<String, Beacon>();
		}
		String thresholdStr = Rainbow.property(Rainbow.PROPKEY_UTILITY_MINSCORE_THRESHOLD);
		if (thresholdStr == null) {
			m_minUtilityThreshold = MIN_UTILITY_THRESHOLD;
		} else {
			m_minUtilityThreshold = Double.valueOf(thresholdStr);
		}

		for (String k : Rainbow.instance().preferenceDesc().utilities.keySet()) {
			UtilityAttributes ua = Rainbow.instance().preferenceDesc().utilities.get(k);
			UtilityFunction uf = new UtilityFunction(k, ua.label, ua.mapping, ua.desc, ua.values);
			m_utils.put(k, uf);
		}

		this.m_repertoire = Oracle.instance().stitchLoader().getParsedStitches();
	}

	public static boolean isConcernStillBroken(String concernString) {
		try {
			Concern concern = Concern.valueOf(concernString);
			doLog(Level.INFO, "Is Concern " + concern + " Still Broken?");

			boolean result = false;
			for (SelfHealingScenario scenario : currentBrokenScenarios) {
				if (scenario.getConcern().equals(concern)
						&& scenarioBrokenDetector4CurrentSystemState.isBroken(scenario)) {
					result = true;
					break;
				}
			}
			doLog(Level.INFO, "Concern " + concern + (result == true ? " Still Broken!" : " Not Broken Anymore!!!"));
			return result;
		} catch (NullPointerException e) {
			doLog(Level.ERROR, "Concern not specified");
			throw e;
		} catch (IllegalArgumentException e) {
			doLog(Level.ERROR, "Concern " + concernString + " does not exist");
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose() {
		for (Stitch stitch : m_repertoire) {
			stitch.dispose();
		}
		m_repertoire.clear();
		m_utils.clear();
		m_pendingStrategies.clear();
		if (m_historyTrackUtilName != null) {
			m_historyCnt.clear();
			m_failTimer.clear();
			m_historyCnt = null;
			m_failTimer = null;
		}

		// null-out data members
		m_repertoire = null;
		m_utils = null;
		m_pendingStrategies = null;
		m_historyTrackUtilName = null;
		m_model = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#log(java.lang.String)
	 */
	@Override
	protected void log(String txt) {
		doLog(Level.INFO, txt);
	}

	protected static void doLog(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEnginePanel(m_logger, level, txt, t);
	}

	public boolean adaptationEnabled() {
		return m_adaptEnabled;
	}

	public void setAdaptationEnabled(boolean b) {
		m_adaptEnabled = b;
	}

	/**
	 * Marks a flag to trigger the Adaptation Engine to go to work finding a repair.
	 */
	public void triggerAdaptation() {
		m_adaptNeeded = true;
	}

	public boolean adaptationInProgress() {
		return m_adaptNeeded;
	}

	/**
	 * Removes a Strategy from the list of pending strategies, marking it as being completed (doesn't incorporate
	 * outcome).
	 * 
	 * @param strategy
	 *            the strategy to mark as being executed.
	 */
	public void markStrategyExecuted(Strategy strategy) {
		if (m_pendingStrategies.contains(strategy)) {
			m_pendingStrategies.remove(strategy);
			doLog(Level.TRACE, "Strategy " + strategy.getName() + " outcome: " + strategy.outcome());
			String s = strategy.getName() + ";" + strategy.outcome();
			Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_STRATEGY + s);
			tallyStrategyOutcome(strategy);
		}
		if (m_pendingStrategies.size() == 0) {
			Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_END);
			// reset adaptation flags
			m_adaptNeeded = false;
			m_model.clearConstraintViolated();
		}
	}

	/**
	 * Computes instantaneous utility of target system given current conditions.
	 * 
	 * @return double the instantaneous utility of current conditions
	 */
	public double computeSystemInstantUtility() {
		Map<String, Double> weights = Rainbow.instance().preferenceDesc().weights.get(Rainbow
				.property(Rainbow.PROPKEY_SCENARIO));
		double[] conds = new double[m_utils.size()];
		int i = 0;
		double score = 0.0;
		for (String k : new ArrayList<String>(m_utils.keySet())) {
			double v = 0.0;
			// find the applicable utility function
			UtilityFunction u = m_utils.get(k);
			// add attribute value from current condition to accumulated agg value
			Object condVal = m_model.getProperty(u.mapping());
			if (condVal != null && condVal instanceof Double) {
				if (m_logger.isTraceEnabled())
					doLog(Level.TRACE, "Avg value of prop: " + u.mapping() + " == " + condVal);
				conds[i] = ((Double) condVal).doubleValue();
				v += conds[i];
			}
			// now compute the utility, apply weight, and accumulate to sum
			if (weights.containsKey(k)) { // but only if weight is defined
				score += weights.get(k) * u.f(v);
			}
		}
		return score;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#runAction()
	 */
	@Override
	protected void runAction() {
		if (m_adaptEnabled && m_adaptNeeded) {
			if ((m_mode == Mode.SERIAL && m_pendingStrategies.size() == 0) || m_mode == Mode.MULTI_PRONE) {
				// in serial mode, only do adaptation if no strategy is pending
				// in multi-prone mode, just do adaptation
				Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_BEGIN);
				doAdaptation();
			}
		}
	}

	/**
	 * For JUnit testing, used to set a stopwatch object used to time duration.
	 */
	StopWatch _stopWatchForTesting = null;

	/**
	 * For JUnit testing, allows fetching the strategy repertoire. NOT for public use!
	 * 
	 * @return list of Stitch objects loaded at initialization from stitch file.
	 */
	List<Stitch> _retrieveRepertoireForTesting() {
		return m_repertoire;
	}

	/**
	 * For JUnit testing, allows fetching the utility objects. NOT for public use!
	 * 
	 * @return map of utility identifiers to functions.
	 */
	Map<String, UtilityFunction> _retrieveUtilityProfilesForTesting() {
		return m_utils;
	}

	private void doAdaptation() {
		// nothing to do, avoid doing Rainbow adaptation
	}

	/**
	 * Algorithm:
	 * <ol>
	 * <li>Iterate through repertoire searching for enabled strategies, where "enabled" means applicable to current
	 * system condition. NOTE: A Strategy is "applicable" iff the conditions of applicability of the root tactic is
	 * true.
	 * <li>Calculate scores of the enabled strategies (this involves evaluating the meta-information of the tactics in
	 * each strategy).
	 * <li>Select and execute the highest scoring strategy
	 */
	public void triggerAdaptation(List<SelfHealingScenario> brokenScenarios) {
		m_adaptNeeded = true;
		currentBrokenScenarios = brokenScenarios;

		doLog(Level.INFO, "Adaptation triggered, let's begin!");
		if (_stopWatchForTesting != null)
			_stopWatchForTesting.start();

		Set<String> candidateStrategies = collectCandidateStrategiesNames(brokenScenarios);

		Environment currentSystemEnvironment = detectCurrentSystemEnvironment(this.m_model);

		// We don't want the "simulated" system utility to be less than the current real one.
		doLog(Level.INFO, "Computing Current System Utility...");
		double maxScore4Strategy = scoreSystemUtilityUsingArcoIris(currentSystemEnvironment,
				scenarioBrokenDetector4CurrentSystemState);

		doLog(Level.INFO, "Current System Utility: " + maxScore4Strategy);

		Strategy selectedStrategy = null;

		// idea: permitir al usuario pesar la solucion de Rainbow vs la nuestra
		// de esta manera se pueden seguir aprovechando las Utility curves configuradas
		double scenariosSolutionWeight = SCENARIOS_BASED_SOLUTION_WEIGHT;
		double rainbowSolutionWeight = RAINBOW_SOLUTION_WEIGHT;

		for (Stitch stitch : m_repertoire) {
			if (!stitch.script.isApplicableForModel(m_model.getAcmeModel())) {
				if (m_logger.isDebugEnabled())
					doLog(Level.DEBUG, "x. skipping " + stitch.script.getName());
				continue; // skip checking this script
			}

			for (Strategy currentStrategy : stitch.script.strategies) {
				// TODO Esto por el momento no funciona ya que hay que agregar una property a rainbow.properties:
				// customize.utility.trackStrategy
				// TODO Ver si esto no es muy fuerte (dilapidar una estrategia para siempre!!!)
				if (!candidateStrategies.contains(currentStrategy.getName())
						|| (getFailureRate(currentStrategy) > FAILURE_RATE_THRESHOLD)) {
					String cause = !candidateStrategies.contains(currentStrategy.getName()) ? "not selected in broken scenarios"
							: "failure rate threshold reached";
					doLog(Level.INFO, "Strategy does not apply (" + cause + ") : " + currentStrategy.getName());
					continue; // don't consider this Strategy
				}
				doLog(Level.INFO, "Evaluating strategy " + currentStrategy.getName() + "...");

				double strategyScore4Scenarios = 0;
				if (scenariosSolutionWeight > 0) {
					doLog(Level.INFO, "Scoring " + currentStrategy.getName() + " with scenarios approach...");

					strategyScore4Scenarios = scoreSystemUtilityUsingArcoIris(currentSystemEnvironment,
							new ScenarioBrokenDetector4StrategyScoring(m_model, currentStrategy));
					doLog(Level.INFO, "Scenarios approach score for strategy " + currentStrategy.getName() + ": "
							+ strategyScore4Scenarios);
				}

				double strategyScore4Rainbow = 0;
				if (rainbowSolutionWeight > 0) {
					doLog(Level.INFO, "Scoring " + currentStrategy.getName() + " with Rainbow approach...");
					Map<String, Double> weights4Rainbow = currentSystemEnvironment.getWeightsForRainbow();
					strategyScore4Rainbow = scoreStrategyUsingRainbow(currentStrategy, weights4Rainbow);
				}

				double weightedScore = strategyScore4Scenarios * scenariosSolutionWeight + strategyScore4Rainbow
						+ rainbowSolutionWeight;

				if (weightedScore > maxScore4Strategy) {
					maxScore4Strategy = weightedScore;
					selectedStrategy = currentStrategy;
				} else if (weightedScore == maxScore4Strategy) {
					if (selectedStrategy != null && getFailureRate(currentStrategy) < getFailureRate(selectedStrategy)) {
						selectedStrategy = currentStrategy;
					}
				}

			}

			if (_stopWatchForTesting != null)
				_stopWatchForTesting.stop();
			if (selectedStrategy != null) {
				doLog(Level.INFO, "Selected strategy: " + selectedStrategy.getName() + "!!!");
				// strategy args removed...
				Object[] args = new Object[0];
				m_pendingStrategies.add(selectedStrategy);
				((Executor) Oracle.instance().strategyExecutor()).enqueueStrategy(selectedStrategy, args);
				doLog(Level.TRACE, "<< Adaptation cycle awaits Executor...");
			} else {
				Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_END);
				doLog(Level.INFO, "NO applicable strategy, adaptation cycle ended.");
				m_adaptNeeded = false;
				m_model.clearConstraintViolated();
			}
		}
	}

	private Set<String> collectCandidateStrategiesNames(List<SelfHealingScenario> brokenScenarios) {
		Set<String> candidateStrategiesNames = new HashSet<String>();
		for (SelfHealingScenario brokenScenario : brokenScenarios) {
			candidateStrategiesNames.addAll(brokenScenario.getRepairStrategies().getRepairStrategiesNames());
		}
		return candidateStrategiesNames;
	}

	/**
	 * Detect the current system environment taking into account all the non-default ones. The first environment that
	 * holds is returned, or the default environment if no one holds.
	 * <p>
	 * <b>NOTE: We assume the environments are mutually exclusive.</b>
	 * 
	 * @return the current system enviroment
	 */
	private Environment detectCurrentSystemEnvironment(RainbowModelWithScenarios rainbowModelWithScenarios) {
		Collection<Environment> environments = this.selfHealingConfigurationManager.getAllNonDefaultEnvironments();
		for (Environment environment : environments) {
			if (environment.holds(rainbowModelWithScenarios)) {
				doLog(Level.INFO, "Current environment: " + environment.getName());
				return environment;
			}
		}
		doLog(Level.INFO, "System is currently in default environment");
		return this.selfHealingConfigurationManager.getDefaultEnvironment();
	}

	private Double scoreSystemUtilityUsingArcoIris(Environment currentSystemEnvironment,
			ScenarioBrokenDetector scenarioBrokenDetector) {
		double score = 0L;
		Collection<SelfHealingScenario> scenarios = this.selfHealingConfigurationManager.getEnabledScenarios();
		Map<Concern, Double> weights = currentSystemEnvironment.getWeights();
		for (SelfHealingScenario scenario : scenarios) {
			if (scenario.applyFor(currentSystemEnvironment)) {
				// here, the "simulation" takes place...
				boolean scenarioSatisfied = !scenarioBrokenDetector.isBroken(scenario);

				if (scenarioSatisfied) {
					doLog(Level.DEBUG, "Scenario " + scenario.getName() + " satisfied");
					Double concernWeight4CurrentEnvironment = weights.get(scenario.getConcern());
					if (concernWeight4CurrentEnvironment == null) {
						// if there is no weight for the concern then its weight it is assumed to be zero
						concernWeight4CurrentEnvironment = new Double(0);
					}
					score = score + scenarioWeight(scenario.getPriority(), concernWeight4CurrentEnvironment);
					// TODO TRATAR DE EVITAR INSTANCEOF!
					if (scenarioBrokenDetector instanceof ScenarioBrokenDetector4StrategyScoring) {
						SortedMap<String, Double> aggAtts = ((ScenarioBrokenDetector4StrategyScoring) scenarioBrokenDetector)
								.getAggregateAttributes();
						score = weightWithConcernUtilityFunction(score, scenario, aggAtts);
					}
				} else {
					doLog(Level.DEBUG, "Scenario " + scenario.getName() + " NOT satisfied");
				}
			} else {
				doLog(Level.DEBUG, "Scenario " + scenario.getName() + " does not apply for current system environment("
						+ currentSystemEnvironment.getName() + ")");
			}
		}
		return score;
	}

	private double weightWithConcernUtilityFunction(double score, SelfHealingScenario scenario,
			Map<String, Double> aggAtts) {
		UtilityFunction u = m_utils.get(scenario.getConcern().getRainbowName());
		Object eavgPropValue = m_model.getProperty(u.mapping());
		if (eavgPropValue != null && eavgPropValue instanceof Double) {
			if (m_logger.isTraceEnabled()) {
				doLog(Level.TRACE, "Avg value of prop: " + u.mapping() + " == " + eavgPropValue);
			}
			Double concernDiffAfterStrategy = aggAtts.get(u.id());
			double concernValueAfterStrategy = concernDiffAfterStrategy + ((Double) eavgPropValue).doubleValue();
			double concernUtilityValue = u.f(concernValueAfterStrategy);
			score = score * concernUtilityValue;
		}
		return score;
	}

	private double scenarioWeight(int scenarioPriority, double concernWeight4CurrentEnvironment) {
		double scenariosRelativePriority = this.scenariosAssignedPriority2RelativePriority(scenarioPriority);

		return scenariosRelativePriority * concernWeight4CurrentEnvironment;
	}

	private double scenariosAssignedPriority2RelativePriority(int scenarioPriority) {
		double maxPriority = this.selfHealingConfigurationManager.getMaxPriority() + 1;

		return (maxPriority - scenarioPriority) / maxPriority;
	}

	/**
	 * The Rainbow's calculus for the score of a strategy
	 * 
	 * @return the score of the strategy calculated by Rainbow
	 */
	private double scoreStrategyUsingRainbow(Strategy strategy, Map<String, Double> weights) {
		double[] conds = null;
		SortedMap<String, Double> aggAtt = strategy.computeAggregateAttributes();
		// add the strategy failure history as another attribute
		accountForStrategyHistory(aggAtt, strategy);
		String s = strategy.getName() + aggAtt;
		Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_STRATEGY_ATTR + s);
		doLog(Level.DEBUG, "aggAttr: " + s);
		/*
		 * compute utility values from attributes that combines values representing current condition, then accumulate
		 * the weighted utility sum
		 */
		double[] items = new double[aggAtt.size()];
		// double[] itemsPred = new double[aggAtt.size()];
		conds = new double[aggAtt.size()];
		// if (condsPred == null)
		// condsPred = new double[aggAtt.size()];
		int i = 0;
		double score = 0.0;
		// double scorePred = 0.0; // score based on predictions
		for (String k : aggAtt.keySet()) {
			double v = aggAtt.get(k);
			// find the applicable utility function
			UtilityFunction u = m_utils.get(k);
			Object condVal = null;
			// Object condValPred = null;
			// add attribute value from CURRENT condition to accumulated agg value
			condVal = m_model.getProperty(u.mapping());
			items[i] = v;
			if (condVal != null && condVal instanceof Double) {
				if (m_logger.isTraceEnabled())
					doLog(Level.TRACE, "Avg value of prop: " + u.mapping() + " == " + condVal);
				conds[i] = ((Double) condVal).doubleValue();
				items[i] += conds[i];
			}
			// now compute the utility, apply weight, and accumulate to sum
			score += weights.get(k) * u.f(items[i]);
			++i;
		}

		// log this
		s = Arrays.toString(items);
		if (score < m_minUtilityThreshold) {
			// utility score too low, don't consider for adaptation
			doLog(Level.TRACE, "score " + score + " below threshold, discarding: " + s);
		}
		Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_STRATEGY_ATTR2 + s);
		doLog(Level.DEBUG, "aggAtt': " + s);
		return score;
	}

	private static final int I_RUN = 0;
	private static final int I_SUCCESS = 1;
	private static final int I_FAIL = 2;
	private static final int I_OTHER = 3;
	private static final int CNT_I = 4;

	private void tallyStrategyOutcome(Strategy s) {
		if (m_historyTrackUtilName == null)
			return;

		String name = s.getName();
		// mark timer of failure, if applicable
		Beacon timer = m_failTimer.get(name);
		if (timer == null) {
			timer = new Beacon();
			m_failTimer.put(name, timer);
		}
		// get the stats array for this strategy
		int[] stat = m_historyCnt.get(name);
		if (stat == null) {
			stat = new int[CNT_I];
			stat[I_RUN] = 0;
			stat[I_SUCCESS] = 0;
			stat[I_FAIL] = 0;
			stat[I_OTHER] = 0;
			m_historyCnt.put(name, stat);
		}
		// tally outcome counts
		++stat[I_RUN];
		switch (s.outcome()) {
		case SUCCESS:
			++stat[I_SUCCESS];
			break;
		case FAILURE:
			++stat[I_FAIL];
			timer.mark();
			break;
		default:
			++stat[I_OTHER];
			break;
		}
		String str = name + Arrays.toString(stat);
		doLog(Level.TRACE, "History: " + str);
		Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_STAT + str);
	}

	private void accountForStrategyHistory(Map<String, Double> aggAtt, Strategy s) {
		if (m_historyTrackUtilName == null)
			return;

		if (m_historyCnt.containsKey(s.getName())) {
			// consider failure only
			aggAtt.put(m_historyTrackUtilName, getFailureRate(s));
		} else {
			// consider no failure
			aggAtt.put(m_historyTrackUtilName, 0.0);
		}
	}

	private double getFailureRate(Strategy s) {
		double rv = 0.0;
		if (m_historyTrackUtilName == null)
			return rv;

		int[] stat = m_historyCnt.get(s.getName());
		if (stat != null) {
			Beacon timer = m_failTimer.get(s.getName());
			double factor = 1.0;
			long failTimeDelta = timer.elapsedTime() - FAILURE_EFFECTIVE_WINDOW;
			if (failTimeDelta > 0) {
				factor = FAILURE_WINDOW_CHUNK * 1.0 / failTimeDelta;
			}
			rv = factor * stat[I_FAIL] / stat[I_RUN];
		}
		return rv;
	}
}