package ar.uba.dc.arcoiris.selfhealing.repair;

import java.util.List;

import ar.uba.dc.arcoiris.common.validation.Validatable;

public interface RepairStrategies extends Validatable {

	boolean useAllRepairStrategies();

	List<String> getRepairStrategiesNames();
}
