package ar.uba.dc.arcoiris.selfhealing.score;

import org.sa.rainbow.scenario.model.ArcoIrisModel;

import ar.uba.dc.arcoiris.selfhealing.ScenarioBrokenDetector;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public class ScenarioBrokenDetector4CurrentSystemState implements ScenarioBrokenDetector {

	private final ArcoIrisModel arcoIrisModel;

	public ScenarioBrokenDetector4CurrentSystemState(ArcoIrisModel model) {
		super();
		this.arcoIrisModel = model;
	}

	@Override
	public boolean isBroken(SelfHealingScenario scenario) {
		return scenario.isBroken(this.arcoIrisModel);
	}

}
