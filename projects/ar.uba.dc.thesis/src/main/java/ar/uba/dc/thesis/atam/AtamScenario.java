package ar.uba.dc.thesis.atam;

import java.util.Collection;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.qa.Concern;

public abstract class AtamScenario extends ThesisPojo {

	private Long id;

	private String name;

	private Concern concern;

	private String stimulusSource;

	private String stimulus;

	private ScenarioEnvironment environment;

	private Artifact artifact;

	private String response;

	private ResponseMeasure responseMeasure;

	private Collection<ArchitecturalDecision> architecturalDecisions;

	public AtamScenario(Long id, String name, Concern concern, String stimulusSource, String stimulus,
			ScenarioEnvironment environment, Artifact artifact, String response, ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions) {
		super();
		this.id = id;
		this.name = name;
		this.concern = concern;
		this.stimulusSource = stimulusSource;
		this.stimulus = stimulus;
		this.environment = environment;
		this.artifact = artifact;
		this.response = response;
		this.responseMeasure = responseMeasure;
		this.architecturalDecisions = architecturalDecisions;
	}

	public AtamScenario() {
		super();
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

	public String getStimulus() {
		return this.stimulus;
	}

	public Artifact getArtifact() {
		return this.artifact;
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

	public ScenarioEnvironment getEnvironment() {
		return this.environment;
	}

	public Long getId() {
		return id;
	}

	public void validate() {
		// TODO Implement me!
	}
}