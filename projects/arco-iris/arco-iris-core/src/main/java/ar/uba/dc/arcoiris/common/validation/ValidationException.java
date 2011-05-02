package ar.uba.dc.arcoiris.common.validation;

import java.util.List;

/**
 * This exception encapsulares a list of validation errors
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ValidationError[] validationErrors;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

	public ValidationException(List<ValidationError> validationErrors) {
		this(validationErrors.toArray(new ValidationError[validationErrors.size()]));
	}

	public ValidationException(ValidationError... validationErrors) {
		super();
		if (validationErrors == null) {
			throw new IllegalArgumentException("Validation errors cannot be null");
		}
		this.validationErrors = validationErrors;
	}

	public ValidationException(Throwable cause) {
		super(cause.getMessage(), cause);
		this.validationErrors = new ValidationError[] { new ValidationError(cause.getMessage()) };
	}

	public ValidationError[] getErrors() {
		return validationErrors;
	}

	public String[] getValidationMessages() {
		String[] messages = new String[this.validationErrors.length];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = this.validationErrors[i].getMessage();
		}
		return messages;
	}

	/**
	 * Returns all of the validation messages, each one in a separate line.
	 * 
	 * @return the list of validation messages, separated by a new line separator.
	 */
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.validationErrors.length; i++) {
			sb.append(this.validationErrors[i].getMessage());
			if (i + 1 != this.validationErrors.length) {
				sb.append(LINE_SEPARATOR);
			}
		}
		return sb.toString();
	}
}