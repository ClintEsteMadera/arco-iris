package commons.gui.model.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueHolder;
import commons.gui.model.ValueModel;
import commons.gui.model.types.EditType;

/**
 * Clase base para los modelos que contienen una lista.
 * @author P.Pastorino
 */
public class DefaultListValueModel<T> implements ListValueModel<T> {
	public DefaultListValueModel(ValueModel<List<T>> model) {
		setValueModel(model);
	}

	@SuppressWarnings("unchecked")
	public DefaultListValueModel() {
		ValueHolder v = new ValueHolder(List.class);
		v.setValue(new ArrayList<T>());
		setValueModel(v);
	}

	public void setList(List<T> list) {
		try {
			List<T> myList = m_valueModel.getValue();
			myList.clear();
			myList.addAll(list);
		} catch (Throwable e) {
			m_valueModel.setValue(list);
		}

		notifyListChange(ListChangeEvent.createResetListEvent(this));
	}

	public List<T> getList() {
		return this.getValue();
	}

	public int size() {
		final List<T> list = getList();
		return list != null ? list.size() : 0;
	}

	public void clear() {
		final List<T> list = getList();
		if (list != null && list.size() > 0) {
			ListChangeEvent event = ListChangeEvent.createRemoveListEvent(this, 0, list.size() - 1);
			list.clear();
			notifyListChange(event);
		}
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		final List<T> list = getList();
		if (list != null) {
			return list.iterator();
		}
		return Collections.EMPTY_LIST.iterator();
	}

	public void add(T element) {
		final List<T> list = checkList();
		final int i = list.size();
		list.add(element);
		notifyListChange(ListChangeEvent.createAddListEvent(this, i, element));
	}

	public void addAll(Collection<T> elements) {

		if (elements == null || elements.size() <= 0)
			return;

		final List<T> list = checkList();

		final int i = list.size();
		final int n = elements.size();
		list.addAll(elements);
		notifyListChange(ListChangeEvent.createAddListEvent(this, i, i + n - 1));
	}

	public void add(int index, T element) {
		checkList().add(index, element);
		notifyListChange(ListChangeEvent.createAddListEvent(this, index, element));
	}

	public void addAll(int index, List<T> elements) {

		if (elements == null || elements.size() <= 0)
			return;

		int n = elements.size();
		checkList().addAll(index, elements);
		notifyListChange(ListChangeEvent.createAddListEvent(this, index, index + n - 1));
	}

	public void remove(int index, int n) {
		final List<T> list = checkList();

		final int to = index + n;
		for (int i = index; i < to; i++) {
			list.remove(index);
		}
		notifyListChange(ListChangeEvent.createRemoveListEvent(this, index, index + n - 1));
	}

	public T remove(int index) {

		final T ret = checkList().remove(index);
		notifyListChange(ListChangeEvent.createRemoveListEvent(this, index, ret));
		return ret;
	}

	public boolean remove(T element) {
		final List<T> list = checkList();

		int index = list.indexOf(element);
		if (index >= 0) {
			remove(index);
			return true;
		}
		return false;
	}

	public T get(int index) {

		return checkList().get(index);
	}

	public T set(int index, T value) {

		final T ret = checkList().set(index, value);
		notifyListChange(ListChangeEvent.createUpdateListEvent(this, index, ret, value));
		return ret;
	}

	public int indexOf(T element) {

		return checkList().indexOf(element);
	}

	public boolean contains(T element) {
		return indexOf(element) >= 0;
	}

	public boolean contains(T element, Comparator<T> comparator) {
		return indexOf(element, comparator) >= 0;
	}

	public void sort(Comparator<T> comparator) {

		final List<T> list = checkList();

		Collections.sort(list, comparator);
		notifyListChange(ListChangeEvent.createSortListEvent(this, list.size()));
	}

	public void addListChangeListener(ListChangeListener listener) {
		m_listeners.add(listener);
	}

	public void removeListChangeListener(ListChangeListener listener) {
		m_listeners.remove(listener);
	}

	/**
	 * Notifica la modificacion de un elemento de la lista.
	 * @param index
	 */
	public void notifyUpdate(int index) {

		final Object value = checkList().get(index);
		notifyListChange(ListChangeEvent.createUpdateListEvent(this, index, value, value));
	}

