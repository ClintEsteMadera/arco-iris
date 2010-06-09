package ar.uba.dc.thesis.selfhealing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.AtamScenario;
import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.atam.ResponseMeasure;
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

	public boolean holdsConsideringAllInstances(final RainbowModelWithScenarios rainbowModelWithScenarios) {
		/*
		 * It is not necessary to check the environment at this point because this scenario was already selected for
		 * being repaired
		 */
		return getResponseMeasure().getConstraint().holdsConsideringAllInstances(rainbowModelWithScenarios);
	}

	/**
	 * Tiene en cuenta la aplicacion de la estrategia sobre las properties involucradas
	 */
	public boolean isEAvgBroken(RainbowModelWithScenarios rainbowModelWithScenarios,
			SortedMap<String, Double> strategyAggregateAttributes) {
		boolean isBroken = false;
		if (isThereAnyEnvironmentApplicable(rainbowModelWithScenarios)) {
			Double concernDiffAfterStrategy = strategyAggregateAttributes.get(getConcern().getRainbowName());
			concernDiffAfterStrategy = (concernDiffAfterStrategy == null) ? 0 : concernDiffAfterStrategy;

			isBroken = !getResponseMeasure().holds4Scoring(rainbowModelWithScenarios, concernDiffAfterStrategy);
		}
		log("Scenario " + this.getName() + " broken? " + isBroken);
		return isBroken;
	}

	private boolean isThereAnyEnvironmentApplicable(final RainbowModelWithScenarios rainbowModelWithScenarios) {
		boolean thereIsAnEnvironmentApplicable = false;

		for (Environment environment : this.getEnvironments()) {
			thereIsAnEnvironmentApplicable = thereIsAnEnvironmentApplicable
					|| environment.holds4Scoring(rainbowModelWithScenarios);
		}
		return thereIsAnEnvironmentApplicable;
	}

	public boolean applyFor(Environment environment) {
		return getEnvironments().contains(Environment.ANY_ENVIRONMENT) || getEnvironments().contains(environment);
	}

	protected void log(String txt) {
		// Oracle.instance().writeEnginePanel(m_logger, txt);
	}

}