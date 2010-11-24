package commons.security.exception;

/**
 * Clase de las cuales heredan todas las excepciones de autenticación. Se crea concreta para el caso dónde se desee
 * querer arrojar una excepción de autorización genérica.
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
