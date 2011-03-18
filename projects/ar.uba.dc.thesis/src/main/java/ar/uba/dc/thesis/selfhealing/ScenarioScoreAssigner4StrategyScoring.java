package ar.uba.dc.thesis.selfhealing;

import java.util.SortedMap;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.stitch.core.Strategy;
import org.sa.rainbow.stitch.core.UtilityFunction;

public class ScenarioScoreAssigner4StrategyScoring extends BaseScenarioScoreAssigner {

	private final RainbowModelWithScenarios rainbowModelWithScenarios;

	private final Strategy strategy;

	private final SortedMap<String, Double> aggregateAttributes;

	public ScenarioScoreAssigner4StrategyScoring(RainbowModelWithScenarios model, Strategy strategy) {
		super();
		this.rainbowModelWithScenarios = model;
		this.strategy = strategy;
		// save aggregate attributes in order to avoid recalculating them
		this.aggregateAttributes = this.strategy.computeAggregateAttributes();
	}

	@Override
	public boolean isBroken(SelfHealingScenario scenario) {
		// here, the "simulation" takes place...
		return scenario.isBrokenAfterStrategy(this.rainbowModelWithScenarios, getAggregateAttributes());
	}

	@Override
	protected double getConcernPropertyValue(double eavgPropValue, UtilityFunction uf) {
		Double concernDiffAfterStrategy = getAggregateAttributes().get(uf.id());
		return concernDiffAfterStrategy + eavgPropValue;
	}

	public SortedMap<String, Double> getAggregateAttributes() {
		return this.aggregateAttributes;
	}

	public Strategy getStrategy() {
		return this.strategy;
	}
}
