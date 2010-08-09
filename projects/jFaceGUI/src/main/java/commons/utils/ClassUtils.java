/*
 * $Id: ClassUtils.java,v 1.2 2008/04/01 16:42:00 cvschioc Exp $
 */
package commons.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author H. Adrián Uribe
 */
public abstract class ClassUtils {
	public static Field[] getFields(Class clazz) {
		Field[] fields = fieldsCache.get(clazz);
		if (fields == null) {
			List<Field> fieldsList = doGetFields(clazz);
			int size = fieldsList.size();
			fields = new Field[size];
			fieldsList.toArray(fields);
			fieldsCache.put(clazz, fields);
		}
		return fields;
	}

	public static Field getField(Class clazz, String fieldName) {
		Field result = null;
		for (Field field : getFields(clazz)) {
			if (fieldName.equals(field.getName())) {
				result = field;
				break;
			}
		}
		return result;
	}

	public static Class getFieldType(Class clazz, String fieldName) {
		return getField(clazz, fieldName).getType();
	}

	public static <T> Object invokeMethod(T instance, String methodName, Object[] args) {
		Object result;
		try {
			Method method = getMethod(instance.getClass(), methodName);
			method.setAccessible(true);
			result = method.invoke(instance, args);
		} catch (Exception e) {
			log.error("No se pudo invocar el metodo solicitado" + e.getMessage());
			result = null;
		}

		return result;
	}

