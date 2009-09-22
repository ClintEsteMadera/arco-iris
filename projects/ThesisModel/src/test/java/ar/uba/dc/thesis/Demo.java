package ar.uba.dc.thesis;

import ar.uba.dc.thesis.rainbow.OrderedRepairStrategySelector;
import ar.uba.dc.thesis.rainbow.RepairHandler;

public class Demo {

	public static void main(String[] args) {
		// Supongamos que se rompio heavyLoadScenario y lo detectamos:
		RepairHandler repairHandler = new RepairHandler(ArchitectureRepository.getDemoArchitecture(),
				new OrderedRepairStrategySelector());
		repairHandler.repairScenario(SelfHealingScenarioRepository.HEAVY_LOAD_SCENARIO);
	}
}