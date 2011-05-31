package commons.exception;

public class ApplicationException extends ServiceException {

	private static final long serialVersionUID = 6277468437111874316L;

	public ApplicationException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public ApplicationException(String detailMessage, Throwable cause) {
		super(detailMessage, cause);
	}

	public ApplicationException(String detailMessage) {
		super(detailMessage);
	}

	public ApplicationException(Throwable cause) {
		super(ServiceException.DEFAULT_ERROR_MESSAGE, cause);
	}
}