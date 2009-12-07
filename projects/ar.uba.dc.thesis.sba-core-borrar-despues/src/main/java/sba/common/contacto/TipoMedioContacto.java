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
 * $Id: TipoMedioContacto.java,v 1.3 2008/03/18 21:15:50 cvschioc Exp $
 */
package sba.common.contacto;

import java.io.Serializable;

/**
 * @author Gabriel Tursi
 * @version $Revision: 1.3 $ $Date: 2008/03/18 21:15:50 $
 */
public enum TipoMedioContacto  implements Serializable {

	TELEFONO("Tel�fono"),
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