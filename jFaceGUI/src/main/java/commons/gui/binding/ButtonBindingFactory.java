package commons.gui.binding;

import org.eclipse.swt.widgets.Button;

import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueModelAdapterBindingFactory;

public class ButtonBindingFactory extends ValueModelAdapterBindingFactory {

	@SuppressWarnings("unchecked")
	@Override
	protected ValueModel createAdapter(ValueModel model, Object component) {
		return new ButtonValueModel((Button) component);
	}

	@Override
	public boolean supports(Object component) {
		return component instanceof Button;
	}
}
