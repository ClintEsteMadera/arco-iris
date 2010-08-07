package commons.validation;

import java.util.Properties;

import commons.properties.EnumPropertiesHelper;
import commons.properties.EnumProperty;

/**
 * 
 */

public enum CommonValidationMessages implements EnumProperty {

	NOT_NULL,
	NOT_ALPHA,
	NOT_ALPHANUMERIC,
	NOT_ALPHANUMERIC_SPACE,
	NOT_NUMERIC,
	NOT_CUIT,
	BETWEEN,
	STRING_SIZE,
	STRING_SIZE_EXACTLY,
	LESS_THAN,
	LESS_THAN_OR_EQUAL,
	BEFORE_THAN,
	BEFORE_THAN_OR_EQUALS,
	BEFORE_THAN_OR_EQUALS_FOR_CONCEPT,
	AFTER_THAN,
	AFTER_THAN_OR_EQUALS,
	PREEXISTENT_CUIT,
	INVALID_CUIT,
	INVALID_CUIL,
	INVALID_EMAIL,
	IS_NOT_ARGENTINA,
	COULD_NOT_BE_ARGENTINA,
	GREATER_THAN,
	GREATER_THAN_OR_EQUAL,
	VALID_CHAR_CONSTRAINT,
	INVALID_CHAR_CONSTRAINT,
	STRING_MIN_SIZE_CONSTRAINT,
	STRING_MAX_SIZE_CONSTRAINT,
	STRING_RANGE_SIZE_CONSTRAINT,
	NOT_ZERO;

	@Override
	public String toString() {
		return EnumPropertiesHelper.getString(props, this.name());
	}

	public String toString(Object... reemplazos) {
		return EnumPropertiesHelper.getString(props, this.name(), reemplazos);
	}

	private static Properties props = EnumPropertiesHelper.load("/CommonValidationMessages.properties");
}