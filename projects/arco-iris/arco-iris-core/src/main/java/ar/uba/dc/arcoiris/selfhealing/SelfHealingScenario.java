package ar.uba.dc.arcoiris.selfhealing;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.scenario.model.ArcoIrisModel;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.arcoiris.atam.scenario.model.AnyEnvironment;
import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;
import ar.uba.dc.arcoiris.atam.scenario.model.QualityAttributeScenario;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.atam.scenario.model.ResponseMeasure;
import ar.uba.dc.arcoiris.atam.scenario.model.Stimulus;
import ar.uba.dc.arcoiris.common.validation.Assert;
import ar.uba.dc.arcoiris.common.validation.ValidationError;
import ar.uba.dc.arcoiris.qa.Concern;
import ar.uba.dc.arcoiris.rainbow.constraint.Constraint;
import ar.uba.dc.arcoiris.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.arcoiris.selfhealing.repair.AllRepairStrategies;
import ar.uba.dc.arcoiris.selfhealing.repair.RepairStrategies;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("selfHealingScenario")
public class SelfHealingScenario extends QualityAttributeScenario {

	private static final long serialVersionUID = 1L;

	private static final String NO_DATA = "(NO DATA)";

	private static final String VALIDATION_MSG_REPAIR_STRATEGIES = "Scenario's repair strategies cannot be empty";

	@XStreamAsAttribute
	private boolean enabled;

	@XStreamAsAttribute
	private int priority;

	private RepairStrategies repairStrategies;

	private static RainbowLogger m_logger = RainbowLoggerFactory.logger(SelfHealingScenario.class);

	public SelfHealingScenario() {
		super();
		this.enabled = true;
		this.repairStrategies = AllRepairStrategies.getInstance();
	}

	public SelfHealingScenario(Long id, String name, Concern concern, Stimulus stimulus,
			List<? extends Environment> environments, Artifact artifact, String response,
			ResponseMeasure responseMeasure, boolean enabled, int priority, RepairStrategies repairStrategies) {
		super(id, name, concern, stimulus, environments, artifact, response, responseMeasure);
		this.enabled = enabled;
		this.priority = priority;
		this.repairStrategies = repairStrategies;

		this.validate();
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public RepairStrategies getRepairStrategies() {
		return this.repairStrategies;
	}

	public void setRepairStrategies(RepairStrategies repairStrategies) {
		this.repairStrategies = repairStrategies;
	}

	public boolean applyFor(Environment environment) {
		return getEnvironments().contains(AnyEnvironment.getInstance()) || getEnvironments().contains(environment);
	}

	/**
	 * Este método es el que normalmente se usa para determinar si un escenario está roto.
	 * <p>
	 * Este método está pensado para ser usado únicamente por una instancia de {@link ScenarioBrokenDetector}. Por eso
	 * es protected.
	 */
	public boolean isBroken(final ArcoIrisModel rainbowModelWithScenarios) {
		if (this.anyEnvironmentApplies(rainbowModelWithScenarios)) {
			Constraint constraint = getResponseMeasure().getConstraint();
			boolean isBroken = false;
			String expValueAsString = NO_DATA;
			Double expValue = rainbowModelWithScenarios.getExponentialValueForConstraint(constraint);
			if (expValue != null) {
				expValueAsString = expValue.toString();
				isBroken = !constraint.holds(expValue);
			}

			String exponentialQuantifierApplied = this.getExpPropertyPrefix(constraint);

			log(Level.INFO, "Scenario " + this.getName() + " broken for " + exponentialQuantifierApplied + " "
					+ expValueAsString + "? " + isBroken);
			return isBroken;
		}
		return false;
	}

	private boolean anyEnvironmentApplies(ArcoIrisModel rainbowModelWithScenarios) {
		boolean holds = false;
		for (Environment environment : this.getEnvironments()) {
			if (environment.holds(rainbowModelWithScenarios)) {
				holds = true;
				break;
			}
		}
		return holds;
	}

	public boolean isBrokenAfterStrategy(final ArcoIrisModel rainbowModelWithScenarios,
			Map<String, Double> strategyAggregateAttributes) {
		// It is not necessary to check the environment at this point because this scenario was already selected for
		// being repaired
		Constraint constraint = getResponseMeasure().getConstraint();
		boolean isBroken = false;
		String expValueAfterStrategyAsString = NO_DATA;
		Double expValue = rainbowModelWithScenarios.getExponentialValueForConstraint(constraint);
		if (expValue != null) {
			Double concernDiffAfterStrategy = strategyAggregateAttributes.get(getConcern().getRainbowName());
			Double estimatedExpValueAfterStrategy = expValue + concernDiffAfterStrategy;
			expValueAfterStrategyAsString = estimatedExpValueAfterStrategy.toString();
			isBroken = !constraint.holds(estimatedExpValueAfterStrategy);
		}

		String exponentialQuantifierApplied = this.getExpPropertyPrefix(constraint);

		log(Level.INFO, "Scenario " + this.getName() + " broken after strategy for simulated "
				+ exponentialQuantifierApplied + " " + expValueAfterStrategyAsString + "? " + isBroken);
		return isBroken;
	}

	/**
	 * Obtains the exponential value (e.g. [EAvg], [ESum], etc...) from the constraint passed as parameter.
	 * 
	 * @param constraint
	 *            the constraint to ask for this data.
	 * @return if this kind of information is not supported by the class, it returns a general string that reads
	 *         "exponential value", if it's supported, then something like EAvg] or [ESum] is returned.
	 */
	private String getExpPropertyPrefix(Constraint constraint) {
		String exponentialQuantifierApplied = "exponential value";
		if (NumericBinaryRelationalConstraint.class.isAssignableFrom(constraint.getClass())) {
			exponentialQuantifierApplied = ((NumericBinaryRelationalConstraint) constraint).getQuantifier()
					.getExpPropertyPrefix();
		}
		return exponentialQuantifierApplied;
	}

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEnginePanel(m_logger, level, txt, t);
	}

	@Override
	protected List<ValidationError> collectValidationErrors() {
		List<ValidationError> validationErrors = super.collectValidationErrors();

		Assert.notNullAndValid(this.repairStrategies, VALIDATION_MSG_REPAIR_STRATEGIES, validationErrors);

		return validationErrors;
	}

}