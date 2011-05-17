package ar.uba.dc.arcoiris.selfhealing.score;

import org.sa.rainbow.scenario.model.ArcoIrisModel;

import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.selfhealing.ScenarioBrokenDetector;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public class DefaultScenarioScoreAssigner implements ScenarioScoreAssigner {

	public final double scenarioScore(ScenarioBrokenDetector scenarioBrokenDetector, SelfHealingScenario scenario,
			Environment currentSystemEnvironment, double scenarioRelativePriority, ArcoIrisModel model,
			String propertyId) {
		double score = 0;
		double concernWeight4CurrentEnv = currentSystemEnvironment.getWeights().get(scenario.getConcern());
		if (!scenario.applyFor(currentSystemEnvironment)
				|| (scenario.applyFor(currentSystemEnvironment) && !scenarioBrokenDetector.isBroken(scenario))) {
			score = scenarioRelativePriority * concernWeight4CurrentEnv;
		}
		return score;
	}

}