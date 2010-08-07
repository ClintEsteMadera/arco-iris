package commons.gui.thread;

import org.eclipse.swt.widgets.Display;

/**
 * 
 * 
 */
public abstract class BackgroundThread extends Thread {

	public BackgroundThread(Display display) {
		super();
		this.display = display;
		this.setUncaughtExceptionHandler(GUIUncaughtExceptionHandler.getInstance());
	}

	@Override
	public final void run() {
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
