package commons.gui.binding;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import commons.gui.model.types.EditType;
import commons.gui.table.GenericTable;

/**
 * Clase utilizada internamente par el binding de tablas con filas "chequeables" El valor que representan es la lista de
 * elementos chequeados.
 * 
 * 
 */
class CheckedTableValueModel extends TableValueModel {

	public CheckedTableValueModel(GenericTable viewer, EditType editType) {
		super(viewer, editType);

		final ISelectionChangedListener selectionChangedListerner = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				CheckedTableValueModel.this.fireValueChange();
			}
		};
		viewer.addSelectionChangedListener(selectionChangedListerner);
	}

	@Override
	public Object getValue() {
		return this.getViewer().getCheckedItems();
	}

	@Override
	public void setValue(Object value) {
		List list = null;

		if (value instanceof List) {
			list = (List) value;
		} else if (value instanceof Object[]) {
			list = Arrays.asList((Object[]) value);
		} else if (value instanceof Set) {
			list = Arrays.asList(((Set) value).toArray());
		} else {
			list = Arrays.asList(new Object[] { value });
		}

		this.getViewer().setCheckedItems(list);
	}
}
