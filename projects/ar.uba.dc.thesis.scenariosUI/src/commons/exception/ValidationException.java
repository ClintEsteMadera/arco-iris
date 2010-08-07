package commons.exception;

import java.util.List;

import org.springframework.util.Assert;

import commons.properties.FakeEnumProperty;
import commons.validation.ValidationError;

/**
 * Una excepción que encapsula una lista de errores de validación.<br>
 * 
 */
public class ValidationException extends ServiceException {

	public ValidationException(List<ValidationError> validationErrors) {
		super(null);
		Assert.notNull(validationErrors, "La lista de errores de validación no puede ser nula");
		this.validationErrors = validationErrors.toArray(new ValidationError[validationErrors.size()]);
	}

	public ValidationException(ValidationError... validationErrors) {
		super(null);
		Assert.notNull(validationErrors, "La lista de errores de validación no puede ser nula");
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
	 * Devuelve todos los mensajes de validaciones, uno por cada renglón.
	 * 
	 * @return lista de los mensajes de validaciones, separados por un <code>Enter</code>
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

	private ValidationError[] validationErrors;

	private static final long serialVersionUID = 1L;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
}