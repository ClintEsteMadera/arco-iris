package ar.uba.dc.thesis.atam.scenario.persist;

import ar.uba.dc.thesis.atam.scenario.model.DefaultEnvironment;

import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * Specific converter for the singleton instance of {@link DefaultEnvironment} class.
 */
public class DefaultEnvironmentConverter implements SingleValueConverter {

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class type) {
		return type.equals(DefaultEnvironment.class);
	}

	public String toString(Object obj) {
		return "";
	}

	public Object fromString(String str) {
		return DefaultEnvironment.getInstance();
	}
}