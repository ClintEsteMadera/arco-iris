package commons.gui.binding;

import java.util.ArrayList;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Widget;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;
import commons.gui.model.binding.WidgetContainer;
import commons.gui.model.types.EditType;
import commons.gui.widget.ComboEditor;

/**
 * Clase utilizada internamente para el binding de Combos.
 * 
 * 
 */
class ComboValueModel implements ValueModel, WidgetContainer {

	public ComboValueModel(ComboEditor editor, ValueModel model) {
		this(editor.getCombo(), model.getValueType(), editor.getComboModel());
	}

	public ComboValueModel(Combo combo, EditType editType, ComboModel model) {
		super();

		this.combo = combo;
		this.model = model;
		this.editType = editType;

		listeners = new ArrayList<ValueChangeListener>();

		modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent ev) {
				onModifyText(ev);
			}
		};
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		listeners.add(listener);
		if (listeners.size() == 1) {
			this.combo.addModifyListener(this.modifyListener);
		}
	}

	public Object getValue() {
		String text = this.combo.getText();
		return text2value(text);
	}

	public EditType getValueType() {
		return this.editType;
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		listeners.remove(listener);
		if (listeners.size() == 0) {
			this.combo.removeModifyListener(this.modifyListener);
		}
	}

	public void setValue(Object value) {
		combo.setText(value2text(value));
	}

	public void notifyChange() {
		final ValueChangeEvent<String> changeEvent = new ValueChangeEvent<String>(this, null, this.combo.getText());

		for (ValueChangeListener listener : listeners) {
			listener.valueChange(changeEvent);
		}
	}

	public void refreshItems() {
		this.combo.removeAll();

		if (this.model.getNullText() != null) {
			this.combo.add(this.model.getNullText());
		}

		for (int i = 0; i < this.model.getItemCount(); i++) {
			this.combo.add(this.model.getItemText(i));
		}
	}

	public Widget getWidget() {
		return this.combo;
	}

	private void onModifyText(ModifyEvent modifyEv) {
		this.notifyChange();
	}

	private Object text2value(String text) {

		if (this.model.getNullText() != null && this.model.getNullText().equals(text)) {
			return null;
		}

		final int size = this.model.getItemCount();

		for (int i = 0; i < size; i++) {
			if (this.model.getItemText(i).equals(text)) {
				return this.model.getItemValue(i);
			}
		}

		return null;
	}

	private String value2text(Object value) {

		if (value == null && this.model.getNullText() != null) {
			return this.model.getNullText();
		}

		final int size = this.model.getItemCount();

		for (int i = 0; i < size; i++) {
			Object v = this.model.getItemValue(i);

			if (v == value || (v != null && v.equals(value))) {
				return this.model.getItemText(i);
			}
		}

		return value == null ? "" : value.toString();
	}

	private ArrayList<ValueChangeListener> listeners;

	private ModifyListener modifyListener;

	private Combo combo;

	private ComboModel model;

	private EditType editType;
}
