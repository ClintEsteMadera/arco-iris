package commons.gui.table;

/**
 * Table edition callbacks.
 */

public interface EditHandler<T> {

	public T handleCreation(int index);

	public boolean handleUpdate(T e, int index);

	public void handleView(T e, int index);

	public boolean handleDelete(T e, int index);
}
