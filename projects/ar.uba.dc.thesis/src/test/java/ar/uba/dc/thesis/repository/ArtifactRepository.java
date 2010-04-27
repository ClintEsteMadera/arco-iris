package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.atam.Artifact;

public final class ArtifactRepository {

	private static final String CLIENT_INSTANCE_NAME = "ClientT";

	private static final String PROXY_INSTANCE_NAME = "ProxyT";

	private static final String SYSTEM_NAME = "ZNewsSys";

	private static final Artifact PROXY = new Artifact(SYSTEM_NAME, PROXY_INSTANCE_NAME);

	private static final Artifact CLIENT = new Artifact(SYSTEM_NAME, CLIENT_INSTANCE_NAME);

	/**
	 * This class is not intended to be extended.
	 */
	private ArtifactRepository() {
		super();
	}

	public static Artifact getProxy() {
		return PROXY;
	}

	public static Artifact getClient() {
		return CLIENT;
	}
}
