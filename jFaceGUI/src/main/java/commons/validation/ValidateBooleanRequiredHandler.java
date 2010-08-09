package commons.validation;

import java.lang.annotation.Annotation;

import commons.validation.ValidationResult;
import commons.validation.ValidationUtils;

public class ValidateBooleanRequiredHandler implements ValidationHandler {

	public void validate(Object value, Annotation annotation, ValidationResult result, ValidationContext context) {
		ValidationUtils.isNotTrue(result, value, "", result.getValueDescription());

	}
}
