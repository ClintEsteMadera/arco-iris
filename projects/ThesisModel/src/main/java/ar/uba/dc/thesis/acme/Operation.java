package ar.uba.dc.thesis.acme;

import ar.uba.dc.thesis.atam.Artifact;

public abstract class Operation<C extends Artifact> {

	private final String name;
	private final C component;

	public Operation(String name, C component) {
		super();
		this.name = name;
		this.component = component;

	}

	public String getName() {
		return this.name;
	}

	public C getComponent() {
		return this.component;
	}

	public abstract void execute();
}