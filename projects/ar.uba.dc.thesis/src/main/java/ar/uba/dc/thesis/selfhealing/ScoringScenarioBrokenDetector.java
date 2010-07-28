package ar.uba.dc.thesis.selfhealing;

import java.util.SortedMap;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.stitch.core.Strategy;

public class ScoringScenarioBrokenDetector implements ScenarioBrokenDetector {

	private final RainbowModelWithScenarios rainbowModelWithScenarios;

	private final Strategy strategy;

	private SortedMap<String, Double> computeAggregateAttributes;

	public ScoringScenarioBrokenDetector(RainbowModelWithScenarios model, Strategy strategy) {
		super();
		this.rainbowModelWithScenarios = model;
		this.strategy = strategy;
	}

	public boolean isBroken(SelfHealingScenario scenario) {
		// save computeAttributes in order to avoid recalculating them
		this.computeAggregateAttributes = this.strategy.computeAggregateAttributes();
		return scenario.isBrokenAfterStrategy(this.rainbowModelWithScenarios, computeAggregateAttributes);
	}

	public SortedMap<String, Double> getComputeAggregateAttributes() {
		return computeAggregateAttributes;
	}

	public Strategy getStrategy() {
		return strategy;
	}

}
