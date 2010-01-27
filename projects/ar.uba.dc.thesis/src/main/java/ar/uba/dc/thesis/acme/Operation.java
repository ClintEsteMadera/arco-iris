package ar.uba.dc.thesis.acme;

public abstract class Operation<C extends Component> {

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
}