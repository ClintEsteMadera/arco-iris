package commons.gui.background;

/**
 * 
 * 
 */

public class BackgroundInvocationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BackgroundInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

	public BackgroundInvocationException(Throwable cause) {
		super(cause);
	}

}
