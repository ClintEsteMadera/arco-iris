package ar.uba.dc.thesis.component.dummy;

import ar.uba.dc.thesis.acme.Component;
import ar.uba.dc.thesis.acme.Operation;

public class PageRenderer extends Component {

	public int load;

	public Fidelity fidelity;

	private boolean active;

	@SuppressWarnings( { "unchecked", "synthetic-access" })
	@Deprecated
	public PageRenderer() {
		super(PageRenderer.class.getSimpleName());
		this.load = 0;
		this.fidelity = Fidelity.HIGH;
		super.addOperations(new RenderPage(this));
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
	public class RenderPage extends Operation<PageRenderer, Object> {

		private RenderPage(PageRenderer server) {
			super(RenderPage.class.getSimpleName(), server);
		}

		@Override
		public Object execute() {
			this.getComponent().renderPage();
			return null;
		}
	}

	public void renderPage() {
		// TODO do something
		System.out.println("Rendering page...");
	}
}