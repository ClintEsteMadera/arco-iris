package ar.uba.dc.thesis.rainbow;

import java.util.Iterator;

import ar.uba.dc.thesis.selfhealing.DummyRepairStrategy;
import ar.uba.dc.thesis.selfhealing.RepairStrategy;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

/**
 * Takes the <code>RepairStrategy</code> insertion order in the <code>SelfHealingScenario</code>
 */
public class OrderedRepairStrategySelector implements IRepairStrategySelector {

	public RepairStrategy selectRepairStrategyFor(SelfHealingScenario scenario) {
		RepairStrategy repairStrategy = DummyRepairStrategy.getInstance();
		Iterator<RepairStrategy> repairStrategies = scenario.getRepairStrategies().keySet().iterator();
		boolean selected = false;
		while (!selected && repairStrategies.hasNext()) {
			repairStrategy = repairStrategies.next();
			if (!affectsMayorScenario(repairStrategy, scenario)) {
				selected = true;
			}
		}
		return repairStrategy;
	}

	// TODO cambiar nombre de metodo
	private boolean affectsMayorScenario(RepairStrategy repairStrategy, SelfHealingScenario scenario) {
		// TODO verificar que no rompa un escenario de mayor prioridad
		return false;
	}

}
