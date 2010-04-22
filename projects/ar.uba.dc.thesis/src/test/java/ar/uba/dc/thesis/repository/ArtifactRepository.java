package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.atam.Artifact;

public final class ArtifactRepository {

	// FIXME We should work with types instead of concrete instances...
	private static final String CLIENT_INSTANCE_NAME = "c0";

	private static final String PROXY_INSTANCE_NAME = "p0";

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
