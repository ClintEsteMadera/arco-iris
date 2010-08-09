package commons.validation;

import java.util.Calendar;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import commons.exception.ValidationException;
import commons.properties.EnumProperty;

/**
 * @author Pablo Pastorino
 * 
 */
public abstract class ValidationUtils {

	// Obligatoriedad y longitud

	public static boolean isNotZero(ValidationResult result, Object value, String location, String valueDescription) {
		return isValidCondition(result, location, (new Integer(value.toString()) != 0),
				CommonValidationMessages.NOT_ZERO, valueDescription);
	}

	public static boolean isNotNull(ValidationResult result, Object value, String location, String valueDescription) {
		return isValidCondition(result, location, (value != null), CommonValidationMessages.NOT_NULL, valueDescription);
	}

	public static boolean isNotEmpty(ValidationResult result, String value, String location, String valueDescription) {
		return isValidCondition(result, location, StringUtils.isNotEmpty(value), CommonValidationMessages.NOT_NULL,
				valueDescription);
	}

	public static boolean isValidLength(ValidationResult result, String string, int min, int max, String location,
			String valueDescription) {

		boolean isValid = true;
		if (StringUtils.isNotEmpty(string) && (string.length() < min || string.length() > max)) {
			isValid = false;
			EnumProperty message = (min == max) ? CommonValidationMessages.STRING_SIZE_EXACTLY
					: CommonValidationMessages.STRING_SIZE;
			Object[] params = new Object[] { valueDescription, min, max };
			if (location == null) {
				result.addError(message, params);
			} else {
				result.addError(location, message, params);
			}
		}
		return isValid;
	}

	// Tipo de caracteres

	public static boolean isAlpha(ValidationResult result, String string, String location, String valueDescription) {
		boolean condition = string != null && StringUtils.isAlphaSpace(string);
		return isValidCondition(result, location, condition, CommonValidationMessages.NOT_ALPHA, valueDescription);
	}

	public static boolean isNumeric(ValidationResult result, String string, String location, String valueDescription) {
		boolean condition = string != null && StringUtils.isNumericSpace(string);
		return isValidCondition(result, location, condition, CommonValidationMessages.NOT_NUMERIC, valueDescription);
	}

	public static boolean isAlphanumeric(ValidationResult result, String string, String location,
			String valueDescription) {
		boolean condition = string != null && StringUtils.isAlphanumeric(string);
		return isValidCondition(result, location, condition, CommonValidationMessages.NOT_ALPHANUMERIC,
				valueDescription);
	}

	public static boolean isAlphanumericSpace(ValidationResult result, String string, String location,
			String valueDescription) {
		boolean condition = string != null && StringUtils.isAlphanumericSpace(string);
		return isValidCondition(result, location, condition, CommonValidationMessages.NOT_ALPHANUMERIC_SPACE,
				valueDescription);
	}

	public static boolean isAsciiPrintable(ValidationResult result, String string, String location,
			String valueDescription) {
		// Se utiliza el método "isAsciiPrintable" para permitir puntos, guiones, etc.
		boolean condition = string != null && StringUtils.isAsciiPrintable(string);
		return isValidCondition(result, location, condition, CommonValidationMessages.NOT_ALPHANUMERIC_SPACE,
				valueDescription);
	}

	// Comparación de números

	public static boolean isBetween(ValidationResult result, Integer value, Integer min, Integer max, String location,
			String valueDescription) {
		boolean condition = value >= min && value <= max;

		return isValidCondition(result, location, condition, CommonValidationMessages.BETWEEN, new Object[] {
				valueDescription, min, max });
	}

	public static <T> boolean isLessThan(ValidationResult result, Comparable<T> value1, T value2, String location,
			String value1Description, String value2Description) {
		return isValidCondition(result, location, (value1.compareTo(value2) < 0), CommonValidationMessages.LESS_THAN,
				value1Description, value2Description);
	}

	public static <T> boolean isLessThanOrEquals(ValidationResult result, Comparable<T> value1, T value2,
			String location, String value1Description, String value2Description) {
		return isValidCondition(result, location, (value1.compareTo(value2) <= 0),
				CommonValidationMessages.LESS_THAN_OR_EQUAL, value1Description, value2Description);
	}

	public static <T> boolean isGreaterThan(ValidationResult result, Comparable<T> value1, T value2, String location,
			String value1Description, String value2Description) {
		return isValidCondition(result, location, (value1.compareTo(value2) > 0),
				CommonValidationMessages.GREATER_THAN, value1Description, value2Description);
	}

