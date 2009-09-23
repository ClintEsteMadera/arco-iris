package ar.uba.dc.thesis;

import ar.uba.dc.thesis.rainbow.ConstraintEvaluator;
import ar.uba.dc.thesis.rainbow.OrderedRepairStrategySelector;
import ar.uba.dc.thesis.rainbow.RepairHandler;

public class Demo {

	public static void main(String[] args) {
		RepairHandler repairHandler = new RepairHandler(ArchitectureRepository.getDemoArchitecture(),
				new OrderedRepairStrategySelector());
		long checkInterval = 10000;
		ConstraintEvaluator constraintEvaluator = new ConstraintEvaluator(repairHandler, checkInterval);
		constraintEvaluator.start();
		// assume that heavyLoadScenario is broken and SelfHealing starts working:
		repairHandler.repairScenario(SelfHealingScenarioRepository.HEAVY_LOAD_SCENARIO);
	}
}