package ar.uba.dc.thesis.scenariosUIWeb.domain;

import org.springmodules.validation.bean.conf.loader.annotation.handler.Min;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

public class SelfHealingScenario {

	private boolean enabled;

	@NotNull
	@Min(1)
	private Integer priority;

	private Long id;

	@NotNull
	private String name;

	@NotNull
	private Concern concern;

	@NotNull
	private String stimulusSource;

	@NotNull
	private String stimulus;

	@NotNull
	private String environment;

	@NotNull
	private String response;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Concern getConcern() {
		return concern;
	}

	public void setConcern(Concern concern) {
		this.concern = concern;
	}

	public String getStimulusSource() {
		return stimulusSource;
	}

	public void setStimulusSource(String stimulusSource) {
		this.stimulusSource = stimulusSource;
	}

	public String getStimulus() {
		return stimulus;
	}

	public void setStimulus(String stimulus) {
		this.stimulus = stimulus;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}