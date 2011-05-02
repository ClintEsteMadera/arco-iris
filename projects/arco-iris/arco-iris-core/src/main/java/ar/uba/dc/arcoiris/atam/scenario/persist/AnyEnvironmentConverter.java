package ar.uba.dc.arcoiris.atam.scenario.persist;

import ar.uba.dc.arcoiris.atam.scenario.model.AnyEnvironment;

import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * Specific converter for the singleton instance of {@link AnyEnvironment} class.
 */
public class AnyEnvironmentConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(AnyEnvironment.class);
	}

	public String toString(Object obj) {
		return "";
	}

	public Object fromString(String str) {
		return AnyEnvironment.getInstance();
	}
}