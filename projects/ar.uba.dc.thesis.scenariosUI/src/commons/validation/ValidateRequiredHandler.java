package commons.validation;

import java.lang.annotation.Annotation;

import commons.validation.ValidationResult;
import commons.validation.ValidationUtils;

public class ValidateRequiredHandler implements ValidationHandler {

	public void validate(Object value, Annotation annotation, ValidationResult result, ValidationContext context) {
		if (value instanceof String) {
			ValidationUtils.isNotEmpty(result, (String) value, "", result.getValueDescription());
		} else {
			ValidationUtils.isNotNull(result, value, "", result.getValueDescription());
		}
	}

	private ValidateRequiredHandler() {

	}

	public static ValidationHandler getInstance() {
		return instance;
	}

	public static final ValidateRequiredHandler instance = new ValidateRequiredHandler();
}
