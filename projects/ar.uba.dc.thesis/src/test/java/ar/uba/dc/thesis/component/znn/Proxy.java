package ar.uba.dc.thesis.component.znn;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.thesis.acme.Component;

public class Proxy extends Component {

	private final List<Server> servers = new ArrayList<Server>();

	private int currentServer;

	@SuppressWarnings("unchecked")
	public Proxy(String systemName, String proxyName, int activeServers) {
		super(systemName, proxyName);
		if (activeServers < 1) {
			throw new IllegalArgumentException("At least one server is mandatory");
		}
		this.init(activeServers);
	}

	public Object getNewsContent() {
		return this.servers.get(this.currentServer).getNewsContent();
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
		this.servers.add(new Server(this.getSystemName(), "Server " + i));
	}
}
