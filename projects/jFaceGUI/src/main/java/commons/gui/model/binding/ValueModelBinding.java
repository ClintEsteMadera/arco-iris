package commons.gui.model.binding;

import java.util.ArrayList;

import commons.gui.model.ValueChangeEvent;
import commons.gui.model.ValueChangeListener;
import commons.gui.model.ValueModel;

public class ValueModelBinding implements ValueBinding {

	public ValueModelBinding(ValueModel model, ValueModel component) {
		this.valueModel = model;
		this.component = component;

		this.valueChangelistener = new ValueChangeListener() {
			public void valueChange(ValueChangeEvent ev) {
				onValueChanged(ev);
			}
		};

		this.componentChangelistener = new ValueChangeListener() {
			public void valueChange(ValueChangeEvent ev) {
				onComponentChanged(ev);
			}
		};
	}

	public ValueModel getComponent() {
		return component;
	}

	public ValueModel getValueModel() {
		return valueModel;
	}

	public void addReadingListener(ValueBindingUpdateListener listener) {
		if (this.readListeners == null) {
			this.readListeners = new ArrayList<ValueBindingUpdateListener>();
		}
		this.readListeners.add(listener);
	}

	public void addWritingListener(ValueBindingUpdateListener listener) {
		if (this.writeListeners == null) {
			this.writeListeners = new ArrayList<ValueBindingUpdateListener>();
		}
		this.writeListeners.add(listener);
	}

	public void removeReadingListener(ValueBindingUpdateListener listener) {
		if (this.readListeners != null) {
			this.readListeners.remove(listener);
		}
	}

	public void removeWritingListener(ValueBindingUpdateListener listener) {
		if (this.writeListeners != null) {
			this.writeListeners.remove(listener);
		}
	}

	@SuppressWarnings("unchecked")
	public void bind() {
		if (BindingInitializationMode.UpdateComponent.equals(initMode)) {
			updateComponent(false);
		} else if (BindingInitializationMode.UpdateValue.equals(initMode)) {
			updateValue(false);
		} else if (BindingInitializationMode.UpdateNull.equals(initMode)) {
			if (valueModel.getValue() != null) {
				updateComponent(false);
			} else if (component.getValue() != null) {
				updateValue(false);
			}
		}

		valueModel.addValueChangeListener(valueChangelistener);
		component.addValueChangeListener(componentChangelistener);
	}

	public void unbind() {
		valueModel.removeValueChangeListener(valueChangelistener);
		component.removeValueChangeListener(componentChangelistener);
	}

	public void read() {
		updateValue(true);
	}

	public void write() {
		updateComponent(true);
	}

	@SuppressWarnings("unchecked")
	private void updateValue(boolean manualUpdate) {
		updatingValue++;
		ignoreValueEvents = true;

		try {
			Object value = this.component.getValue();

			if (this.readListeners != null) {
				ValueBindingUpdateEvent ev = new ValueBindingUpdateEvent(this, value, manualUpdate);

				for (ValueBindingUpdateListener l : this.readListeners) {
					l.updatingValueBinding(ev);
				}

				value = ev.getNewValue();
			}

			this.valueModel.setValue(value);
		} finally {
			ignoreValueEvents = false;
			updatingValue--;
		}
	}

	@SuppressWarnings("unchecked")
	private void updateComponent(boolean manualUpdate) {
		ignoreComponentEvents = true;

		try {
			Object value = this.valueModel.getValue();

			if (this.writeListeners != null) {
				ValueBindingUpdateEvent ev = new ValueBindingUpdateEvent(this, value, manualUpdate);

				for (ValueBindingUpdateListener l : this.writeListeners) {
					l.updatingValueBinding(ev);
				}

				value = ev.getNewValue();
			}

			this.component.setValue(value);
		} finally {
			ignoreComponentEvents = false;
		}
	}

	@SuppressWarnings("unchecked")
	private void onValueChanged(ValueChangeEvent ev) {
		// si no es una actualizacion que provenga del modelo o de una
		// cancelacion
		// la ignoro
		if (this.updatingValue > 1 || this.updatingValue == 1 && !ev.isCanceling()) {
			return;
		}
		updateComponent(false);
	}

	private void onComponentChanged(ValueChangeEvent ev) {
		if (!this.ignoreComponentEvents) {
			updateValue(false);
		}
	}

	private ValueModel valueModel;

	private ValueModel component;

	@SuppressWarnings("unused")
	private boolean ignoreValueEvents;

	private boolean ignoreComponentEvents;

	private int updatingValue = 0;

	private ArrayList<ValueBindingUpdateListener> writeListeners;

	private ArrayList<ValueBindingUpdateListener> readListeners;

	private BindingInitializationMode initMode = BindingInitializationMode.UpdateComponent;

	private ValueChangeListener valueChangelistener;

	private ValueChangeListener componentChangelistener;
}