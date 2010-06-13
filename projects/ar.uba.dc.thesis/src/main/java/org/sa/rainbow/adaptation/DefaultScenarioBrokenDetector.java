package org.sa.rainbow.adaptation;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class DefaultScenarioBrokenDetector implements ScenarioBrokenDetector {

	private final RainbowModelWithScenarios rainbowModelWithScenarios;

	public DefaultScenarioBrokenDetector(RainbowModelWithScenarios model) {
		super();
		this.rainbowModelWithScenarios = model;
	}

	public boolean isBroken(SelfHealingScenario scenario) {
		return !scenario.holdsConsideringAllInstances(this.rainbowModelWithScenarios);
	}

}
