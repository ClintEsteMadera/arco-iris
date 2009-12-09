/*
 * $Id: ClassUtils.java,v 1.8 2009/06/22 21:13:18 cvscalab Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author H. Adrián Uribe
 * @version $Revision: 1.8 $ $Date: 2009/06/22 21:13:18 $
 */
public final class ClassUtils {
	private ClassUtils() {
		super();
	}

	public static Class<?> getCurrentClass() {
		return helperSecurityManager.getCurrentClass();
	}

	/**
	 * @param aClass Clase parametrizada
	 * @return Clase utilizada para parametrizar
	 */
	public static Class<?> getParameterizedClass(Class<?> aClass) {
		Class<?> parameterizedClass = null;
		try {
			ParameterizedType parameterizedType = (ParameterizedType) aClass.getGenericSuperclass();
			parameterizedClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
		} catch (Throwable ex) {
			log.error("Class " + aClass + " no es una clase parametrizada");
		}
		return parameterizedClass;
	}

	/**
	 * @param aClass Clase parametrizada
	 * @return Clase utilizada para parametrizar (debe tener 2 o más parámetros de tipo)
	 */
	public static Class<?> get2ndParameterizedClass(Class<?> aClass) {
		Class<?> parameterizedClass = null;
		try {
			ParameterizedType parameterizedType = (ParameterizedType) aClass.getGenericSuperclass();
			parameterizedClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];
		} catch (Throwable ex) {
			log.error("Class " + aClass
			        + " no es una clase parametrizada o no tiene 2 o más parámetros de tipo");
		}
		return parameterizedClass;
	}

	/**
	 * Setea un valor dado en un atributo de una instancia
	 * @param instance Instancia a la cual se le seteará el valor
	 * @param labelKey Nombre del atributo de la instancia que será seteado
	 * @param value Valor a setear
	 * @return true si y solo si el valor ha sido seteado correctamente
	 */
	public static boolean setValueByReflection(Object instance, String labelKey, Object value) {
		Boolean result = false;
		if (instance != null) {
			result = true;
			Field field = null;
			try {
				String[] fields = labelKey.split(Pattern.quote("."));
				for (int i = 0; i < fields.length; i++) {
					field = getField(instance.getClass(), fields[i]);
					if (i != fields.length - 1) {
						// si es el ultimo atributo ya es el atributo a setear
						instance = field.get(instance);
					}
				}
				if (field != null) {
					field.set(instance, value);
				}
			} catch (Exception exc) {
				result = false;
				log.error(instance.getClass().getName() + "." + labelKey, exc);
			}
		}
		return result;
	}

	public static Field[] getFields(Class<?> clazz) {
		Field[] fields = classFields.get(clazz);
		if (fields == null) {
			List<Field> fieldsList = doGetFields(clazz);
			int size = fieldsList.size();
			fields = new Field[size];
			fieldsList.toArray(fields);
			classFields.put(clazz, fields);
		}
		return fields;
	}

	private static Field getField(Class<?> clazz, String fieldName) {
		Field result = null;
		for (Field field : getFields(clazz)) {
			if (fieldName.equals(field.getName())) {
				result = field;
				break;
			}
		}
		return result;
	}

	private static List<Field> doGetFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<Field>();
		Field[] declaredFields;
		Field declaredField;
		int mods;
		do {
			declaredFields = clazz.getDeclaredFields();
			for (Field element : declaredFields) {
				declaredField = element;
				mods = declaredField.getModifiers();
				if ((mods & Modifier.STATIC) == 0 && (mods & Modifier.TRANSIENT) == 0
				        && !declaredField.isSynthetic()
				        && !declaredField.getType().getSimpleName().equals("PK")) {
					fields.add(declaredField);
					declaredField.setAccessible(true);
				}
			}
			clazz = clazz.getSuperclass();
		} while (clazz != Object.class);
		return fields;
	}

	private static final class HelperSecurityManager extends SecurityManager {
		public Class<?> getCurrentClass() {
			return getClassContext()[2];
		}
	};

	private static final HelperSecurityManager helperSecurityManager = new HelperSecurityManager();
	private static final Map<Class<?>,Field[]> classFields = new HashMap<Class<?>,Field[]>();
	private static final Log log = LogFactory.getLog(ClassUtils.getCurrentClass());
}
