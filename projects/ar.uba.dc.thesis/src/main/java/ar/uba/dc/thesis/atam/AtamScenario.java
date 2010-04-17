package ar.uba.dc.thesis.atam;

import java.util.Collection;
import java.util.Set;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.qa.Concern;

public abstract class AtamScenario extends ThesisPojo {

	private Long id;

	private String name;

	private Concern concern;

	private String stimulusSource;

	private String stimulus;

	private Set<Environment> environments;

	private Artifact artifact;

	private String response;

	private ResponseMeasure responseMeasure;

	private Collection<ArchitecturalDecision> architecturalDecisions;

	public AtamScenario(Long id, String name, Concern concern, String stimulusSource, String stimulus,
			Set<Environment> environments, Artifact artifact, String response, ResponseMeasure responseMeasure,
			Collection<ArchitecturalDecision> architecturalDecisions) {
		super();
		this.id = id;
		this.name = name;
		this.concern = concern;
		this.stimulusSource = stimulusSource;
		this.stimulus = stimulus;
		this.environments = environments;
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

	public Set<Environment> getEnvironments() {
		return this.environments;
	}

	public Long getId() {
		return id;
	}

	public void validate() {
		// Do nothing
	}
}