package commons.exception;

/**
 * Clase base para excepciones de aplicación.
 * 
 * 
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