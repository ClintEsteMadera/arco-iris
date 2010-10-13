package ar.uba.dc.thesis.selfhealing.repair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ar.uba.dc.thesis.common.ArcoIrisDomainObject;
import ar.uba.dc.thesis.common.validation.ValidationError;
import ar.uba.dc.thesis.common.validation.ValidationException;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("specificRepairStrategies")
public class SpecificRepairStrategies extends ArcoIrisDomainObject implements RepairStrategies {

	private static final ValidationError VALIDATION_ERROR_REPAIR_STRATEGIES_NAMES = new ValidationError(
			"Strategy names cannot be empty");

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
		if (CollectionUtils.isEmpty(this.repairStrategiesNames)) {
			throw new ValidationException(VALIDATION_ERROR_REPAIR_STRATEGIES_NAMES);
		}
	}
}
