package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.component.znn.Client;
import ar.uba.dc.thesis.component.znn.Proxy;

public class ComponentRepository {

	private static final String PROXY_NAME = "lbproxy";

	private static final String SYSTEM_NAME = "ZNewsSys";

	private static final Proxy PROXY = new Proxy(SYSTEM_NAME, PROXY_NAME, 1);

	private static final Client CLIENT = new Client(SYSTEM_NAME, "c1", PROXY);

	public static Proxy getProxy() {
		return PROXY;
	}

	public static Client getClient() {
		return CLIENT;
	}
}
