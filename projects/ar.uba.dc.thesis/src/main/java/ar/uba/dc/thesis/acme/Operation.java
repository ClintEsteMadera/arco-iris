package ar.uba.dc.thesis.acme;

import ar.uba.dc.thesis.atam.Stimulus;

public abstract class Operation<C extends Component, RETURN_TYPE> implements Stimulus {

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

	public abstract RETURN_TYPE execute();
}