	/**
	 * Retorna el método indicado por los parámetros.
	 * 
	 * @param clazz
	 *            la clase a la cual pedirle el método.
	 * @param methodName
	 *            el nombre del método a obtener.
	 * @return una instancia de la clase <code>Method</code> que representa al método que se quiere obtener, ó
	 *         <code>null</code> en caso que el método no exista o no pueda ser accedido.
	 */
	public static Method getMethod(Class clazz, String methodName) {
		Method result = null;
		result = getPublicMethod(clazz, methodName);
		if (result == null) {
			result = getPrivateMethod(clazz, methodName);
		}

		if (result == null) {
			log.error("No se pudo hallar el metodo solicitado");
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private static Method getPublicMethod(Class clazz, String methodName) {
		Method result = null;
		try {
			result = clazz.getMethod(methodName);
		} catch (Exception e) {
			log.error("getPublicMethod(" + clazz.getName() + ", " + methodName + ") falló");
		}
		return result;
	}

	private static Method getPrivateMethod(Class clazz, String methodName) {
		Method result = null;
		try {
			Method[] methods = clazz.getDeclaredMethods();
			boolean found = false;
			for (int i = 0; !found && i < methods.length; i++) {
				// toma la primera aparición del método
				found = methodName.equals(methods[i].getName());
				if (found) {
					result = methods[i];
				}
			}
		} catch (Exception e) {
			log.error("getPrivateMethod(" + clazz.getName() + ", " + methodName + ") falló");
		}
		return result;
	}

	public static Class getTypeParameterClass(Class clazz) {
		return getTypeParameterClass(clazz, (byte) 0);
	}

	public static Class getTypeParameterClass(Class clazz, byte index) {
		return clazz.getTypeParameters()[index].getClass();
	}

	public static Object newTypeParameterInstance(Field field) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		String className = field.getGenericType().toString();
		int index1 = className.indexOf('<');
		int index2 = className.indexOf('>');
		className = className.substring(index1 + 1, index2);
		return Class.forName(className).newInstance();
	}

	private static List<Field> doGetFields(Class clazz) {
		List<Field> fields = new ArrayList<Field>();
		Field[] declaredFields;
		Field declaredField;
		int mods;
		do {
			declaredFields = clazz.getDeclaredFields();
			for (int i = 0; i < declaredFields.length; i++) {
				declaredField = declaredFields[i];
				mods = declaredField.getModifiers();
				if ((mods & Modifier.STATIC) == 0 && (mods & Modifier.TRANSIENT) == 0 && !declaredField.isSynthetic()
						&& !declaredField.getType().getSimpleName().equals("PK")) {
					fields.add(declaredField);
					declaredField.setAccessible(true);
				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != Object.class);
		return fields;
	}

	public static String getValue(Object object, String chainOfProperties) {
		String value = "";
		Object currentObject = getObject(object, chainOfProperties);
		if (currentObject instanceof Calendar) {
			value = DateUtils.formatCalendar(((Calendar) currentObject));
		} else if (currentObject instanceof Date) {
			value = DateUtils.formatDate(((Date) currentObject));
		} else if (currentObject != null) {
			value = currentObject.toString();
		}
		return value;
	}

	/**
	 * Provee el objecto producto de haber navegado por la cadena de propiedades especificada por parámetro.
	 * 
	 * @param model
	 *            objeto a partir del cual comenzar la navegación de propiedades.
	 * @param chainOfProperties
	 *            string con las propiedades separadas por puntos.
	 * @return el valor producto de haber navegado por la cadena de propiedades especificada por parámetro.
	 */
	public static Object getObject(Object model, String chainOfProperties) {
		if (model == null || chainOfProperties == null) {
			return null;
		}
		Object currentObject = model;
		String[] properties = chainOfProperties.split(Pattern.quote("."));
		Method method;
		for (int i = 0; i < properties.length; i++) {
			try {
				if (properties[i].endsWith("()")) {
					// es un metodo
					String methodName = properties[i].substring(0, properties[i].length() - 2);
					method = ClassUtils.getPublicMethod(currentObject.getClass(), methodName);
					currentObject = method.invoke(currentObject, new Object[] {});
				} else {
					// es un atributo
					Field field = ClassUtils.getField(currentObject.getClass(), properties[i]);
					if (field == null) {
						// intento buscarlo como Java Bean...
						method = getReadMethod(currentObject, properties[i]);
						currentObject = method.invoke(currentObject, new Object[] {});
					} else {
						currentObject = field.get(currentObject);
					}
				}
			} catch (Exception e) {
				log.error("Error al recuperar " + chainOfProperties + " de la clase " + model.getClass().getName());
			}
		}
		return currentObject;
	}

	private static Method getReadMethod(Object target, String property) {
		PropertyDescriptor pDesc = getPropertyDescriptor(target.getClass(), property);

		if (pDesc == null) {
			throw new IllegalArgumentException("La propiedad '" + property + "' no esta definida para la clase "
					+ target.getClass().getName());
		}

		return pDesc.getReadMethod();
	}

	private static PropertyDescriptor getPropertyDescriptor(Class clazz, String propName) {
		BeanInfo info = null;

		try {
			info = Introspector.getBeanInfo(clazz);
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("No se pudo obtener informacion para la clase " + clazz.getName());
		}

		PropertyDescriptor[] props = info.getPropertyDescriptors();
		for (int i = 0; i < props.length; i++) {
			if (props[i].getName().equals(propName)) {
				return props[i];
			}
		}

		return null;
	}

	/**
	 * Setea un valor dado en un atributo de una instancia
	 * 
	 * @param model
	 *            Instancia a la cual se le seteará el valor
	 * @param chainOfProps
	 *            Nombre del atributo de la instancia que será seteado
	 * @param value
	 *            Valor a setear
	 * @return true si y solo si el valor ha sido seteado correctamente
	 */
	public static boolean setValueByReflection(Object instance, String chainOfProps, Object value) {
		Boolean result = false;
		if (instance != null) {
			result = true;
			Field field = null;
			try {
				String[] fields = chainOfProps.split(Pattern.quote("."));
				for (int i = 0; i < fields.length; i++) {
					field = getField(instance.getClass(), fields[i]);
					if (i != fields.length - 1) {
						// si es el ultimo atributo ya es el atributo a setear
						instance = field.get(instance);
					}
				}
				if (field != null) {
					if (value == null) {
						if ((String.class).equals(field.getType())) {
							field.set(instance, "");
						} else {
							field.set(instance, null);
						}

					} else if ((String.class).equals(field.getType())) {
						field.set(instance, value.toString().trim());

					} else if (field.getType() == Integer.class) {
						String text = value.toString().trim();
						Integer value2Set = (text.length() == 0) ? null : Integer.valueOf(text);
						field.set(instance, value2Set);

					} else if (field.getType() == Long.class) {
						String text = value.toString().trim();
						Long value2Set = (text.length() == 0) ? null : Long.valueOf(text);
						field.set(instance, value2Set);

					} else if (field.getType() == Boolean.TYPE) {
						field.set(instance, value);

					} else if (field.getType().equals(BigDecimal.class)) {
						String str = value.toString().trim().replace(',', '.');
						BigDecimal number;
						try {
							number = new BigDecimal(str);
						} catch (NumberFormatException exc) {
							number = BigDecimal.ZERO;
						}
						field.set(instance, number);

					} else if (field.getType().isEnum()) {
						if (value.equals(EMPTY_STRING)) {
							value = null;
						}
						field.setAccessible(true);
						field.set(instance, value);
					} else {
						if (value.equals(EMPTY_STRING)) {
							// cubre el caso en que el campo sea una interface y el valor un enum
							value = null;
						}
						field.setAccessible(true);
						field.set(instance, value);
					}
				}
			} catch (NumberFormatException numFormatExcept) {
				result = false;
				String string = value != null && value.toString() != null ? value.toString() : "";
				log.error("No se pudo formatear a numero el siguiente String: " + string, numFormatExcept);
			} catch (Exception exc) {
				result = false;
				log.error(instance.getClass().getName() + "." + chainOfProps, exc);
			}
		}
		return result;
	}

	/**
	 * Retorna el valor establecido en el atributo denotado por <code>propertyName</code> en el objeto receptor
	 * denotado por <code>model</code>.
	 * 
	 * @param model
	 *            el objeto que contiene la propertyName (o aquél a partir del cuál debe comenzar la búsqueda de campos,
	 *            en el caso que <code>propertyName</code> sea una cadena de propiedades)
	 * @param propertyName
	 *            puede ser una cadena de propiedades separada por puntos.
	 */
	public static String obtenerValor(Object model, String propertyName) {
		String result = "";
		if (model != null && propertyName != null) {
			try {
				Field field = null;
				String[] fields = propertyName.split(Pattern.quote("."));
				for (int i = 0; i < fields.length; i++) {
					field = getField(model.getClass(), fields[i]);
					if (i != fields.length - 1) {
						// si es el ultimo atributo ya es el atributo a setear
						model = field.get(model);
					}
				}

				if (field != null && field.get(model) != null) {
					if (field.getType() == BigDecimal.class) {
						result = ((BigDecimal) field.get(model)).toPlainString();
					} else if (field.getType() == Integer.class) {
						result = ((Integer) field.get(model)).toString();
					} else if (field.getType() == Long.class) {
						result = ((Long) field.get(model)).toString();
					} else if (field.getType() == Date.class) {
						Date date = (Date) field.get(model);
						result = DateUtils.formatDate(date);
					} else if (field.getType() == Calendar.class) {
						Calendar calendar = (Calendar) field.get(model);
						result = DateUtils.formatCalendar(calendar);
					} else {
						result = field.get(model).toString();
					}
				}
			} catch (Exception exc) {
				log.error(null, exc);
			}
		}
		return result;
	}

	public static <T> T newInstance(Class<T> aClass) {
		if (aClass == null) {
			throw new IllegalArgumentException();
		}
		T result = null;
		Class[] empty = {};
		Constructor<T> constructor;
		try {
			constructor = aClass.getDeclaredConstructor(empty);
			constructor.setAccessible(true);
			result = constructor.newInstance((Object[]) null);
		} catch (Exception e) {
			log.error("No se pudo instanciar la clase " + aClass.getCanonicalName());
		}
		return result;
	}

	private static final Map<Class, Field[]> fieldsCache = new HashMap<Class, Field[]>();

	private static final Log log = LogFactory.getLog(ClassUtils.class);

	public static final String EMPTY_STRING = "";
}