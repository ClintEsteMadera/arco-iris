package ar.uba.dc.thesis.selfhealing;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

public class ScenarioBrokenDetector4CurrentSystemState implements ScenarioBrokenDetector {

	private final RainbowModelWithScenarios rainbowModelWithScenarios;

	public ScenarioBrokenDetector4CurrentSystemState(RainbowModelWithScenarios model) {
		super();
		this.rainbowModelWithScenarios = model;
	}

	public boolean isBroken(SelfHealingScenario scenario) {
		return scenario.isBroken(this.rainbowModelWithScenarios);
	}

}
