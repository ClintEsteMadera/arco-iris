package ar.uba.dc.thesis.selfhealing;

import java.util.SortedMap;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.stitch.core.Strategy;

public class ScenarioBrokenDetector4StrategyScoring implements ScenarioBrokenDetector {

	private final RainbowModelWithScenarios rainbowModelWithScenarios;

	private final Strategy strategy;

	private SortedMap<String, Double> aggregateAttributes;

	public ScenarioBrokenDetector4StrategyScoring(RainbowModelWithScenarios model, Strategy strategy) {
		super();
		this.rainbowModelWithScenarios = model;
		this.strategy = strategy;
	}

	public boolean isBroken(SelfHealingScenario scenario) {
		// save aggregate attributes in order to avoid recalculating them
		this.aggregateAttributes = this.strategy.computeAggregateAttributes();

		return scenario.isBrokenAfterStrategy(this.rainbowModelWithScenarios, aggregateAttributes);
	}

	public SortedMap<String, Double> getAggregateAttributes() {
		return this.aggregateAttributes;
	}

	public Strategy getStrategy() {
		return this.strategy;
	}
}
