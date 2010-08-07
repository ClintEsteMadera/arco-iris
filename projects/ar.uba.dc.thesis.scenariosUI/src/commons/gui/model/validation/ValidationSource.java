package commons.gui.model.validation;

import commons.validation.ValidationError;

/**
 * 
 * 
 */

public interface ValidationSource {

	public ValidationError[] getValidationErrors();

	public void setValidationErrors(ValidationError[] errors);

	public void addValidationChangedListener(ValidationChangedListener listener);

	public void removeValidationChangedListener(ValidationChangedListener listener);
}
