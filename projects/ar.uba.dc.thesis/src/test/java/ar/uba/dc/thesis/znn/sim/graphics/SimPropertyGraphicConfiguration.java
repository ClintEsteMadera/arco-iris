package ar.uba.dc.thesis.znn.sim.graphics;

import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

public class SimPropertyGraphicConfiguration {

	private String property;

	private String eavgPropertyName;

	public SimPropertyGraphicConfiguration(String artifact, String property) {
		this(artifact, property, false);
	}

	public SimPropertyGraphicConfiguration(String artifact, String property, boolean sum) {
		super();
		this.property = property;
		String expPropPrefix = sum ? RainbowModelWithScenarios.EXP_SUM_KEY : RainbowModel.EXP_AVG_KEY;
		this.eavgPropertyName = expPropPrefix + artifact + "." + property;
	}

	public String getProperty() {
		return property;
	}

	public String getEavgPropertyName() {
		return eavgPropertyName;
	}

}
