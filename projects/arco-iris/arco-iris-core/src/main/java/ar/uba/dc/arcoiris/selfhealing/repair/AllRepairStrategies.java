package ar.uba.dc.arcoiris.selfhealing.repair;

import java.util.List;

import ar.uba.dc.arcoiris.common.ArcoIrisDomainObject;
import ar.uba.dc.arcoiris.common.validation.ValidationException;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("allRepairStrategies")
public final class AllRepairStrategies extends ArcoIrisDomainObject implements RepairStrategies {

	private static final long serialVersionUID = 1L;

	private static final String ALL_AVAILABLE_REPAIR_STRATEGIES = "<ALL AVAILABLE REPAIR STRATEGIES>";

	private static final AllRepairStrategies instance = new AllRepairStrategies();

	private AllRepairStrategies() {
		super();
	}

	public static AllRepairStrategies getInstance() {
		return instance;
	}

	@Override
	public boolean useAllRepairStrategies() {
		return true;
	}

	@Override
	public List<String> getRepairStrategiesNames() {
		throw new RuntimeException("This method should not be invoked");
	}

	@Override
	public String toString() {
		return ALL_AVAILABLE_REPAIR_STRATEGIES;
	}

	@Override
	public void validate() throws ValidationException {
		// do nothing
	}
}