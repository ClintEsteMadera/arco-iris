package commons.gui.model.binding;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Widget;

import commons.gui.model.ValueModel;

public abstract class ValueModelAdapterBindingFactory implements ValueBindingFactory {

	public ValueBinding createBinding(ValueModel model, Object component) {

		final ValueModel adapted = createAdapter(model, component);
		final ValueBinding binding = new ValueModelBinding(model, adapted);

		if (adapted instanceof WidgetContainer) {
			Widget widget = ((WidgetContainer) adapted).getWidget();

			final DisposeListener disposeListener = new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					binding.unbind();
				}
			};
			widget.addDisposeListener(disposeListener);
		}

		return binding;
	}

	public abstract boolean supports(Object component);

	protected abstract ValueModel createAdapter(ValueModel model, Object component);

}
