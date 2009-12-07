package commons.gui.model;

import commons.gui.model.types.EditType;

/**
 * Implementacion basica de ValueModel. Esta implementacion almacena el valor del modelo.
 */
public class ValueHolder<T> extends AbstractValueModel<T> {

	public ValueHolder(EditType descriptor) {
		m_descriptor = descriptor;
	}

	@SuppressWarnings("unchecked")
	public ValueHolder(T value) {
		this((Class<T>)value.getClass());
		m_value=value;
	}

	public ValueHolder(Class<T> clazz) {
		this(new EditType<T>(clazz));
	}

	public ValueHolder(Class<T> valueClass, String editType) {
		this(new EditType<T>(valueClass, editType));
	}

	public T getValue() {
		return m_value;
	}

	public void setValue(T value) {
		final T oldValue = this.getValue();
		m_value = value;
		fireValueChange(oldValue, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public EditType getValueType() {
		return m_descriptor;
	}

	public Class getValueClass() {
		return m_descriptor.getValueClass();
	}

	private EditType m_descriptor;

	private T m_value;
}
