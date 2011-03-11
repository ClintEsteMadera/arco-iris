package ar.uba.dc.thesis.selfhealing;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.stitch.core.UtilityFunction;

public class ScenarioScoreAssigner4CurrentSystemState extends BaseScenarioScoreAssigner {

	private final RainbowModelWithScenarios rainbowModelWithScenarios;

	public ScenarioScoreAssigner4CurrentSystemState(RainbowModelWithScenarios model) {
		super();
		this.rainbowModelWithScenarios = model;
	}

	@Override
	public boolean isBroken(SelfHealingScenario scenario) {
		return scenario.isBroken(this.rainbowModelWithScenarios);
	}

	@Override
	protected double getConcernPropertyValue(double eavgPropValue, UtilityFunction uf) {
		return eavgPropValue;
	}

}
