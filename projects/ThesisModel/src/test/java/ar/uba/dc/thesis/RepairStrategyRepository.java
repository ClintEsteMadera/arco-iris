package ar.uba.dc.thesis;

import java.util.HashMap;
import java.util.Map;

import ar.uba.dc.thesis.selfhealing.RepairStrategy;

public class RepairStrategyRepository {

	private static Map<String, RepairStrategy> repairStrategies = new HashMap<String, RepairStrategy>();

	public static RepairStrategy getRepairStrategy(String repairStrategyName) {
		return repairStrategies.get(repairStrategyName);
	}

	public static void addRepairStrategy(RepairStrategy repairStrategy) {
		repairStrategies.put(repairStrategy.getName(), repairStrategy);
	}
}
