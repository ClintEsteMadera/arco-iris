package ar.uba.dc.arcoiris.selfhealing.score;

import org.sa.rainbow.scenario.model.ArcoIrisModel;
import org.sa.rainbow.stitch.core.UtilityFunction;

import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.selfhealing.ScenarioBrokenDetector;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public abstract class BaseScenarioScoreAssigner implements ScenarioScoreAssigner, ScenarioBrokenDetector {

	public final double scenarioScore(SelfHealingScenario scenario, Environment currentSystemEnvironment,
			double concernWeightedScenarioRelativePriority, ArcoIrisModel model, UtilityFunction uf) {
		double score = 0;
		if (scenario.applyFor(currentSystemEnvironment) && !isBroken(scenario)) {
			// TODO: doLog(Level.DEBUG, "Scenario " + scenario.getName() + " satisfied");
			Object eavgPropValue = model.getProperty(uf.mapping());
			if (eavgPropValue != null && eavgPropValue instanceof Double) {
				score = doScenarioScore(concernWeightedScenarioRelativePriority, (Double) eavgPropValue, uf);
			}
		} else {
			// TODO: doLog(Level.DEBUG, "Scenario " + scenario.getName()
			// + " trivially SATISFIED because does not apply for current system environment("
			// + currentSystemEnvironment.getName() + ")");
		}
		return score;
	}

	protected double doScenarioScore(double concernWeightedScenarioRelativePriority, double eavgPropValue,
			UtilityFunction uf) {
		double concernPropertyValue = getConcernPropertyValue(eavgPropValue, uf);
		double concernUtilityValue = uf.f(concernPropertyValue);
		return concernWeightedScenarioRelativePriority * concernUtilityValue;
	}

	protected abstract double getConcernPropertyValue(double eavgPropValue, UtilityFunction uf);
}