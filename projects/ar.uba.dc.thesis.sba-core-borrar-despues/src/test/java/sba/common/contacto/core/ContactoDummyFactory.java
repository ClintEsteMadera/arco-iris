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
 * $Id: ContactoDummyFactory.java,v 1.3 2008/01/07 13:38:30 cvschioc Exp $
 */

package sba.common.contacto.core;

import sba.common.contacto.Contacto;
import sba.common.contacto.TipoContacto;
import sba.common.contacto.TipoMedioContacto;

/**
 * Factory que crea objetos de la librería de Contactos. Los valores se encuentran hardcodeados en
 * el código.
 * @author Jonathan Chiocchio
 * @author Miguel Díaz
 * @version $Revision: 1.3 $ $Date: 2008/01/07 13:38:30 $
 */

public abstract class ContactoDummyFactory {

	public static Contacto crearContacto() {
		return new Contacto(DENOMINACION, TipoContacto.PARTICULAR, 
				TipoMedioContacto.DOMICILIO, DESCRIPCION);
	}
		
	public static Contacto crearContactoCorreo() {
		Contacto contactoXcorreo =
			new Contacto(DENOMINACION, TipoContacto.NO_ESPECIFICADO, 
					TipoMedioContacto.DOMICILIO, DESCRIPCION);
		return contactoXcorreo;
	}

	public static Contacto crearContactoMail() {
		return new Contacto(DENOMINACION, TipoContacto.NO_ESPECIFICADO, 
				TipoMedioContacto.EMAIL, DESCRIPCION);
	}

	private static final String DENOMINACION = "Denominación Contacto";
	
	private static final String DESCRIPCION = "Descripción Contacto";
}