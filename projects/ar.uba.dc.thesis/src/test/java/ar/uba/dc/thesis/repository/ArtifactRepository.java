package ar.uba.dc.thesis.repository;

import ar.uba.dc.thesis.atam.Artifact;

public final class ArtifactRepository {

	private static final String CLIENT_TYPE_NAME = "ClientT";

	private static final String SERVER_TYPE_NAME = "ServerT";

	private static final String SYSTEM_NAME = "ZNewsSys";

	private static final Artifact SERVER = new Artifact(SYSTEM_NAME, SERVER_TYPE_NAME);

	private static final Artifact CLIENT = new Artifact(SYSTEM_NAME, CLIENT_TYPE_NAME);

	/**
	 * This class is not intended to be extended.
	 */
	private ArtifactRepository() {
		super();
	}

	public static Artifact getServer() {
		return SERVER;
	}

	public static Artifact getClient() {
		return CLIENT;
	}
}
