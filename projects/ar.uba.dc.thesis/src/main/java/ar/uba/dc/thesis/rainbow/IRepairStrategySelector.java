package ar.uba.dc.thesis.rainbow;

import ar.uba.dc.thesis.selfhealing.RepairStrategySpecification;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public interface IRepairStrategySelector {

	RepairStrategySpecification selectRepairStrategyFor(SelfHealingScenario scenario);
}
