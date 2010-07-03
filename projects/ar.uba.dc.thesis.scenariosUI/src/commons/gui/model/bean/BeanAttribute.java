package commons.gui.model.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.StringTokenizer;


import commons.annotations.DataTypeAnnotation;
import commons.gui.model.AbstractAttribute;

/**
 * Atributo que toma los valores de una propiedad de un bean. <br>
 * <br>
 * Mediante reflection, esta clase invoca los getter y el setter de una
 * propiedad para extraer valores de un objeto. Además soporta: <br>
 * <ul>
 * <li> Propiedades anidadas: <br>
 * Es posible expresar una propiedad "anidada" (por ejemplo: la propiedad
 * "calle.numero" utilizará los métodos <code>getCalle().getNumero()</code> y
 * <code>getCalle().setNumero()</code> </li>
 * <li> Conversiones de tipos: <br>
 * Para tener una mayor flexibilidad, se utilizan las conversiones de tipos
 * definidas en {@link commons.gui.model.bean.TypeConversionManager} </li>
 * </ul>
 * 
 * @author P.Pastorino
 */
public class BeanAttribute extends AbstractAttribute {

	/**
	 * Construye el atributo.
	 * 
	 * @param clazz
	 *            Clase de los objetos sobre los cuales se va a aplicar el
	 *            atributo.
	 * @param propertyName
	 *            Nombre de la propiedad asociada.
	 */
	public BeanAttribute(Class clazz, String propertyName, Object target) {
		StringTokenizer tokenizer = new StringTokenizer(propertyName, ".");
		int count = tokenizer.countTokens();
		m_readMethods = new Method[count];
		m_propertyPath = new String[count];

		PropertyDescriptor propertyDesc = null;

		m_beanClass = target != null ? target.getClass() : clazz;

		for (int i = 0; i < count; i++) {
			final String name = tokenizer.nextToken();

			m_propertyPath[i] = name;

			if (clazz != null) {
				propertyDesc = target != null ? getTargetProperty(target, name) : getProperty(
						clazz, name);
			}

			if (propertyDesc == null) {
				clazz = null;
			} else {
				m_readMethods[i] = propertyDesc.getReadMethod();
				clazz = propertyDesc.getPropertyType();

				if (target != null && m_readMethods[i] != null) {
					try {
						target = m_readMethods[i].invoke(target, new Object[] {});
					} catch (Exception unused) {
						target = null;
						System.err.println("Error obteniendo prototipo para propiedad "
								+ propertyName);
					}
				}
			}
		}

		if (propertyDesc != null) {
			m_writeMethod = propertyDesc.getWriteMethod();
			m_conversion = TypeConversionManager.getConversionInfo(propertyDesc.getPropertyType());
			m_attributeClass = m_conversion != null ? m_conversion.getWrapperClass() : propertyDesc
					.getPropertyType();
		}

		m_propertyName = propertyName;

		// tomo la anotacion del tipo de dato del getter o setter
		if (count > 0) {
			DataTypeAnnotation ann = getDataTypeAnnotation(m_readMethods[count - 1]);

			if (ann == null) {
				ann = getDataTypeAnnotation(m_writeMethod);
			}
			if (ann != null) {
				this.setEditConfiguration(ann.typeId());
			}
		}
	}

	
//	private Object getSetterParameter(Object target, Object value){
//		if (m_conversion != null) {
//			if (value == null) {
//				return m_conversion.getNullValue();
//			}
//			// revert
//			if (m_conversion.getConversor() != null) {
//				return m_conversion.getConversor().convertFrom(value);
//			}
//		}
//		return value;
//	}
	
	/**
	 * Setea el valor de la propiedad en el objeto.
	 * 
	 * @param target
	 *            Objeto cuya propiedad se quiere modificar.
	 * @param value
	 *            Valor que se quiere asignar. Si el tipo de dato de la
	 *            propiedad tiene un conversor asociado a través de
	 *            {@link commons.gui.model.bean.TypeConversionManager}, se
	 *            invoca
	 *            {@link sba.ui.util.ValueConversor#convertFrom(java.lang.Object)}
	 *            con el parámetro <code>value</code> y se asigna el valor
	 *            retornado a la propiedad del bean. <br>
	 *            <br>
	 *            En caso de tartarse de una propiedad de tipo primitivo, se
	 *            hace un tratamiento especial del <code>null</code>
	 *            convirtiendolo al valor por default según el tipo (<ode>0</code>
	 *            para <code>int</code>,<code>false</false> para
	 *            <code>boolean</code>, etc)
	 * @see commons.gui.model.Attribute#setValue(Object, Object)
	 */
	public void setValue(Object target, Object value) {

		target = getValue(target, m_readMethods.length - 1);

		if (m_writeMethod == null && target != null) {
			m_writeMethod = getWriteMethod(target, m_propertyPath[m_readMethods.length - 1]);
		}

		if (m_writeMethod == null) {
			throw new IllegalArgumentException("Clase " + m_beanClass.getName() + ": "
					+ "La propiedad " + m_propertyName
					+ " no tiene un setter definido para la clase " + getAttributeClass().getName());
		}

		if (m_conversion != null) {
			if (value == null) {
				value = m_conversion.getNullValue();
			}
			// revert
			if (m_conversion.getConversor() != null) {
				value = m_conversion.getConversor().convertFrom(value);
			}
		}

		try {
			m_writeMethod.invoke(target, new Object[] { value });
		} catch (Throwable t) {
			throw new IllegalArgumentException("Clase " + m_beanClass.getName() + ": "
					+ "Error al setear la propiedad " + m_propertyName + " :" + t.getMessage());
		}
	}

