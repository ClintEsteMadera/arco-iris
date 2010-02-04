package ar.uba.dc.thesis.acme;

import ar.uba.dc.thesis.atam.Artifact;

@SuppressWarnings("unchecked")
public class Component implements Artifact {

	private final String name;

	private final String systemName;

	public Component(String systemName, String name) {
		super();
		this.systemName = systemName;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getSystemName() {
		return systemName;
	}

}