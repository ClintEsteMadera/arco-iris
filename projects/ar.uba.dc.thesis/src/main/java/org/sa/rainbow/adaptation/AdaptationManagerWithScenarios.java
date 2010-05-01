package org.sa.rainbow.adaptation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
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

import org.sa.rainbow.adaptation.executor.Executor;
import org.sa.rainbow.core.AbstractRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.model.UtilityPreferenceDescription.UtilityAttributes;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.scenario.model.ScenariosManager;
import org.sa.rainbow.stitch.Ohana;
import org.sa.rainbow.stitch.core.Strategy;
import org.sa.rainbow.stitch.core.Tactic;
import org.sa.rainbow.stitch.core.UtilityFunction;
import org.sa.rainbow.stitch.core.Strategy.Outcome;
import org.sa.rainbow.stitch.error.DummyStitchProblemHandler;
import org.sa.rainbow.stitch.visitor.Stitch;
import org.sa.rainbow.util.StopWatch;
import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.repository.EnvironmentRepository;
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

	private final ScenariosManager scenariosManager;

	private final EnvironmentRepository environmentRepository;

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

	private final Mode m_mode = Mode.SERIAL;
	private RainbowModelWithScenarios m_model = null;
	private boolean m_adaptNeeded = false; // treat as synonymous with constraint being violated
	private boolean m_adaptEnabled = true; // by default, we adapt
	private List<Stitch> m_repertoire = null;
	private SortedMap<String, UtilityFunction> m_utils = null;
	private List<Strategy> m_pendingStrategies = null;

	// track history
	private String m_historyTrackUtilName = null;
	private Map<String, int[]> m_historyCnt = null;
	private Map<String, Beacon> m_failTimer = null;

	/**
	 * Default constructor.
	 */
	public AdaptationManagerWithScenarios(ScenariosManager scenariosManager, EnvironmentRepository environmentRepository) {
		super(NAME);

		this.scenariosManager = scenariosManager;
		this.environmentRepository = environmentRepository;
		m_model = Oracle.instance().rainbowModel();
		m_repertoire = new ArrayList<Stitch>();
		m_utils = new TreeMap<String, UtilityFunction>();
		m_pendingStrategies = new ArrayList<Strategy>();
		m_historyTrackUtilName = Rainbow.property(Rainbow.PROPKEY_TRACK_STRATEGY);
		if (m_historyTrackUtilName != null) {
			m_historyCnt = new HashMap<String, int[]>();
			m_failTimer = new HashMap<String, Beacon>();
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
		initAdaptationRepertoire();
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
		Oracle.instance().writeEnginePanel(m_logger, txt);
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
			String s = strategy.getName() + ";" + strategy.outcome();
			log("*S* outcome: " + s);
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
					m_logger.trace("Avg value of prop: " + u.mapping() + " == " + condVal);
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

	/**
	 * For JUnit testing, allows re-invoking defineAttributes to artificially increase the number of quality dimensions
	 * in tactic attribute vectors.
	 * 
	 * @param stitch
	 * @param attrVectorMap
	 */
	void _defineAttributesFromTester(Stitch stitch, Map<String, Map<String, Object>> attrVectorMap) {
		defineAttributes(stitch, attrVectorMap);
	}

	private void doAdaptation() {
		// nothing to do, avoid doing Rainbow adaptation
	}

	/**
	 * Algorithm:
	 * <ol>
	 * <li> Iterate through repertoire searching for enabled strategies, where "enabled" means applicable to current
	 * system condition. NOTE: A Strategy is "applicable" iff the conditions of applicability of the root tactic is
	 * true.
	 * <li> Calculate scores of the enabled strategies (this involves evaluating the meta-information of the tactics in
	 * each strategy).
	 * <li> Select and execute the highest scoring strategy
	 */
	public void triggerAdaptation(List<SelfHealingScenario> brokenScenarios) {

		log("Adaptation triggered, let's begin!");
		if (_stopWatchForTesting != null)
			_stopWatchForTesting.start();

		Set<String> candidateStrategies = collectCandidateStrategies(brokenScenarios);

		Environment systemEnvironment = detectCurrentSystemEnvironment(this.m_model);
		Map<String, Double> weights4Rainbow = extractConcernWeights(systemEnvironment);

		double maxScore4Strategy = 0L;
		Strategy selectedStrategy = null;

		// idea: permitir al usuario pesar la solucion de Rainbow vs la nuestra
		// de esta manera se pueden seguir aprovechando las Utility curves configuradas
		double scenariosSolutionWeight = 1;
		int rainbowSolutionWeight = 0;

		for (Stitch stitch : m_repertoire) {
			if (!stitch.script.isApplicableForModel(m_model.getAcmeModel())) {
				if (m_logger.isDebugEnabled())
					m_logger.debug("x. skipping " + stitch.script.getName());
				continue; // skip checking this script
			}

			for (Strategy strategy : stitch.script.strategies) {
				// check first for failures threshold
				if (!candidateStrategies.contains(strategy.getName())
						|| (getFailureRate(strategy) != 0.0 && getFailureRate(strategy) > FAILURE_RATE_THRESHOLD)) {
					continue; // don't consider this Strategy
				}
				RainbowModelWithScenarios simulationModel = simulateStrategyApplication(strategy);

				double scenariosScore = 0;
				if (scenariosSolutionWeight > 0) {
					scenariosScore = scoreStrategyWithScenarios(this.scenariosManager.getEnabledScenarios(),
							systemEnvironment, simulationModel);
				}

				double rainbowScoreStrategy = 0;
				if (rainbowSolutionWeight > 0) {
					rainbowScoreStrategy = scoreStrategy(strategy, weights4Rainbow);
				}

				double weightedScore = scenariosScore * scenariosSolutionWeight + rainbowScoreStrategy
						+ rainbowSolutionWeight;
				if (weightedScore > maxScore4Strategy) {
					maxScore4Strategy = weightedScore;
					selectedStrategy = strategy;
				}
			}

			// TODO lo siguiente es tomado de rainbow tal cual (ver)
			if (_stopWatchForTesting != null)
				_stopWatchForTesting.stop();
			if (selectedStrategy != null) {
				log(">> do strategy: " + selectedStrategy.getName());
				// strategy args removed...
				Object[] args = new Object[0];
				m_pendingStrategies.add(selectedStrategy);
				((Executor) Oracle.instance().strategyExecutor()).enqueueStrategy(selectedStrategy, args);
				log("<< Adaptation cycle awaits Executor...");
			} else {
				Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_END);
				log("<< NO applicable strategy, adaptation cycle ended.");
				m_adaptNeeded = false;
				m_model.clearConstraintViolated();
			}
		}
	}

	private Set<String> collectCandidateStrategies(List<SelfHealingScenario> brokenScenarios) {
		Set<String> candidateStrategies = new HashSet<String>();
		for (SelfHealingScenario brokenScenario : brokenScenarios) {
			candidateStrategies.addAll(brokenScenario.getRepairStrategies());
		}
		return candidateStrategies;
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
		Collection<Environment> environments = this.environmentRepository.getAllNonDefaultEnvironments();
		for (Environment environment : environments) {
			if (environment.holds(rainbowModelWithScenarios)) {
				log("Current environment: " + environment.getName());
				return environment;
			}
		}
		log("System is currently in default environment");
		return environmentRepository.getDefaultEnvironment();
	}

	private RainbowModelWithScenarios simulateStrategyApplication(Strategy strategy) {
		log("Simulating " + strategy.getName() + " strategy begin...");

		RainbowModelWithScenarios simulationModel = null;
		try {
			// TODO Ver si es necesario hacer esto!!
			simulationModel = this.m_model.clone();
		} catch (CloneNotSupportedException e) {
			log(e.getMessage());
		}
		// TODO simular aplicacion de estrategia sobre simulationModel!
		Outcome simulationResult = (Outcome) strategy.evaluate(null);
		if (simulationResult == Outcome.SUCCESS) {
			log("Simulating " + strategy.getName() + " strategy end...");
		} else {
			log("Error simulating " + strategy.getName() + " strategy. The strategy won't be selected.");
		}
		// System.out.println(strategy.modelElementsUsed());TODO ver strategy.modelElementsUsed()!
		return simulationModel;
	}

	/**
	 * Rainbow works with concern as Strings, so a translation is needed
	 * 
	 * @param weights
	 * @return
	 */
	private Map<String, Double> extractConcernWeights(Environment environment) {
		Map<Concern, Double> weights = environment.getWeights();

		Map<String, Double> result = new HashMap<String, Double>(weights.size());
		for (Concern concern : weights.keySet()) {
			result.put(concern.getRainbowName(), weights.get(concern));
		}
		return result;
	}

	private Double scoreStrategyWithScenarios(Collection<SelfHealingScenario> scenarios, Environment systemEnvironment,
			RainbowModelWithScenarios simulationModel) {
		double score = 0L;
		Map<Concern, Double> weights = systemEnvironment.getWeights();
		for (SelfHealingScenario scenario : scenarios) {
			if (scenario.applyFor(systemEnvironment)) {
				boolean scenarioSatisfiedInSimulation = !scenario.isBroken(simulationModel);
				// FIXME scenarioSatisfiedInSimulation da siempre false, ver si cambia el modelo al aplicar la
				// estrategia
				if (scenarioSatisfiedInSimulation) {
					Double concernWeight = weights.get(scenario.getConcern());
					if (concernWeight == null) {
						// if there is no weight for the concern then its weight it is assumed to be zero
						concernWeight = new Double(0);
					}
					score = score + scenarioWeight(scenario.getPriority(), concernWeight);
				}
			}
		}
		return score;
	}

	// TODO Analizar e implementar la utilización de la prioridad del escenario en el cálculo del score de la
	// estrategia.
	private double scenarioWeight(int priority, double concernWeight) {
		// TODO tomar la ultima prioridad (mas uno para evitar que pese cero) de entre todos los escenarios?
		int LAST_PRIORITY = 1000;
		// TODO ver como pesar prioridades (ojo con outliers en las prioridades)
		double priorityWeight = (LAST_PRIORITY - priority) / LAST_PRIORITY;
		// TODO idea: el usuario puede pesar la importancia de los concerns vs la de las prioridades
		// por ahora pesan lo mismo los pesos de los concerns que la prioridad:
		double concernsPerEnvironmentWeight = 0.5;
		double scenariosPrioritiesWeight = 0.5;
		// TODO: Analizar la utilización del peso del concern del escenario en el cálculo del score de la estrategia.
		// por ahora solamente se esta sumando el peso del concern (si el concern pesa 0.6 suma 0.6)
		return concernWeight * concernsPerEnvironmentWeight + priorityWeight * scenariosPrioritiesWeight;
	}

	/**
	 * The Rainbow's calculus for the score of a strategy
	 * 
	 * @return the score of the strategy calculated by Rainbow
	 */
	private double scoreStrategy(Strategy strategy, Map<String, Double> weights) {
		double[] conds = null;
		SortedMap<String, Double> aggAtt = strategy.computeAggregateAttributes();
		// add the strategy failure history as another attribute
		accountForStrategyHistory(aggAtt, strategy);
		String s = strategy.getName() + aggAtt;
		Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_STRATEGY_ATTR + s);
		log("aggAttr: " + s);
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
					m_logger.trace("Avg value of prop: " + u.mapping() + " == " + condVal);
				conds[i] = ((Double) condVal).doubleValue();
				items[i] += conds[i];
			}
			// TODO agregar peso segun prioridad
			// Idea: sumar un valor fijo para que su peso no sea tan importante, i.e: estrategia nro 1 suma 100, nro 2
			// suma 50, etc

			// now compute the utility, apply weight, and accumulate to sum
			score += weights.get(k) * u.f(items[i]);
			++i;
		}

		// log this
		s = Arrays.toString(items);
		if (score < m_minUtilityThreshold) {
			// utility score too low, don't consider for adaptation
			log("score " + score + " below threshold, discarding: " + s);
			// TODO descartar estrategia, no calcular su score directamente
		}
		Util.dataLogger().info(IRainbowHealthProtocol.DATA_ADAPTATION_STRATEGY_ATTR2 + s);
		log("aggAtt': " + s);
		return score;
	}

	/**
	 * Retrieves the adaptation repertoire; for each tactic, store the respective tactic attribute vectors.
	 */
	private void initAdaptationRepertoire() {
		File stitchPath = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow
				.property(Rainbow.PROPKEY_SCRIPT_PATH));
		if (stitchPath.exists() && stitchPath.isDirectory()) {
			FilenameFilter ff = new FilenameFilter() { // find only ".s" files
				public boolean accept(File dir, String name) {
					return name.endsWith(".s");
				}
			};
			for (File f : stitchPath.listFiles(ff)) {
				try {
					// don't duplicate loading of script files
					Stitch stitch = Ohana.instance().findStitch(f.getCanonicalPath());
					if (stitch == null) {
						stitch = Stitch.newInstance(f.getCanonicalPath(), new DummyStitchProblemHandler());
						Ohana.instance().parseFile(stitch);
						// apply attribute vectors to tactics, if available
						defineAttributes(stitch, Rainbow.instance().preferenceDesc().attributeVectors);
						m_repertoire.add(stitch);
						log("Parsed script " + stitch.path);
					} else {
						log("Previously known script " + stitch.path);
					}
				} catch (IOException e) {
					m_logger.error("Obtaining file canonical path failed! " + f.getName(), e);
				}
			}
		}
	}

	private void defineAttributes(Stitch stitch, Map<String, Map<String, Object>> attrVectorMap) {
		for (Tactic t : stitch.script.tactics) {
			Map<String, Object> attributes = attrVectorMap.get(t.getName());
			if (attributes != null) {
				// found attribute def for tactic, save all key-value pairs
				if (m_logger.isTraceEnabled())
					m_logger.trace("Found attributes for tactic " + t.getName() + ", saving pairs...");
				for (Map.Entry<String, Object> e : attributes.entrySet()) {
					t.putAttribute(e.getKey(), e.getValue());
					if (m_logger.isTraceEnabled())
						m_logger.trace(" - (" + e.getKey() + ", " + e.getValue() + ")");
				}
			}
		}
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
		log("History: " + str);
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