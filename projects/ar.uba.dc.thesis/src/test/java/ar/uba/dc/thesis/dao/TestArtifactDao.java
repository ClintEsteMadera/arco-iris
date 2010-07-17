package ar.uba.dc.thesis.dao;

import java.util.List;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.util.Collections;

public final class TestArtifactDao {

	private static final String CLIENT_TYPE_NAME = "ClientT";

	private static final String SERVER_TYPE_NAME = "ServerT";

	private static final String SYSTEM_NAME = "ZNewsSys";

	public static final Artifact CLIENT = new Artifact(0L, SYSTEM_NAME, CLIENT_TYPE_NAME);

	public static final Artifact SERVER = new Artifact(1L, SYSTEM_NAME, SERVER_TYPE_NAME);

	private List<Artifact> allArtifacts;

	public TestArtifactDao() {
		super();
		this.allArtifacts = createAllArtifacts();
	}

	public List<Artifact> getAllArtifacts() {
		return this.allArtifacts;
	}

	private List<Artifact> createAllArtifacts() {
		return Collections.createList(CLIENT, SERVER);
	}
}
