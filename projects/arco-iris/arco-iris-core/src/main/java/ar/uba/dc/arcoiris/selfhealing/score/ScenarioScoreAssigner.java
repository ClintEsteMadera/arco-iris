package ar.uba.dc.arcoiris.selfhealing.score;

import org.sa.rainbow.scenario.model.ArcoIrisModel;

import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.selfhealing.ScenarioBrokenDetector;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public interface ScenarioScoreAssigner {

	double scenarioScore(ScenarioBrokenDetector scenarioBrokenDetector, SelfHealingScenario scenario,
			Environment currentSystemEnvironment, double scenarioRelativePriority, ArcoIrisModel model, String property);

}
