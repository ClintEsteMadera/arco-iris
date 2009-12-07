/*
 * $Id: Organismo.java,v 1.5 2007/12/19 18:15:14 cvschioc Exp $
 *
 * Copyright (c) 2003 Caja de Valores S.A.
 * 25 de Mayo 362, Buenos Aires, República Argentina.
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores
 * S.A. ("Información Confidencial"). Usted no divulgará tal Información
 * Confidencial y solamente la usará conforme a los terminos del Acuerdo que Ud.
 * posee con Caja de Valores S.A.
 */
package sba.common.persona.core;

import sba.common.persona.identificacion.IdentificadorImpositivo;

/**
 * 
 * 
 * @author Miguel Díaz
 * @version $Revision: 1.5 $ - $Date: 2007/12/19 18:15:14 $
 */
public class Organismo extends Parte {

	public Organismo() {
		super();
		this.identificadorImpositivo = new IdentificadorImpositivo();
	}
	
	/**
	 * 
	 * @param identificadorImpositivo
	 * @param denominacion
	 * @param descripcion
	 */
	public Organismo(IdentificadorImpositivo identificadorImpositivo, String denominacion,
			String descripción) {
		super();
		this.identificadorImpositivo = identificadorImpositivo;
		this.denominacion = denominacion;
		this.descripcion = descripción;
	}

	@Override
	public String getIdentificadorUnivoco() {
		return this.denominacion + " - CUIT " + this.identificadorImpositivo.toString();
	}

	@Override
	public String getDescripcionTipoParte() {
		return "Organismo Público";
	}
	
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setIdentificadorImpositivo(IdentificadorImpositivo identificadorImpositivo) {
		this.identificadorImpositivo = identificadorImpositivo;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof Organismo) {
			equals = this.equalsTo((Organismo) obj);
		}
		return equals;
	}

	private boolean equalsTo(Organismo organismo) {
		boolean equals = true;
		if (this.denominacion != null) {
			equals = equals && this.denominacion.equals(organismo.denominacion);
		} else {
			equals = equals && (organismo.denominacion == null);
		}
		if (this.descripcion != null) {
			equals = equals && this.descripcion.equals(organismo.descripcion);
		} else {
			equals = equals && (organismo.descripcion == null);
		}
		if (this.identificadorImpositivo != null) {
			equals = equals
					&& this.identificadorImpositivo.equals(organismo.identificadorImpositivo);
		} else {
			equals = equals && (organismo.identificadorImpositivo == null);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result + ((this.denominacion == null) ? 0 : this.denominacion.hashCode());
		result = PRIMO * result + ((this.descripcion == null) ? 0 : this.descripcion.hashCode());
		result = PRIMO
				* result
				+ ((this.identificadorImpositivo == null) ? 0 : this.identificadorImpositivo
						.hashCode());

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.denominacion != null ? denominacion : STR_NULO);
		sb.append(" ");
		sb.append(this.descripcion != null ? descripcion : STR_NULO);
		sb.append(" ");
		sb.append(this.identificadorImpositivo != null ? identificadorImpositivo.toString()
				: STR_NULO);
		sb.append(" ");

		return sb.toString();
	}

	private IdentificadorImpositivo identificadorImpositivo;

	private String denominacion;

	private String descripcion;

	private static final long serialVersionUID = 1L;
}