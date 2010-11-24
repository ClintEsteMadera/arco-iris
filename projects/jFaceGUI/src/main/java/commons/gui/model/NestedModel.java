package commons.gui.model;

import commons.gui.model.types.EditType;

/**
 * Modelo anidado.
 * 
 */
public class NestedModel<T> implements CompositeModel<T> {

	public NestedModel(CompositeModel parent, String nestedProperty) {
		this.parentModel = parent;
		this.propertyName = new PropertyName(nestedProperty);

		parent.addComplexValueChangeListener(new ComplexValueChangeListener() {
			public void complexValueChange(ComplexValueChangeEvent ev) {
				onParentChange(ev);
			}
		});
	}

	public CompositeModel getParentModel() {
		return parentModel;
	}

	public void addValueChangeListener(ValueChangeListener listener) {
		this.valueChange.addValueListener(listener);
	}

	public void notifyChange() {
		this.valueChange.fireValueChange();
	}

	public void removeValueChangeListener(ValueChangeListener listener) {
		this.valueChange.removeValueListener(listener);
	}

	@SuppressWarnings("unchecked")
	public T getValue() {
		return (T) this.parentModel.getValue(this.propertyName.getPropertyName());
	}

	@SuppressWarnings("unchecked")
	public EditType getValueType() {
		return this.parentModel.getValueType(this.propertyName.getPropertyName());
	}

	public void setValue(T value) {
		this.parentModel.setValue(this.propertyName.getPropertyName(), value);
	}

	public void addComplexValueChangeListener(ComplexValueChangeListener listener) {
		this.propertyChange.addListener(listener);
	}

	public Object getValue(Object key) throws IllegalStateException, IllegalArgumentException {
		return this.parentModel.getValue(getParentProperty(key));
	}

	public ValueModel getValueModel(Object key) throws IllegalArgumentException {
		return this.parentModel.getValueModel(getParentProperty(key));
	}

	public EditType getValueType(Object key) throws IllegalArgumentException {
		return this.parentModel.getValueType(getParentProperty(key));
	}

	public void removeComplexValueChangeListener(ComplexValueChangeListener listener) {
		this.propertyChange.removeListener(listener);
	}

	public void setValue(Object key, Object value) throws IllegalStateException, IllegalArgumentException {
		this.parentModel.setValue(getParentProperty(key), value);
	}

	public <NESTED_TYPE> CompositeModel<NESTED_TYPE> getNestedModel(String key, Class<NESTED_TYPE> clazz) {
		return new NestedModel<NESTED_TYPE>(this, key);
	}

	public String getRootProperty() {
		return this.propertyName.getPropertyName().toString();
	}

	private void onParentChange(ComplexValueChangeEvent ev) {

		final String child = propertyName.convertToChildProperty(ev.getKey());

		if (child == null) {
			return;
		}

		if (child.length() == 0) {
			this.valueChange.fireValueChange();
		} else {
			this.propertyChange.fireChange(child, ev.getOldValue(), ev.getNewValue(), ev.isCanceling());
		}
	}

	/**
	 * Mapea una propiedad "anidada" a una propiedad del padre
	 * 
	 * @param key
	 * @return
	 */
	private String getParentProperty(Object key) {
		return this.propertyName.convertToParentProperty(key);
	}

	private CompositeModel parentModel;

	private PropertyName propertyName;

	private ComplexValueChangeSupport propertyChange = new ComplexValueChangeSupport(this);

	private ValueChangeSupport valueChange = new ValueChangeSupport(this);
}
