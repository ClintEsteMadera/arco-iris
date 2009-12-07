package sba.common.anotaciones.domain;

import sba.common.annotations.PropertyDescription;
import sba.common.annotations.ValidateRequired;

public class Empleadito extends Empleado {

	
	private String sector;

	@PropertyDescription(value="SECTOR")
	@ValidateRequired
	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	
}
