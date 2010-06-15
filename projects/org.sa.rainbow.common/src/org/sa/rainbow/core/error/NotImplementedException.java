/**
 * Created January 16, 2007.
 */
package org.sa.rainbow.core.error;

/**
 * A RuntimeException indicating that the method has not yet been implemented.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class NotImplementedException extends RuntimeException {

	private static final long serialVersionUID = -7295910148428936023L;

	/**
	 * Default Contructor.
	 */
	public NotImplementedException() {
		this("");
	}

	/**
	 * @param message
	 */
	public NotImplementedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public NotImplementedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotImplementedException(String message, Throwable cause) {
		super(message, cause);
	}

}
