package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.component.znn.Client;
import ar.uba.dc.thesis.component.znn.Proxy;

public class ComponentRepository {

	private static final String SYSTEM_NAME = "System 1";

	private static final Proxy PROXY = new Proxy(SYSTEM_NAME, 1);

	private static final Client CLIENT = new Client(SYSTEM_NAME, "Default Client", PROXY);

	public static Proxy getProxy() {
		return PROXY;
	}

	public static Client getClient() {
		return CLIENT;
	}
}
