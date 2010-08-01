package ar.uba.dc.thesis.atam.scenario.model;

import java.util.List;
import java.util.Set;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.qa.Concern;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public abstract class AtamScenario extends ThesisPojo {

	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private String name;

	private Concern concern;

	private String stimulusSource;

	private String stimulus;

	@XStreamImplicit
	private List<? extends Environment> environments;

	private Artifact artifact;

	private String response;

	private ResponseMeasure responseMeasure;

	@XStreamImplicit
	private Set<ArchitecturalDecision> architecturalDecisions;

	public AtamScenario() {
		super();
		this.responseMeasure = new ResponseMeasure();
	}

	public AtamScenario(Long id, String name, Concern concern, String stimulusSource, String stimulus,
			List<? extends Environment> environments, Artifact artifact, String response,
			ResponseMeasure responseMeasure, Set<ArchitecturalDecision> architecturalDecisions) {
		super(id);
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

	public List<? extends Environment> getEnvironments() {
		return this.environments;
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

	public void setEnvironments(List<Environment> environments) {
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

	@Override
	public String toString() {
		return "\"" + this.getName() + "\" " + super.toString();
	}
}