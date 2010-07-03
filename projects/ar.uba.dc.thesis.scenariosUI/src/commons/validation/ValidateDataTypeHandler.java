package commons.validation;

import commons.annotations.DataTypeAnnotation;
import commons.validation.ValidationResult;


public class ValidateDataTypeHandler implements ValidationHandler<DataTypeAnnotation> {

	public void validate(Object value, DataTypeAnnotation a, ValidationResult result,ValidationContext context) {
		if(value == null){
			return;
		}
		context.getDataTypeDictionaryDirectory().validate(value, a.typeId(), result);
	}

}

	

