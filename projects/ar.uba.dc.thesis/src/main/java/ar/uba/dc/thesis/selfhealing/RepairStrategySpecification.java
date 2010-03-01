package ar.uba.dc.thesis.selfhealing;

import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.ThesisPojo;

public class RepairStrategySpecification extends ThesisPojo {

	private final String repairStrategyName;

	// TODO es necesario los parametros en la estrategia?
	private final String[] params;

	public RepairStrategySpecification(String repairStrategyName, String[] params) {
		super();
		this.repairStrategyName = repairStrategyName;
		this.params = params;

		this.validate();
	}

	public String getRepairStrategyName() {
		return this.repairStrategyName;
	}

	public String[] getParams() {
		return this.params;
	}

	public void validate() {
		if (StringUtils.isBlank(this.getRepairStrategyName())) {
			throw new IllegalArgumentException("The name of the repair strategy cannot be empty");
		}
	}
}