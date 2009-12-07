package commons.gui.model;

import java.util.EventObject;

/**
 * Evento asociado a una modificacion del ValueModel
 */
public class ValueChangeEvent<T> extends EventObject {

	public ValueChangeEvent(Object source, T oldValue, T newValue, boolean canceling) {
		super(source);
		m_oldValue = oldValue;
		m_newValue = newValue;
		m_canceling=canceling;
	}

	public ValueChangeEvent(Object source, T oldValue, T newValue) {
		this(source,oldValue,newValue,false);
	}

	public T getNewValue() {
		return m_newValue;
	}

	public T getOldValue() {
		return m_oldValue;
	}
	
	public boolean isCanceling() {
		return m_canceling;
	}

	private final T m_oldValue;

	private final T m_newValue;
	
	private boolean m_canceling;
	
	private static final long serialVersionUID = 1L;
};
