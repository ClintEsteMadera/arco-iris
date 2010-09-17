package ar.uba.dc.thesis.atam.scenario.persist;

import ar.uba.dc.thesis.selfhealing.repair.AllRepairStrategies;

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