package ar.uba.dc.thesis.component.znn;

import java.util.List;

import ar.uba.dc.thesis.acme.Component;
import ar.uba.dc.thesis.acme.Operation;

public class Proxy extends Component {

	private List<Server> servers;

	private int currentServer;

	@SuppressWarnings("unchecked")
	public Proxy(int activeServers) {
		super(Proxy.class.getSimpleName());
		if (activeServers < 1) {
			throw new IllegalArgumentException("At least one server is mandatory");
		}
		this.init(activeServers);
		super.addOperations(new GetNewsContent(this), new GetActiveServersAmount(this));
	}

	public void getNewsContent() {
		this.servers.get(this.currentServer).getNewsContent();
	}

	public void activateAnotherServer() {
		this.addServerNumber(this.getActiveServersAmount() + 1);
	}

	public int getActiveServersAmount() {
		return this.servers.size();
	}

	private void init(int activeServers) {
		for (int i = 1; i <= activeServers; i++) {
			addServerNumber(i);
		}
	}

	private void addServerNumber(int i) {
		this.servers.add(new Server("Server " + i));
	}

	/**
	 * Proxy specific Operation: GetNewsContent
	 */
	public class GetNewsContent extends Operation<Proxy> {

		private GetNewsContent(Proxy proxy) {
			super(GetNewsContent.class.getSimpleName(), proxy);
		}

		@Override
		public void execute() {
			this.getComponent().getNewsContent();
		}
	}

	/**
	 * Proxy specific Operation: GetActiveServersAmount
	 */
	public class GetActiveServersAmount extends Operation<Proxy> {

		private GetActiveServersAmount(Proxy proxy) {
			super(GetNewsContent.class.getSimpleName(), proxy);
		}

		@Override
		public void execute() {
			this.getComponent().getActiveServersAmount();
		}
	}
}
