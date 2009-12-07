package sba.common.persona.identificacion;

import java.io.Serializable;

public enum TipoIdentificadorPersonaFisica implements Serializable {

	DNI("Documento Nacional de Identidad"),

	LE("Libreta de Enrolamiento"),

	LC("Libreta Cívica"),

	CI("Cédula de Identidad");

	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * Retorna una representación textual de la instancia.
	 * 
	 * @return texto que denota el estado de la instancia
	 */
	@Override
	public String toString() {
		return this.descripcion;
	}

	/**
	 * Constructor parametrizado con la descripción del elemento.
	 * 
	 * @param descripcion
	 *            descripción del tipo de documento
	 */
	private TipoIdentificadorPersonaFisica(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Almancena la descripción del elemento.
	 */
	private String descripcion;
}