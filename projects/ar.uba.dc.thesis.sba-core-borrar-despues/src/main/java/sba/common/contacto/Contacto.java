/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: Contacto.java,v 1.3 2008/01/07 13:44:02 cvschioc Exp $
 */
package sba.common.contacto;

import sba.common.core.CopyConstructible;
import sba.common.persistence.BasePersistentObject;

/**
 * Modela un Contacto.
 * @author Gabriel Tursi
 * @version $Revision: 1.3 $ $Date: 2008/01/07 13:44:02 $
 */
public class Contacto extends BasePersistentObject implements Cloneable,
		CopyConstructible<Contacto> {

	public Contacto() {
		super();
	}

	public Contacto(String denominacion, TipoContacto tipoContacto,
			TipoMedioContacto tipoMedioContacto, String descripcion) {
		super();
		this.denominacion = denominacion;
		this.tipoContacto = tipoContacto;
		this.tipoMedioContacto = tipoMedioContacto;
		this.descripcion = descripcion;
	}

	public Contacto copyConstructor() {
		return new Contacto(this.getDenominacion(), this.getTipoContacto(), this
				.getTipoMedioContacto(), this.getDescripcion());
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String contacto) {
		this.denominacion = contacto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoContacto getTipoContacto() {
		return tipoContacto;
	}

	public void setTipoContacto(TipoContacto tipoContacto) {
		this.tipoContacto = tipoContacto;
	}

	public TipoMedioContacto getTipoMedioContacto() {
		return tipoMedioContacto;
	}

	public void setTipoMedioContacto(TipoMedioContacto tipoMedioContacto) {
		this.tipoMedioContacto = tipoMedioContacto;
	}

	@Override
	public Contacto clone() throws CloneNotSupportedException {
		return (Contacto) super.clone();
	}

	private String denominacion;

	private TipoContacto tipoContacto;

	private TipoMedioContacto tipoMedioContacto;

	private String descripcion;

	private static final long serialVersionUID = -8148259851921348625L;
}
