package ar.uba.dc.arcoiris.selfhealing.repair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ar.uba.dc.arcoiris.common.ArcoIrisDomainObject;
import ar.uba.dc.arcoiris.common.validation.ValidationError;
import ar.uba.dc.arcoiris.common.validation.ValidationException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("specificRepairStrategies")
public class SpecificRepairStrategies extends ArcoIrisDomainObject implements RepairStrategies {

	private static final long serialVersionUID = 1L;

	private static final ValidationError VALIDATION_ERROR_REPAIR_STRATEGIES_NAMES = new ValidationError(
			"At least one strategy name must be provided");

	@XStreamImplicit(itemFieldName = "repairStrategy")
	private List<String> repairStrategiesNames;

	/**
	 * This constructor is only meant to be used by XStream
	 */
	@SuppressWarnings("unused")
	private SpecificRepairStrategies() {
		super();
		this.repairStrategiesNames = new ArrayList<String>();
	}

	public SpecificRepairStrategies(String... repairStrategiesNames) {
		this(Arrays.asList(repairStrategiesNames));
	}

	public SpecificRepairStrategies(List<String> repairStrategiesNames) {
		super();
		this.repairStrategiesNames = repairStrategiesNames;

		this.validate();
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
		return this.repairStrategiesNames.toString();
	}

	@Override
	public void validate() throws ValidationException {
		if (this.repairStrategiesNames == null) {
			throw new ValidationException(VALIDATION_ERROR_REPAIR_STRATEGIES_NAMES);
		}
	}
}
