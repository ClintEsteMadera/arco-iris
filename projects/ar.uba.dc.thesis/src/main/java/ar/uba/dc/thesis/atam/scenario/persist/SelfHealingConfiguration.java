package ar.uba.dc.thesis.atam.scenario.persist;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("selfHealingConfiguration")
public class SelfHealingConfiguration extends ThesisPojo {

	private static final long serialVersionUID = 1L;

	@XStreamImplicit
	private List<SelfHealingScenario> scenarios;

	@XStreamAsAttribute
	private String description;

	public SelfHealingConfiguration() {
		this("", null);
	}

	public SelfHealingConfiguration(String description, List<SelfHealingScenario> scenarios) {
		super();
		this.description = description == null ? "" : description;

		if (scenarios == null) {
			scenarios = new ArrayList<SelfHealingScenario>();
		}
		this.scenarios = scenarios;

		this.validate();
	}

	public String getDescription() {
		return description;
	}

	public List<SelfHealingScenario> getScenarios() {
		return scenarios;
	}

	public void setScenarios(List<SelfHealingScenario> scenarios) {
		this.scenarios = scenarios;
	}

	public void addScenario(SelfHealingScenario model) {
		this.scenarios.add(model);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void validate() {
		if (this.description == null) {
			throw new IllegalStateException("The description cannot be null");
		}
		if (this.scenarios == null) {
			throw new IllegalStateException("The list of scenarios cannot be null");
		}
	}
}
