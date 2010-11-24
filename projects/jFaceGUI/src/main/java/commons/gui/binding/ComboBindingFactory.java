package commons.gui.binding;

import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueModelAdapterBindingFactory;
import commons.gui.widget.ComboEditor;

public class ComboBindingFactory extends ValueModelAdapterBindingFactory {

	@Override
	protected ValueModel createAdapter(ValueModel model, Object component) {
		return createAdapterForEditor(model, (ComboEditor) component);
	}

	protected ValueModel createAdapterForEditor(ValueModel model, ComboEditor editor) {
		return new ComboValueModel(editor.getCombo(), model.getValueType(), editor.getComboModel());
	}

	@Override
	public boolean supports(Object component) {
		return component instanceof ComboEditor;
	}
}
