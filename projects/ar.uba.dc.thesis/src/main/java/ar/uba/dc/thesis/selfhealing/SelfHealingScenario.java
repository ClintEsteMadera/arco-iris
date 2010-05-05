package ar.uba.dc.thesis.selfhealing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

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

	public void addRepairStrategy(String repairStrategy) {
		this.repairStrategies.add(repairStrategy);
	}

	public boolean isBroken(final RainbowModelWithScenarios rainbowModelWithScenarios) {
		boolean isBroken = false;
		if (anyEnvironmentApplies(rainbowModelWithScenarios)) {
			isBroken = !getResponseMeasure().holds(rainbowModelWithScenarios);
		}
		return isBroken;
	}

	/**
	 * Tiene en cuenta la aplicacion de la estrategia sobre las properties involucradas
	 */
	public boolean isBroken(RainbowModelWithScenarios rainbowModelWithScenarios,
			SortedMap<String, Double> strategyAggregateAttributes) {
		boolean isBroken = false;
		if (anyEnvironmentApplies(rainbowModelWithScenarios)) {
			Double concernDiffAfterStrategy = strategyAggregateAttributes.get(getConcern().getRainbowName());
			boolean strategyAffectsConcern = concernDiffAfterStrategy != null && !concernDiffAfterStrategy.equals(0.0);
			if (strategyAffectsConcern) {
				isBroken = !getResponseMeasure().holds(rainbowModelWithScenarios, concernDiffAfterStrategy);
			} else {
				isBroken = !getResponseMeasure().holds(rainbowModelWithScenarios);
			}
		}
		return isBroken;
	}

	private boolean anyEnvironmentApplies(final RainbowModelWithScenarios rainbowModelWithScenarios) {
		boolean anyEnvironmentApplies = false;

		for (Environment environment : this.getEnvironments()) {
			anyEnvironmentApplies = anyEnvironmentApplies || environment.holds(rainbowModelWithScenarios);
		}
		return anyEnvironmentApplies;
	}

	public boolean applyFor(Environment environment) {
		return getEnvironments().contains(environment);
	}

}