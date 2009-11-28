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
		this.addOperations(new GetNewsContentClientOperation(this));
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
	 * Specific Client operation: GetNewsContentOperation
	 */
	public class GetNewsContentClientOperation extends Operation<Client, Object> {
		public GetNewsContentClientOperation(Client component) {
			super(GetNewsContentClientOperation.class.getSimpleName(), component);
		}

		@Override
		public Object execute() {
			this.getComponent().executeGetNewsContentOperation(this.getComponent().getProxy());
			return null;
		}
	}

	/**
	 * Specific Client operation: GetExperienceResponseTime
	 */
	public class GetExperiencedResponseTimeOperation extends Operation<Client, Long> {
		public GetExperiencedResponseTimeOperation(Client component) {
			super(GetExperiencedResponseTimeOperation.class.getSimpleName(), component);
		}

		@Override
		public Long execute() {
			return this.getComponent().getExperienceResponseTime();
		}
	}
}
