package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.component.znn.Proxy;

public class ComponentRepository {

	private static final Proxy PROXY = new Proxy(1);

	public static Proxy getProxy() {
		return PROXY;
	}
}
