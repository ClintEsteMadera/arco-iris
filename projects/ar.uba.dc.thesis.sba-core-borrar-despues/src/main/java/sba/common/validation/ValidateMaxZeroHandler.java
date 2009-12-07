package sba.common.validation;

import java.lang.annotation.Annotation;

public class ValidateMaxZeroHandler implements ValidationHandler {

	public void validate(Object value, Annotation annotation, ValidationResult result, ValidationContext context) {
		
		ValidationUtils.isNotZero( result, value, "", result.getValueDescription());
		
	}
}

	

