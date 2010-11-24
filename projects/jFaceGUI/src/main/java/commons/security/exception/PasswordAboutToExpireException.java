package commons.security.exception;

/**
 * Esta clase modela una excepción causada porque el password de un usuario está por expirar y es obligatorio cambiarlo.
 */
public class PasswordAboutToExpireException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public PasswordAboutToExpireException(String message) {
		super(message);
	}

	public PasswordAboutToExpireException(String message, Throwable cause) {
		super(message, cause);
	}
}
