package commons.gui.model.binding;

import commons.gui.model.ValueModel;

public interface ValueBindingFactory {

	ValueBinding createBinding(ValueModel model, Object component);

	boolean supports(Object component);
}
