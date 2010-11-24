package commons.security.exception;

/**
 * Esta clase modela una excepción causada porque la cuenta de un usuario expiró.
 */
public class AccountLockedException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public AccountLockedException(String message) {
		super(message);
	}

	public AccountLockedException(String message, Throwable cause) {
		super(message, cause);
	}
}
