package ar.uba.dc.thesis.atam.scenario.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.common.Heuristic;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("environment")
public class Environment extends ThesisPojo {

	private static final long serialVersionUID = 1L;

	private static final Heuristic DEFAULT_HEURISTIC = Heuristic.MOST;

	private static final int DEFAULT_HISTORY_SIZE = 10;

	@XStreamAsAttribute
	private String name;

	private List<? extends Constraint> conditions;

	private SortedMap<Concern, Double> weights;

	@XStreamOmitField
	private Map<String, Double> weightsForRainbow;

	private Heuristic heuristic;

	@XStreamOmitField
	private List<Boolean> history;

	/**
	 * Constructor
	 */
	public Environment() {
		super();
		this.conditions = new ArrayList<Constraint>();
		this.weights = createMapWithEquallyDistributedWeights();
	}

	/**
	 * This constructor has the same effect than invoking {@link #Environment(String, List, Map, Integer, Heuristic)}
	 * with the latest two parameters in <code>null</code>
	 * 
	 * @param id
	 *            environment's id
	 * @param name
	 *            environment's name
	 * @param conditions
	 *            the conditions that need to hold in order to consider this Environment applicable
	 * @param weights
	 *            how Concerns are weightened in this particular Environment <b>NOTE: ALL concerns must be present,
	 *            otherwise, an exception will be thrown</b>
	 */
	public Environment(Long id, String name, List<? extends Constraint> conditions, SortedMap<Concern, Double> weights) {
		this(id, name, conditions, weights, null, null);
		this.validateId();
	}

	/**
	 * /** This constructor has the same effect than invoking
	 * {@link #Environment(String, List, Map, Integer, Heuristic)} with the latest two parameters in <code>null</code>
	 * 
	 * @param id
	 *            environment's id
	 * @param name
	 *            environment's name
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
	public Environment(Long id, String name, List<? extends Constraint> conditions, SortedMap<Concern, Double> weights,
			Integer historySize, Heuristic heuristic) {
		super(id);
		this.name = name;
		this.conditions = conditions;
		this.weights = weights;

		this.history = new ArrayList<Boolean>(historySize == null ? DEFAULT_HISTORY_SIZE : historySize);
		this.heuristic = heuristic == null ? DEFAULT_HEURISTIC : heuristic;

		this.validate();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<? extends Constraint> getConditions() {
		return conditions;
	}

	public void setConditions(List<? extends Constraint> conditions) {
		this.conditions = conditions;
	}

	public SortedMap<Concern, Double> getWeights() {
		return weights;
	}

	public void setWeights(SortedMap<Concern, Double> weights) {
		this.weights = weights;
		this.assertAllPossibleConcernsAreDefined(weights.keySet());
	}

	public Heuristic getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	public List<Boolean> getHistory() {
		return history;
	}

	public void setHistory(List<Boolean> history) {
		this.history = history;
	}

	public Map<String, Double> getWeightsForRainbow() {
		if (this.weightsForRainbow == null) {
			this.weightsForRainbow = new HashMap<String, Double>(this.weights.size());
			for (Concern concern : weights.keySet()) {
				this.weightsForRainbow.put(concern.getRainbowName(), weights.get(concern));
			}
		}
		return this.weightsForRainbow;
	}

	public boolean holds4Scoring(RainbowModelWithScenarios rainbowModelWithScenarios) {
		boolean holds = true;

		for (Constraint constraint : this.getConditions()) {
			if (constraint instanceof NumericBinaryRelationalConstraint) {
				NumericBinaryRelationalConstraint numericConstraint = (NumericBinaryRelationalConstraint) constraint;
				Number eavg = (Number) rainbowModelWithScenarios.getProperty(RainbowModel.EXP_AVG_KEY
						+ numericConstraint.getArtifact().getName() + "." + numericConstraint.getProperty());
				holds = holds && eavg != null && numericConstraint.holds(eavg);
			} else {
				holds = false;
			}
		}
		holds = this.weightCurrentEvaluationUsingHistory(holds);

		this.addToHistory(holds);

		return holds;
	}

	@Override
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
		Set<Concern> definedConcerns = this.getWeights().keySet();
		this.assertAllPossibleConcernsAreDefined(definedConcerns);
	}

	@Override
	protected String[] getEqualsAndHashCodeExcludedFields() {
		return new String[] { "weightsForRainbow", "history" };
	}

	/**
	 * This method should ensure that the DefaultEnvironment's id is not being used for an object not instance of that
	 * class. It should be overriden accordingly on all of the subclasses.
	 */
	protected void validateId() {
		if (this.getId().equals(DefaultEnvironment.ID)) {
			throw new RuntimeException("The identifier " + DefaultEnvironment.ID
					+ " cannot be used for other Environment than the default one");
		}
	}

	/**
	 * Uses an heuristic (configured by the user) to decide if, based on the history, a current calculation of the
	 * underlying constraints holds or not.<br>
	 * The possible values are specified in {@link Heuristic}
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

	@Override
	public String toString() {
		return this.getName();
	}

	private void assertAllPossibleConcernsAreDefined(Collection<Concern> definedConcerns) {
		List<Concern> allPossibleConcerns = Arrays.asList(Concern.values());

		if (!definedConcerns.containsAll(allPossibleConcerns)) {
			throw new IllegalArgumentException("Concern-Multiplier map must contain all possible concerns");
		}
	}

	protected static SortedMap<Concern, Double> createMapWithEquallyDistributedWeights() {
		SortedMap<Concern, Double> equallyDistributedWeights = new TreeMap<Concern, Double>();
		Concern[] values = Concern.values();
		Double aWeight = (double) 1 / values.length;
		for (Concern concern : values) {
			equallyDistributedWeights.put(concern, aWeight);
		}

		return equallyDistributedWeights;
	}

}