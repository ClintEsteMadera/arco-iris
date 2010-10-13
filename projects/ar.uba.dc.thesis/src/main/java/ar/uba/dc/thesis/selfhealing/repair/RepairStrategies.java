package ar.uba.dc.thesis.selfhealing.repair;

import java.util.List;

import ar.uba.dc.thesis.common.validation.Validatable;

public interface RepairStrategies extends Validatable {

	boolean useAllRepairStrategies();

	List<String> getRepairStrategiesNames();
}
