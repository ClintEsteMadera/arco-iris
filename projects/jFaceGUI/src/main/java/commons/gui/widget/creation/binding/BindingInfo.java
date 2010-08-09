package commons.gui.widget.creation.binding;

import commons.gui.model.CompositeModel;
import commons.gui.model.CompositeModelAdapter;
import commons.gui.model.ValueModel;
import commons.gui.model.binding.ValueBinding;
import commons.gui.model.binding.ValueBindingManager;
import commons.gui.model.binding.ValueBindingUpdateListener;
import commons.utils.ClassUtils;

public class BindingInfo implements Binding {

	public BindingInfo(CompositeModel compositeModel, String propertyName) {
		super();
		if (compositeModel == null) {
			throw new IllegalArgumentException("El atributo compositeModel no puede ser nulo");
		}
		if (propertyName == null) {
			throw new IllegalArgumentException("El atributo propertyName no puede ser nulo");
		}
		this.propertyName = propertyName;
		this.compositeModel = compositeModel;
	}

	@SuppressWarnings("unchecked")
	public BindingInfo(ValueModel valueModel) {
		super();
		this.propertyName = "";
		this.compositeModel = valueModel instanceof CompositeModel ? (CompositeModel) valueModel
				: new CompositeModelAdapter(valueModel);
	}

	public boolean isFakeBinding() {
		return false;
	}

	public void setBeanModel(CompositeModel model) {
		this.compositeModel = model;
	}

	/*
	 * @see jface.gui.gui.widget.creation.binding.Binding#getCompositeModel()
	 */
	public CompositeModel getCompositeModel() {
		return this.compositeModel;
	}

	/*
	 * @see jface.gui.gui.widget.creation.binding.Binding#getModel()
	 */
	public Object getModel() {
		return this.getCompositeModel().getValue();
	}

	/*
	 * @see jface.gui.gui.widget.creation.binding.Binding#getValue()
	 */
	public String getValue() {
		Object bean = this.getModel();
		return ClassUtils.getValue(bean, getPropertyName());
	}

	/*
	 * @see jface.gui.gui.widget.creation.binding.Binding#getPropertyName()
	 */
	public String getPropertyName() {
		return this.propertyName;
	}

	@SuppressWarnings("unchecked")
	public ValueBinding bind(final Object component) {
		if (component == null) {
			throw new IllegalArgumentException("El control con el cual bindear no puede ser nulo");
		}

		ValueModel valueModel = this.getCompositeModel().getValueModel(this.getPropertyName());
		this.binding = ValueBindingManager.getInstance().createBinding(valueModel, component);

		if (this.bindingReadListener != null) {
			this.binding.addReadingListener(this.bindingReadListener);
		}

		if (this.bindingWriteListener != null) {
			this.binding.addWritingListener(this.bindingWriteListener);
		}

		this.binding.bind();
		return binding;
	}

	public ValueBindingUpdateListener getBindingReadListener() {
		return bindingReadListener;
	}

	public void setBindingReadListener(ValueBindingUpdateListener bindingReadListener) {
		this.bindingReadListener = bindingReadListener;
	}

	public ValueBindingUpdateListener getBindingWriteListener() {
		return bindingWriteListener;
	}

	public void setBindingWriteListener(ValueBindingUpdateListener bindingWriteListener) {
		this.bindingWriteListener = bindingWriteListener;
	}

	/**
	 * Binding entre el modelo y el componente
	 * 
	 * @return
	 */
	public ValueBinding getBinding() {
		return binding;
	}

	private CompositeModel compositeModel;

	private ValueBinding binding;

	private final String propertyName;

	private ValueBindingUpdateListener bindingReadListener;

	private ValueBindingUpdateListener bindingWriteListener;
}