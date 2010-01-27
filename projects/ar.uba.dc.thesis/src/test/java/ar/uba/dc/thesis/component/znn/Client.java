package ar.uba.dc.thesis.component.znn;

import ar.uba.dc.thesis.acme.Component;

public class Client extends Component {

	public long experienceResponseTime;
	private final Proxy proxy;

	@SuppressWarnings("unchecked")
	public Client(String systemName, String name, Proxy proxy) {
		super(systemName, name);
		this.proxy = proxy;
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
}
