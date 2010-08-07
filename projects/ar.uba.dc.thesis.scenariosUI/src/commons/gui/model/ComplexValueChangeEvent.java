package commons.gui.model;

/**
 * Evento asociado a una modificacion de un modelo complejo.
 */
public class ComplexValueChangeEvent<T> extends ValueChangeEvent<T> {

	/**
	 * Construye el evento.
	 * 
	 * @param source
	 *            Generador del evento
	 * @param key
	 *            Identificador de la propiedad modificada Si el evento responde al cambio de múltiples propiedades pued
	 *            eutilizarse <code>null</code>
	 * @param oldValue
	 *            Valor anterior En caso de que el generador del evento desconozca el valor anterior puede utilizarse
	 *            <code>null</code>
	 * @param newValue
	 *            Valor nuevo En caso de que el generador del evento desconozca el nuevo valor puede utilizarse
	 *            <code>null</code>
	 */
	public ComplexValueChangeEvent(Object source, Object key, T oldValue, T newValue, boolean canceling) {
		super(source, oldValue, newValue, canceling);
		m_key = key;
	}

	/**
	 * Returns the key.
	 * 
	 * @return Object
	 */
	public Object getKey() {
		return m_key;
	}

	public boolean getCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	private final Object m_key;

	private boolean cancel;

	private static final long serialVersionUID = 1L;

};
