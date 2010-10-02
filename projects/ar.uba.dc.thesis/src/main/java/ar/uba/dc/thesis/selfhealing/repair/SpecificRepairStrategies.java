package ar.uba.dc.thesis.selfhealing.repair;

import java.util.Arrays;
import java.util.List;

import ar.uba.dc.thesis.common.ThesisPojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("specificRepairStrategies")
public class SpecificRepairStrategies extends ThesisPojo implements RepairStrategies {

	private static final long serialVersionUID = 1L;

	@XStreamImplicit(itemFieldName = "repairStrategy")
	private List<String> repairStrategiesNames;

	public SpecificRepairStrategies(String... repairStrategiesNames) {
		this(Arrays.asList(repairStrategiesNames));
	}

	public SpecificRepairStrategies(List<String> repairStrategiesNames) {
		super();
		this.repairStrategiesNames = repairStrategiesNames;
	}

	@Override
	public boolean useAllRepairStrategies() {
		return false;
	}

	@Override
	public List<String> getRepairStrategiesNames() {
		return this.repairStrategiesNames;
	}

	@Override
	public String toString() {
		String result = "";
		if (this.repairStrategiesNames != null) {
			result = this.repairStrategiesNames.toString();
		}
		return result;
	}
}
