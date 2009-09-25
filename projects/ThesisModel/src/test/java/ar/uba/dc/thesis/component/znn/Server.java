package ar.uba.dc.thesis.component.znn;

import ar.uba.dc.thesis.acme.Component;
import ar.uba.dc.thesis.acme.Operation;

public class Server extends Component {

	public int load;

	public Fidelity fidelity;

	private boolean active;

	@SuppressWarnings( { "unchecked", "synthetic-access" })
	public Server(String name) {
		super(name);
		this.load = 0;
		this.fidelity = Fidelity.HIGH;
		super.addOperations(new Activate(this), new Deactivate(this), new SetFidelityLevel(this));
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

	/**
	 * Server specific definition of "fidelity"
	 */
	public enum Fidelity {
		HIGH, MEDIUM, LOW;
	}

	/**
	 * Server specific Operation: Activate
	 */
	public class Activate extends Operation<Server> {

		private Activate(Server server) {
			super(Activate.class.getSimpleName(), server);
		}

		@Override
		public void execute() {
			this.getComponent().activate();
		}
	}

	/**
	 * Server specific Operation: Deactivate
	 */
	public class Deactivate extends Operation<Server> {

		private Deactivate(Server server) {
			super(Activate.class.getSimpleName(), server);
		}

		@Override
		public void execute() {
			this.getComponent().deactivate();
		}
	}

	/**
	 * Server specific Operation: SetFidelityLevel
	 */
	public class SetFidelityLevel extends Operation<Server> {

		private SetFidelityLevel(Server server) {
			super(Activate.class.getSimpleName(), server);
		}

		@Override
		public void execute() {
			this.getComponent().deactivate();
		}
	}
}