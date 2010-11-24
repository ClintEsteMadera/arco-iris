package commons.security.exception;

/**
 * Esta clase modela una excepción causada porque el password ingresado por un usuario no posee la composición correcta,
 * de acuerdo a una política de password determinada.
 */
public class BadPasswordCompositionException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public BadPasswordCompositionException(String message) {
		super(message);
	}

	public BadPasswordCompositionException(String message, Throwable cause) {
		super(message, cause);
	}
}
