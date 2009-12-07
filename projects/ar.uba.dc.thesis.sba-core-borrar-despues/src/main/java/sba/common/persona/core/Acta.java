/*
 * $Id: Acta.java,v 1.2 2007/10/02 21:09:36 cvschioc Exp $
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

import sba.common.core.CopyConstructible;
import sba.common.persistence.BasePersistentObject;

/**
 * @author Miguel Díaz
 * @version $Revision: 1.2 $ - $Date: 2007/10/02 21:09:36 $
 */
public class Acta extends BasePersistentObject implements Serializable, Cloneable,
		CopyConstructible<Acta> {

	public Acta() {
		super();
	}

	public Acta(Calendar fechaCreacion, Calendar fechaVencimiento, String descripcion) {
		this();
		this.fechaCreacion = fechaCreacion;
		this.fechaVencimiento = fechaVencimiento;
		this.descripcion = descripcion;
	}

	public Acta copyConstructor() {
		return new Acta(this.getFechaCreacion(), this.getFechaVencimiento(), this.getDescripcion());
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * @return Returns the fechaCreacion.
	 */
	public Calendar getFechaCreacion() {
		return this.fechaCreacion;
	}

	/**
	 * @return Returns the fechaVencimiento.
	 */
	public Calendar getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setFechaCreacion(Calendar fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public void setFechaVencimiento(Calendar fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean equals = false;
		if ((obj != null) && (this.getClass().isInstance(obj))) {
			equals = this.equalsTo((Acta) obj);
		}
		return equals;
	}

	public boolean equalsTo(Acta acta) {
		boolean equals = true;
		if (this.descripcion != null) {
			equals = equals && this.descripcion.equals(acta.descripcion);
		} else {
			equals = equals && (acta.descripcion == null);
		}
		if (this.fechaCreacion != null) {
			equals = equals && this.fechaCreacion.equals(acta.fechaCreacion);
		} else {
			equals = equals && (acta.fechaCreacion == null);
		}
		if (this.fechaVencimiento != null) {
			equals = equals && this.fechaVencimiento.equals(acta.fechaVencimiento);
		} else {
			equals = equals && (acta.fechaVencimiento == null);
		}
		return equals;
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIMO = 37;
		int result = 17;
		result = PRIMO * result + ((this.descripcion == null) ? 0 : this.descripcion.hashCode());
		result = PRIMO * result
				+ ((this.fechaCreacion == null) ? 0 : this.fechaCreacion.hashCode());
		result = PRIMO * result
				+ ((this.fechaVencimiento == null) ? 0 : this.fechaVencimiento.hashCode());
		return result;
	}

	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		final String STR_NULO = "";
		sb.append(this.descripcion != null ? descripcion : STR_NULO);
		sb.append(" ");
		sb.append(this.fechaCreacion != null ? fechaCreacion.toString() : STR_NULO);
		sb.append(" ");
		sb.append(this.fechaVencimiento != null ? fechaVencimiento.toString() : STR_NULO);
		sb.append(" ");
		return sb.toString();
	}

	private Calendar fechaCreacion;

	private Calendar fechaVencimiento;

	private String descripcion;

	public static final long serialVersionUID = 1L;
}
