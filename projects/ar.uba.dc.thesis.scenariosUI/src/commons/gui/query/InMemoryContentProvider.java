package commons.gui.query;

import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;


public class InMemoryContentProvider extends ArrayContentProvider {

	@Override
	public Object[] getElements(Object input){
		// Cast input appropriately and perform a database query
		return ((Collection) input).toArray();
	}

}
