package commons.exception;

import java.util.List;

import org.springframework.util.Assert;

import commons.properties.FakeEnumProperty;
import commons.validation.ValidationError;

/**
 * This exception encapsulares a list of validation errors
 */
public class ValidationException extends ServiceException {

	private ValidationError[] validationErrors;

	private static final long serialVersionUID = 1L;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

	public ValidationException(List<ValidationError> validationErrors) {
		this(validationErrors.toArray(new ValidationError[validationErrors.size()]));
	}

	public ValidationException(ValidationError... validationErrors) {
		super(null);
		Assert.notNull(validationErrors, "The list of validation errors cannot be null");
		this.validationErrors = validationErrors;
	}

	public ValidationException(Throwable cause) {
		super(cause.getMessage(), cause);
		FakeEnumProperty message = new FakeEnumProperty(cause.getMessage());
		this.validationErrors = new ValidationError[] { new ValidationError(message) };
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

	public ValidationError[] getErrors() {
		return validationErrors;
	}
}