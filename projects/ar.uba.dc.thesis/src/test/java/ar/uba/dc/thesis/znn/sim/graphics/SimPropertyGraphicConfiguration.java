package ar.uba.dc.thesis.znn.sim.graphics;

public class SimPropertyGraphicConfiguration {

	private String systemName;

	private String artifact;

	private String property;

	private GraphicQuantifier quantifier;

	public SimPropertyGraphicConfiguration(String systemName, String artifact, String property,
			GraphicQuantifier quantifier) {
		super();
		this.systemName = systemName;
		this.artifact = artifact;
		this.property = property;
		this.quantifier = quantifier;
	}

	public String getSystemName() {
		return systemName;
	}

	public String getArtifact() {
		return artifact;
	}

	public String getProperty() {
		return property;
	}

	public GraphicQuantifier getQuantifier() {
		return quantifier;
	}

}
