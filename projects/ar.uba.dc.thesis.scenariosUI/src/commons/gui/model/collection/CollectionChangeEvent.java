package commons.gui.model.collection;

/**
 * Evento asociado a la modificacion de una coleccion.
 * 
 * @author P.Pastorino
 */
public class CollectionChangeEvent {

	protected CollectionChangeEvent(CollectionModel model, int type, Object oldValue, Object newValue) {
		m_source = model;
		m_eventType = type;
		m_oldValue = oldValue;
		m_newValue = newValue;
	}

	public static CollectionChangeEvent createCollectionAddEvent(CollectionModel model, Object value) {
		return new CollectionChangeEvent(model, ADD, null, value);
	}

	public static CollectionChangeEvent createCollectionRemoveEvent(CollectionModel model, Object value) {
		return new CollectionChangeEvent(model, REMOVE, value, null);
	}

	public static CollectionChangeEvent createCollectionUpdateEvent(CollectionModel model, Object value) {
		return new CollectionChangeEvent(model, UPDATE, value, value);
	}

	public static CollectionChangeEvent createCollectionUpdateEvent(CollectionModel model, Object oldValue,
			Object newValue) {
		return new CollectionChangeEvent(model, UPDATE, oldValue, newValue);
	}

	public int getEventType() {
		return m_eventType;
	}

	public CollectionModel getSource() {
		return m_source;
	}

	public Object getNewValue() {
		return m_newValue;
	}

	public Object getOldValue() {
		return m_oldValue;
	}

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer(25);
		str.append("{");
		str.append("type=");
		str.append(m_eventType);
		str.append(", old=");
		str.append(m_oldValue);
		str.append(", new=");
		str.append(m_newValue);
		return str.toString();
	}

	private CollectionModel m_source;

	private int m_eventType;

	private Object m_oldValue;

	private Object m_newValue;

	public static final int ADD = 0;

	public static final int REMOVE = 1;

	public static final int UPDATE = 2;

	public static final int ALL = 3;
}