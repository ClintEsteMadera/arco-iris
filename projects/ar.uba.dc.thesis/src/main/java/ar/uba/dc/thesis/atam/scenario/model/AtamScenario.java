package ar.uba.dc.thesis.atam.scenario.model;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.thesis.common.IdentifiableArcoIrisDomainObject;
import ar.uba.dc.thesis.common.validation.Assert;
import ar.uba.dc.thesis.common.validation.ValidationError;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.util.Collections;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class AtamScenario extends IdentifiableArcoIrisDomainObject {

	private static final long serialVersionUID = 1L;

	private static final String FRIENDLY_NAME = "Scenario";

	private static final String VALIDATION_MSG_NAME = "Scenario's name cannot be empty";

	private static final String VALIDATION_MSG_CONCERN = "Scenario's concern cannot be empty";

	private static final String VALIDATION_MSG_STIMULUS = "Scenario's stimulus cannot be empty";

	private static final String VALIDATION_MSG_ENVIRONMENTS = "Scenario's environments cannot be empty";

	private static final String VALIDATION_MSG_ARTIFACT = "Scenario's artifact cannot be empty";

	private static final String VALIDATION_MSG_RESPONSE = "Scenario's expected response cannot be empty";

	private static final String VALIDATION_MSG_RESPONSE_MEASURE = "Scenario's response measure cannot be empty";

	@XStreamAsAttribute
	private String name;

	private Concern concern;

	private Stimulus stimulus;

	private List<? extends Environment> environments;

	private Artifact artifact;

	private String response;

	private ResponseMeasure responseMeasure;

	public AtamScenario() {
		super();
		this.responseMeasure = new ResponseMeasure();
		this.stimulus = Stimulus.ANY;
		this.environments = Collections.createList(AnyEnvironment.getInstance());
	}

	public AtamScenario(Long id, String name, Concern concern, Stimulus stimulus,
			List<? extends Environment> environments, Artifact artifact, String response,
			ResponseMeasure responseMeasure) {
		super(id);
		this.name = name;
		this.concern = concern;
		this.stimulus = stimulus;
		this.environments = environments;
		this.artifact = artifact;
		this.response = response;
		this.responseMeasure = responseMeasure;
	}

	public String getName() {
		return this.name;
	}

	public Concern getConcern() {
		return this.concern;
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

	public List<? extends Environment> getEnvironments() {
		return this.environments;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConcern(Concern concern) {
		this.concern = concern;
	}

	public void setStimulus(Stimulus stimulus) {
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

	@Override
	public String toString() {
		return "\"" + this.getName() + "\" " + super.toString();
	}

	@Override
	protected List<ValidationError> collectValidationErrors() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Assert.notBlank(this.name, VALIDATION_MSG_NAME, validationErrors);

		Assert.notNull(this.concern, VALIDATION_MSG_CONCERN, validationErrors);

		Assert.notNullAndValid(this.stimulus, VALIDATION_MSG_STIMULUS, validationErrors);

		Assert.notEmptyAndValid(this.environments, VALIDATION_MSG_ENVIRONMENTS, validationErrors);

		Assert.notNullAndValid(this.artifact, VALIDATION_MSG_ARTIFACT, validationErrors);

		Assert.notBlank(this.response, VALIDATION_MSG_RESPONSE, validationErrors);

		Assert.notNullAndValid(this.responseMeasure, VALIDATION_MSG_RESPONSE_MEASURE, validationErrors);

		return validationErrors;
	}

	@Override
	protected String getObjectFriendlyName() {
		return FRIENDLY_NAME;
	}
}