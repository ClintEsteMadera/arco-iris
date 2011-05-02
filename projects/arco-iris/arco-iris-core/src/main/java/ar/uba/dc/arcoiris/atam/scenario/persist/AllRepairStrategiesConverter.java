package ar.uba.dc.arcoiris.atam.scenario.persist;

import ar.uba.dc.arcoiris.selfhealing.repair.AllRepairStrategies;

import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * Specific converter for the singleton instance of {@link AllRepairStrategies} class.
 */
public class AllRepairStrategiesConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(AllRepairStrategies.class);
	}

	public String toString(Object obj) {
		return "";
	}

	public Object fromString(String str) {
		return AllRepairStrategies.getInstance();
	}
}