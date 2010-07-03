package commons.validation;

import java.lang.reflect.Method;

import commons.annotations.ValidateComponent;
import commons.annotations.ValidationCondition;


public class ValidateComponentHandler implements ValidationHandler<ValidateComponent> {

	@SuppressWarnings("unchecked")
	public void validate(Object value, ValidateComponent annotation, ValidationResult result,
			ValidationContext context) {
		if (value == null) {
			return;
		}

		final ValidationCondition condition = annotation.condition();

		if (!evaluateCondition(value, condition, context)) {
			return;
		}

		final Validator validator = context.getValidatorFor(value.getClass());

		if (validator == null) {
			throw new IllegalArgumentException("No se definió un validador para la clase "
					+ value.getClass().getName());
		}

		validator.validate(value, result);
	}

	private boolean evaluateCondition(Object value, ValidationCondition condition,
			ValidationContext context) {
		if (ValidationCondition.ALWAYS.equals(condition)) {
			return true;
		}
		if (ValidationCondition.IF_NOT_EMPTY.equals(condition)) {
			return isNotEmpty(value);
		}
		return false;
	}

	private boolean isNotEmpty(Object value) {
		final Class cl = value.getClass();

		Method isEmptyMethod=null;
		
		try {
			isEmptyMethod=cl.getMethod(IS_EMPTY_METHOD, new Class[] {});
		} catch (Exception e) {
			throw new IllegalArgumentException("La clase " + cl.getName()
					+ " no tiene declarado el método '" + IS_EMPTY_METHOD + "'", e);
		}
		
		
		try {
			final Object result=isEmptyMethod.invoke(value, new Object[]{});
			
			if(!(result instanceof Boolean)){
				throw new IllegalArgumentException("El método '" + 
						IS_EMPTY_METHOD + "' en la clase " + cl.getName() + 
						" no retorna un booleano");
			}
			
			return !((Boolean)result).booleanValue();
		} catch (Exception e) {
			throw new IllegalArgumentException("Error invocando el método '" + 
					IS_EMPTY_METHOD + "' en la clase " + cl.getName() + ": " + e.getMessage(), e);
		} 
		
	}

	private static final String IS_EMPTY_METHOD = "isEmpty";
}
