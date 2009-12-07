package ar.uba.dc.thesis.atam;

import java.util.Collection;

import ar.uba.dc.thesis.qa.Concern;

public class AtamScenario {

	private Long id;

	private String name;

	private Concern concern;

	private String stimulusSource;

	private Stimulus stimulus;

	private String environment;

	private Artifact artifact;

	private String response;

	private ResponseMeasure responseMeasure;

	private Collection<ArchitecturalDecision> architecturalDecisions;

	public AtamScenario(Long id, String name, Concern concern, String stimulusSource, Stimulus stimulus,
			String environment, Artifact artifact, String response, ResponseMeasure responseMeasure,
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

	public Stimulus getStimulus() {
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

	public String getEnvironment() {
		return this.environment;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		this.toString(sb);
		return sb.toString();
	}

	protected void toString(StringBuilder sb) {
		sb.append("Scenario: ").append(this.name).append("\n");
		this.append(sb, "stimulus source", this.stimulusSource);
		this.append(sb, "stimulus", this.stimulus.getName());
		this.append(sb, "environment", this.environment);
		this.append(sb, "response", this.response);
		this.append(sb, "response measure", this.responseMeasure.getDescription());
	}

	protected void append(StringBuilder sb, String fieldName, String fieldValue) {
		sb.append("\t").append(fieldName).append(": ").append(fieldValue).append("\n");
	}

	public Long getId() {
		return id;
	}
}