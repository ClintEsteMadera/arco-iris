package scenariosui.gui.thread;

import ar.uba.dc.thesis.common.validation.ValidationException;

import commons.gui.thread.AbstractGUIUncaughtExceptionHandler;
import commons.gui.widget.dialog.ValidationMessageDialog;

public class ScenariosUIUncaughtExceptionHandler extends AbstractGUIUncaughtExceptionHandler {

	@Override
	protected boolean isKnownException(Throwable throwable) {
		return throwable instanceof ValidationException;
	}

	@Override
	protected void handleKnownException(Throwable throwable) {
		final ValidationException validationException = (ValidationException) throwable;

		ValidationMessageDialog.open(null, validationException.getValidationMessages());
	}

}
