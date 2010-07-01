package ar.uba.dc.thesis.atam.scenario.model;

import java.util.Set;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.qa.Concern;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public abstract class AtamScenario extends ThesisPojo {

	@XStreamAsAttribute
	private String name;

	private Concern concern;

	private String stimulusSource;

	private String stimulus;

	@XStreamImplicit
	private Set<Environment> environments;

	private Artifact artifact;

	private String response;

	private ResponseMeasure responseMeasure;

	@XStreamImplicit
	private Set<ArchitecturalDecision> architecturalDecisions;

	@XStreamAsAttribute
	private Long id;

	public AtamScenario(Long id, String name, Concern concern, String stimulusSource, String stimulus,
			Set<Environment> environments, Artifact artifact, String response, ResponseMeasure responseMeasure,
			Set<ArchitecturalDecision> architecturalDecisions) {
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

	public Set<ArchitecturalDecision> getArchitecturalDecisions() {
		return this.architecturalDecisions;
	}

	public Set<Environment> getEnvironments() {
		return this.environments;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConcern(Concern concern) {
		this.concern = concern;
	}

	public void setStimulusSource(String stimulusSource) {
		this.stimulusSource = stimulusSource;
	}

	public void setStimulus(String stimulus) {
		this.stimulus = stimulus;
	}

	public void setEnvironments(Set<Environment> environments) {
		this.environments = environments;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public void setResponseMeasure(ResponseMeasure responseMeasure) {
		this.responseMeasure = responseMeasure;
	}

	public void setArchitecturalDecisions(Set<ArchitecturalDecision> architecturalDecisions) {
		this.architecturalDecisions = architecturalDecisions;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "\"" + this.getName() + "\" " + super.toString();
	}

	public void validate() {
		if (this.id == null) {
			throw new RuntimeException("Self Healing Scenario's id cannot be null");
		}
	}
}