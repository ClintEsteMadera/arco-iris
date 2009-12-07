package commons.gui.command;

import java.util.Collection;


public interface RowSelectionHandlerCommand<T> {

	T store(T object);
	
	T update(int rowIndex, T object);

	Collection<T> findAll();

	T cloneElement(T object);

	void delete(T elementAt);

}
