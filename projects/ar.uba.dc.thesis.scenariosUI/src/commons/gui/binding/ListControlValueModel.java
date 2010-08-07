package commons.gui.binding;

import java.text.Format;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Widget;

import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.binding.WidgetContainer;
import commons.gui.model.types.DefaultFormat;
import commons.gui.model.types.EditConfigurationManager;
import commons.gui.model.types.EditType;

/**
 * Clase utilizada internamente para el binding de controles de tipo 'List'. NOTA: por el momento este control no se
 * consideran los cambios en el control para actualziar el modelo.
 * 
 * 
 */
class ListControlValueModel implements ValueModel, WidgetContainer {

	public ListControlValueModel(List control, EditType editType) {
		super();

		this.control = control;
		this.editType = editType;
	}

	public void addValueChangeListener(ValueChangeListener listener) {
	}

	public Object getValue() {
		return control.getItems();
	}

	public EditType getValueType() {
		return this.editType;
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
	}

	public void notifyChange() {
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		Collection collection = null;

		if (value instanceof Collection) {
			collection = (Collection) value;
		} else if (value instanceof Object[]) {
			collection = Arrays.asList((Object[]) value);
		} else if (value != null) {
			collection = Arrays.asList(new Object[] { value });
		} else {
			collection = Collections.EMPTY_LIST;
		}

		Format format = null;

		control.removeAll();

		for (Object o : collection) {
			String s = "";

			if (o != null) {
				if (format == null) {
					format = EditConfigurationManager.getInstance().getFormat(new EditType(o.getClass()));
					if (format == null) {
						format = DefaultFormat.getInstance();
					}
				}
				s = format.format(o);
			}

			control.add(s);
		}
	}

	public Widget getWidget() {
		return this.control;
	}

	private List control;

	private EditType editType;
}