	public static <T> boolean isGreaterThanOrEquals(ValidationResult result, Comparable<T> value1, T value2,
			String location, String value1Description, String value2Description) {
		return isValidCondition(result, location, (value1.compareTo(value2) >= 0),
				CommonValidationMessages.GREATER_THAN_OR_EQUAL, value1Description, value2Description);
	}

	// Comparación de Fechas

	public static boolean isBeforeOrEqualsThan(ValidationResult result, Calendar value1, Calendar value2,
			String location, String value1Description, String value2Description) {
		return isValidCondition(result, location, (value1.compareTo(value2) <= 0),
				CommonValidationMessages.BEFORE_THAN_OR_EQUALS, value1Description, value2Description);
	}

	public static boolean isBeforeThan(ValidationResult result, Calendar value1, Calendar value2, String location,
			String value1Description, String value2Description) {
		return isValidCondition(result, location, (value1.compareTo(value2) < 0), CommonValidationMessages.BEFORE_THAN,
				value1Description, value2Description);
	}

	public static boolean isAfterOrEqualsThan(ValidationResult result, Calendar value1, Calendar value2,
			String location, String value1Description, String value2Description) {
		return isValidCondition(result, location, (value1.compareTo(value2) >= 0),
				CommonValidationMessages.AFTER_THAN_OR_EQUALS, value1Description, value2Description);
	}

	public static boolean isAfterThan(ValidationResult result, Calendar value1, Calendar value2, String location,
			String value1Description, String value2Description) {
		return isValidCondition(result, location, (value1.compareTo(value2) > 0), CommonValidationMessages.AFTER_THAN,
				value1Description, value2Description);
	}

	/**
	 * @deprecated Usar tipo de dato
	 * 
	 * Método de conveniencia para facilitar las validaciones.
	 * @param string
	 *            el String a validar.
	 * @param result
	 *            el resultado al cual agregar errores (de haberlos)
	 * @param location
	 *            la fuente del error
	 * @param valueDescription
	 *            la descripción del valor a validar
	 * @param characterType
	 *            Indica el tipo de caracter que debe posee el <code>string</code>
	 * @param min
	 *            Indica la cantidad mínima de caracteres que debe poseer el <code>string</code>
	 * @param max
	 *            Indica la cantidad máxima de caracteres que debe poseer el <code>string</code>
	 * @return <code>true</code> si el string no es vacío, posee entre 1 y 255 caracteres y sus caracteres son del
	 *         tipo indicado por <code>characterType</code>. <code>false</code>, en otro caso, en cuyo caso,
	 *         agrega un mensaje de error a <code>result</code>
	 */
	@Deprecated
	public static boolean isMandatory(String string, ValidationResult result, String location, String valueDescription,
			CharacterType characterType, int min, int max) {
		boolean isValid = ValidationUtils.isNotEmpty(result, string, location, valueDescription);

		if (isValid) {
			isValid = ValidationUtils.isValidLength(result, string, min, max, location, valueDescription);
			isValid = characterType.isValid(result, string, location, valueDescription) && isValid;
		}
		return isValid;
	}

	/**
	 * @deprecated Usar tipo de dato
	 * 
	 * Método de conveniencia para facilitar las validaciones.
	 * @param string
	 *            el String a validar.
	 * @param result
	 *            el resultado al cual agregar errores (de haberlos)
	 * @param location
	 *            la fuente del error
	 * @param valueDescription
	 *            la descripción del valor a validar
	 * @param characterType
	 *            Indica el tipo de caracter que debe posee el <code>string</code>
	 * @param min
	 *            Indica la cantidad mínima de caracteres que debe poseer el <code>string</code>
	 * @param max
	 *            Indica la cantidad máxima de caracteres que debe poseer el <code>string</code>
	 * @return <code>true</code> si el string es vacío ó no siendo vacío, posee entre 1 y 255 caracteres y sus
	 *         caracteres son del tipo indicado por <code>characterType</code>. <code>false</code>, en otro caso,
	 *         en cuyo caso, agrega un mensaje de error a <code>result</code>
	 */
	@Deprecated
	public static boolean isNotMandatory(String string, ValidationResult result, String location,
			String valueDescription, CharacterType characterType, int min, int max) {
		boolean isValid = true;

		if (StringUtils.isNotEmpty(string)) {
			isValid = ValidationUtils.isValidLength(result, string, min, max, location, valueDescription);
			isValid = characterType.isValid(result, string, location, valueDescription) && isValid;
		}
		return isValid;
	}

	public static boolean isNotMandatory(Object value, ValidationResult result, DataTypeDictionary dict, String typeId,
			String location, String valueDescription) {

		boolean isNull = false;

		if (value instanceof String) {
			isNull = !StringUtils.isNotEmpty((String) value);
		} else {
			isNull = value == null;
		}

		if (!isNull) {
			return dict.validate(value, typeId, result, location, valueDescription);
		} else {
			return true;
		}
	}

