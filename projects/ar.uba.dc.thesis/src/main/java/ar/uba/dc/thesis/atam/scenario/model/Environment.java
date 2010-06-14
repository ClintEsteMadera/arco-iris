package ar.uba.dc.thesis.atam.scenario.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.common.Heuristic;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public class Environment extends ThesisPojo {

	private final String name;

	private final List<? extends Constraint> conditions;

	private final Map<Concern, Double> weights;

	private final HashMap<String, Double> weightsForRainbow;

	private final Heuristic heuristic;

	private final List<Boolean> history;

	private static final Heuristic DEFAULT_HEURISTIC = Heuristic.MOST;

	public static final Environment ANY_ENVIRONMENT = createAnyEnvironment();

	private static final int DEFAULT_HISTORY_SIZE = 10;

	/**
	 * This constructor has the same effect than invoking {@link #Environment(String, List, Map, Integer, Heuristic)}
	 * with the latest two parameters in <code>null</code>
	 * 
	 * @param name
	 *            the environment's name
	 * @param conditions
	 *            the conditions that need to hold in order to consider this Environment applicable
	 * @param weights
	 *            how Concerns are weightened in this particular Environment <b>NOTE: ALL concerns must be present,
	 *            otherwise, an exception will be thrown</b>
	 */
	public Environment(String name, List<? extends Constraint> conditions, Map<Concern, Double> weights) {
		this(name, conditions, weights, null, null);
	}

	/**
	 * /** This constructor has the same effect than invoking
	 * {@link #Environment(String, List, Map, Integer, Heuristic)} with the latest two parameters in <code>null</code>
	 * 
	 * @param name
	 *            the environment's name
	 * @param conditions
	 *            the conditions that need to hold in order to consider this Environment applicable
	 * @param weights
	 *            how Concerns are weightened in this particular Environment <b>NOTE: ALL concerns must be present,
	 *            otherwise, an exception will be thrown</b>
	 * @param historySize
	 *            the size of the collection holding the latest N past evaluations of environment's constraints.
	 * @param heuristic
	 *            the Heuristic that needs to be used when weightening past evaluations of environment's constraints.
	 */
	public Environment(String name, List<? extends Constraint> conditions, Map<Concern, Double> weights,
			Integer historySize, Heuristic heuristic) {
		super();
		this.name = name;
		this.conditions = conditions;
		this.weights = weights;

		this.history = new ArrayList<Boolean>(historySize == null ? DEFAULT_HISTORY_SIZE : historySize);
		this.heuristic = heuristic == null ? DEFAULT_HEURISTIC : heuristic;

		this.validate();

		this.weightsForRainbow = new HashMap<String, Double>(this.weights.size());
		for (Concern concern : weights.keySet()) {
			this.weightsForRainbow.put(concern.getRainbowName(), weights.get(concern));
		}
	}

	public String getName() {
		return name;
	}

	public List<? extends Constraint> getConditions() {
		return conditions;
	}

	public Map<Concern, Double> getWeights() {
		return weights;
	}

	public Map<String, Double> getWeightsForRainbow() {
		return this.weightsForRainbow;
	}

	public boolean holds4Scoring(RainbowModelWithScenarios rainbowModelWithScenarios) {
		boolean holds = true;

		for (Constraint constraint : this.getConditions()) {
			holds = holds && constraint.holds4Scoring(rainbowModelWithScenarios);
		}
		holds = this.weightCurrentEvaluationUsingHistory(holds);

		this.addToHistory(holds);

		return holds;
	}

	public void validate() {
		if (StringUtils.isBlank(this.getName())) {
			throw new IllegalArgumentException("Environment's name cannot be empty");
		}
		for (Constraint condition : this.getConditions()) {
			condition.validate();
		}
		if (this.getWeights() == null) {
			throw new IllegalArgumentException("Concern-Multiplier map cannot be null");
		}
		Set<Concern> allDefinedConcerns = this.getWeights().keySet();
		List<Concern> allPossibleConcerns = Arrays.asList(Concern.values());

		allDefinedConcerns.containsAll(allPossibleConcerns);
	}

	@Override
	protected String[] getEqualsAndHashCodeExcludedFields() {
		return new String[] { "history" };
	}

	/**
	 * Uses an heuristic (configured by the user) to decide if, based on the history, a current calculation of the
	 * underlying constraints holds or not.<br>
	 * The possible values are specified in {@link HeuristicType}
	 * 
	 * @param currentConstraintsEvaluation
	 *            the current evaluation of the underlying constraints, to be quantified with the history.
	 * @return a (hopefully) more accurate value than the last one obtained recently (i.e.
	 *         <code>currentConstraintsEvaluation</code>)
	 */
	private boolean weightCurrentEvaluationUsingHistory(boolean currentConstraintsEvaluation) {
		return this.heuristic.run(currentConstraintsEvaluation, this.history);
	}

	/**
	 * Adds the value to the history. If the history has reached its maximum size, then the oldest value is overriden
	 * (FIFO Cache)
	 * 
	 * @param holds
	 */
	private void addToHistory(boolean holds) {
		if (this.history.size() == DEFAULT_HISTORY_SIZE) {
			this.history.add(0, holds);
		} else {
			this.history.add(holds);
		}
	}

	/**
	 * This method creates an Environment which is intended to be used as a wildcard (i.e. "this scenario applies for
	 * <b>any</b> environment")
	 * 
	 * @return an Environment in which its weights are equally distributed considering the amount of concerns returned
	 *         by {@link Concern#values()}
	 */
	private static Environment createAnyEnvironment() {
		Map<Concern, Double> equallyDistributedWeights = new HashMap<Concern, Double>();
		Concern[] values = Concern.values();
		Double aWeight = (double) 1 / values.length;
		for (Concern concern : values) {
			equallyDistributedWeights.put(concern, aWeight);
		}
		return new Environment("ANY", Collections.<Constraint> emptyList(), equallyDistributedWeights);
	}

}