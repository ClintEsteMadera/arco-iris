package ar.uba.dc.arcoirisui.gui.thread;

import ar.uba.dc.arcoiris.common.validation.ValidationException;

import commons.gui.thread.AbstractGUIUncaughtExceptionHandler;
import commons.gui.widget.dialog.ValidationMessageDialog;

public class ArcoIrisUIUncaughtExceptionHandler extends AbstractGUIUncaughtExceptionHandler {

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
