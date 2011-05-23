package commons.security.exception;

public class BadPasswordCompositionException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public BadPasswordCompositionException(String message) {
		super(message);
	}

	public BadPasswordCompositionException(String message, Throwable cause) {
		super(message, cause);
	}
}
