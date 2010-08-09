package commons.validation;

/**
 * 
 * 
 */

public class CompositeValidator implements Validator {

	public CompositeValidator(Validator validator1, Validator validator2) {
		this.validator1 = validator1;
		this.validator2 = validator2;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(Object objectToValidate, ValidationResult result) {
		return validator1.validate(objectToValidate, result) & validator2.validate(objectToValidate, result);
	}

	private Validator validator1;
	private Validator validator2;
}
