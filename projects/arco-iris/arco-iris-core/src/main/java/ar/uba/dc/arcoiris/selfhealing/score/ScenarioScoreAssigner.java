package ar.uba.dc.arcoiris.selfhealing.score;

import org.sa.rainbow.scenario.model.ArcoIrisModel;
import org.sa.rainbow.stitch.core.UtilityFunction;

import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public interface ScenarioScoreAssigner {

	double scenarioScore(SelfHealingScenario scenario, Environment currentSystemEnvironment2,
			double scenarioRelativePriority, ArcoIrisModel model, UtilityFunction uf);

}
