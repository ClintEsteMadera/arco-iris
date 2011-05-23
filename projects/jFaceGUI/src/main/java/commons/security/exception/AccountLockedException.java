package commons.security.exception;

public class AccountLockedException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public AccountLockedException(String message) {
		super(message);
	}

	public AccountLockedException(String message, Throwable cause) {
		super(message, cause);
	}
}
