package commons.gui.model;

import commons.gui.model.types.EditType;

/**
 * Adaptador de un modelo simple a uno complejo. Solo acepta la propiedad vacía ("")
 */
public class CompositeModelAdapter<T> implements CompositeModel<T> {

	public CompositeModelAdapter(Class<T> clazz) {
		this(new ValueHolder<T>(clazz));
	}

	public CompositeModelAdapter(T value) {
		this(new ValueHolder<T>(value));
	}

	public CompositeModelAdapter(ValueModel<T> valueModel) {
		this.valueModel = valueModel;
		this.complexListeners = new ComplexValueChangeSupport(this);
	}

	public ValueModel<T> getWrappedModel() {
		return this.valueModel;
	}

	public void addComplexValueChangeListener(ComplexValueChangeListener listener) {
		if (this.m_valueChangeListener == null) {
			this.m_valueChangeListener = new ValueChangeListener() {
				public void valueChange(ValueChangeEvent ev) {
					CompositeModelAdapter.this.complexListeners.fireChange("", ev.getOldValue(), ev.getNewValue(),
							ev.isCanceling());
				}
			};
			this.addValueChangeListener(this.m_valueChangeListener);
		}
		this.complexListeners.addListener(listener);
	}

	public T getValue(Object key) throws IllegalStateException, IllegalArgumentException {
		checkKey(key);
		return this.getValue();
	}

	public ValueModel getValueModel(Object key) throws IllegalArgumentException {
		checkKey(key);
		return this.getWrappedModel();
	}

	public EditType getValueType(Object key) throws IllegalArgumentException {
		checkKey(key);
		return this.getValueType();
	}

	public void notifyChange() {
		this.valueModel.notifyChange();
	}

	public void removeComplexValueChangeListener(ComplexValueChangeListener listener) {
		this.complexListeners.removeListener(listener);
	}

	@SuppressWarnings("unchecked")
	public void setValue(Object key, Object value) throws IllegalStateException, IllegalArgumentException {
		checkKey(key);
		this.setValue((T) value);
	}

	public T getValue() {
		return this.valueModel.getValue();
	}

	public void setValue(T value) {
		this.valueModel.setValue(value);
	}

	@SuppressWarnings("unchecked")
	public EditType getValueType() {
		return this.valueModel.getValueType();
	}

	@SuppressWarnings("unchecked")
	public <NESTED_TYPE> CompositeModel<NESTED_TYPE> getNestedModel(String key, Class<NESTED_TYPE> clazz) {
		checkKey(key);
		return (CompositeModel<NESTED_TYPE>) this;
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		this.valueModel.addValueChangeListener(listener);

	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		this.valueModel.removeValueChangeListener(listener);

	}

	private void checkKey(Object key) {
		if (key != null && !key.equals("")) {
			throw new IllegalArgumentException("No está definida la propiedad '" + key + "'");
		}
	}

	private ValueModel<T> valueModel;

	private ValueChangeListener m_valueChangeListener;

	private ComplexValueChangeSupport complexListeners;

}
