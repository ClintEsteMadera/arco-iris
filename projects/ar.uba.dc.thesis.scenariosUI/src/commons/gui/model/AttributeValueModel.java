package commons.gui.model;

import commons.gui.model.types.EditType;

/**
 * Modelo asociado a una propiedad o atributo de un objeto.
 * 
 * <br>
 * <br>
 * Este modelo se utiliza como un modelo "hijo" dentro de un modelo compuesto.
 * 
 * @see commons.gui.model.AttributesModel
 */
public class AttributeValueModel extends AbstractValueModel<Object> {

	/**
	 * Construye el modelo.
	 * 
	 * @param attribute
	 *            Atributo
	 * @param parent
	 *            Modelo "padre"
	 * @param id
	 *            Identificador del atributo.
	 */
	public AttributeValueModel(Attribute attribute, AttributesModel parent, Object id) {
		m_attribute = attribute;
		install(parent, id);
	}

	/**
	 * @see commons.gui.model.ValueModel#getValue()
	 */
	public Object getValue() {
		final Object target = getTarget();
		if (target == null) {
			return null;
		}
		return m_attribute.getValue(target);
	}

	/**
	 * @see commons.gui.model.ValueModel#setValue(Object)
	 */
	public final void setValue(Object value) throws IllegalStateException {
		doSetValue(value, false);
	}

	/**
	 * @see commons.gui.model.ValueModel#setValue(Object)
	 */
	private final void doSetValue(Object value, boolean canceling) throws IllegalStateException {
		beforeSetValue(value);

		Object oldValue = getValue();
		setAttributeValue(value);

		afterSetValue(value);

		InvalidValueException invalidValue = null;

		try {
			m_firingParentChange = true;

			try {
				m_parentModel.firePropertyChange(m_id.getPropertyName(), oldValue, value, canceling);
			} catch (InvalidValueException e) {
				invalidValue = e;
			}
		} finally {
			m_firingParentChange = false;
		}

		try {
			this.fireValueChange(oldValue, value, canceling);
		} catch (InvalidValueException e) {
			invalidValue = e;
		}

		if (!canceling && invalidValue != null) {
			doSetValue(oldValue, true);
		}
	}

	/**
	 * Callback que se invoca antes de setear el valor
	 * 
	 * @param value
	 */
	protected void beforeSetValue(Object value) {
	}

	/**
	 * Callback que se invoca despues de setear el valor (y antes de generar los eventos)
	 * 
	 * @param value
	 */
	protected void afterSetValue(Object value) {
	}

	/**
	 * @see commons.gui.model.ValueModel#getValueType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public EditType getValueType() {
		return m_attribute.getValueType();
	}

	/**
	 * Obtiene el nombre de la propiedad asociada
	 * 
	 * @return
	 */
	public String getPropertyName() {
		return m_id.getPropertyName().toString();
	}

	/**
	 * Obtiene el atributo asociado.
	 * 
	 * @return
	 */
	public Attribute getAttribute() {
		return m_attribute;
	}

	private Object getTarget() {
		return m_parentModel.getValue();
	}

	/**
	 * Procesa el evento de modificación en el modelo "padre".
	 * 
	 * @param ev
	 */
	protected void onParentValueChange(ValueChangeEvent ev) {
		if (!hasListeners()) {
			return;
		}

		try {
			this.m_firingParentChange = true;

			if (ev.getOldValue() == null && ev.getNewValue() == null) {
				AttributeValueModel.this.fireValueChange();
			} else {
				Object oldValue = null;
				Object newValue = null;

				if (ev.getOldValue() != null) {
					oldValue = m_attribute.getValue(ev.getOldValue());
				}
				if (ev.getNewValue() != null) {
					newValue = m_attribute.getValue(ev.getNewValue());
				}
				AttributeValueModel.this.fireValueChange(oldValue, newValue);
			}

		} finally {
			this.m_firingParentChange = false;
		}
	}

	/**
	 * Para el cambio en una propiedad del padre (se utiliza para manejar modelos anidados)
	 * 
	 * @param ev
	 */
	private void onParentPropertyChange(ComplexValueChangeEvent ev) {

		if (!hasListeners() || this.m_firingParentChange) {
			return;
		}

		// si cambio una propiedad que incluye a esta...
		if (m_id.isContainedIn(ev.getKey())) {
			try {
				this.m_firingParentChange = true;

				AttributeValueModel.this.fireValueChange();
			} finally {
				this.m_firingParentChange = false;
			}
		}
	}

	private void install(AttributesModel parent, Object id) {
		m_parentModel = parent;
		m_id = new PropertyName(id);

		final ValueChangeListener parentListener = new ValueChangeListener() {
			public void valueChange(ValueChangeEvent ev) {
				onParentValueChange(ev);
			}
		};

		final ComplexValueChangeListener parentPropertyListener = new ComplexValueChangeListener() {
			public void complexValueChange(ComplexValueChangeEvent ev) {
				onParentPropertyChange(ev);
			}
		};

		m_parentModel.addValueChangeListener(parentListener);
		m_parentModel.addComplexValueChangeListener(parentPropertyListener);
	}

	/**
	 * Setea el valor del atributo.
	 * 
	 * @param value
	 * @throws IllegalStateException
	 *             En caso de que el modelo padre no tenga ningún valor.
	 */
	protected final void setAttributeValue(Object value) throws IllegalStateException {
		final Object target = getTarget();
		if (target == null) {
			throw new IllegalStateException("Valor nulo: no puede accederse la propiedad");
		}
		m_attribute.setValue(target, value);
	}

	private AttributesModel m_parentModel;
	private Attribute m_attribute;
	private PropertyName m_id;

	private boolean m_firingParentChange;
};
