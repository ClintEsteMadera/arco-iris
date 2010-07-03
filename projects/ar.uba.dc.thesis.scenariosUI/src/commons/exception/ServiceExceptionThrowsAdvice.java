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
 * $Id: ServiceExceptionThrowsAdvice.java,v 1.2 2008/02/18 18:04:07 cvschioc Exp $
 */
package commons.exception;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

import sba.common.security.exception.AuthenticationException;

/**
 * Clase encargada de interceptar todas las excepciones y asegurarse que se retorne un mensaje adecuado para ser
 * mostrado al usuario. En caso de ser una excepci�n de la jerarqu�a de <code>ServiceException</code> el mensaje
 * original ser� el definitivo, por lo que debe ser adecuado para mostrar al usuario; en otro caso se mostrar� un error
 * gen�rico. <br>
 * NOTA: se podr�a realimentar esta clase para interpretar las excepciones m�s recurrentes y generar un mensaje m�s
 * espec�fico.
 * 
 * @author Gabriel Tursi
 * @author Juan Jos� Zapico
 * @author Jonathan Chiocchio
 * @version $Revision: 1.2 $ - $Date: 2008/02/18 18:04:07 $
 */
public class ServiceExceptionThrowsAdvice implements ThrowsAdvice {

	public void afterThrowing(Exception ex) throws Throwable {
		boolean excepcionListaParaMostrarAlUsuario = ex instanceof ServiceException
				|| ex instanceof AuthenticationException;
		if (!excepcionListaParaMostrarAlUsuario) {
			String msg = ex.getMessage();
			if (msg == null) {
				msg = ex.toString() + "\n";
				ex.printStackTrace();
			}
			this.logger.error(msg);

			if (ex instanceof AccessDeniedException) {
				// El usuario no tiene permisos para invocar un servicio
				throw new ApplicationException(ACCESS_DENIED_MESSAGE, ex);
			}
			// Se mostrara un error generico
			throw new ApplicationException(ex);
		}
	}

	protected final Log logger = LogFactory.getLog(getClass());

	private static final String ROW_WAS_UPDATE_OR_DELETE_BY_ANOTHER_TRANSACTION = "Los datos que intenta modificar han sido modificados previamente por otro usuario";

	private static final String ACCESS_DENIED_MESSAGE = "No posee permisos para ejecutar esta operaci�n";
}