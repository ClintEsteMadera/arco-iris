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
 * $Id: BackgroundThread.java,v 1.5 2008/01/18 19:33:42 cvschioc Exp $
 */
package commons.gui.thread;


import org.eclipse.swt.widgets.Display;

/**
 *
 * @author Gabriel Tursi
 * @version $Revision: 1.5 $ $Date: 2008/01/18 19:33:42 $
 */
public abstract class BackgroundThread extends Thread {

	public BackgroundThread(Display display){
		super();
		this.display = display;
		this.setUncaughtExceptionHandler(GUIUncaughtExceptionHandler.getInstance());
	}

	@Override
	public final void run(){
		this.instance = this;
		performBackgroundOperation();
		display.asyncExec(runnable);
	}
	
	protected abstract void performBackgroundOperation();

	protected abstract void updateUI();
	
	private final Runnable runnable = new Runnable() {

		public void run() {
			BackgroundThread.this.instance.updateUI();
		}
	};
	
	private final Display display;

	private BackgroundThread instance;
}

