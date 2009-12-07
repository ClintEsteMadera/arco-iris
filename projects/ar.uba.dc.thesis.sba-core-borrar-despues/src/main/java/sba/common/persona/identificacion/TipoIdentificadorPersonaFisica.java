package sba.common.persona.identificacion;

import java.io.Serializable;

public enum TipoIdentificadorPersonaFisica implements Serializable {

	DNI("Documento Nacional de Identidad"),

	LE("Libreta de Enrolamiento"),

	LC("Libreta C�vica"),

	CI("C�dula de Identidad");

	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * Retorna una representaci�n textual de la instancia.
	 * 
	 * @return texto que denota el estado de la instancia
	 */
	@Override
	public String toString() {
		return this.descripcion;
	}

	/**
	 * Constructor parametrizado con la descripci�n del elemento.
	 * 
	 * @param descripcion
	 *            descripci�n del tipo de documento
	 */
	private TipoIdentificadorPersonaFisica(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Almancena la descripci�n del elemento.
	 */
	private String descripcion;
}