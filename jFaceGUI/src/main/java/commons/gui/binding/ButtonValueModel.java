package commons.gui.binding;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.binding.WidgetContainer;
import commons.gui.model.types.EditType;

/**
 * Clase utilziada internamente para el binding de botones.
 * 
 * 
 */
class ButtonValueModel implements ValueModel, WidgetContainer {

	public <T> ButtonValueModel(Button button) {
		super();

		this.button = button;

		listeners = new ArrayList<ValueChangeListener>();

		selectionListener = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				onWidgetSelected(e);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		listeners.add(listener);
		if (listeners.size() == 1) {
			this.button.addSelectionListener(this.selectionListener);
		}
	}

	public Object getValue() {
		return button.getSelection();
	}

	public EditType getValueType() {
		return editType;
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		listeners.remove(listener);
		if (listeners.size() == 0) {
			this.button.removeSelectionListener(this.selectionListener);
		}
	}

	public void setValue(Object value) {
		if (value instanceof Boolean) {
			button.setSelection(((Boolean) value).booleanValue());
		}
	}

	@SuppressWarnings("unchecked")
	public final void notifyChange() {
		final ValueChangeEvent changeEvent = new ValueChangeEvent(this, null, Boolean.valueOf(this.button
				.getSelection()));

		for (ValueChangeListener listener : listeners) {
			listener.valueChange(changeEvent);
		}
	}

	public Widget getWidget() {
		return this.button;
	}

	@SuppressWarnings("unchecked")
	private void onWidgetSelected(SelectionEvent e) {
		this.notifyChange();
	}

	private ArrayList<ValueChangeListener> listeners;

	private SelectionListener selectionListener;

	private Button button;

	private static EditType<Boolean> editType = new EditType<Boolean>(Boolean.class);
}