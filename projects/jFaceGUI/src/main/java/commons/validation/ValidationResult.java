package commons.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import commons.properties.EnumProperty;

/**
 * 
 */

public class ValidationResult {

	public ValidationResult() {
		this(new ArrayList<ValidationError>());
	}

	public ValidationResult(List<ValidationError> errors) {
		super();
		Assert.notNull(errors, "La lista de errores no puede ser nula");
		this.validationErrors = errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#hasErrors()
	 */
	public boolean hasErrors() {
		return !this.validationErrors.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#getErrorCount()
	 */
	public int getErrorCount() {
		return this.validationErrors.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#getErrors()
	 */
	public List<ValidationError> getErrors() {
		return this.validationErrors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#addErrors(jface.gui.validation.ValidationResult)
	 */
	public void addErrors(ValidationResult errors) {
		this.validationErrors.addAll(errors.validationErrors);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#addError(java.lang.String, sba.common.properties.EnumProperty)
	 */
	public void addError(String errorLocation, EnumProperty validationMessage) {
		this.validationErrors.add(new ValidationError(validationMessage, getPath(errorLocation)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#addError(sba.common.properties.EnumProperty)
	 */
	public void addError(EnumProperty validationMessage) {
		this.validationErrors.add(new ValidationError(validationMessage));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#addError(sba.common.properties.EnumProperty, java.lang.Object)
	 */
	public void addError(EnumProperty validationMessage, Object... args) {
		this.validationErrors.add(new ValidationError(validationMessage, args));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#addError(java.lang.String, sba.common.properties.EnumProperty,
	 *      java.lang.Object)
	 */
	public void addError(String errorLocation, EnumProperty validationMessage, Object... args) {
		this.validationErrors.add(new ValidationError(getPath(errorLocation), validationMessage, args));
	}

	@Override
	public String toString() {
		return this.getErrorMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#getErrorMessage()
	 */
	public String getErrorMessage() {
		StringBuilder sb = new StringBuilder();
		for (ValidationError validationError : this.validationErrors) {
			sb.append(validationError.getMessage()).append(LINE_SEPARATOR);
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#pushNestedPath(int)
	 */
	public void pushNestedPath(int index) {
		pushNestedPath("[" + index + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#pushNestedPath(java.lang.String)
	 */
	public void pushNestedPath(String location) {
		this.nestedPath = getPath(this.nestedPath, location);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#popNestedPath()
	 */
	public void popNestedPath() {
		if (this.nestedPath.length() == 0) {
			throw new IllegalStateException("No se hizo previamente un push del nested path");
		}

		final int i = this.nestedPath.lastIndexOf(PATH_SEPARATOR);
		if (i < 0) {
			this.nestedPath = "";
		} else {
			this.nestedPath = this.nestedPath.substring(0, i);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#pushValueDescription(java.lang.String)
	 */
	public void pushValueDescription(String desc) {
		this.valueDescription = getValueDescription(this.valueDescription, desc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#popValueDescription()
	 */
	public void popValueDescription() {
		if (this.valueDescription.length() == 0) {
			throw new IllegalStateException("No se hizo previamente un push del 'value description'");
		}

		final int i = this.valueDescription.lastIndexOf(DESCRIPTION_SEPARATOR);
		if (i < 0) {
			this.valueDescription = "";
		} else {
			this.valueDescription = this.valueDescription.substring(0, i);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see jface.gui.validation.IValidationResult#getValueDescription()
	 */
	public String getValueDescription() {
		return this.valueDescription;
	}

	private String getPath(String location) {
		return nestedPath.length() == 0 ? location : nestedPath
				+ (location.length() > 0 ? PATH_SEPARATOR + location : "");
	}

	private static String getPath(String nestedPath, String location) {
		return nestedPath.length() == 0 ? location : location.length() == 0 ? nestedPath : nestedPath + PATH_SEPARATOR
				+ location;
	}

	private static String getValueDescription(String valueDescription, String v) {
		return valueDescription.length() == 0 ? v : valueDescription + DESCRIPTION_SEPARATOR + v;
	}

	private String valueDescription = "";

	private String nestedPath = "";

	private List<ValidationError> validationErrors;

	private static final String PATH_SEPARATOR = ".";

	public static final String DESCRIPTION_SEPARATOR = "/ ";

	private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
}