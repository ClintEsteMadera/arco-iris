package commons.security.exception;

public class ExpiredPasswordException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public ExpiredPasswordException(String message) {
		super(message);
	}

	public ExpiredPasswordException(String message, Throwable cause) {
		super(message, cause);
	}
}
