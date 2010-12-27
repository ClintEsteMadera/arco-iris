package ar.uba.dc.thesis.atam.scenario.persist;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.common.ArcoIrisDomainObject;
import ar.uba.dc.thesis.common.validation.Assert;
import ar.uba.dc.thesis.common.validation.ValidationError;
import ar.uba.dc.thesis.common.validation.ValidationException;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("selfHealingConfiguration")
public class SelfHealingConfiguration extends ArcoIrisDomainObject {

	private static final long serialVersionUID = 1L;

	private static final String VALIDATION_MSG_DESCRIPTION = "Self Healing Configuration's description cannot be empty";

	private static final String VALIDATION_MSG_ARTIFACTS = "Self Healing Configuration's artifacts cannot be empty";

	private static final String VALIDATION_MSG_ENVIRONMENTS = "Self Healing Configuration's environments cannot be empty";

	private static final String VALIDATION_MSG_SCENARIOS = "Self Healing Configuration's scenarios cannot be empty";

	@XStreamAsAttribute
	private String description;

	private List<Artifact> artifacts;

	private List<Environment> environments;

	private List<SelfHealingScenario> scenarios;

	public SelfHealingConfiguration() {
		this("", null, null, null);
	}

	public SelfHealingConfiguration(String description) {
		this(description, null, null, null);
	}

	public SelfHealingConfiguration(String description, List<Artifact> artifacts, List<Environment> environments,
			List<SelfHealingScenario> scenarios) {
		super();
		this.description = description == null ? "" : description;
		this.artifacts = artifacts == null ? new ArrayList<Artifact>() : artifacts;
		this.environments = environments == null ? new ArrayList<Environment>() : environments;
		this.scenarios = scenarios == null ? new ArrayList<SelfHealingScenario>() : scenarios;

		this.validate();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Artifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(List<Artifact> artifacts) {
		this.artifacts = artifacts;
	}

	public void addArtifact(Artifact artifact) {
		this.artifacts.add(artifact);
	}

	public void removeArtifact(Artifact artifact) {
		this.artifacts.remove(artifact);
	}

	public List<Environment> getEnvironments() {
		return environments;
	}

	public void setEnvironments(List<Environment> environments) {
		this.environments = environments;
	}

	public void addEnvironment(Environment environment) {
		this.environments.add(environment);
	}

	public void removeEnvironment(Environment environment) {
		this.environments.remove(environment);
	}

	public List<SelfHealingScenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<SelfHealingScenario> scenarios) {
		this.scenarios = scenarios;
	}

	public void addScenario(SelfHealingScenario scenario) {
		this.scenarios.add(scenario);
	}

	public void removeScenario(SelfHealingScenario scenario) {
		this.scenarios.remove(scenario);
	}

	@Override
	public void validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		Assert.notNull(this.description, VALIDATION_MSG_DESCRIPTION, validationErrors);

		Assert.notNull(this.artifacts, VALIDATION_MSG_ARTIFACTS, validationErrors);

		Assert.notNull(this.environments, VALIDATION_MSG_ENVIRONMENTS, validationErrors);

		Assert.notNull(this.scenarios, VALIDATION_MSG_SCENARIOS, validationErrors);

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
	}
}