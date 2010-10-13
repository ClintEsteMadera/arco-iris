package commons.validation;

import java.io.Serializable;

import org.springframework.util.Assert;

import commons.properties.EnumProperty;

public class ValidationError implements Serializable {

	private String message;

	private String errorLocation;

	private static final long serialVersionUID = 1L;

	public ValidationError(EnumProperty message) {
		this(null, message);
	}

	public ValidationError(EnumProperty message, Object... replacements) {
		this(null, message, replacements);
	}

	public ValidationError(String errorLocation, EnumProperty message) {
		super();
		Assert.notNull(message, "The error message cannot be null");
		this.errorLocation = errorLocation;
		this.message = message.toString();
	}

	public ValidationError(String errorLocation, EnumProperty message, Object... replacements) {
		super();
		Assert.notNull(message, "The error message cannot be null");
		Assert.notNull(replacements, "The replacements cannot be null");
		this.errorLocation = errorLocation;
		this.message = message.toString(replacements);
	}

	public String getErrorLocation() {
		return errorLocation;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return this.getMessage();
	}
}