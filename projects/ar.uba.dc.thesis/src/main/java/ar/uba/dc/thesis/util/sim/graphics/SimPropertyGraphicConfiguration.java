package ar.uba.dc.thesis.util.sim.graphics;

import ar.uba.dc.thesis.rainbow.constraint.numerical.Quantifier;

public class SimPropertyGraphicConfiguration {

	private String property;

	private String eavgPropertyName;

	public SimPropertyGraphicConfiguration(String artifact, String property) {
		this(artifact, property, Quantifier.IN_AVERAGE);
	}

	public SimPropertyGraphicConfiguration(String artifactName, String property, Quantifier quantifier) {
		super();
		this.property = property;
		this.eavgPropertyName = quantifier.getExpPropertyPrefix() + artifactName + "." + property;
	}

	public String getProperty() {
		return property;
	}

	public String getEavgPropertyName() {
		return eavgPropertyName;
	}

}
