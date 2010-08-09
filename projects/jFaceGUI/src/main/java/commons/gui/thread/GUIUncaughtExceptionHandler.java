package commons.gui.thread;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;

import commons.exception.ValidationException;
import commons.gui.background.BackgroundInvocationException;
import commons.gui.model.validation.ValidationSource;
import commons.gui.util.PageHelper;
import commons.gui.widget.dialog.InternalErrorDialog;
import commons.gui.widget.dialog.ValidationMessageDialog;

/**
 * Clase que maneja las excepciones no chequeadas en la GUI.
 * 
 */
public class GUIUncaughtExceptionHandler implements UncaughtExceptionHandler {

	/**
	 * Constructor Privado (singleton)
	 */
	private GUIUncaughtExceptionHandler() {
		super();
	}

	public static GUIUncaughtExceptionHandler getInstance() {
		return instance;
	}

	public ValidationSource getValidationSource() {
		return validationSource;
	}

	public void setValidationSource(ValidationSource validationSource) {
		this.validationSource = validationSource;
	}

	public synchronized void uncaughtException(Thread thread, Throwable throwable) {

		if (throwable instanceof BackgroundInvocationException && throwable.getCause() != null) {
			throwable = throwable.getCause();
		}

		if (throwable instanceof ValidationException) {
			final ValidationException validationException = (ValidationException) throwable;

			if (this.validationSource != null) {
				this.validationSource.setValidationErrors(validationException.getErrors());
			}

			ValidationMessageDialog.open(null, validationException.getValidationMessages());
		} else {
			try {
				log.fatal(throwable.getMessage(), throwable);
				final String localizedMessage = StringUtils.isBlank(throwable.getLocalizedMessage()) ? DEFAULT_ERROR_MESSAGE
						: throwable.getLocalizedMessage();

				final Throwable dialogException = throwable;

				Runnable runnable = new Runnable() {
					public void run() {
						if (PageHelper.getMainWindow().getSystemConfiguration().stackTraceEnErrores()) {
							InternalErrorDialog.openError(null, "Error", localizedMessage, dialogException);
						} else {
							MessageDialog.openError(null, "Error", localizedMessage);
						}
					}
				};

				PageHelper.getDisplay().syncExec(runnable);

			} catch (Exception e) {
				System.err.print((new StringBuilder()).append("Exception in thread \"").append(thread.getName())
						.append("\" ").toString());
				e.printStackTrace(System.err);
			}

		}
	}

	private ValidationSource validationSource;

	private static final String DEFAULT_ERROR_MESSAGE = "Ha ocurrido un error en la aplicación.";

	/**
	 * Única instancia de esta clase (singleton).
	 */
	private static GUIUncaughtExceptionHandler instance = new GUIUncaughtExceptionHandler();

	private static final Log log = LogFactory.getLog(GUIUncaughtExceptionHandler.class);
}