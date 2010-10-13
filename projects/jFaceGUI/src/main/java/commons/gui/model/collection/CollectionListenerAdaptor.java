package commons.gui.model.collection;

/**
 * Clase de soporte para adaptar listeners mas especificos a CollectionListener.
 */
class CollectionListenerAdaptor implements ListChangeListener {
	public CollectionListenerAdaptor(CollectionChangeListener listener) {
		m_listener = listener;
	}

	protected CollectionChangeListener getListener() {
		return m_listener;
	}

	/**
	 * @see jface.gui.gui.model.collection.ListChangeListener#listChange(jface.gui.gui.model.collection.ListChangeEvent)
	 */
	public void listChange(ListChangeEvent e) {
		m_listener.collectionChange(e);
	}

	@Override
	public boolean equals(Object o) {
		return o == this || o instanceof CollectionListenerAdaptor ? m_listener == ((CollectionListenerAdaptor) o).m_listener
				: false;
	}

	private CollectionChangeListener m_listener;
}