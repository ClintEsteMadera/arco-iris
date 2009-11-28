package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.component.znn.Client;
import ar.uba.dc.thesis.component.znn.Proxy;

public class ComponentRepository {

	private static final Proxy PROXY = new Proxy(1);
	private static final Client CLIENT = new Client("Default Client", PROXY);

	public static Proxy getProxy() {
		return PROXY;
	}

	public static Client getClient() {
		return CLIENT;
	}
}
