package sba.common.validation;

import java.lang.annotation.Annotation;

import sba.common.validation.ValidationResult;
import sba.common.validation.ValidationUtils;

public class ValidateCuitHandler implements ValidationHandler {

	public void validate(Object value, Annotation annotation, ValidationResult result, ValidationContext context) {
		
		ValidationUtils.isNotCuit(result, value, "", result.getValueDescription());
		
	}
}

	

