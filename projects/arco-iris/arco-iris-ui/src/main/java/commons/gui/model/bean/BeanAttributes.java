package commons.gui.model.bean;

import commons.datastructures.LazyMap;
import commons.gui.model.AbstractAttribute;
import commons.gui.model.Attribute;
import commons.gui.model.Attributes;

/**
 * Conjunto de atributos asociados a un bean. Utiliza reflection para acceder a las propiedades de una clase.
 */
public class BeanAttributes implements Attributes<String> {

	/**
	 * Construye los atributos asociados a la clase.
	 * 
	 * @param clazz
	 *            Clase de los objetos a los cuales se van a aplicar los atributos.
	 */
	public BeanAttributes(Class clazz) {
		m_beanClass = clazz;
	}

	public Attribute getAttribute(String key) {
		return m_attributes.get(key);
	}

	/**
	 * Configura el "tipo de dato" asociada a una propiedad. A través del tipo de dato se puede acceder a la
	 * meta-información asociada mediante {@link sba.ui.model.types.EditConfigurationManager}
	 * 
	 * @param key
	 *            Identificador de la propiedad
	 * @param cfg
	 *            Identificador del tipo de dato.
	 */
	public void setEditConfiguration(String key, String cfg) {
		Attribute attr = getAttribute(key);
		if (attr != null && attr instanceof AbstractAttribute) {
			((AbstractAttribute) attr).setEditConfiguration(cfg);
		}
	}

	/**
	 * Agrega un atributo adicional. Mediante este mecanismo es posible agregar nuevos atributos a una clase (por
	 * ejemplo: un valor "calculado") o redefinir el comportamiento de un atributo.
	 * 
	 * @param id
	 *            Identificador del nuevo atributo
	 * @param attribute
	 *            Atributo.
	 */
	public void addAttribute(String id, Attribute attribute) {
		m_attributes.put(id, attribute);
	}

	public Object getPrototype() {
		return m_prototype;
	}

	void setPrototype(Object prototype) {
		this.m_prototype = prototype;
	}

	private Attribute createAttribute(String key) {
		// con el prefijo "/" se indica la clase de un atributo "artificial"
		if (key.startsWith("/")) {
			final String className = key.substring(1);
			try {
				final Class attributeClass = this.getClass().getClassLoader().loadClass(className);
				Object o = null;

				try {
					o = attributeClass.newInstance();
				} catch (Exception e) {
					throw new IllegalArgumentException("Error instanciando la clase '" + className + "' :"
							+ e.getMessage(), e);
				}

				if (!(o instanceof Attribute)) {
					throw new IllegalArgumentException("La clase '" + className + "' no implementa "
							+ Attribute.class.getName());
				}
				return (Attribute) o;
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("No se pudo la clase de atributo '" + className + "'");
			}

		} else {
			return new BeanAttribute(m_beanClass, key.toString(), m_prototype);
		}
	}

	private LazyMap<String, Attribute> m_attributes = new LazyMap<String, Attribute>() {
		@Override
		public Attribute createElement(String key) {
			return createAttribute(key);
		}
	};

	private Class m_beanClass;

	private Object m_prototype;
}
