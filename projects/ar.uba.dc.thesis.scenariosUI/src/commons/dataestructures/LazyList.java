package commons.dataestructures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author P.Pastorino
 */
public class LazyList<T> implements Cloneable {

	public void add(T element) {
		getList().add(element);
	}

	public void add(int index, T element) {
		getList().add(index, element);
	}

	public void addAll(Collection<T> elements) {
		getList().addAll(elements);
	}

	public boolean remove(T element) {
		if (m_list == null) {
			return false;
		}
		return m_list.remove(element);
	}

	public boolean removeAll(Collection<T> elements) {
		if (m_list == null) {
			return false;
		}
		return m_list.removeAll(elements);
	}

	public boolean isEmpty() {
		return m_list == null || m_list.isEmpty();
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		if (m_list == null) {
			return EMPTY_ITERATOR;
		}
		return m_list.iterator();
	}

	public Object[] toArray() {
		return m_list == null ? new Object[0] : m_list.toArray();
	}

	public T[] toArray(T[] array) {
		return m_list == null ? array : m_list.toArray(array);
	}

	public int size() {
		return m_list == null ? 0 : m_list.size();
	}

	public int indexOf(T element) {
		return m_list == null ? -1 : m_list.indexOf(element);
	}

	public void clear() {
		if (m_list != null) {
			m_list.clear();
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<T> collection() {
		return m_list == null ? Collections.EMPTY_LIST : m_list;
	}

	public T get(int index) {
		if (m_list == null) {
			throw new IndexOutOfBoundsException(index + ">=" + 0);
		}
		return m_list.get(index);
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e.toString());
		}
	}

	private List<T> getList() {
		if (m_list == null) {
			m_list = new ArrayList<T>(DEFAULT_INITIAL_CAPACITY);
		}
		return m_list;
	}

	private static final int DEFAULT_INITIAL_CAPACITY = 2;

	private List<T> m_list;

	private static final Iterator EMPTY_ITERATOR = Collections.EMPTY_LIST.iterator();
}
