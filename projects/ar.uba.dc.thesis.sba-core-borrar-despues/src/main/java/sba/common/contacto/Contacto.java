/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
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
