package commons.gui.model.collection;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Interface base para distintos tipos de colecciones.
 */
public interface CollectionModel<T> {

	public boolean contains(T elem);

	public boolean contains(T elem, Comparator<T> comparator);

	public Iterator<T> iterator();

	public void addCollectionListener(CollectionChangeListener listener);

	public void removeCollectionListener(CollectionChangeListener listener);
}