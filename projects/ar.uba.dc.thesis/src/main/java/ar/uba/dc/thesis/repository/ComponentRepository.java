package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.component.znn.Client;
import ar.uba.dc.thesis.component.znn.Proxy;

public class ComponentRepository {

	private static final String CLIENT_TYPE_NAME = "ClientT";

	private static final String PROXY_TYPE_NAME = "ProxyT";

	private static final String SYSTEM_NAME = "ZNewsSys";

	private static final Proxy PROXY = new Proxy(SYSTEM_NAME, PROXY_TYPE_NAME, 1);

	private static final Client CLIENT = new Client(SYSTEM_NAME, CLIENT_TYPE_NAME, PROXY);

	public static Proxy getProxy() {
		return PROXY;
	}

	public static Client getClient() {
		return CLIENT;
	}
}
