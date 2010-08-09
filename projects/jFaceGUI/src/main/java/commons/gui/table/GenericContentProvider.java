package commons.gui.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueChangeNotifier;

public class GenericContentProvider implements IStructuredContentProvider, ValueChangeNotifier {
	public GenericContentProvider() {
		listeners = new ArrayList<ValueChangeListener>();
	}

	public Object[] getElements(Object o) {
		if (o instanceof Object[]) {
			return (Object[]) o;
		}
		if (o instanceof List) {
			return ((List) o).toArray();
		}
		if (o instanceof Set) {
			return ((Set) o).toArray();
		}
		throw new IllegalArgumentException("Tipo de dato no soportado por GenericTable: " + o.getClass().getName());
	}

	public void dispose() {
	}

	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		// al hacer dispose de la tabla se genera este evento: lo ignoro
		if (newInput == null) {
			return;
		}

		ValueChangeEvent ev = new ValueChangeEvent(this, oldInput, newInput);

		for (ValueChangeListener listener : listeners) {
			listener.valueChange(ev);
		}
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		listeners.add(listener);
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifica a los ValueChangeListener listeners cuando hay un cambio en el modelo subyacente.
	 * 
	 * NOTA: Este método no debería ser invocado explicitamente (es para uso interno del framework)
	 */
	@SuppressWarnings("unchecked")
	public void notifyChange() {
		ValueChangeEvent ev = new ValueChangeEvent(this, null, null);

		for (ValueChangeListener listener : listeners) {
			listener.valueChange(ev);
		}
	}

	private ArrayList<ValueChangeListener> listeners;
}
