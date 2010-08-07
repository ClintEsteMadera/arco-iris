package commons.validation;

import java.util.HashMap;

/**
 * Diccionario para 'tipos de datos'
 * 
 * 
 */
public class DataTypeDictionary {

	public DataTypeDictionary() {
		this.validators = new HashMap<String, Validator>();
	}

	public void putStringType(String id, StringConstraints c) {
		validators.put(id, c);
	}

	public void putNumericType(String id, NumericConstraints c) {
		validators.put(id, c);
	}

	public StringConstraints getStringType(String id) {
		return (StringConstraints) validators.get(id);
	}

	public NumericConstraints getNumericType(String id) {
		return (NumericConstraints) validators.get(id);
	}

	@SuppressWarnings("unchecked")
	public boolean validate(Object o, String typeId, ValidationResult result, String location, String valueDescription) {

		final Validator validator = getTypeValidator(typeId);

		if (validator == null) {
			throw new IllegalArgumentException("No se registró validador para el tipo '" + typeId + "'");
		}

		if (location != null) {
			result.pushNestedPath(location);
		}

		if (valueDescription != null) {
			result.pushValueDescription(valueDescription);
		}

		boolean isValid = validator.validate(o, result);

		if (location != null) {
			result.popNestedPath();
		}

		if (valueDescription != null) {
			result.popValueDescription();
		}

		return isValid;
	}

	Validator getTypeValidator(String typeId) {
		return validators.get(typeId);
	}

	private HashMap<String, Validator> validators;
}
