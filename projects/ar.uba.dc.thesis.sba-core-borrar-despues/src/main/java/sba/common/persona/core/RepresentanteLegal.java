/*
 * $Id: RepresentanteLegal.java,v 1.5 2008/01/04 19:13:37 cvschioc Exp $
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

import org.apache.commons.lang.StringUtils;

import sba.common.domicilio.Domicilio;
import sba.common.persistence.BasePersistentObject;

public class RepresentanteLegal extends BasePersistentObject {

	public RepresentanteLegal() {
		super();
		this.domicilio = new Domicilio();
	}

	public RepresentanteLegal(String nombre, String apellido, String numeroInscripcion,
			Domicilio domicilio) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.numeroInscripcion = numeroInscripcion;
		this.domicilio = domicilio;
	}

	public String getApellido() {
		return apellido;
	}

	public String getNombre() {
		return nombre;
	}

	public String getNumeroInscripcion() {
		return numeroInscripcion;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNumeroInscripcion(String numeroInscripcion) {
		this.numeroInscripcion = numeroInscripcion;
	}

	public Domicilio getDomicilio() {
		if(this.domicilio == null) {
			this.domicilio = new Domicilio();
		}
		return this.domicilio;
	}

	public void setDomicilio(Domicilio domicilio) {
		this.domicilio = domicilio;
	}

	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if (obj != null && obj instanceof RepresentanteLegal) {
			equals = this.equalsTo((RepresentanteLegal) obj);
		}
		return equals;
	}

	private boolean equalsTo(RepresentanteLegal representanteLegal) {
		boolean equals = true;
		if (this.apellido != null) {
			equals = equals && this.apellido.equals(representanteLegal.apellido);
		} else {
			equals = equals && (representanteLegal.apellido == null);
		}
		if (this.nombre != null) {
			equals = equals && this.nombre.equals(representanteLegal.nombre);
		} else {
			equals = equals && (representanteLegal.nombre == null);
		}
		if (this.numeroInscripcion != null) {
			equals = equals && this.numeroInscripcion.equals(representanteLegal.numeroInscripcion);
		} else {
			equals = equals && (representanteLegal.numeroInscripcion == null);
		}
		return equals;
	}

	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result + ((this.apellido == null) ? 0 : this.apellido.hashCode());
		result = PRIMO * result + ((this.nombre == null) ? 0 : this.nombre.hashCode());
		result = PRIMO * result
				+ ((this.numeroInscripcion == null) ? 0 : this.numeroInscripcion.hashCode());

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.apellido != null ? apellido : STR_NULO);
		sb.append(" ");
		sb.append(this.nombre != null ? nombre : STR_NULO);
		sb.append(" ");
		sb.append(this.numeroInscripcion != null ? numeroInscripcion.toString() : STR_NULO);
		sb.append(" ");
		sb.append(this.domicilio != null ? domicilio.toString() : STR_NULO);
		return sb.toString();
	}

	public boolean isEmpty() {
		boolean domicilioIsEmpty = this.domicilio == null || this.domicilio.isEmpty(false, true);
		return domicilioIsEmpty && StringUtils.isEmpty(this.nombre)
				&& StringUtils.isEmpty(this.apellido)
				&& StringUtils.isEmpty(this.numeroInscripcion);
	}
	
	private String nombre;

	private String apellido;

	private String numeroInscripcion;

	private Domicilio domicilio;

	private static final long serialVersionUID = 1L;
}