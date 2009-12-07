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
 * $Id: TipoMedioContacto.java,v 1.3 2008/03/18 21:15:50 cvschioc Exp $
 */
package sba.common.contacto;

import java.io.Serializable;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.3 $ $Date: 2008/03/18 21:15:50 $
 */
public enum TipoMedioContacto  implements Serializable {

	TELEFONO("Teléfono"),
	EMAIL("E-Mail"),
	DOMICILIO("Domicilio"),
	FAX("Fax");

	private TipoMedioContacto(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return this.descripcion;
	}

	private String descripcion;
}