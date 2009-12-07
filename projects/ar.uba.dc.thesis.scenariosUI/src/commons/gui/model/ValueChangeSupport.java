package commons.gui.model;

import java.util.Iterator;

import sba.common.dataestructures.LazyList;


/**
 * Soporte para manejar ValueListener. <br>
 * El enumerado "T" referencia al tipo del objeto que cambia (habitualmente "wrappeado" en un
 * "ValueModel").
 * @author P.Pastorino
 */
class ValueChangeSupport<T> {

    public ValueChangeSupport(Object source) {
        m_source = source;
    }

    public void fireValueChange(ValueChangeEvent<T> ev) {
        for (Iterator<ValueChangeListener> e = m_listeners.iterator(); e.hasNext();) {
            e.next().valueChange(ev);
        }
    }

    public void fireValueChange(T oldValue, T newValue) {
    	this.fireValueChange(oldValue, newValue,false);
    }

    public void fireValueChange(T oldValue, T newValue, boolean canceling) {
        if (!hasListeners())
            return;

        if (oldValue == newValue || (oldValue != null && oldValue.equals(newValue)))
            return;

        final ValueChangeEvent<T> ev = new ValueChangeEvent<T>(m_source, oldValue, newValue, canceling);
        
        fireValueChange(ev);
    }

    public void fireValueChange() {
        if (!hasListeners())
            return;

        final ValueChangeEvent<T> ev = new ValueChangeEvent<T>(m_source, null, null);
        fireValueChange(ev);
    }

    public void addValueListener(ValueChangeListener listener) {
        m_listeners.add(listener);
    }

    public void removeValueListener(ValueChangeListener listener) {
        m_listeners.remove(listener);
    }

    public Iterator<ValueChangeListener> getListeners() {
        return m_listeners.iterator();
    }

    public boolean hasListeners() {
        return !m_listeners.isEmpty();
    }

    private LazyList<ValueChangeListener> m_listeners = new LazyList<ValueChangeListener>();

    private final Object m_source;
};
