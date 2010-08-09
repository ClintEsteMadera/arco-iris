package commons.gui.model.binding;

import java.util.EventObject;

/**
 * Información del evento de actualización (lectura o escritura) de un binding.
 * 
 */
public class ValueBindingUpdateEvent extends EventObject {
	public ValueBindingUpdateEvent(ValueBinding source, Object newValue, boolean manualUpdate) {
		super(source);
		this.newValue = newValue;
		this.manualUpdate = manualUpdate;
	}

	/**
	 * Obtiene el binding que generó el evento.
	 * 
	 * @return
	 */
	public ValueBinding getBinding() {
		return (ValueBinding) this.getSource();
	}

	/**
	 * Obtiene el valor que va a ser asignado.
	 * 
	 * @return
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * Setea el valor que va a ser asignado.
	 * 
	 * @param newValue
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	/**
	 * Informa si el evento fue generado por una actualización "manual".
	 * 
	 * @return
	 */
	public boolean isManualUpdate() {
		return manualUpdate;
	}

	private Object newValue;

	private boolean manualUpdate;

	private static final long serialVersionUID = 1L;
}
