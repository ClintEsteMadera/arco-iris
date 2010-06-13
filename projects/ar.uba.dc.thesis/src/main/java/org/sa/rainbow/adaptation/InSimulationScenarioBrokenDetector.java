package org.sa.rainbow.adaptation;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.stitch.core.Strategy;

import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class InSimulationScenarioBrokenDetector implements ScenarioBrokenDetector {

	private final RainbowModelWithScenarios rainbowModelWithScenarios;

	private final Strategy strategy;

	public InSimulationScenarioBrokenDetector(RainbowModelWithScenarios model, Strategy strategy) {
		super();
		this.rainbowModelWithScenarios = model;
		this.strategy = strategy;
	}

	public boolean isBroken(SelfHealingScenario scenario) {
		return scenario.isEAvgBroken(this.rainbowModelWithScenarios, this.strategy.computeAggregateAttributes());
	}

}
