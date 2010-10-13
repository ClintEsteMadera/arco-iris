package commons.gui.thread;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;

import commons.gui.background.BackgroundInvocationException;
import commons.gui.util.PageHelper;
import commons.gui.widget.dialog.InternalErrorDialog;

/**
 * This class handles all of the not checked exceptions raised within the GUI.
 * 
 */
public abstract class AbstractGUIUncaughtExceptionHandler implements UncaughtExceptionHandler {

	private static final String DEFAULT_ERROR_MESSAGE = "An error has occurred. See error log for more details.";

	private static final Log log = LogFactory.getLog(AbstractGUIUncaughtExceptionHandler.class);

	public AbstractGUIUncaughtExceptionHandler() {
		super();
	}

	public final synchronized void uncaughtException(Thread thread, Throwable throwable) {

		if (throwable instanceof BackgroundInvocationException && throwable.getCause() != null) {
			throwable = throwable.getCause();
		}
		if (this.isKnownException(throwable)) {
			this.handleKnownException(throwable);
		} else {
			this.handleUnknownThrowable(thread, throwable);
		}
	}

	protected abstract boolean isKnownException(Throwable throwable);

	protected abstract void handleKnownException(Throwable throwable);

	protected void handleUnknownThrowable(Thread thread, Throwable throwable) {
		try {
			log.fatal(throwable.getMessage(), throwable);
			final String localizedMessage = StringUtils.isBlank(throwable.getLocalizedMessage()) ? DEFAULT_ERROR_MESSAGE
					: throwable.getLocalizedMessage();

			final Throwable dialogException = throwable;

			Runnable runnable = new Runnable() {
				public void run() {
					if (PageHelper.getMainWindow().getSystemConfiguration().showStackTraceForErrors()) {
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