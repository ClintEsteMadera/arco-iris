package ar.uba.dc.thesis.selfhealing;

import java.util.ArrayList;
import java.util.Arrays;
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
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("selfHealingScenario")
public class SelfHealingScenario extends AtamScenario {

	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private boolean enabled;

	@XStreamAsAttribute
	private int priority;

	@XStreamImplicit(itemFieldName = "repairStrategy")
	private final List<String> repairStrategies;

	private static RainbowLogger m_logger = RainbowLoggerFactory.logger(SelfHealingScenario.class);

	public SelfHealingScenario() {
		super();
		this.repairStrategies = new ArrayList<String>();
	}

	public SelfHealingScenario(Long id, String name, Concern concern, String stimulusSource, String stimulus,
			List<? extends Environment> environments, Artifact artifact, String response,
			ResponseMeasure responseMeasure, Set<ArchitecturalDecision> architecturalDecisions, boolean enabled,
			int priority) {
		super(id, name, concern, stimulusSource, stimulus, environments, artifact, response, responseMeasure,
				architecturalDecisions);
		this.enabled = enabled;
		this.priority = priority;
		this.repairStrategies = new ArrayList<String>();

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

	public List<String> getRepairStrategies() {
		return this.repairStrategies;
	}

	public void addRepairStrategy(String... repairStrategy) {
		this.repairStrategies.addAll(Arrays.asList(repairStrategy));
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
		/*
		 * It is not necessary to check the environment at this point because this scenario was already selected for
		 * being repaired
		 */

		Constraint constraint = getResponseMeasure().getConstraint();
		boolean isBroken = false;
		String eavgString = "(NO DATA)";
		Double eavg = getEavgForConstraint(constraint, rainbowModelWithScenarios);
		if (eavg != null) {
			eavgString = eavg.toString();
			isBroken = !constraint.holds(eavg);
		}
		log(Level.INFO, "Scenario " + this.getName() + " broken for eavg " + eavgString + "? " + isBroken);
		return isBroken;
	}

	protected boolean isBrokenAfterStrategy(final RainbowModelWithScenarios rainbowModelWithScenarios,
			SortedMap<String, Double> strategyAggregateAttributes) {
		/*
		 * It is not necessary to check the environment at this point because this scenario was already selected for
		 * being repaired
		 */

		Constraint constraint = getResponseMeasure().getConstraint();
		boolean isBroken = false;
		String eavgAfterStrategyString = "(NO DATA)";
		Double eavg = getEavgForConstraint(constraint, rainbowModelWithScenarios);
		if (eavg != null) {
			Double concernDiffAfterStrategy = strategyAggregateAttributes.get(getConcern().getRainbowName());
			Double eavgAfterStrategy = eavg + concernDiffAfterStrategy;
			eavgAfterStrategyString = eavgAfterStrategy.toString();
			isBroken = !constraint.holds(eavgAfterStrategy);
		}

		log(Level.INFO, "Scenario " + this.getName() + " broken after strategy for simulated eavg "
				+ eavgAfterStrategyString + "? " + isBroken);
		return isBroken;
	}

	private Double getEavgForConstraint(Constraint constraint, RainbowModelWithScenarios rainbowModelWithScenarios) {
		Double result = null;
		if (constraint instanceof NumericBinaryRelationalConstraint) {
			NumericBinaryRelationalConstraint numericConstraint = (NumericBinaryRelationalConstraint) constraint;
			result = (Double) rainbowModelWithScenarios.getProperty(numericConstraint.getEAvgPropertyName());
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