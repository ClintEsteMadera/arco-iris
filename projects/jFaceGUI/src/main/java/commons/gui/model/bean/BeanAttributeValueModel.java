package commons.gui.model.bean;

import commons.gui.model.AttributeValueModel;
import commons.gui.model.AttributesModel;
import commons.gui.model.ValueChangeEvent;

/**
 * ValueModel basado en la propiedad de un bean.
 */
class BeanAttributeValueModel extends AttributeValueModel {

	public BeanAttributeValueModel(BeanAttribute attribute, AttributesModel parent, Object id) {
		super(attribute, parent, id);
	}

	/**
	 * Una vez que se seteo el valor:actualizo el flag de 'valor nulo'
	 */
	@Override
	protected final void afterSetValue(Object value) {
		m_isNull = ((BeanAttribute) getAttribute()).isPrimitive() && value == null;
	}

	/**
	 * Redefinicion para tratamiento de nulos.
	 * 
	 * @see jface.gui.gui.model.AttributeValueModel#onParentValueChange(ValueChangeEvent)
	 */
	@Override
	protected void onParentValueChange(ValueChangeEvent ev) {
		m_isNull = false;
		super.onParentValueChange(ev);
	}

	/**
	 * Redefinicion para tratamiento de nulos.
	 * 
	 * @see jface.gui.gui.model.ValueModel#getValue()
	 */
	@Override
	public Object getValue() {
		return m_isNull ? null : super.getValue();
	}

	private boolean m_isNull = false;
}
