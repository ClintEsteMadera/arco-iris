package ar.uba.dc.thesis.rainbow;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.selfhealing.RepairStrategy;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class RepairHandler {

	private final IRepairStrategySelector repairStrategySelector;

	// TODO: permitir seleccionar la implementacion de repairStrategySelector a utilizar
	private static final RepairHandler instance = new RepairHandler(new OrderedRepairStrategySelector());

	// TODO: continuar implementacion de RepairHandler!

	public void repairScenario(SelfHealingScenario scenario) {
		RepairStrategy selectedRepairStrategy = repairStrategySelector.selectRepairStrategyFor(scenario);
		// TODO como llegan aca los parametros de la repairStrategy?
		Artifact[] params = new Artifact[] {};
		selectedRepairStrategy.execute(params);
	}

	public static RepairHandler getInstance() {
		return instance;
	}

	private RepairHandler(IRepairStrategySelector repairStrategySelector) {
		this.repairStrategySelector = repairStrategySelector;
	}

}
