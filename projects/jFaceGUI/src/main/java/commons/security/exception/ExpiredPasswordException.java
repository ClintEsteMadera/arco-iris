package commons.security.exception;

/**
 * Esta clase modela una excepci�n causada porque el password de un usuario expir�.
 */
public class ExpiredPasswordException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public ExpiredPasswordException(String message) {
		super(message);
	}

	public ExpiredPasswordException(String message, Throwable cause) {
		super(message, cause);
	}
}
