package ar.uba.dc.thesis.selfhealing;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.stitch.core.UtilityFunction;

import ar.uba.dc.thesis.atam.scenario.model.Environment;

public interface ScenarioScoreAssigner extends ScenarioBrokenDetector {

	double scenarioScore(SelfHealingScenario scenario, Environment currentSystemEnvironment2,
			double scenarioRelativePriority, RainbowModelWithScenarios model, UtilityFunction uf);

}
