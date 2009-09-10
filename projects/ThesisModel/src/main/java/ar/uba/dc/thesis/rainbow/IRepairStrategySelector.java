package ar.uba.dc.thesis.rainbow;

import ar.uba.dc.thesis.selfhealing.RepairStrategy;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public interface IRepairStrategySelector {

	RepairStrategy selectRepairStrategyFor(SelfHealingScenario scenario);
}