	/**
	 * Obtiene el valor de la propiedad para el objeto. Si el tipo de dato de la
	 * propiedad tiene un conversor asociado a través de
	 * {@link commons.gui.model.bean.TypeConversionManager}, se invoca
	 * {@link sba.ui.util.ValueConversor#convertTo(java.lang.Object)} con el valor
	 * de la propiedad y se retorna el valor obtenido.
	 * 
	 * @param target
	 *            Objeto cuya propiedad se quiere obtener.
	 * @return Valor de la propiedad para el objeto.
	 * @see commons.gui.model.Attribute#getValue(Object)
	 */
	public Object getValue(Object target) {
		final Object ret = getValue(target, m_readMethods.length);

		// convert
		if (m_conversion != null && m_conversion.getConversor() != null) {
			return m_conversion.getConversor().convertTo(ret);
		}
		return ret;
	}

	/**
	 * Obtiene el nombre de la propiedad asociada.
	 * 
	 * @return
	 */
	public String getPropertyName() {
		return m_propertyName;
	}

	/**
	 * Informa si se trata de una propiedad de tipo primitivo
	 * (int,long,double,float,bool)
	 * 
	 * @return
	 */
	public boolean isPrimitive() {
		return m_conversion != null && m_conversion.m_isPrimitive;
	}

	/**
	 * @see commons.gui.model.AbstractAttribute#getAttributeClass()
	 */
	@Override
	public Class getAttributeClass() {
		return m_attributeClass != null ? m_attributeClass : Object.class;
	}

	private static PropertyDescriptor getProperty(Class clazz, String propName) {
		BeanInfo info = null;

		try {
			info = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("No se pudo obtener informacion para la clase "
					+ clazz.getName());
		}

		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getName().equals(propName)) {
				return props[i];
			}
		}

		return null;
	}

	private Object getValue(Object target, int pathLen) {
		Object ret = target;

		try {
			for (int i = 0; ret != null && i < pathLen; i++) {

				/*
				 * me fijo si puedo obtener el getter (en caso de que sea un
				 * metodo definido en una subclase)
				 */
				if (m_readMethods[i] == null) {
					m_readMethods[i] = getReadMethod(ret, m_propertyPath[i]);
				}

				if (m_readMethods[i] == null) {
					String errorMsg = "Clase " + m_beanClass.getName() + ": " + m_propertyName
							+ " no tiene getter definido para la clase "
							+ m_attributeClass.getName();
					throw new IllegalArgumentException(errorMsg);
				}
				try {
					ret = m_readMethods[i].invoke(ret, new Object[] {});
				} catch (Exception e) {
					// puede pasar que el método esté desactualizado, lo refresco...
					m_readMethods[i] = ret.getClass().getMethod(m_readMethods[i].getName(),
							new Class[] {});
					ret = m_readMethods[i].invoke(ret, new Object[] {});
				}
			}
		} catch (Throwable t) {
			IllegalArgumentException e = new IllegalArgumentException("Clase "
					+ m_beanClass.getName() + ": " + "Error al obtener la propiedad "
					+ m_propertyName + " para el objeto "
					+ (target == null ? "null" : target.getClass().getName()) + ": "
					+ t.getMessage());
			e.initCause(t);
			throw e;
		}
		return ret;
	}

	private PropertyDescriptor getTargetProperty(Object target, String property) {
		return getProperty(target.getClass(), property);
	}

	private Method getReadMethod(Object target, String property) {
		PropertyDescriptor pDesc = getTargetProperty(target, property);

		if (pDesc == null) {
			throw new IllegalArgumentException("La propiedad '" + property
					+ "' no esta definida para la clase " + target.getClass().getName());
		}

		return pDesc.getReadMethod();
	}

	private Method getWriteMethod(Object target, String property) {
		PropertyDescriptor pDesc = getTargetProperty(target, property);

		if (pDesc == null) {
			throw new IllegalArgumentException("La propiedad '" + property
					+ "' no esta definida para la clase " + target.getClass().getName());
		}

		return pDesc.getWriteMethod();
	}

	@SuppressWarnings("unchecked")
	private DataTypeAnnotation getDataTypeAnnotation(Method m) {
		if (m == null) {
			return null;
		}
		DataTypeAnnotation ann = m.getAnnotation(DataTypeAnnotation.class);

		if (ann != null) {
			return ann;
		}

		// busco el metodo en las interfaces
		for (Class interf : m.getDeclaringClass().getInterfaces()) {
			try {
				m = interf.getMethod(m.getName(),m.getParameterTypes());
				return getDataTypeAnnotation(m);
			} catch (Exception ignored) {
			}
		}
		return null;
	}

	private Class m_beanClass;

	private Class m_attributeClass;

	private Method[] m_readMethods;

	private Method m_writeMethod;

	private String m_propertyName;

	private String[] m_propertyPath;

	private TypeConversionInfo m_conversion;
};
