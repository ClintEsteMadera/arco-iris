package ar.uba.dc.thesis;

import java.util.Collections;

import ar.uba.dc.thesis.selfhealing.RepairStrategySpecification;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;
import ar.uba.dc.thesis.selfhealing.SelfHealingTradeoff;

public class RepairStrategySpecRepository {

	public static RepairStrategySpecification createRepairStrategySpec(SelfHealingScenario affectedScenario,
			String repairStrategyName) {

		SelfHealingTradeoff tradeoff = new SelfHealingTradeoff(Collections.singletonList(affectedScenario), Collections
				.<SelfHealingScenario> emptyList());

		return new RepairStrategySpecification(repairStrategyName, new String[] {}, tradeoff);
	}

	public static RepairStrategySpecification getDisableVideosRepairStrategy() {
		return createRepairStrategySpec(SelfHealingScenarioRepository.USABILITY_SCENARIO, "disableVideos");
	}

	public static RepairStrategySpecification getDisableDynamicContentRepairStrategy() {
		return createRepairStrategySpec(SelfHealingScenarioRepository.USABILITY_SCENARIO, "disableDynamicContent");
	}
}
