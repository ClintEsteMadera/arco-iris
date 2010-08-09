package commons.gui.model.binding;

import java.util.ArrayList;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;

public class DefaultValueBinding implements ValueBinding {

	public DefaultValueBinding(ValueModel valueModel, Object component) {
		this.valueModel = valueModel;
		this.component = component;
		this.writeListeners = new ArrayList<ValueBindingUpdateListener>();
		this.readListeners = new ArrayList<ValueBindingUpdateListener>();

		this.valueChangeListener = new ValueChangeListener() {
			public void valueChange(ValueChangeEvent ev) {
				onValueModelChanged(ev);
			}
		};
	}

	public Object getComponent() {
		return component;
	}

	public ValueModel getValueModel() {
		return valueModel;
	}

	public void unbind() {
		detachValueModel();
	}

	public void bind() {
		attachValueModel();
	}

	@SuppressWarnings("unchecked")
	public void read() {
		ValueBindingUpdateEvent event = new ValueBindingUpdateEvent(this, null, true);

		for (ValueBindingUpdateListener l : this.readListeners) {
			l.updatingValueBinding(event);

			try {
				this.ignoreValueEvents = true;
				this.getValueModel().setValue(event.getNewValue());
			} finally {
				this.ignoreValueEvents = false;
			}
		}
	}

	public void write() {
		ValueBindingUpdateEvent e = new ValueBindingUpdateEvent(this, this.getValueModel().getValue(), true);

		for (ValueBindingUpdateListener l : this.writeListeners) {
			l.updatingValueBinding(e);
		}
	}

	public void addReadingListener(ValueBindingUpdateListener listener) {
		this.readListeners.add(listener);
	}

	public void addWritingListener(ValueBindingUpdateListener listener) {
		this.writeListeners.add(listener);
	}

	public void removeReadingListener(ValueBindingUpdateListener listener) {
		this.readListeners.remove(listener);
	}

	public void removeWritingListener(ValueBindingUpdateListener listener) {
		this.writeListeners.remove(listener);
	}

	private void attachValueModel() {
		this.getValueModel().addValueChangeListener(valueChangeListener);
	}

	private void detachValueModel() {
		this.getValueModel().removeValueChangeListener(valueChangeListener);
	}

	private void onValueModelChanged(ValueChangeEvent ev) {
		if (this.ignoreValueEvents) {
			return;
		}
		write();
	}

	private ValueModel valueModel;

	private Object component;

	private boolean ignoreValueEvents;

	private ArrayList<ValueBindingUpdateListener> writeListeners;

	private ArrayList<ValueBindingUpdateListener> readListeners;

	private ValueChangeListener valueChangeListener;
}
