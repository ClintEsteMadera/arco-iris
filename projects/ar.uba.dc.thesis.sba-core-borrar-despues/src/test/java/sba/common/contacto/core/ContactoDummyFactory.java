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
 * $Id: ContactoDummyFactory.java,v 1.3 2008/01/07 13:38:30 cvschioc Exp $
 */

package sba.common.contacto.core;

import sba.common.contacto.Contacto;
import sba.common.contacto.TipoContacto;
import sba.common.contacto.TipoMedioContacto;

/**
 * Factory que crea objetos de la librer�a de Contactos. Los valores se encuentran hardcodeados en
 * el c�digo.
 * @author Jonathan Chiocchio
 * @author Miguel D�az
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

	private static final String DENOMINACION = "Denominaci�n Contacto";
	
	private static final String DESCRIPCION = "Descripci�n Contacto";
}