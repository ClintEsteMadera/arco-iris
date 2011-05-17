package ar.uba.dc.arcoiris.selfhealing.priority;

import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.arcoiris.atam.scenario.SelfHealingConfigurationManager;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public class DefaultScenarioRelativePriorityAssigner implements ScenarioRelativePriorityAssigner {

	private SelfHealingConfigurationManager selfHealingConfigurationManager;

	public DefaultScenarioRelativePriorityAssigner(SelfHealingConfigurationManager selfHealingConfigurationManager) {
		this.selfHealingConfigurationManager = selfHealingConfigurationManager;
	}

	@Override
	public double relativePriority(SelfHealingScenario scenario) {
		double maxPriority = selfHealingConfigurationManager.getMaxPriority();
		double relativePriority = (maxPriority - scenario.getPriority() + 1) / maxPriority;
		logger.debug(String.format("Relative priority for scenario %s with priority %d and max priority %d: %f",
				scenario.getName(), scenario.getPriority(), (int) maxPriority, relativePriority));
		return relativePriority;
	}

	private static RainbowLogger logger = RainbowLoggerFactory.logger(SelfHealingConfigurationManager.class);

}
