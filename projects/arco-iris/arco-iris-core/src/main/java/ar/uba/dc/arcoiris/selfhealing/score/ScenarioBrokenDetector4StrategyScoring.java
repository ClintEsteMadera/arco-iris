package ar.uba.dc.arcoiris.selfhealing.score;

import java.util.SortedMap;

import org.sa.rainbow.scenario.model.ArcoIrisModel;
import org.sa.rainbow.stitch.core.Strategy;

import ar.uba.dc.arcoiris.selfhealing.ScenarioBrokenDetector;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public class ScenarioBrokenDetector4StrategyScoring implements ScenarioBrokenDetector {

	private final ArcoIrisModel arcoIrisModel;

	private final Strategy strategy;

	private final SortedMap<String, Double> aggregateAttributes;

	public ScenarioBrokenDetector4StrategyScoring(ArcoIrisModel model, Strategy strategy) {
		super();
		this.arcoIrisModel = model;
		this.strategy = strategy;
		// save aggregate attributes in order to avoid recalculating them
		this.aggregateAttributes = this.strategy.computeAggregateAttributes();
	}

	@Override
	public boolean isBroken(SelfHealingScenario scenario) {
		// here, the "simulation" takes place...
		return scenario.isBrokenAfterStrategy(this.arcoIrisModel, getAggregateAttributes());
	}

	public SortedMap<String, Double> getAggregateAttributes() {
		return this.aggregateAttributes;
	}

	public Strategy getStrategy() {
		return this.strategy;
	}
}
