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

	private Collection<Artifact> components;

	private String response;

	private ResponseMeasure responseMeasure;

	private Collection<ArchitecturalDecision> architecturalDecisions;

	public AtamScenario(String name, Concern concern, String stimulusSource, Operation stimulus,
			String environment, Collection<Artifact> components, String response,
			ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions) {
		super();
		this.name = name;
		this.concern = concern;
		this.stimulusSource = stimulusSource;
		this.stimulus = stimulus;
		this.environment = environment;
		this.components = components;
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
		return this.components;
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

	@Override
	public String toString(){
		return "Scenario: " + this.name + "\n"
        + this.toString("stimulus source", this.stimulusSource)
        + this.toString("stimulus", this.stimulus.getName())
        + this.toString("environment", this.environment)
        + this.toString("response", this.response)
        + this.toString("response measure", this.responseMeasure.getDescription())
        ;
	}

	protected String toString(String fieldName, String fieldValue){
		return "\t" + fieldName + ": " + fieldValue + "\n";
	}
}