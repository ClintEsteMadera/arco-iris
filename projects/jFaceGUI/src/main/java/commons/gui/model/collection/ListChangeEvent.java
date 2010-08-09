package commons.gui.model.collection;

/**
 * Evento de modificacion de lista.
 * 
 * @author P.Pastorino
 */
public class ListChangeEvent extends CollectionChangeEvent {

	protected ListChangeEvent(ListValueModel model, int type, int indexFrom, int indexTo, Object oldValue,
			Object newValue) {
		super(model, type, oldValue, newValue);
		m_index = indexFrom;
		m_indexTo = indexTo;
	}

	public static ListChangeEvent createUpdateListEvent(ListValueModel model, int indexFrom, int indexTo) {
		return new ListChangeEvent(model, UPDATE, indexFrom, indexTo, null, null);
	}

	public static ListChangeEvent createUpdateListEvent(ListValueModel model, int index, Object oldValue,
			Object newValue) {
		return new ListChangeEvent(model, UPDATE, index, index, oldValue, newValue);
	}

	public static ListChangeEvent createAddListEvent(ListValueModel model, int index, Object value) {
		return new ListChangeEvent(model, ADD, index, index, null, value);
	}

	public static ListChangeEvent createAddListEvent(ListValueModel model, int indexFrom, int indexTo) {
		return new ListChangeEvent(model, ADD, indexFrom, indexTo, null, null);
	}

	public static ListChangeEvent createRemoveListEvent(ListValueModel model, int index, Object value) {
		return new ListChangeEvent(model, REMOVE, index, index, value, null);
	}

	public static ListChangeEvent createRemoveListEvent(ListValueModel model, int indexFrom, int indexTo) {
		return new ListChangeEvent(model, REMOVE, indexFrom, indexTo, null, null);
	}

	public static ListChangeEvent createSortListEvent(ListValueModel model, int size) {
		return new ListChangeEvent(model, SORT, 0, size - 1, null, null);
	}

	public static ListChangeEvent createResetListEvent(ListValueModel model) {
		return new ListChangeEvent(model, ALL, UNDEFINED_INDEX, UNDEFINED_INDEX, null, null);
	}

	/**
	 * Returns the index.
	 * 
	 * @return int
	 */
	public int getIndex() {
		return m_index;
	}

	public int getIndexTo() {
		return m_indexTo;
	}

	public static final int SORT = 4;

	public static final int UNDEFINED_INDEX = -1;

	private int m_index;

	private int m_indexTo;
}