	// Validaciones Genéricas

	/**
	 * Valida que se cumpla la condición especificada por parámetro. Si NO se cumple dicha condición, agrega un error al
	 * parámetro <code>result</code>
	 * 
	 * @param result
	 *            el resultado acumulado
	 * @param location
	 *            la ubicación del error
	 * @param condition
	 *            la condición a validar
	 * @param message
	 *            el mensaje a agregar si la condición no se cumple
	 * @param valueDescription
	 *            la/s descripción/es que completa/n el mensaje de error
	 * @return <code>true</code> si la condición provista por parámetro se verifica. <code>false</code>, en otro
	 *         caso, en cuyo caso, agrega un mensaje de error a <code>result</code>
	 */
	public static boolean isValidCondition(ValidationResult result, String location, boolean condition,
			EnumProperty message, Object... valueDescription) {
		boolean isValid = true;
		if (!condition) {
			isValid = false;
			if (location == null) {
				result.addError(message, valueDescription);
			} else {
				result.addError(location, message, valueDescription);
			}
		}
		return isValid;
	}

	public static <T> boolean validateCollection(ValidationResult result, Collection<T> collection, String location,
			Validator<T> validator) {
		int i = 0;

		boolean valid = true;

		result.pushNestedPath(location);

		for (T objectToValidate : collection) {
			result.pushNestedPath(i);

			valid = validator.validate(objectToValidate, result) & valid;

			result.popNestedPath();
			i++;
		}
		result.popNestedPath();

		return valid;
	}

	public static void throwExceptionIfItsNotOk(ValidationResult validationResult) {
		if (validationResult.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("Error de Validación:\n" + validationResult.getErrorMessage());
			}
			throw new ValidationException(validationResult.getErrors());
		}
	}

	public static boolean isMandatory(String string, ValidationResult result, DataTypeDictionary dict, String typeId,
			String location, String valueDescription) {
		boolean isValid = ValidationUtils.isNotEmpty(result, string, location, valueDescription);

		if (isValid) {
			isValid = dict.validate(string, typeId, result, location, valueDescription);
		}
		return isValid;
	}

	public static boolean isMandatoryObject(Object value, ValidationResult result, DataTypeDictionary dict,
			String typeId, String location, String valueDescription) {
		boolean isValid = ValidationUtils.isNotNull(result, value, location, valueDescription);

		if (isValid) {
			isValid = dict.validate(value, typeId, result, location, valueDescription);
		}
		return isValid;
	}

	public static boolean isNotMandatory(String string, ValidationResult result, DataTypeDictionary dict,
			String typeId, String location, String valueDescription) {
		boolean isValid = true;

		if (StringUtils.isNotEmpty(string)) {
			isValid = dict.validate(string, typeId, result, location, valueDescription);
		}
		return isValid;
	}

	// Boolean Obligatorio

	public static void isNotTrue(ValidationResult result, Object value, String location, String valueDescription) {

		if (value instanceof Boolean) {
			isValidCondition(result, location, (!(value.equals(new Boolean(false)))),
					CommonValidationMessages.NOT_NULL, valueDescription);
		}
	}

	// Inner Class

	public enum CharacterType {
		ALPHA() {
			@Override
			boolean isValid(ValidationResult result, String string, String location, String valueDescription) {
				return isAlpha(result, string, location, valueDescription);
			}
		},
		NUMERIC() {
			@Override
			boolean isValid(ValidationResult result, String string, String location, String valueDescription) {
				return isNumeric(result, string, location, valueDescription);
			}
		},

		ALPHANUMERIC() {
			@Override
			boolean isValid(ValidationResult result, String string, String location, String valueDescription) {
				return isAlphanumeric(result, string, location, valueDescription);
			}
		},
		ALPHANUMERIC_SPACE() {
			@Override
			boolean isValid(ValidationResult result, String string, String location, String valueDescription) {
				return isAlphanumericSpace(result, string, location, valueDescription);
			}
		},
		ASCII_PRINTABLE() {
			@Override
			boolean isValid(ValidationResult result, String string, String location, String valueDescription) {
				return isAsciiPrintable(result, string, location, valueDescription);
			}
		},
		NOT_SPECIFIED() {
			@Override
			boolean isValid(ValidationResult result, String string, String location, String valueDescription) {
				return true;
			}
		};

		abstract boolean isValid(ValidationResult result, String string, String location, String valueDescription);
	}

	private static Log log = LogFactory.getLog(ValidationUtils.class);

}