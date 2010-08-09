package commons.datetime;

import java.io.Serializable;

/**
 * Modela los meses del año.
 * 
 */

public enum Mes implements Serializable {

	ENERO("Enero"),
	FEBRERO("Febrero"),
	MARZO("Marzo"),
	ABRIL("Abril"),
	MAYO("Mayo"),
	JUNIO("Junio"),
	JULIO("Julio"),
	AGOSTO("Agosto"),
	SEPTIEMBRE("Septiembre"),
	OCTUBRE("Octubre"),
	NOVIEMBRE("Noviembre"),
	DICIEMBRE("Diciembre");

	private Mes(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return this.descripcion;
	}

	private String descripcion;
}
