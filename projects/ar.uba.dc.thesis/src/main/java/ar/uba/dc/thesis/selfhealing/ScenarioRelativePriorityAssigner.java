package ar.uba.dc.thesis.selfhealing;

import ar.uba.dc.thesis.atam.scenario.SelfHealingConfigurationManager;

public class ScenarioRelativePriorityAssigner {

	private SelfHealingConfigurationManager selfHealingConfigurationManager;

	public ScenarioRelativePriorityAssigner(SelfHealingConfigurationManager selfHealingConfigurationManager) {
		this.selfHealingConfigurationManager = selfHealingConfigurationManager;
	}

	public int relativePriority(SelfHealingScenario scenario) {
		int maxPriority = selfHealingConfigurationManager.getMaxPriority() + 1;
		return (maxPriority - scenario.getPriority()) / maxPriority;
	}

}
