package ar.uba.dc.thesis.component.znn;

import ar.uba.dc.thesis.acme.Component;
import ar.uba.dc.thesis.acme.Operation;

public class Server extends Component {

	public int load;

	public Fidelity fidelity;

	private boolean active;

	@SuppressWarnings( { "unchecked", "synthetic-access" })
	public Server(String systemName, String name) {
		super(systemName, name);
		this.load = 0;
		this.fidelity = Fidelity.HIGH;
		super.addOperations(new ActivateOperation(this), new DeactivateOperation(this), new SetFidelityLevelOperation(
				this, null));
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

	/**
	 * Server specific Operation: Activate
	 */
	public class ActivateOperation extends Operation<Server, Object> {

		private ActivateOperation(Server server) {
			super(ActivateOperation.class.getSimpleName(), server);
		}

		@Override
		public Object execute() {
			this.getComponent().activate();
			return null;
		}
	}

	/**
	 * Server specific Operation: Deactivate
	 */
	public class DeactivateOperation extends Operation<Server, Object> {

		private DeactivateOperation(Server server) {
			super(ActivateOperation.class.getSimpleName(), server);
		}

		@Override
		public Object execute() {
			this.getComponent().deactivate();
			return null;
		}
	}

	/**
	 * Server specific Operation: SetFidelityLevel
	 */
	public class SetFidelityLevelOperation extends Operation<Server, Object> {

		private Fidelity fidelityLevel;

		private SetFidelityLevelOperation(Server server, Fidelity fidelityLevel) {
			super(ActivateOperation.class.getSimpleName(), server);
			this.fidelityLevel = fidelityLevel;
		}

		public void setFidelityLevel(Fidelity fidelityLevel) {
			this.fidelityLevel = fidelityLevel;
		}

		@Override
		public Object execute() {
			this.getComponent().setFidelityLevel(this.fidelityLevel);
			return null;
		}
	}

	/**
	 * Server specific Operation: ServeContent
	 */
	public class ServeContentOperation extends Operation<Server, Object> {

		private ServeContentOperation(Server server) {
			super(ActivateOperation.class.getSimpleName(), server);
		}

		@Override
		public Object execute() {
			return this.getComponent().getNewsContent();
		}
	}
}