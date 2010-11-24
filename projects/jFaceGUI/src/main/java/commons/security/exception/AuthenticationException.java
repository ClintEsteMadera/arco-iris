package commons.security.exception;

/**
 * Clase de las cuales heredan todas las excepciones de autenticaci�n. Se crea concreta para el caso d�nde se desee
 * querer arrojar una excepci�n de autorizaci�n gen�rica.
 */
public class AuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}
