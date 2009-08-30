package ar.uba.dc.thesis.atam;

import java.util.Collection;

import ar.uba.dc.thesis.acme.Operation;
import ar.uba.dc.thesis.qa.Concern;

public class AtamScenario {

	private String name;

	private Concern concern;

	private String stimulusSource;

	private Operation stimulus;

	private String environment;

	private Collection<Artifact> artifacts;

	private String response;

	private ResponseMeasure responseMeasure;

	private Collection<ArchitecturalDecision> architecturalDecisions;

	public AtamScenario(String name, Concern concern, String stimulusSource, Operation stimulus,
			String environment, Collection<Artifact> artifacts, String response,
			ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions) {
		super();
		this.name = name;
		this.concern = concern;
		this.stimulusSource = stimulusSource;
		this.stimulus = stimulus;
		this.environment = environment;
		this.artifacts = artifacts;
		this.response = response;
		this.responseMeasure = responseMeasure;
		this.architecturalDecisions = architecturalDecisions;
	}

	public String getName() {
		return this.name;
	}

	public Concern getConcern() {
		return this.concern;
	}

	public String getStimulusSource() {
		return this.stimulusSource;
	}

	public Operation getStimulus() {
		return this.stimulus;
	}

	public Collection<Artifact> getArtifacts() {
		return this.artifacts;
	}

	public String getResponse() {
		return this.response;
	}

	public ResponseMeasure getResponseMeasure() {
		return this.responseMeasure;
	}

	public Collection<ArchitecturalDecision> getArchitecturalDecisions() {
		return this.architecturalDecisions;
	}

	public String getEnvironment() {
		return this.environment;
	}
}