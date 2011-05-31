package commons.gui.model;

import java.util.Iterator;

import commons.datastructures.LazyList;

/**
 * Soporte para manejar ComplexValueChangeListener
 */
class ComplexValueChangeSupport {

	public ComplexValueChangeSupport(Object source) {
		m_source = source;
	}

	public <T> boolean fireChange(Object key, T oldValue, T newValue, boolean canceling) {
		if (oldValue == newValue || (oldValue != null && oldValue.equals(newValue)))
			return false;

		final ComplexValueChangeEvent ev = new ComplexValueChangeEvent<T>(m_source, key, oldValue, newValue, canceling);

		fireChange(ev);

		return ev.getCancel();
	}

	public <T> void fireChange(Object key) {
		final ComplexValueChangeEvent ev = new ComplexValueChangeEvent<T>(m_source, key, null, null, false);
		fireChange(ev);
	}

	public <T> void fireChange() {
		final ComplexValueChangeEvent ev = new ComplexValueChangeEvent<T>(m_source, null, null, null, false);
		fireChange(ev);
	}

	private void fireChange(ComplexValueChangeEvent ev) {
		for (Iterator<ComplexValueChangeListener> e = m_listeners.iterator(); e.hasNext();) {
			e.next().complexValueChange(ev);
		}
	}

	public void addListener(ComplexValueChangeListener listener) {
		m_listeners.add(listener);
	}

	public void removeListener(ComplexValueChangeListener listener) {
		m_listeners.remove(listener);
	}

	public Iterator<ComplexValueChangeListener> getListeners() {
		return m_listeners.iterator();
	}

	public boolean hasListeners() {
		return !m_listeners.isEmpty();
	}

	private LazyList<ComplexValueChangeListener> m_listeners = new LazyList<ComplexValueChangeListener>();

	private final Object m_source;
};
