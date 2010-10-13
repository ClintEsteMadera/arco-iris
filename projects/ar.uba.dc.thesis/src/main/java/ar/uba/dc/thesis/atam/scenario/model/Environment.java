package ar.uba.dc.thesis.atam.scenario.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.common.Heuristic;
import ar.uba.dc.thesis.common.IdentifiableArcoIrisDomainObject;
import ar.uba.dc.thesis.common.validation.Assert;
import ar.uba.dc.thesis.common.validation.ValidationError;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("environment")
public class Environment extends IdentifiableArcoIrisDomainObject {

	private static final String FRIENDLY_NAME = "Environment";

	private static final String VALIDATION_MSG_ID = "The identifier '-1' can only be used by the 'Default' environment";

	private static final String VALIDATION_MSG_NAME = "Name cannot be empty";

	private static final String VALIDATION_MSG_NULL_WEIGHTS = "Concern-Weight map cannot be empty";

	private static final String VALIDATION_MSG_INVALID_WEIGHTS = "Concern-Weight map must contain all possible concerns";

	private static final String VALIDATION_MSG_WEIGHTS_DO_NOT_SUM_ONE = "The sum of all weights should be between 0.99 and 1.00";

	private static final String VALIDATION_MSG_NULL_HEURISTIC = "The heuristic to be used cannot be empty";

	private static final Heuristic DEFAULT_HEURISTIC = Heuristic.MOST;

	private static final int HISTORY_MAX_SIZE = 10;

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
		this.heuristic = DEFAULT_HEURISTIC;
		this.history = new ArrayList<Boolean>(HISTORY_MAX_SIZE);
	}

	/**
	 * This constructor has the same effect than invoking {@link #Environment(String, List, Map, Integer, Heuristic)}
	 * with the latest two parameters set as <code>null</code>
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

		this.history = new ArrayList<Boolean>(historySize == null ? HISTORY_MAX_SIZE : historySize);
		this.heuristic = (heuristic == null) ? DEFAULT_HEURISTIC : heuristic;

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

	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios) {
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

		this.addToHistory(holds);
		return this.heuristic.run(this.history);
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	protected String[] getEqualsAndHashCodeExcludedFields() {
		return new String[] { "weightsForRainbow", "history" };
	}

	@Override
	protected List<ValidationError> collectValidationErrors() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Assert.isTrue(this.isMyIdValid(), VALIDATION_MSG_ID, validationErrors);

		Assert.notBlank(this.name, VALIDATION_MSG_NAME, validationErrors);

		Assert.isValid(this.conditions, validationErrors);

		Assert.notNull(this.heuristic, VALIDATION_MSG_NULL_HEURISTIC, validationErrors);

		this.validateWeights(validationErrors);

		return validationErrors;
	}

	/**
	 * @return <code>true</code>, as long as the user did not try to use DefaultEnvironment's id for another instance of
	 *         Environment. Otherwise, this method returns <code>false</code>
	 * 
	 * @see DefaultEnvironment#isMyIdValid()
	 */
	protected boolean isMyIdValid() {
		return !this.getId().equals(DefaultEnvironment.ID);
	}

	private void validateWeights(List<ValidationError> validationErrors) {
		if (this.weights == null) {
			validationErrors.add(new ValidationError((": " + VALIDATION_MSG_NULL_WEIGHTS)));
		} else {
			this.validateAllPossibleWeightsAreDefined(validationErrors);
			this.validateAllWeightsSumUpOne(validationErrors);
		}
	}

	private void validateAllWeightsSumUpOne(List<ValidationError> validationErrors) {
		double sum = 0;
		for (Double currentWeight : this.weights.values()) {
			sum += currentWeight;
		}
		if (sum < 0.99 || sum > 1.00) {
			validationErrors.add(new ValidationError(VALIDATION_MSG_WEIGHTS_DO_NOT_SUM_ONE));
		}
	}

	private void validateAllPossibleWeightsAreDefined(List<ValidationError> validationErrors) {
		Set<Concern> definedConcerns = this.getWeights().keySet();
		List<Concern> allPossibleConcerns = Arrays.asList(Concern.values());

		if (!definedConcerns.containsAll(allPossibleConcerns)) {
			validationErrors.add(new ValidationError(VALIDATION_MSG_INVALID_WEIGHTS));
		}
	}

	/**
	 * Adds the value to the history. If the history has reached its maximum size, then the oldest value is overriden
	 * (FIFO Cache)
	 * 
	 * @param holds
	 */
	private void addToHistory(boolean holds) {
		if (this.history.size() == HISTORY_MAX_SIZE) {
			this.history.set(0, holds);
		} else {
			this.history.add(holds);
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

	@Override
	protected String getObjectFriendlyName() {
		return FRIENDLY_NAME;
	}
}