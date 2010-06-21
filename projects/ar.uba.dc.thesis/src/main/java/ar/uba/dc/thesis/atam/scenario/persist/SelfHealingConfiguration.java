package ar.uba.dc.thesis.atam.scenario.persist;

import java.util.HashSet;
import java.util.Set;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("selfHealingConfiguration")
public class SelfHealingConfiguration extends ThesisPojo {

	@XStreamImplicit
	private final Set<SelfHealingScenario> scenarios;

	public SelfHealingConfiguration() {
		this(new HashSet<SelfHealingScenario>());
	}

	public SelfHealingConfiguration(Set<SelfHealingScenario> scenarios) {
		super();
		this.scenarios = scenarios;

		this.validate();
	}

	public Set<SelfHealingScenario> getScenarios() {
		return scenarios;
	}

	public void validate() {
		if (this.scenarios == null) {
			throw new IllegalStateException("The list of scenarios cannot be null");
		}
	}
}
