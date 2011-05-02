package ar.uba.dc.arcoiris.common.validation;

import java.text.MessageFormat;

public class ValidationError {

	private String message;

	public ValidationError(String message) {
		super();
		if (message == null) {
			throw new IllegalArgumentException("The error message cannot be null");
		}
		this.message = message.toString();
	}

	public ValidationError(String message, Object... replacements) {
		super();
		if (message == null) {
			throw new IllegalArgumentException("The error message cannot be null");
		}
		if (replacements == null) {
			throw new IllegalArgumentException("The replacements cannot be null");
		}
		this.message = MessageFormat.format(message, replacements);
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return this.getMessage();
	}
}