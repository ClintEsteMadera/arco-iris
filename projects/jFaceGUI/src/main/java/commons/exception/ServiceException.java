package commons.exception;

/**
 * Clase base para excepciones de servicio.
 * 
 * 
 */
public abstract class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);

	}

	public ServiceException(String message) {
		super(message);
	}

	public static final String DEFAULT_ERROR_MESSAGE = "Ha ocurrido un error en la aplicación";

}