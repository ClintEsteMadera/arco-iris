package ar.uba.dc.thesis.rainbow;

import ar.uba.dc.thesis.selfhealing.RepairStrategySpecification;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;
import ar.uba.dc.thesis.selfhealing.SelfHealingTradeoff;

/**
 * Takes the <code>RepairStrategy</code> insertion order in the <code>SelfHealingScenario</code>
 */
public class OrderedRepairStrategySelector implements IRepairStrategySelector {

	public RepairStrategySpecification selectRepairStrategyFor(SelfHealingScenario scenario) {
		for (RepairStrategySpecification repairStrategySpec : scenario.getRepairStrategySpecs()) {
			if (!affectsAHigherPriorityScenario(repairStrategySpec, scenario)) {
				return repairStrategySpec;
			}
		}

		return null;
	}

	private boolean affectsAHigherPriorityScenario(RepairStrategySpecification spec,
			SelfHealingScenario scenarioToRepair) {
		SelfHealingTradeoff selfHealingTradeoff = spec.getSelfHealingTradeoff();

		for (SelfHealingScenario affectedScenario : selfHealingTradeoff.getAffects()) {
			if (affectedScenario.getPriority() > scenarioToRepair.getPriority()) {
				return true;
			}
		}

		for (SelfHealingScenario brokenScenario : selfHealingTradeoff.getBreaks()) {
			if (brokenScenario.getPriority() > scenarioToRepair.getPriority()) {
				return true;
			}
		}

		return false;
	}

}
