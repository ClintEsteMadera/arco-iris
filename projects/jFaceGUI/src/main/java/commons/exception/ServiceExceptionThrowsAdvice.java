package commons.exception;

import org.acegisecurity.AccessDeniedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

import commons.security.exception.AuthenticationException;

/**
 * This class intercepts all exceptions and makes sure that an appropiate message is shown to the user. If the exception
 * inherits from {@link ServiceException}, the message obtained via {@link ServiceException#getMessage()} will be used
 * directly, otherwise, a generic error will be shown.<br>
 */
public class ServiceExceptionThrowsAdvice implements ThrowsAdvice {

	protected final Log logger = LogFactory.getLog(getClass());

	private static final String ACCESS_DENIED_MESSAGE = "No posee permisos para ejecutar esta operación";

	public void afterThrowing(Exception ex) throws Throwable {
		boolean msgReadyForBeingDisplayedToTheUser = ex instanceof ServiceException
				|| ex instanceof AuthenticationException;

		if (!msgReadyForBeingDisplayedToTheUser) {
			String msg = ex.getMessage();
			if (msg == null) {
				msg = ex.toString() + "\n";
				ex.printStackTrace();
			}
			this.logger.error(msg);

			if (ex instanceof AccessDeniedException) {
				throw new ApplicationException(ACCESS_DENIED_MESSAGE, ex);
			}
			throw new ApplicationException(ex); // a generic error is shown
		}
	}
}