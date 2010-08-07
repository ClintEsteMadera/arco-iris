package commons.gui.binding;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.binding.WidgetContainer;
import commons.gui.model.types.EditType;
import commons.gui.table.GenericTable;

/**
 * Clase utilizada internamente para el binding de tablas.
 * 
 * 
 */
class TableValueModel implements ValueModel, WidgetContainer {

	public TableValueModel(GenericTable viewer, EditType editType) {
		super();

		this.viewer = viewer;
		this.editType = editType;
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		if (viewer.getGenericContentProvider() != null) {
			viewer.getGenericContentProvider().addValueChangeListener(listener);
		}
	}

	public Object getValue() {
		return this.viewer.getInput();
	}

	public EditType getValueType() {
		return this.editType;
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		if (viewer.getGenericContentProvider() != null) {
			viewer.getGenericContentProvider().removeValueChangeListener(listener);
		}
	}

	public void notifyChange() {
		if (viewer.getGenericContentProvider() != null) {
			viewer.getGenericContentProvider().notifyChange();
		}
	}

	public void setValue(Object value) {
		this.viewer.setInput(value);
	}

	public Widget getWidget() {
		return this.getTable();
	}

	public GenericTable getViewer() {
		return viewer;
	}

	public Table getTable() {
		return viewer.getTable();
	}

	protected void fireValueChange() {
		viewer.getGenericContentProvider().notifyChange();
	}

	private GenericTable viewer;

	private EditType editType;
}
