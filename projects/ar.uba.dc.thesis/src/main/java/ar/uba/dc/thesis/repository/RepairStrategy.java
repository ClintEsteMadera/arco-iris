package ar.uba.dc.thesis.repository;

import java.util.ArrayList;
import java.util.List;

import org.sa.rainbow.adaptation.AdaptationManagerWithScenarios;

/**
 * 
 * TODO La GUI tiene que parsear el .s de estrategias (ver
 * {@link AdaptationManagerWithScenarios#initAdaptationRepertoire}
 */
public enum RepairStrategy {

	QUICK_DIRTY_REDUCE_RESPONSE_TIME("QuickDirtyReduceResponseTime"),
	BRUTE_REDUCE_RESPONSE_TIME("BruteReduceResponseTime"),
	VARIED_REDUCE_RESPONSE_TIME("VariedReduceResponseTime"),
	SMARTER_REDUCE_RESPONSE_TIME("SmarterReduceResponseTime"),
	SOPHISTICATED_REDUCE_RESPONSE_TIME("SophisticatedReduceResponseTime"),
	REDUCE_OVERALL_COST("ReduceOverallCost"),
	IMPROVE_OVERALL_FIDELITY("ImproveOverallFidelity");

	private static List<String> repairStrategiesNames = initializeAllRepairStrategiesNames();
	private final String strategyName;

	RepairStrategy(String strategyName) {
		this.strategyName = strategyName;
	}

	@Override
	public String toString() {
		return this.getStrategyName();
	}

	public String getStrategyName() {
		return strategyName;
	}

	public static List<String> getAllRepairStrategiesNames() {
		return repairStrategiesNames;
	}

	private static List<String> initializeAllRepairStrategiesNames() {
		repairStrategiesNames = new ArrayList<String>(values().length);
		for (RepairStrategy repairStrategy : values()) {
			repairStrategiesNames.add(repairStrategy.getStrategyName());
		}
		return repairStrategiesNames;
	}
}