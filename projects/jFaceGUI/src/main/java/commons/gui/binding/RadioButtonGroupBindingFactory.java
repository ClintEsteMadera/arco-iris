package commons.gui.binding;

import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueModelAdapterBindingFactory;
import commons.gui.widget.RadioButtonGroup;

public class RadioButtonGroupBindingFactory extends ValueModelAdapterBindingFactory {

	@SuppressWarnings("unchecked")
	@Override
	protected ValueModel createAdapter(ValueModel model, Object component) {
		return new RadioButtonGroupValueModel((RadioButtonGroup) component);
	}

	@Override
	public boolean supports(Object component) {
		return component instanceof RadioButtonGroup;
	}
}
