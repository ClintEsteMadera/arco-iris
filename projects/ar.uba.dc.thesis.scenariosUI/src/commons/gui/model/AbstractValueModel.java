package commons.gui.model;

import java.util.Iterator;

import commons.gui.model.types.EditType;

/**
 * Implementacion basica de ValueModel.
 */
public abstract class AbstractValueModel<T> implements ValueModel<T> {
	
	public AbstractValueModel() {
		m_valueChangeSupport = new ValueChangeSupport<T>(this);
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		m_valueChangeSupport.addValueListener(listener);
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		m_valueChangeSupport.removeValueListener(listener);
	}

	@SuppressWarnings("unchecked")
	public EditType getValueType() {
		return DEFAULT_TYPE;
	}

	public void notifyChange(){
		m_valueChangeSupport.fireValueChange();
	}
	
	protected void fireValueChange(T oldValue, T newValue) {
		m_valueChangeSupport.fireValueChange(oldValue, newValue);
	}

	protected void fireValueChange(T oldValue, T newValue, boolean canceling) {
		m_valueChangeSupport.fireValueChange(oldValue, newValue, canceling);
	}

	protected void fireValueChange(ValueChangeEvent<T> ev) {
		m_valueChangeSupport.fireValueChange(ev);
	}

	protected void fireValueChange() {
		m_valueChangeSupport.fireValueChange();
	}

	protected boolean hasListeners() {
		return m_valueChangeSupport.hasListeners();
	}

	protected Iterator<ValueChangeListener> getListeners() {
		return m_valueChangeSupport.getListeners();
	}

	private ValueChangeSupport<T> m_valueChangeSupport;

	private static final EditType<Object> DEFAULT_TYPE = new EditType<Object>(Object.class);
}
