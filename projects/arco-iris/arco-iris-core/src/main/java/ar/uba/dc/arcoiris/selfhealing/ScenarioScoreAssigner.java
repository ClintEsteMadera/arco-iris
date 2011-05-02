package ar.uba.dc.arcoiris.selfhealing;

import org.sa.rainbow.scenario.model.ArcoIrisModel;
import org.sa.rainbow.stitch.core.UtilityFunction;

import ar.uba.dc.arcoiris.atam.scenario.model.Environment;

public interface ScenarioScoreAssigner extends ScenarioBrokenDetector {

	double scenarioScore(SelfHealingScenario scenario, Environment currentSystemEnvironment2,
			double scenarioRelativePriority, ArcoIrisModel model, UtilityFunction uf);

}
