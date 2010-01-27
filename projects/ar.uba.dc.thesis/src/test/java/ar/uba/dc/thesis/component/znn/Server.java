package ar.uba.dc.thesis.component.znn;

import ar.uba.dc.thesis.acme.Component;

public class Server extends Component {

	public int load;

	public Fidelity fidelity;

	private boolean active;

	public Server(String systemName, String name) {
		super(systemName, name);
		this.load = 0;
		this.fidelity = Fidelity.HIGH;
	}

	public boolean isActive() {
		return this.active;
	}

	public void activate() {
		this.active = true;
	}

	public void deactivate() {
		this.active = false;
	}

	public void setFidelityLevel(Fidelity fidelityLevel) {
		this.fidelity = fidelityLevel;
	}

	public Object getNewsContent() {
		// TODO Implement this method!!!
		System.out.println(this.getName() + ": Serving content...");
		return null;
	}

	/**
	 * Server specific definition of "fidelity"
	 */
	public enum Fidelity {
		HIGH, MEDIUM, LOW;
	}
}