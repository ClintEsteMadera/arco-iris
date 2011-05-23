package commons.security.exception;

public class PasswordAboutToExpireException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public PasswordAboutToExpireException(String message) {
		super(message);
	}

	public PasswordAboutToExpireException(String message, Throwable cause) {
		super(message, cause);
	}
}
