package ar.uba.dc.thesis.atam.scenario.persist;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("selfHealingConfiguration")
public class SelfHealingConfiguration extends ThesisPojo {

	private static final long serialVersionUID = 1L;

	@XStreamAsAttribute
	private String description;

	private List<Artifact> artifacts;

	private List<Environment> environments;

	private List<SelfHealingScenario> scenarios;

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
		if (this.description == null) {
			throw new IllegalStateException("The description cannot be null");
		}
		if (this.artifacts == null) {
			throw new IllegalStateException("The list of artifacts cannot be null");
		}
		if (this.environments == null) {
			throw new IllegalStateException("The list of environments cannot be null");
		}
		if (this.scenarios == null) {
			throw new IllegalStateException("The list of scenarios cannot be null");
		}
	}
}
