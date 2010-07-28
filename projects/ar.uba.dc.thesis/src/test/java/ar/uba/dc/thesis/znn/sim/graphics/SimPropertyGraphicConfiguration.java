package ar.uba.dc.thesis.znn.sim.graphics;

import org.sa.rainbow.model.RainbowModel;

public class SimPropertyGraphicConfiguration {

	private String property;

	private String eavgPropertyName;

	public SimPropertyGraphicConfiguration(String artifact, String property) {
		super();
		this.property = property;
		this.eavgPropertyName = RainbowModel.EXP_AVG_KEY + artifact + "." + property;
	}

	public String getProperty() {
		return property;
	}

	public String getEavgPropertyName() {
		return eavgPropertyName;
	}

}
