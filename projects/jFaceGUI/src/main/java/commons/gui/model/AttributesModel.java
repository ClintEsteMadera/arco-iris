package commons.gui.model;

import commons.dataestructures.LazyMap;
import commons.gui.model.types.EditType;

/**
 * Modelo complejo basado en atributos. <br>
 * <br>
 * Este modelo contiene un valor "raíz" y valores "hijos" correspondientes a los atributos o propiedades del elemento
 * "raíz".
 * 
 * @author P.Pastorino
 */
@SuppressWarnings("unchecked")
public class AttributesModel<T> extends ValueHolder<T> implements CompositeModel<T>, Attributes {

	/**
	 * Construye el modelo
	 * 
	 * @param valueClass
	 *            Clase del valor "raíz"
	 * @param attributes
	 *            Atributos del valor "raíz"
	 */
	public AttributesModel(Class<T> valueClass, Attributes attributes) {
		this(new EditType<T>(valueClass), attributes);
	}

	/**
	 * Construye el modelo
	 * 
	 * @param editType
	 *            Tipo del valor "raíz"
	 * @param attributes
	 *            Atributos del valor "raíz"
	 */
	public AttributesModel(EditType editType, Attributes attributes) {
		super(editType);
		m_attributes = attributes;
	}

	/**
	 * Agrega un atributo Permite extender el modelo agregando nuevos atributos.
	 * 
	 * @param id
	 *            Identificador del atributo
	 * @param attribute
	 *            Atributo
	 */
	public void addAttribute(String id, Attribute attribute) {
		m_valueModels.put(id, createValueModel(attribute, id));
	}

	/**
	 * Agrega un valor. Permite extender el modelo por composición agregando un nuevo valor.
	 * 
	 * @param id
	 * @param valueModel
	 */
	public void addValueModel(String id, ValueModel valueModel) {
		m_valueModels.put(id, valueModel);
	}

	/**
	 * @see jface.gui.gui.model.Attributes#getAttribute(Object)
	 */
	@SuppressWarnings("unchecked")
	public Attribute getAttribute(Object id) {
		return m_attributes.getAttribute(id);
	}

	/**
	 * @see jface.gui.gui.model.ComplexModel#getValue(Object)
	 */
	public Object getValue(Object key) {
		return getValueModel(key).getValue();
	}

	/**
	 * @see jface.gui.gui.model.ComplexModel#getValueModel(Object)
	 */
	public ValueModel getValueModel(Object key) {
		if (key == null || key.equals("")) {
			return this;
		}
		final ValueModel model = m_valueModels.get(key);
		if (model == null) {
			throw new IllegalArgumentException("Atributo invalido: " + key);
		}
		return model;
	}

	/**
	 * @see jface.gui.gui.model.ComplexModel#getValueType(Object)
	 */
	public EditType getValueType(Object key) {
		return getValueModel(key).getValueType();
	}

	/**
	 * @see jface.gui.gui.model.ComplexModel#setValue(Object, Object)
	 */
	@SuppressWarnings("unchecked")
	public void setValue(Object key, Object value) {
		checkValid();
		ValueModel vH = getValueModel(key);
		if (vH != null) {
			vH.setValue(value);
		}
	}

	/**
	 * Configura el tipo de dato de un atributo.
	 * 
	 * @param id
	 *            Identificador del atributo
	 * @param cfg
	 *            Identificador del tipo de dato
	 */
	public void setEditConfiguration(Object id, String cfg) {
		final Attribute attr = getAttribute(id);
		if (attr != null && attr instanceof AbstractAttribute) {
			((AbstractAttribute) attr).setEditConfiguration(cfg);
		}
	}

	/**
	 * @see jface.gui.gui.model.ComplexModel#addComplexValueChangeListener(ComplexValueChangeListener)
	 */
	public void addComplexValueChangeListener(ComplexValueChangeListener listener) {
		m_propertyChange.addListener(listener);
	}

	/**
	 * @see jface.gui.gui.model.ComplexModel#removeComplexValueChangeListener(ComplexValueChangeListener)
	 */
	public void removeComplexValueChangeListener(ComplexValueChangeListener listener) {
		m_propertyChange.removeListener(listener);
	}

	/**
	 * Retorna un modelo "anidado" asociado a este modelo. Se puede utilizar cuando se desea extraer el modelo asociado
	 * a un objeto complejo incluido en este modelo (por ejemplo: el modelo asociado al 'Domicilio' contenido en el
	 * objeto 'Persona')
	 * 
	 * @param key
	 *            Clave que identifica la propiedad del modelo.
	 * @return
	 */
	public <NESTED_TYPE> CompositeModel<NESTED_TYPE> getNestedModel(String key, Class<NESTED_TYPE> clazz) {
		// TODO: Otra posibilidad sería inferir este tipo por reflection para no obligar a pasarlo
		// desde afuera programáticamente...
		return new NestedModel<NESTED_TYPE>(this, key);
	}

	/**
	 * Copia los valores de un objeto.
	 * 
	 * @param source
	 *            Objeto del cual se toman los atributos.
	 * @param attributes
	 *            Identificadores de los atributos que se copian.
	 */
	public void copy(Object source, Object[] attributes) {
		for (int i = 0; i < attributes.length; i++) {
			setValue(attributes[i], getAttribute(attributes[i]).getValue(source));
		}
	}

	/**
	 * Crea el {@link jface.gui.gui.model.ValueModel} asociado al atributo. Este factory method se encarga de crear los
	 * modelos "hijos" retornando <code>new AttributeValueModel(getAttribute(id), this, id);</new>.
	 * <br>
	 * Puede ser redefinido por una subclase.
	 * 	
	 * @param id Identificador del atributo.
	 * @return
	 */
	protected ValueModel createValueModel(Object id) {
		final Attribute attr = getAttribute(id);
		if (attr == null) {
			throw new IllegalArgumentException("Attributo invalido: " + id);
		}
		return createValueModel(attr, id);
	}

	/**
	 * Chequea que el modelo tenga un valor <code>!= null</code>
	 * 
	 * @throws IllegalStateException
	 */
	protected void checkValid() throws IllegalStateException {
		if (!isValid()) {
			throw new IllegalStateException();
		}
	}

	/**
	 * Informa si el modelo tiene un valor <code>!= null</code>
	 * 
	 * @return
	 */
	protected boolean isValid() {
		return getValue() != null;
	}

	/**
	 * Notifica un cambio en el modelo.
	 * 
	 * @param key
	 *            Identificador del atributo
	 * @param prevValue
	 *            Valor anterior
	 * @param newValue
	 *            Nuevo valor
	 */
	protected boolean firePropertyChange(Object key, Object prevValue, Object newValue, boolean canceling) {
		return m_propertyChange.fireChange(key, prevValue, newValue, canceling);
	}

	protected void firePropertyChange(Object key) {
		m_propertyChange.fireChange(key);
	}

	protected Attributes getAttributes() {
		return m_attributes;
	}

	private ValueModel createValueModel(Attribute attr, Object id) {
		return new AttributeValueModel(attr, this, id);
	}

	private Attributes m_attributes;

	private ComplexValueChangeSupport m_propertyChange = new ComplexValueChangeSupport(this);

	private LazyMap<Object, ValueModel> m_valueModels = new LazyMap<Object, ValueModel>() {
		@Override
		public ValueModel createElement(Object key) {
			return AttributesModel.this.createValueModel(key);
		}
	};
}