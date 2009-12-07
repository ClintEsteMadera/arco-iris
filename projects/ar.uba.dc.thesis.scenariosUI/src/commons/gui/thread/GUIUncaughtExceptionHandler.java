/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: GUIUncaughtExceptionHandler.java,v 1.13 2008/05/15 20:53:30 cvspasto Exp $
 */
package commons.gui.thread;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;

import sba.common.exception.ValidationException;

import commons.gui.background.BackgroundInvocationException;
import commons.gui.model.validation.ValidationSource;
import commons.gui.util.PageHelper;
import commons.gui.widget.dialog.InternalErrorDialog;
import commons.gui.widget.dialog.ValidationMessageDialog;

/**
 * Clase que maneja las excepciones no chequeadas en la GUI.
 * @author Gabriel Tursi
 * @version $Revision: 1.13 $ $Date: 2008/05/15 20:53:30 $
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
		
		if(throwable instanceof ValidationException) {
			final ValidationException validationException = (ValidationException) throwable;
			
			if(this.validationSource != null){
				this.validationSource.setValidationErrors(validationException.getErrors());
			}

			ValidationMessageDialog.open(null, validationException.getValidationMessages());
		} else {
			try {
				log.fatal(throwable.getMessage(), throwable);
				final String localizedMessage = StringUtils.isBlank(throwable.getLocalizedMessage())
						? DEFAULT_ERROR_MESSAGE
						: throwable.getLocalizedMessage();

				final Throwable dialogException = throwable;
				
				Runnable runnable = new Runnable() {
					public void run() {
						if (PageHelper.getMainWindow().getSystemConfiguration()
								.stackTraceEnErrores()) {
							InternalErrorDialog.openError(null, "Error", localizedMessage,
									dialogException);
						} else {
							MessageDialog.openError(null, "Error", localizedMessage);
						}
					}
				};

				PageHelper.getDisplay().syncExec(runnable);

			} catch (Exception e) {
				System.err.print((new StringBuilder()).append("Exception in thread \"").append(
						thread.getName()).append("\" ").toString());
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