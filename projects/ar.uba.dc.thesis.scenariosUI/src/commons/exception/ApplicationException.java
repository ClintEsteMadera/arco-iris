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
 * $Id: ApplicationException.java,v 1.1 2007/12/21 19:21:57 cvschioc Exp $
 */
package commons.exception;

/**
 * Clase base para excepciones de aplicaci�n.
 * 
 * @author Gabriel Tursi
 * @version $Revision: 1.1 $ - $Date: 2007/12/21 19:21:57 $
 */
public class ApplicationException extends ServiceException {

    public ApplicationException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public ApplicationException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}

	public ApplicationException(String detailMessage) {
		super(detailMessage);
	}

	public ApplicationException(Throwable cause) {
		super(ServiceException.DEFAULT_ERROR_MESSAGE, cause);
	}

    private static final long serialVersionUID = 6277468437111874316L;
    
}