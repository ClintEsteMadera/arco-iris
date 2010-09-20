package ar.uba.dc.thesis.selfhealing;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Level;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.atam.scenario.model.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.atam.scenario.model.AtamScenario;
import ar.uba.dc.thesis.atam.scenario.model.DefaultEnvironment;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.atam.scenario.model.ResponseMeasure;
import ar.uba.dc.thesis.atam.scenario.model.Stimulus;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.thesis.selfhealing.repair.AllRepairStrategies;
import ar.uba.dc.thesis.selfhealing.repair.RepairStrategies;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("selfHealingScenario")
public class SelfHealingScenario extends AtamScenario {

	private static final long serialVersionUID = 1L;

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
			ResponseMeasure responseMeasure, Set<ArchitecturalDecision> architecturalDecisions, boolean enabled,
			int priority, RepairStrategies repairStrategies) {
		super(id, name, concern, stimulus, environments, artifact, response, responseMeasure, architecturalDecisions);
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
		return getEnvironments().contains(DefaultEnvironment.getInstance()) || getEnvironments().contains(environment);
	}

	/**
	 * Este método es el que normalmente se usa para determinar si un escenario está roto.
	 * <p>
	 * Este método está pensado para ser usado únicamente por una instancia de {@link ScenarioBrokenDetector}. Por eso
	 * es protected.
	 */
	protected boolean isBroken(final RainbowModelWithScenarios rainbowModelWithScenarios) {
		// It is not necessary to check the environment at this point because this scenario was already selected for
		// being repaired
		Constraint constraint = getResponseMeasure().getConstraint();
		boolean isBroken = false;
		String expValueAsString = "(NO DATA)";
		Double expValue = this.getExponentialValueForConstraint(constraint, rainbowModelWithScenarios);
		if (expValue != null) {
			expValueAsString = expValue.toString();
			isBroken = !constraint.holds(expValue);
		}

		String exponentialQuantifierApplied = this.getExpPropertyPrefix(constraint);

		log(Level.INFO, "Scenario " + this.getName() + " broken for " + exponentialQuantifierApplied + " "
				+ expValueAsString + "? " + isBroken);
		return isBroken;
	}

	protected boolean isBrokenAfterStrategy(final RainbowModelWithScenarios rainbowModelWithScenarios,
			SortedMap<String, Double> strategyAggregateAttributes) {
		// It is not necessary to check the environment at this point because this scenario was already selected for
		// being repaired
		Constraint constraint = getResponseMeasure().getConstraint();
		boolean isBroken = false;
		String expValueAfterStrategyAsString = "(NO DATA)";
		Double expValue = this.getExponentialValueForConstraint(constraint, rainbowModelWithScenarios);
		if (expValue != null) {
			Double concernDiffAfterStrategy = strategyAggregateAttributes.get(getConcern().getRainbowName());
			Double expValueAfterStrategy = expValue + concernDiffAfterStrategy;
			expValueAfterStrategyAsString = expValueAfterStrategy.toString();
			isBroken = !constraint.holds(expValueAfterStrategy);
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

	private Double getExponentialValueForConstraint(Constraint constraint,
			RainbowModelWithScenarios rainbowModelWithScenarios) {
		Double result = null;
		if (constraint instanceof NumericBinaryRelationalConstraint) {
			NumericBinaryRelationalConstraint numericConstraint = (NumericBinaryRelationalConstraint) constraint;
			result = (Double) rainbowModelWithScenarios.getProperty(numericConstraint.getExponentialPropertyName());
		}
		return result;
	}

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEnginePanel(m_logger, level, txt, t);
	}

	// FIXME no deberia usarse???
	private boolean isThereAnyEnvironmentApplicable(final RainbowModelWithScenarios rainbowModelWithScenarios) {
		boolean thereIsAnEnvironmentApplicable = false;

		for (Environment environment : this.getEnvironments()) {
			thereIsAnEnvironmentApplicable = thereIsAnEnvironmentApplicable
					|| environment.holds4Scoring(rainbowModelWithScenarios);
		}
		return thereIsAnEnvironmentApplicable;
	}
}