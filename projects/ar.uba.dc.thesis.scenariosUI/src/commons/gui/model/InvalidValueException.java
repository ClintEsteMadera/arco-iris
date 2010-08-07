package commons.gui.model;

/**
 * Excepcion que señala un cambio de valor incorrecto (que debe ser revertido)
 * 
 */
public class InvalidValueException extends RuntimeException {

	public InvalidValueException() {
		super();
		// TODO InvalidValueException IMPLEMENTAR!
	}

	public InvalidValueException(String message, Throwable cause) {
		super(message, cause);
		// TODO InvalidValueException IMPLEMENTAR!
	}

	public InvalidValueException(String message) {
		super(message);
		// TODO InvalidValueException IMPLEMENTAR!
	}

	public InvalidValueException(Throwable cause) {
		super(cause);
		// TODO InvalidValueException IMPLEMENTAR!
	}

	private static final long serialVersionUID = 1L;
}