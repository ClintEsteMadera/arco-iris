/*
 * $Id: ConstitucionPersonaJuridica.java,v 1.9 2008/04/22 20:39:51 cvschioc Exp $
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362 Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de
 * Caja de Valores S.A. ("Información Confidencial"). Usted no
 * divulgará tal Información Confidencial y la usará solamente
 * de acuerdo a los términos del acuerdo de licencia que Ud. posee
 * con Caja de Valores S.A.
 */
package sba.common.persona.core;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import sba.common.annotations.DataTypeAnnotation;
import sba.common.datetime.Mes;
import sba.common.validation.CommonDataTypes;

/**
 * @author Miguel Díaz
 * @version $Revision: 1.9 $ - $Date: 2008/04/22 20:39:51 $
 */
public class ConstitucionPersonaJuridica implements Serializable {

	public ConstitucionPersonaJuridica() {
		super();
	}

	public ConstitucionPersonaJuridica(Calendar fechaConstitucion, Integer duracion,
			String descripcion, Mes fechaCierreEjercicio) {
		this();
		this.fechaConstitucion = fechaConstitucion;
		this.duracion = duracion;
		this.descripcion = descripcion;
		this.mesCierreEjercicio = fechaCierreEjercicio;
	}

	public ConstitucionPersonaJuridica(Calendar fechaConstitucion, Mes fechaCierreEjercicio,
			Integer duracion) {
		this(fechaConstitucion, duracion, "", fechaCierreEjercicio);
	}

	/**
	 * @return Returns the descripcion.
	 */
	@DataTypeAnnotation(typeId=CommonDataTypes.DESCRIPCION)
	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * @return Returns the duracion.
	 */
	public Integer getDuracion() {
		return this.duracion;
	}

	/**
	 * @return Returns the fechaConstitucion.
	 */
	public Calendar getFechaConstitucion() {
		return this.fechaConstitucion;
	}

	/**
	 * @param descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @param duracion
	 */
	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}

	/**
	 * @param fechaConstitucion
	 */
	public void setFechaConstitucion(Calendar fechaConstitucion) {
		this.fechaConstitucion = fechaConstitucion;
	}

	public Mes getMesCierreEjercicio() {
		return mesCierreEjercicio;
	}

	public void setMesCierreEjercicio(Mes fechaCierreEjercicio) {
		this.mesCierreEjercicio = fechaCierreEjercicio;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if ((obj != null) && (this.getClass().isInstance(obj))) {
			equals = this.equalsConstitucion((ConstitucionPersonaJuridica) obj);
		}
		return equals;
	}

	/**
	 * @param constitucion
	 * @return
	 */
	public boolean equalsConstitucion(ConstitucionPersonaJuridica constitucion) {
		boolean equals = true;
		if (this.fechaConstitucion != null) {
			equals = equals && this.fechaConstitucion.equals(constitucion.fechaConstitucion);
		} else {
			equals = equals && (constitucion.fechaConstitucion == null);
		}
		if (this.mesCierreEjercicio != null) {
			equals = equals && this.mesCierreEjercicio.equals(constitucion.mesCierreEjercicio);
		} else {
			equals = equals && (constitucion.mesCierreEjercicio == null);
		}
		if (this.duracion != null) {
			equals = equals && this.duracion.equals(constitucion.duracion);
		} else {
			equals = equals && (constitucion.duracion == null);
		}
		if (this.descripcion != null) {
			equals = equals && this.descripcion.equals(constitucion.descripcion);
		} else {
			equals = equals && (constitucion.descripcion == null);
		}
		return equals;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result
				+ ((this.fechaConstitucion == null) ? 0 : this.fechaConstitucion.hashCode());
		result = PRIMO * result
				+ ((this.mesCierreEjercicio == null) ? 0 : this.mesCierreEjercicio.hashCode());
		result = PRIMO * result + ((this.duracion == null) ? 0 : this.duracion.hashCode());
		result = PRIMO * result + ((this.descripcion == null) ? 0 : this.descripcion.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.fechaConstitucion != null ? fechaConstitucion : STR_NULO);
		sb.append(" ");
		sb.append(this.descripcion != null ? descripcion : STR_NULO);
		sb.append(" ");
		sb.append(this.mesCierreEjercicio != null ? mesCierreEjercicio : STR_NULO);
		sb.append(" ");
		sb.append(this.duracion != null ? duracion : STR_NULO);
		sb.append(" ");
		return sb.toString();
	}

	/**
	 * Decide si el objeto se encuentra vacío o no.
	 * @return <code>true</code> en caso que el objeto tenga todos sus valores por defecto,
	 *         <code>false</code>, en otro caso.
	 */
	public boolean isEmpty() {
		return this.fechaConstitucion == null && this.duracion == null
				&& this.mesCierreEjercicio == null && StringUtils.isEmpty(this.descripcion);
	}

	private Calendar fechaConstitucion;

	private Integer duracion; // en años

	private Mes mesCierreEjercicio;

	private String descripcion;

	public static final long serialVersionUID = 1L;
}