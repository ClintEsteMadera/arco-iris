package ar.uba.dc.arcoiris.selfhealing;

import org.sa.rainbow.scenario.model.ArcoIrisModel;
import org.sa.rainbow.stitch.core.UtilityFunction;

public class ScenarioScoreAssigner4CurrentSystemState extends BaseScenarioScoreAssigner {

	private final ArcoIrisModel rainbowModelWithScenarios;

	public ScenarioScoreAssigner4CurrentSystemState(ArcoIrisModel model) {
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
