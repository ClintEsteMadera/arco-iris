package commons.gui.background;

public class InvocationResult {

	public InvocationResult(Object result, Throwable error) {
		super();
		this.error = error;
		this.returnedObject = result;
	}

	public Throwable getError() {
		return error;
	}

	public Object getReturnedObject() throws BackgroundInvocationException {
		if (error != null) {
			throw new BackgroundInvocationException(error);
		}
		return returnedObject;
	}

	public boolean getInvocationStatus() {
		return this.error == null;
	}

	Throwable error;
	Object returnedObject;
}
