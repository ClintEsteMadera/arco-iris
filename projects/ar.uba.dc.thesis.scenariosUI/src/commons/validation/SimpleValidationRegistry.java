package commons.validation;

/**
 * Registry de validadores "simples"
 * 
 */

public class SimpleValidationRegistry<T> extends ValidationRegistry<Validator<T>> implements Validator<T> {

	public boolean validate(T objectToValidate, ValidationResult result) {
		return this.getValidatorFor(objectToValidate.getClass()).validate(objectToValidate, result);
	}

	public ValidationResult validate(T objectToValidate) {
		ValidationResult result = new ValidationResult();
		validate(objectToValidate, result);
		return result;
	}

}
