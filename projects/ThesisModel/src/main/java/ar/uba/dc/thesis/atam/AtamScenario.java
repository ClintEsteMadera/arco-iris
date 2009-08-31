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
		return name;
	}

	public Concern getConcern() {
		return concern;
	}

	public String getStimulusSource() {
		return stimulusSource;
	}

	public Operation getStimulus() {
		return stimulus;
	}

	public Collection<Artifact> getArtifacts() {
		return artifacts;
	}

	public String getResponse() {
		return response;
	}

	public ResponseMeasure getResponseMeasure() {
		return responseMeasure;
	}

	public Collection<ArchitecturalDecision> getArchitecturalDecisions() {
		return architecturalDecisions;
	}

	public String getEnvironment() {
		return environment;
	}

	public String toString(){
		return "Scenario: " + name + "\n"
        + toString("stimulus source", stimulusSource)
        + toString("stimulus", stimulus.getName())
        + toString("environment", environment)
        + toString("response", response)
        + toString("response measure", responseMeasure.getDescription())
        ;
	}

	protected String toString(String fieldName, String fieldValue){
		return "\t" + fieldName + ": " + fieldValue + "\n";
	}
}