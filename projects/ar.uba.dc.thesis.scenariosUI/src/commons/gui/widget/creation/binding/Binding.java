package commons.gui.widget.creation.binding;

import commons.gui.model.CompositeModel;
import commons.gui.model.binding.ValueBinding;

public interface Binding {

	String getValue();

	CompositeModel getCompositeModel();

	Object getModel();

	String getPropertyName();

	boolean isFakeBinding();

	ValueBinding bind(final Object component);
}