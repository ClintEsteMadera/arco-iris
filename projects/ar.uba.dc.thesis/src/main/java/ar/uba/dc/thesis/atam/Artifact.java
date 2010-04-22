package ar.uba.dc.thesis.atam;

import ar.uba.dc.thesis.common.ThesisPojo;

public class Artifact extends ThesisPojo {

	private final String typeName;

	private final String systemName;

	public Artifact(String systemName, String typeName) {
		super();
		this.systemName = systemName;
		this.typeName = typeName;

		this.validate();
	}

	public String getTypeName() {
		return this.typeName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void validate() {
		// Do nothing
	}
}
