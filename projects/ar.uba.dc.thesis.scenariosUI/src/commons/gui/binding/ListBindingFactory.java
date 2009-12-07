package commons.gui.binding;

import org.eclipse.swt.widgets.List;

import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueModelAdapterBindingFactory;

public class ListBindingFactory extends ValueModelAdapterBindingFactory {

	@SuppressWarnings("unchecked")
	@Override
	protected ValueModel createAdapter(ValueModel model, Object component) {
		return createAdapterForEditor(model, (List) component);
	}

	protected ValueModel createAdapterForEditor(ValueModel model, List list) {
		return new ListControlValueModel(list, model.getValueType());
	}

	@Override
	public boolean supports(Object component) {
		return component instanceof List;
	}
}
