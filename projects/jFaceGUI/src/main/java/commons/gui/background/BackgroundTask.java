package commons.gui.background;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.ApplicationWindow;

/**
 * Tarea que se ejecuta en background
 * 
 * 
 */
public class BackgroundTask {

	public BackgroundTask(ApplicationWindow window) {
		super();
		this.runningListeners = new CopyOnWriteArrayList<BackgroundRunningListener>();
		this.finishedListeners = new CopyOnWriteArrayList<BackgroundFinishedListener>();
		this.window = window;
		this.handleExceptions = true;
	}

	/**
	 * Ejecuta la tarea.
	 */
	public Object execute(Object[] arguments) {
		try {
			window.run(true, false, getBackgroundRunnable(arguments));

			if (this.getLastResult().getError() != null) {
				throw new BackgroundInvocationException(this.getLastResult().getError());
			}

			return this.getLastResult().getReturnedObject();
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isHandleExceptions() {
		return handleExceptions;
	}

	public void setHandleExceptions(boolean handleExceptions) {
		this.handleExceptions = handleExceptions;
	}

	/**
	 * Agrega un listener de ejecucion (se invoca en un thread independiente)
	 * 
	 * @param listener
	 */
	public void addRunningListener(BackgroundRunningListener listener) {
		this.runningListeners.add(listener);
	}

	/**
	 * Elimina un listener de ejecucion
	 * 
	 * @param listener
	 */
	public void removeRunningListener(BackgroundRunningListener listener) {
		this.runningListeners.remove(listener);
	}

	/**
	 * Agrega un listener de finalizacion (se ejecuta en el thread de la GUI)
	 * 
	 * @param listener
	 */
	public void addFinishedListener(BackgroundFinishedListener listener) {
		this.finishedListeners.add(listener);
	}

	/**
	 * Elimina un listener de finalizacion
	 * 
	 * @param listener
	 */
	public void removeFinishedListener(BackgroundFinishedListener listener) {
		this.finishedListeners.remove(listener);
	}

	public InvocationResult getLastResult() {
		return lastResult;
	}

	private void performBackground(Object[] arguments, IProgressMonitor monitor) {
		final BackgroundRunningEvent ev = new BackgroundRunningEvent(this, arguments);

		monitor.beginTask("Invocando el servicio...", IProgressMonitor.UNKNOWN);

		Throwable error = null;

		for (BackgroundRunningListener listener : this.runningListeners) {
			try {
				listener.backgroundRunning(ev);
			} catch (Throwable e) {
				error = e;
			}
		}

		monitor.done();

		this.lastResult = new InvocationResult(ev.getResult(), error);

		window.getShell().getDisplay().asyncExec(getUpdateGUIRunnable(this.lastResult));
	}

	private void performUpdateGUI(final InvocationResult result) {
		final BackgroundFinishedEvent ev = new BackgroundFinishedEvent(this, result.returnedObject, result.error);

		for (BackgroundFinishedListener listener : BackgroundTask.this.finishedListeners) {
			listener.backgroundFinished(ev);
		}
	};

	private IRunnableWithProgress getBackgroundRunnable(final Object[] arguments) {
		final IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				BackgroundTask.this.performBackground(arguments, monitor);
			}
		};

		return runnable;
	};

	private Runnable getUpdateGUIRunnable(final InvocationResult result) {
		return new Runnable() {
			public void run() {
				BackgroundTask.this.performUpdateGUI(result);
			}
		};
	}

	private boolean handleExceptions;

	private final ApplicationWindow window;

	private CopyOnWriteArrayList<BackgroundRunningListener> runningListeners;

	private CopyOnWriteArrayList<BackgroundFinishedListener> finishedListeners;

	private InvocationResult lastResult;
}