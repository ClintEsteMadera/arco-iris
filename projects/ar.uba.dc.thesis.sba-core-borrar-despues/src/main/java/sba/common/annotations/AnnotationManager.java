package sba.common.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import sba.common.annotations.DataTypeAnnotation;
import sba.common.dataestructures.Pair;
import sba.common.validation.ValidateBooleanRequiredHandler;
import sba.common.validation.ValidateComponentHandler;
import sba.common.validation.ValidateCuitHandler;
import sba.common.validation.ValidateDataTypeHandler;
import sba.common.validation.ValidateRequiredHandler;
import sba.common.validation.ValidationHandler;

public class AnnotationManager {

	public static AnnotationManager getInstance(){
		return instance;
	}
	
	private AnnotationManager(){
		handlers = new HashMap<Class, ValidationHandler>();
		this.registerValidationHandler(ValidateRequired.class, ValidateRequiredHandler.getInstance());
		this.registerValidationHandler(ValidateBooleanRequired.class, new ValidateBooleanRequiredHandler());
		this.registerValidationHandler(DataTypeAnnotation.class, new ValidateDataTypeHandler());
		this.registerValidationHandler(ValidateComponent.class, new ValidateComponentHandler());
		this.registerValidationHandler(ValidateCuit.class, new ValidateCuitHandler());
	}
	
	private ValidationHandler getValidationHandler(Annotation annotation) {
		return handlers.get(annotation.annotationType());
	}

	public String getValueDescription(Method method) {
		PropertyDescription desc = method.getAnnotation(PropertyDescription.class);
		return desc != null ? desc.value() : null;
	}

	public void registerValidationHandler(Class<? extends Annotation> annotationClass,
			ValidationHandler handler){
		this.handlers.put(annotationClass, handler);
	}
	
	@SuppressWarnings({ "unchecked", "unchecked" })
	public Pair<Annotation, ValidationHandler>[] getValidationAnnotations(Method method) {
		ArrayList<Pair<Annotation, ValidationHandler>> list = new ArrayList<Pair<Annotation, ValidationHandler>>();

		Annotation[] annotations = method.getAnnotations();

		for (Annotation a : annotations) {
			final ValidationHandler h = getValidationHandler(a);
			if (h != null) {
				list.add(new Pair<Annotation, ValidationHandler>(a, h));
			}
		}

		Pair<Annotation, ValidationHandler>[] array = new Pair[list.size()];
		return list.toArray(array);
	}

	private HashMap<Class, ValidationHandler> handlers;

	private static final AnnotationManager instance=new AnnotationManager();
}