	public void notifyChange() {
		this.m_valueModel.notifyChange();
	}

	/**
	 * @see commons.gui.model.collection.ListValueModel#find(java.util.Comparator)
	 */
	public int indexOf(T value, Comparator<T> comp) {
		final List<T> list = checkList();

		final int s = list.size();
		for (int i = 0; i < s; i++) {
			if (comp.compare(value, list.get(i)) == 0) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * @see commons.gui.model.collection.CollectionModel#addCollectionListener(commons.gui.model.collection.CollectionChangeListener)
	 */
	public void addCollectionListener(CollectionChangeListener listener) {
		addListChangeListener(new CollectionListenerAdaptor(listener));
	}

	/**
	 * @see commons.gui.model.collection.CollectionModel#removeCollectionListener(commons.gui.model.collection.CollectionChangeListener)
	 */
	public void removeCollectionListener(CollectionChangeListener listener) {
		removeListChangeListener(new CollectionListenerAdaptor(listener));
	}

	/**
	 * Metodo para redefinir en las clases derivadas (transformando ListChangeEvent en el tipo de
	 * evento requerido). *
	 * @param ev
	 *            Evento.
	 */
	protected void onListChange(ListChangeEvent ev) {}

	@SuppressWarnings("unchecked")
	protected final void notifyListChange(ListChangeEvent ev) {
		notifyListChangeListeners(ev);

		ValueChangeEvent valueChange = new ValueChangeEvent(this, null, null);
		notifyValueChangeListeners(valueChange);
	}

	private final void notifyListChangeListeners(ListChangeEvent ev) {
		onListChange(ev);

		for (Iterator it = m_listeners.iterator(); it.hasNext();) {
			final ListChangeListener listener = (ListChangeListener) it.next();
			if (listener != null) {
				listener.listChange(ev);
			}
		}

	}

	private final void notifyValueChangeListeners(ValueChangeEvent ev) {

		for (Iterator it = m_valueListeners.iterator(); it.hasNext();) {
			final ValueChangeListener listener = (ValueChangeListener) it.next();
			if (listener != null) {
				listener.valueChange(ev);
			}
		}
	}

	private void notifyValueChange(ValueChangeEvent ev) {
		ListChangeEvent listEvent = ListChangeEvent.createResetListEvent(this);
		notifyListChangeListeners(listEvent);
		notifyValueChangeListeners(ev);
	}

	protected List<T> checkList() {
		List<T> list = this.getList();

		if (list == null) {
			throw new IllegalStateException("La lista es nula");
		}

		return list;
	}

	public void removeFrom(int from) {
		remove(from, size() - from);
	}

	/**
	 * @see commons.gui.model.ValueModel#setValue(java.lang.Object)
	 */
	public void setValue(List<T> value) {
		setList(value);
	}

	public List<T> getValue() {
		return this.m_valueModel.getValue();
	}

	/**
	 * @see commons.gui.model.ValueModel#addValueChangeListener(commons.gui.model.ValueChangeListener)
	 */
	public void addValueChangeListener(ValueChangeListener listener) {
		this.m_valueListeners.add(listener);
	}

	/**
	 * @see commons.gui.model.ValueModel#getValueType()
	 */
	public EditType<List<T>> getValueType() {
		return m_valueModel.getValueType();
	}

	/**
	 * @see commons.gui.model.ValueModel#removeValueChangeListener(commons.gui.model.ValueChangeListener)
	 */
	public void removeValueChangeListener(ValueChangeListener listener) {
		this.m_valueListeners.remove(listener);
	}

	public static boolean isListConvertible(Class clazz) {
		return List.class.isAssignableFrom(clazz);
	}

	@SuppressWarnings("unchecked")
	private void setValueModel(ValueModel model) {
		m_valueModel = model;
		m_valueModel.addValueChangeListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent ev) {
				notifyValueChange(ev);
			}
		});
	}

	private List<ValueChangeListener> m_valueListeners = new ArrayList<ValueChangeListener>(1);

	private List<ListChangeListener> m_listeners = new ArrayList<ListChangeListener>(1);

	private ValueModel<List<T>> m_valueModel;
}