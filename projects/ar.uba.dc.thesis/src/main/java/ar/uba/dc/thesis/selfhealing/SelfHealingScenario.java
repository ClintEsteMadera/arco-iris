package ar.uba.dc.thesis.selfhealing;

import java.util.ArrayList;
import java.util.Collection;
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
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.atam.scenario.model.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;

public class SelfHealingScenario extends AtamScenario {

	private boolean enabled;

	private int priority;

	private final List<String> repairStrategies = new ArrayList<String>();

	private static RainbowLogger m_logger = RainbowLoggerFactory.logger(SelfHealingScenario.class);

	public SelfHealingScenario() {
		super();
	}

	public SelfHealingScenario(Long id, String name, Concern concern, String stimulusSource, String stimulus,
			Set<Environment> environments, Artifact artifact, String response, ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions, boolean enabled, int priority) {
		super(id, name, concern, stimulusSource, stimulus, environments, artifact, response, responseMeasure,
				architecturalDecisions);
		this.enabled = enabled;
		this.priority = priority;

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

	public SelfHealingScenario addRepairStrategy(String repairStrategy) {
		this.repairStrategies.add(repairStrategy);
		return this;
	}

	public boolean applyFor(Environment environment) {
		return getEnvironments().contains(Environment.ANY_ENVIRONMENT) || getEnvironments().contains(environment);
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
		boolean isBroken = !getResponseMeasure().getConstraint()
				.holdsConsideringAllInstances(rainbowModelWithScenarios);
		log(Level.INFO, "Scenario " + this.getName() + " broken "
				+ getResponseMeasure().getConstraint().getQuantifier() + "? " + isBroken);
		return isBroken;
	}

	/**
	 * Tiene en cuenta la aplicacion de la estrategia sobre las properties involucradas.
	 * <p>
	 * Este método está pensado para ser usado únicamente por una instancia de {@link ScenarioBrokenDetector}. Por eso
	 * es protected.
	 */
	protected boolean isEAvgBroken(RainbowModelWithScenarios rainbowModelWithScenarios,
			SortedMap<String, Double> strategyAggregateAttributes) {
		boolean isBroken = false;
		if (isThereAnyEnvironmentApplicable(rainbowModelWithScenarios)) {
			Double concernDiffAfterStrategy = strategyAggregateAttributes.get(getConcern().getRainbowName());
			concernDiffAfterStrategy = (concernDiffAfterStrategy == null) ? 0 : concernDiffAfterStrategy;

			isBroken = !getResponseMeasure().holds4Scoring(rainbowModelWithScenarios, concernDiffAfterStrategy);
		}
		log(Level.INFO, "Scenario " + this.getName() + " broken IN SIMULATION? " + isBroken);
		return isBroken;
	}

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEnginePanel(m_logger, level, txt, t);
	}

	private boolean isThereAnyEnvironmentApplicable(final RainbowModelWithScenarios rainbowModelWithScenarios) {
		boolean thereIsAnEnvironmentApplicable = false;

		for (Environment environment : this.getEnvironments()) {
			thereIsAnEnvironmentApplicable = thereIsAnEnvironmentApplicable
					|| environment.holds4Scoring(rainbowModelWithScenarios);
		}
		return thereIsAnEnvironmentApplicable;
	}

}