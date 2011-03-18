package ar.uba.dc.thesis.selfhealing;

import ar.uba.dc.thesis.atam.scenario.SelfHealingConfigurationManager;

public class DefaultScenarioRelativePriorityAssigner implements ScenarioRelativePriorityAssigner {

	private SelfHealingConfigurationManager selfHealingConfigurationManager;

	public DefaultScenarioRelativePriorityAssigner(SelfHealingConfigurationManager selfHealingConfigurationManager) {
		this.selfHealingConfigurationManager = selfHealingConfigurationManager;
	}

	@Override
	public int relativePriority(SelfHealingScenario scenario) {
		int maxPriority = selfHealingConfigurationManager.getMaxPriority() + 1;
		return (maxPriority - scenario.getPriority()) / maxPriority;
	}

}
