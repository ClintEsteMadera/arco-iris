package commons.exception;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

import sba.common.security.exception.AuthenticationException;

/**
 * Clase encargada de interceptar todas las excepciones y asegurarse que se retorne un mensaje adecuado para ser
 * mostrado al usuario. En caso de ser una excepción de la jerarquía de <code>ServiceException</code> el mensaje
 * original será el definitivo, por lo que debe ser adecuado para mostrar al usuario; en otro caso se mostrará un error
 * genérico. <br>
 * NOTA: se podría realimentar esta clase para interpretar las excepciones más recurrentes y generar un mensaje más
 * específico.
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

	private static final String ACCESS_DENIED_MESSAGE = "No posee permisos para ejecutar esta operación";
}