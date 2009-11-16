package ar.uba.dc.thesis.repository;

import java.util.Collections;

import ar.uba.dc.thesis.selfhealing.RepairStrategySpecification;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;
import ar.uba.dc.thesis.selfhealing.SelfHealingTradeoff;

public class RepairStrategySpecRepository {

	public static RepairStrategySpecification getReduceOverallCostRepairStrategy() {
		return createRepairStrategySpec(SelfHealingScenarioRepository.SERVER_COST_SCENARIO, "reduceOverallCost");
	}

	private static RepairStrategySpecification createRepairStrategySpec(SelfHealingScenario affectedScenario,
			String repairStrategyName) {

		SelfHealingTradeoff tradeoff = new SelfHealingTradeoff(Collections.singletonList(affectedScenario), Collections
				.<SelfHealingScenario> emptyList());

		return new RepairStrategySpecification(repairStrategyName, new String[] {}, tradeoff);
	}
}