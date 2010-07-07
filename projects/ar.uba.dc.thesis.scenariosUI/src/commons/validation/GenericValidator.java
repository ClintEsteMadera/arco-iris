/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: GenericValidator.java,v 1.5 2008/05/14 17:55:03 cvspasto Exp $
 */

package commons.validation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import commons.annotations.AnnotationManager;
import commons.dataestructures.Pair;

/**
 * 
 * @author Pablo Pastorino
 * @version $Revision: 1.5 $ $Date: 2008/05/14 17:55:03 $
 */
public class GenericValidator implements Validator {

	public GenericValidator(Class clazz, ValidationContext context) {
		this.context = context;
		this.annotationCache = new ArrayList<ValidationInfo>();
		this.propertyCache = new ConcurrentHashMap<PropertyKey, PropertyInfo>();

		loadValidationInfo(clazz);
	}

	public boolean validate(Object objectToValidate, ValidationResult result) {
		final int c = result.getErrorCount();

		for (ValidationInfo vi : this.annotationCache) {
			this.validate(objectToValidate, vi, result);
		}

		return c == result.getErrorCount();
	}

	/**
	 * Valida que una propuedad sea no nula.
	 * 
	 * @param value
	 * @param property
	 * @param result
	 * @return
	 */
	protected boolean validateRequired(Object value, String property, ValidationResult result) {
		return validate(value, property, ValidateRequiredHandler.getInstance(), result);
	}

