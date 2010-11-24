package commons.validation;

import java.lang.annotation.Annotation;

public class ValidateBooleanRequiredHandler implements ValidationHandler {

	public void validate(Object value, Annotation annotation, ValidationResult result, ValidationContext context) {
		ValidationUtils.isNotTrue(result, value, "", result.getValueDescription());

	}
}
