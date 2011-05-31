package commons.gui.binding;

import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueModelAdapterBindingFactory;
import commons.gui.widget.adapter.IntegerModifierControl;

public class IntegerModifierControlBindingFactory extends ValueModelAdapterBindingFactory {

	@Override
	protected ValueModel createAdapter(ValueModel model, Object component) {
		return new IntegerModifierControlValueModel((IntegerModifierControl) component);
	}

	@Override
	public boolean supports(Object component) {
		return component instanceof IntegerModifierControl;
	}
}
