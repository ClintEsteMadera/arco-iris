package ar.uba.dc.thesis.component.znn;

import ar.uba.dc.thesis.acme.Component;
import ar.uba.dc.thesis.acme.Operation;

public class Client extends Component {

	public long experienceResponseTime;
	private final Proxy proxy;

	@SuppressWarnings("unchecked")
	public Client(String name, Proxy proxy) {
		super(name);
		this.proxy = proxy;
		this.addOperations(new GetNewsContentOperation(this));
	}

	public long getExperienceResponseTime() {
		return this.experienceResponseTime;
	}

	public void executeGetNewsContentOperation(Proxy proxy) {
		long start = System.currentTimeMillis();
		proxy.getNewsContent();
		long end = System.currentTimeMillis();
		this.experienceResponseTime = end - start;
	}

	public Proxy getProxy() {
		return this.proxy;
	}

	/**
	 * Specific Client operation
	 */
	public class GetNewsContentOperation extends Operation<Client> {
		public GetNewsContentOperation(Client component) {
			super(GetNewsContentOperation.class.getSimpleName(), component);
		}

		@Override
		public void execute() {
			this.getComponent().executeGetNewsContentOperation(this.getComponent().getProxy());
		}
	}
}
