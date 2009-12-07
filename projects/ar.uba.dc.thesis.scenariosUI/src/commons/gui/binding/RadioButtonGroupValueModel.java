package commons.gui.binding;

import java.util.ArrayList;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.binding.WidgetContainer;
import commons.gui.model.types.EditType;
import commons.gui.widget.RadioButtonGroup;

/**
 * Clase utilziada internamente para el binding de RadioButtonGroup
 * 
 * @author ppastorino
 */
class RadioButtonGroupValueModel implements ValueModel, WidgetContainer {

	public RadioButtonGroupValueModel(RadioButtonGroup rbGroup) {
		this.rbGroup = rbGroup;

		listeners = new ArrayList<ValueChangeListener>();

		changeListener = new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				onPropertyChange(event);
			}
		};
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		listeners.add(listener);
		if (listeners.size() == 1) {
			this.rbGroup.setPropertyChangeListener(this.changeListener);
		}
	}

	public static boolean supportsControl(Control control) {
		return control instanceof Text || control instanceof Label;
	}

	public Object getValue() {
		return rbGroup.getValue();
	}

	public EditType getValueType() {
		return editType;
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		listeners.remove(listener);
	}

	@SuppressWarnings("unchecked")
	public void notifyChange() {
		final ValueChangeEvent changeEvent = new ValueChangeEvent(this, null, null);

		for (ValueChangeListener listener : listeners) {
			listener.valueChange(changeEvent);
		}
	}

	public void setValue(Object value) {
		rbGroup.setValue(value);
	}

	public Widget getWidget() {
		return this.rbGroup.getRadioBox();
	}

	@SuppressWarnings("unchecked")
	private void onPropertyChange(PropertyChangeEvent ev) {
		final ValueChangeEvent changeEvent = new ValueChangeEvent(this, ev.getOldValue(), ev
				.getNewValue());

		for (ValueChangeListener listener : listeners) {
			listener.valueChange(changeEvent);
		}
	}

	private ArrayList<ValueChangeListener> listeners;

	private IPropertyChangeListener changeListener;

	private RadioButtonGroup rbGroup;

	private static EditType<Object> editType = new EditType<Object>(Object.class);

}