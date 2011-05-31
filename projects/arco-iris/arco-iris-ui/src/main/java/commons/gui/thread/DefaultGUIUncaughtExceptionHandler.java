package commons.gui.thread;

import commons.exception.ValidationException;
import commons.gui.model.validation.ValidationSource;
import commons.gui.widget.dialog.ValidationMessageDialog;

public class DefaultGUIUncaughtExceptionHandler extends AbstractGUIUncaughtExceptionHandler {

	private ValidationSource validationSource;

	public DefaultGUIUncaughtExceptionHandler() {
		super();
	}

	public ValidationSource getValidationSource() {
		return validationSource;
	}

	public void setValidationSource(ValidationSource validationSource) {
		this.validationSource = validationSource;
	}

	@Override
	protected boolean isKnownException(Throwable throwable) {
		return throwable instanceof ValidationException;
	}

	@Override
	protected void handleKnownException(Throwable throwable) {
		final ValidationException validationException = (ValidationException) throwable;

		if (this.validationSource != null) {
			this.validationSource.setValidationErrors(validationException.getErrors());
		}

		ValidationMessageDialog.open(null, validationException.getValidationMessages());
	}
}