	/**
	 * Valida una propiedad cualquiera utilizando un handler.
	 * 
	 * @param value
	 * @param property
	 * @param handler
	 * @param result
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected boolean validate(Object value, String property, ValidationHandler handler, ValidationResult result) {
		final int c = result.getErrorCount();

		final Class propertyClass = value.getClass();
		PropertyInfo pi = this.getPropertyFromCache(propertyClass, property);

		if (pi == null) {
			pi = this.loadPropertyInfo(propertyClass, property);
			this.putPropertyInCache(propertyClass, property, pi);
		}

		Object target = value;

		for (Method m : pi.getMethods()) {
			if (target == null) {
				break;
			}
			target = invokeMethod(target, m);
		}

		result.pushNestedPath(property);
		for (String d : pi.getValueDescriptions()) {
			result.pushValueDescription(d);
		}
		context.pushValidationStack(value);

		try {
			handler.validate(target, null, result, context);
		} finally {
			result.popNestedPath();
			for (int i = 0; i < pi.getValueDescriptions().length; i++) {
				result.popValueDescription();
			}
			context.popValidationStack();
		}

		return c == result.getErrorCount();
	}

	@SuppressWarnings("unchecked")
	private void validate(Object value, ValidationInfo vi, ValidationResult result) {

		result.pushNestedPath(vi.getValueLocation());
		if (vi.getValueDescription() != null) {
			result.pushValueDescription(vi.getValueDescription());
		}

		context.pushValidationStack(value);

		try {
			final Object objectToValidate = invokeMethod(value, vi.getMethod());

			for (Pair<Annotation, ValidationHandler> p : vi.getAnnotations()) {
				p.getSecond().validate(objectToValidate, p.getFirst(), result, this.context);
			}

		} finally {
			result.popNestedPath();

			if (vi.getValueDescription() != null) {
				result.popValueDescription();
			}
			context.popValidationStack();
		}
	}

	private Object invokeMethod(Object target, Method method) {
		try {
			return method.invoke(target, new Object[] {});
		} catch (Exception e) {
			throw new RuntimeException("Error invocando el metodo " + method.getName() + " de la clase "
					+ method.getDeclaringClass().getName());
		}
	}

	private void loadValidationInfo(Class clazz) {
		this.annotationCache = new ArrayList<ValidationInfo>();

		PropertyDescriptor[] pds;

		try {
			pds = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
		} catch (IntrospectionException e) {
			throw new RuntimeException("Error obteniendno información para la clase " + clazz.getName() + " : "
					+ e.getMessage(), e);
		}

		for (PropertyDescriptor pd : pds) {
			ValidationInfo info = this.loadInfo(pd);

			if (info != null) {
				this.annotationCache.add(info);
			}
		}
	}

	private ValidationInfo loadInfo(PropertyDescriptor pd) {
		final Method method = pd.getReadMethod();

		if (method == null) {
			return null;
		}

		final Pair<Annotation, ValidationHandler>[] annotations = AnnotationManager.getInstance()
				.getValidationAnnotations(method);
		if ((annotations == null || annotations.length == 0)) {
			return null;
		}

		final String valueDescription = getValueDescription(pd);

		final String valueLocation = pd.getName();
		return new ValidationInfo(method, annotations, valueLocation, valueDescription);
	}

	private String getValueDescription(PropertyDescriptor pd) {
		String valueDescription = AnnotationManager.getInstance().getValueDescription(pd.getReadMethod());

		if (valueDescription != null) {
			valueDescription = this.context.getEnumPropertiesDirectory().getEnum(valueDescription).toString();
		}
		return valueDescription;
	}

	private PropertyInfo loadPropertyInfo(Class targetClass, String propertyName) {
		final StringTokenizer tokenizer = new StringTokenizer(propertyName, ".");
		final int count = tokenizer.countTokens();

		PropertyDescriptor propertyDesc = null;
		final ArrayList<Method> methods = new ArrayList<Method>();
		final ArrayList<String> descriptions = new ArrayList<String>();

		for (int i = 0; i < count; i++) {
			final String name = tokenizer.nextToken();

			propertyDesc = getProperty(targetClass, name);

			if (propertyDesc == null) {
				throw new IllegalArgumentException("No se encontró la propiedad '" + name + "' para la clase "
						+ targetClass.getName());
			}

			if (propertyDesc.getReadMethod() == null) {
				throw new IllegalArgumentException("No se encontró el método de lectura para la propiedad '" + name
						+ "' de la clase " + targetClass.getName());
			}

			final String valueDescription = this.getValueDescription(propertyDesc);
			if (valueDescription != null) {
				descriptions.add(valueDescription);
			}
			methods.add(propertyDesc.getReadMethod());

			targetClass = propertyDesc.getPropertyType();
		}

		final PropertyInfo pi = new PropertyInfo(methods.toArray(new Method[methods.size()]), descriptions
				.toArray(new String[descriptions.size()]));

		return pi;
	}

	private static PropertyDescriptor getProperty(Class clazz, String propName) {
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

	private PropertyInfo getPropertyFromCache(Class clazz, String propName) {
		return this.propertyCache.get(new PropertyKey(clazz, propName));
	}

	private void putPropertyInCache(Class clazz, String propName, PropertyInfo pi) {
		this.propertyCache.put(new PropertyKey(clazz, propName), pi);
	}

	private static class ValidationInfo {

		ValidationInfo(Method method, Pair<Annotation, ValidationHandler>[] annotations, String valueLocation,
				String valueDescription) {
			super();
			this.method = method;
			this.annotations = annotations;
			this.valueLocation = valueLocation;
			this.valueDescription = valueDescription;
		}

		public Pair<Annotation, ValidationHandler>[] getAnnotations() {
			return annotations;
		}

		public Method getMethod() {
			return method;
		}

		public String getValueDescription() {
			return valueDescription;
		}

		public String getValueLocation() {
			return valueLocation;
		}

		private String valueLocation;

		private String valueDescription;

		private Pair<Annotation, ValidationHandler>[] annotations;

		private Method method;

	}

	private static class PropertyKey {

		PropertyKey(Class propertyClass, String propertyName) {
			super();
			this.propertyClass = propertyClass;
			this.propertyName = propertyName;
		}

		@Override
		public boolean equals(Object obj) {
			final PropertyKey k = (PropertyKey) obj;
			return k.propertyClass.equals(this.propertyClass) && k.propertyName.equals(this.propertyName);
		}

		@Override
		public int hashCode() {
			return this.propertyClass.hashCode() + 31 * this.propertyName.hashCode();
		}

		private Class propertyClass;

		private String propertyName;

	}

	private static class PropertyInfo {

		PropertyInfo(Method methods[], String[] valueDescriptions) {
			super();
			this.methods = methods;
			this.valueDescriptions = valueDescriptions;
		}

		public Method[] getMethods() {
			return methods;
		}

		public String[] getValueDescriptions() {
			return valueDescriptions;
		}

		private String[] valueDescriptions;

		private Method[] methods;
	}

	private List<ValidationInfo> annotationCache;

	private ConcurrentHashMap<PropertyKey, PropertyInfo> propertyCache;

	private ValidationContext context;
}
