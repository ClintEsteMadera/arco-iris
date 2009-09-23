package ar.uba.dc.thesis;

import ar.uba.dc.thesis.rainbow.OrderedRepairStrategySelector;
import ar.uba.dc.thesis.rainbow.RepairHandler;

public class Demo {

	public static void main(String[] args) {
		RepairHandler repairHandler = new RepairHandler(ArchitectureRepository.getDemoArchitecture(),
				new OrderedRepairStrategySelector());
		// assume that heavyLoadScenario is broken and SelfHealing starts working:
		repairHandler.repairScenario(SelfHealingScenarioRepository.HEAVY_LOAD_SCENARIO);
	}
}