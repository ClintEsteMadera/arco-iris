package commons.gui.binding;

import org.eclipse.swt.widgets.Control;

import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueModelAdapterBindingFactory;

public class TextBindingFactory extends ValueModelAdapterBindingFactory {

	@SuppressWarnings("unchecked")
	@Override
	protected ValueModel createAdapter(ValueModel model, Object component) {
		return new TextValueModel((Control) component, model.getValueType());
	}

	@Override
	public boolean supports(Object component) {
		return component instanceof Control && TextValueModel.supportsControl((Control) component);
	}

}
