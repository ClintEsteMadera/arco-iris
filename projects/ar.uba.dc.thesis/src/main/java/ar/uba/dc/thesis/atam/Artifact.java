package ar.uba.dc.thesis.atam;

import ar.uba.dc.thesis.common.ThesisPojo;

public class Artifact extends ThesisPojo {
	private final String name;

	private final String systemName;

	public Artifact(String systemName, String typeName) {
		super();
		this.systemName = systemName;
		this.name = typeName;

		this.validate();
	}

	public String getName() {
		return this.name;
	}

	public String getSystemName() {
		return systemName;
	}

	public void validate() {
		// Do nothing
	}
}
