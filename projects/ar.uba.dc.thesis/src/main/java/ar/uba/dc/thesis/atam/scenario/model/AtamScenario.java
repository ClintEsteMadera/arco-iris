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

	@Override
	public String toString() {
		return "\"" + this.getName() + "\" " + super.toString();
	}

	public void validate() {
		// Do nothing
	}

	public Long getId() {
		return id;
	}
}