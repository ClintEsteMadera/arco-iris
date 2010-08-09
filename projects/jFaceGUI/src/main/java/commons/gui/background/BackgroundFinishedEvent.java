package commons.gui.background;

import java.util.EventObject;

/**
 * Evento asociado a la finalizacion de una tarea background
 * 
 * 
 */

public class BackgroundFinishedEvent extends EventObject {

	BackgroundFinishedEvent(Object source, Object result, Throwable error) {
		super(source);
		this.result = result;
		this.error = error;
	}

	public Throwable getError() {
		return error;
	}

	public Object getResult() {
		if (this.error != null) {
			throw new IllegalStateException("La tarea termino con error", this.error);
		}
		return result;
	}

	public boolean isOk() {
		return error == null;
	}

	private Object result;

	private Throwable error;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
