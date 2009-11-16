package ar.uba.dc.thesis.repository;

import java.util.HashMap;
import java.util.Map;

import ar.uba.dc.thesis.selfhealing.RepairStrategy;

public class RepairStrategyRepository {

	private static final String DISABLE_FLASH_OPERATION_NAME = "disableFlash";

	private static final String DISABLE_VIDEOS_OPERATION_NAME = "disableVideos";

	private static Map<String, RepairStrategy> repairStrategies = new HashMap<String, RepairStrategy>();

	static {
		initRepairStrategies();
	}

	public static RepairStrategy getRepairStrategy(String repairStrategyName) {
		return repairStrategies.get(repairStrategyName);
	}

	public static void addRepairStrategy(RepairStrategy repairStrategy) {
		repairStrategies.put(repairStrategy.getName(), repairStrategy);
	}

	private static void initRepairStrategies() {
		String pageRendererName = ComponentRepository.getProxy().getName();
		String code = pageRendererName + "." + DISABLE_VIDEOS_OPERATION_NAME + ";" + pageRendererName + "."
				+ DISABLE_FLASH_OPERATION_NAME;
		RepairStrategy disableDynamicContent = new RepairStrategy("disableDynamicContent", code);

		addRepairStrategy(disableDynamicContent);

		code = pageRendererName + "." + DISABLE_VIDEOS_OPERATION_NAME + ";";
		RepairStrategy disableVideos = new RepairStrategy("disableVideos", code);

		RepairStrategyRepository.addRepairStrategy(disableVideos);
	}
}
