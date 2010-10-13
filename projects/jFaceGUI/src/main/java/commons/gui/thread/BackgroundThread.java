package commons.gui.thread;

import org.eclipse.swt.widgets.Display;

public abstract class BackgroundThread extends Thread {

	private final Display display;

	private BackgroundThread instance;

	private final Runnable updateUIRunnable = new Runnable() {

		public void run() {
			BackgroundThread.this.instance.updateUI();
		}
	};

	public BackgroundThread(Display display) {
		super();
		this.display = display;
		this.setUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler());
	}

	@Override
	public final void run() {
		this.instance = this;
		performBackgroundOperation();
		display.asyncExec(this.updateUIRunnable);
	}

	protected abstract void performBackgroundOperation();

	protected abstract void updateUI();
}
