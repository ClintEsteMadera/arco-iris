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
 * $Id: ServiceException.java,v 1.1 2007/12/21 19:21:57 cvschioc Exp $
 */
package sba.common.exception;

/**
 * Clase base para excepciones de servicio.
 * 
 * @author Gabriel Tursi
 * @version $Revision: 1.1 $ - $Date: 2007/12/21 19:21:57 $
 */
public abstract class ServiceException extends RuntimeException {

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ServiceException(String message) {
		super(message);
	}

    public static final String DEFAULT_ERROR_MESSAGE = "Ha ocurrido un error en la aplicaci�n";
    
}