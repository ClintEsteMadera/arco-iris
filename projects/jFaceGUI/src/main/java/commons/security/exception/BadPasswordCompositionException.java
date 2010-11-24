package commons.security.exception;

/**
 * Esta clase modela una excepci�n causada porque el password ingresado por un usuario no posee la composici�n correcta,
 * de acuerdo a una pol�tica de password determinada.
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
