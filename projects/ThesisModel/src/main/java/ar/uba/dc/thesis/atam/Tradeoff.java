package ar.uba.dc.thesis.atam;

import java.util.Collection;

import ar.uba.dc.thesis.qa.Concern;

public class Tradeoff {

	private Concern concern;

	private Collection<Artifact> artifacts;

	public Tradeoff(Concern concern, Collection<Artifact> artifacts) {
		super();
		this.concern = concern;
		this.artifacts = artifacts;
	}

	public Concern getConcern() {
		return this.concern;
	}

	public Collection<Artifact> getArtifacts() {
		return this.artifacts;
	}
}
