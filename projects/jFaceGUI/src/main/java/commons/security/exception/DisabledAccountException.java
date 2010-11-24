package commons.security.exception;

/**
 * Esta clase modela una excepción causada porque la cuenta de un usuario ha sido deshabilitada.
 */
public class DisabledAccountException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public DisabledAccountException(String message) {
		super(message);
	}

	public DisabledAccountException(String message, Throwable cause) {
		super(message, cause);
	}
}
