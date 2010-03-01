package ar.uba.dc.thesis.atam;

import ar.uba.dc.thesis.common.ThesisPojo;

public class Artifact extends ThesisPojo {
	private final String name;

	private final String systemName;

	public Artifact(String systemName, String name) {
		super();
		this.systemName = systemName;
		this.name = name;

		this.validate();
	}

	public String getName() {
		return this.name;
	}

	public String getSystemName() {
		return systemName;
	}

	public void validate() {
		// TODO Implement me!
	}
}
