package commons.gui.command;

import java.util.List;


public abstract class RowSelectionHandlerCommand4ObjectArray<T>
		implements RowSelectionHandlerCommand<T> {

	public abstract List<T> findAll();

	public abstract T findElement(int index, Object[] objectArray);

	public T findElement(int index, Object objectArray){
		return findElement(index, (Object[]) objectArray);
	}
	
}
