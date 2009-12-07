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

    public static final String DEFAULT_ERROR_MESSAGE = "Ha ocurrido un error en la aplicación";
    
}