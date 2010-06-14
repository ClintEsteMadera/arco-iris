package ar.uba.dc.thesis.selfhealing;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;


public class DefaultScenarioBrokenDetector implements ScenarioBrokenDetector {

	private final RainbowModelWithScenarios rainbowModelWithScenarios;

	public DefaultScenarioBrokenDetector(RainbowModelWithScenarios model) {
		super();
		this.rainbowModelWithScenarios = model;
	}

	public boolean isBroken(SelfHealingScenario scenario) {
		return !scenario.isBroken(this.rainbowModelWithScenarios);
	}

}
