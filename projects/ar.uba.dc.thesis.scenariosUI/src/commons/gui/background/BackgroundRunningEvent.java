package commons.gui.background;

import java.util.EventObject;

/**
 * Evento asociado a la ejecucion de una tarea background
 * 
 * 
 */

public class BackgroundRunningEvent extends EventObject {

	public BackgroundRunningEvent(Object source, Object... arguments) {
		super(source);
		this.arguments = arguments;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	private Object[] arguments;

	private Object result;

	private static final long serialVersionUID = 1L;
}
