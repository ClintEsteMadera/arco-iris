package ar.uba.dc.arcoiris.atam.scenario.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.sa.rainbow.scenario.model.ArcoIrisModel;

import ar.uba.dc.arcoiris.common.IdentifiableArcoIrisDomainObject;
import ar.uba.dc.arcoiris.common.validation.Assert;
import ar.uba.dc.arcoiris.common.validation.ValidationError;
import ar.uba.dc.arcoiris.qa.Concern;
import ar.uba.dc.arcoiris.rainbow.constraint.Constraint;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("environment")
public class Environment extends IdentifiableArcoIrisDomainObject {

	private static final long serialVersionUID = 1L;

	private static final String FRIENDLY_NAME = "Environment";

	private static final String VALIDATION_MSG_ID = "The identifier '-1' can only be used by the 'Default' environment";

	private static final String VALIDATION_MSG_NAME = "Name cannot be empty";

	private static final String VALIDATION_MSG_NULL_WEIGHTS = "Concern-Weight map cannot be empty";

	private static final String VALIDATION_MSG_INVALID_WEIGHTS = "Concern-Weight map must contain all possible concerns";

	private static final String VALIDATION_MSG_WEIGHTS_DO_NOT_SUM_ONE = "The sum of all weights should be between 0.99 and 1.00";

	@XStreamAsAttribute
	private String name;

	private List<? extends Constraint> conditions;

	private SortedMap<Concern, Double> weights;

	@XStreamOmitField
	private Map<String, Double> weightsForRainbow;

	/**
	 * Constructor
	 */
	public Environment() {
		super();
		this.conditions = new ArrayList<Constraint>();
		this.weights = createMapWithEquallyDistributedWeights();
	}

	/**
	 * Constructor
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
		super(id);
		this.name = name;
		this.conditions = conditions;
		this.weights = weights;

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

	public Map<String, Double> getWeightsForRainbow() {
		if (this.weightsForRainbow == null) {
			this.weightsForRainbow = new HashMap<String, Double>(this.weights.size());
			for (Concern concern : weights.keySet()) {
				this.weightsForRainbow.put(concern.getRainbowName(), weights.get(concern));
			}
		}
		return this.weightsForRainbow;
	}

	public boolean holds(ArcoIrisModel arcoIrisModel) {
		boolean holds = true;
		for (Constraint constraint : this.getConditions()) {
			Double expValue = arcoIrisModel.getExponentialValueForConstraint(constraint);
			if (expValue != null) {
				holds = holds && constraint.holds(expValue);
			} else {
				holds = false;
				break;
			}
		}
		return holds;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	protected String[] getEqualsAndHashCodeExcludedFields() {
		return new String[] { "weightsForRainbow" };
	}

	@Override
	protected List<ValidationError> collectValidationErrors() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Assert.isTrue(this.isMyIdValid(), VALIDATION_MSG_ID, validationErrors);

		Assert.notBlank(this.name, VALIDATION_MSG_NAME, validationErrors);

		Assert.isValid(this.conditions, validationErrors);

		this.validateWeights(validationErrors);

		return validationErrors;
	}

	/**
	 * @return <code>true</code>, as long as the user did not try to use AnyEnvironment's id for another instance of
	 *         Environment. Otherwise, this method returns <code>false</code>
	 * 
	 * @see AnyEnvironment#isMyIdValid()
	 */
	protected boolean isMyIdValid() {
		return !this.getId().equals(AnyEnvironment.ID);
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