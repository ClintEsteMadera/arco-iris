package ar.uba.dc.thesis.selfhealing.repair;

import java.util.List;

public interface RepairStrategies {

	boolean useAllRepairStrategies();

	List<String> getRepairStrategiesNames();
}
