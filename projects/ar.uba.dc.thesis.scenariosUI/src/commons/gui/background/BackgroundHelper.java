package commons.gui.background;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jface.window.ApplicationWindow;

import commons.gui.util.PageHelper;

/**
 * Helper para la creacion de tareas background
 * 
 * 
 */

public class BackgroundHelper {

	/**
	 * Constructor
	 * 
	 * @param target
	 *            Objeto que provee los callbacks asincrónicos.
	 */
	public BackgroundHelper() {
		this.window = PageHelper.getMainWindow();
	}

	public BackgroundTask createTask(String methodName, Object target, Class[] parameterTypes) {
		BackgroundTask task = new BackgroundTask(window);

		Method method = null;

		try {
			method = target.getClass().getMethod(methodName, parameterTypes);
		} catch (Exception ex) {
			throw new RuntimeException("Error al obtener el método " + methodName, ex);
		}

		task.addRunningListener(new GenericListenerAdapter(target, method));

		return task;
	}

	public BackgroundTask createTask(String methodName, Object target) {
		BackgroundTask task = new BackgroundTask(window);

		Method[] methods = target.getClass().getMethods();
		Method targetMethod = null;

		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				targetMethod = m;
				break;
			}
		}

		if (targetMethod == null) {
			throw new IllegalArgumentException("No está definido el método '" + methodName + "' para la clase "
					+ target.getClass().getName());
		}

		task.addRunningListener(new GenericListenerAdapter(target, targetMethod));

		return task;
	}

	public Object run(Object target, String method, Object... args) {
		final BackgroundTask task = createTask(method, target);
		return task.execute(args);
	}

	public Object run(Object target, String method, Class[] parameterTypes, Object... args) {
		final BackgroundTask task = createTask(method, target, parameterTypes);
		return task.execute(args);
	}

	public Object runWithListener(BackgroundRunningListener listener, Object... args) {
		final BackgroundTask task = new BackgroundTask(window);
		task.addRunningListener(listener);
		return task.execute(args);
	}

	private static RuntimeException getRuntimeException(Method method, Exception ex) {
		Throwable cause = ex;

		if (ex instanceof InvocationTargetException && ex.getCause() != null) {
			cause = ex.getCause();
		}

		StringBuffer message = new StringBuffer("Error invocando el método ");
		message.append(method.getName());
		message.append(" : ");
		message.append(cause.getMessage());

		return new RuntimeException(message.toString(), cause);
	}

	private ApplicationWindow window;

	private class GenericListenerAdapter implements BackgroundRunningListener {

		public GenericListenerAdapter(Object target, Method method) {
			this.method = method;
			this.target = target;
		}

		public void backgroundRunning(BackgroundRunningEvent ev) throws Throwable {
			try {
				ev.setResult(method.invoke(target, ev.getArguments()));
			} catch (IllegalAccessException ex) {
				throw getRuntimeException(method, ex);
			} catch (InvocationTargetException ex) {
				if (ex.getCause() != null) {
					throw ex.getCause();
				}
			}
		}

		private Method method;

		private Object target;
	}
}
