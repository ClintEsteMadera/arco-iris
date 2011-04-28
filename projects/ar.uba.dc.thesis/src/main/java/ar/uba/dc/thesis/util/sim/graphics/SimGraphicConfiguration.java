package ar.uba.dc.thesis.util.sim.graphics;

import java.util.ArrayList;
import java.util.List;

public class SimGraphicConfiguration {

	private List<SimPropertyGraphicConfiguration> propertyGraphicConfiguration = new ArrayList<SimPropertyGraphicConfiguration>();

	public void add(SimPropertyGraphicConfiguration respTimeGraphicConfig) {
		getPropertyGraphicConfiguration().add(respTimeGraphicConfig);
	}

	public List<SimPropertyGraphicConfiguration> getPropertyGraphicConfiguration() {
		return propertyGraphicConfiguration;
	}

	public void setPropertyGraphicConfiguration(List<SimPropertyGraphicConfiguration> propertyGraphicConfiguration) {
		this.propertyGraphicConfiguration = propertyGraphicConfiguration;
	}

}
