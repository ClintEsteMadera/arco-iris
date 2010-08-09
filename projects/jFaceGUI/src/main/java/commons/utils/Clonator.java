package commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Clonador de objetos.
 */

public class Clonator {

	private Clonator() {
		super();
	}

	/**
	 * Clona un objeto de acuerdo a los siguientes pasos:
	 * <li>Si implementa {@link java.lang.Cloneable Cloneable}, invoca el método <code>clone()</code> utilizando
	 * reflection.</li>
	 * <li>Si implementa {@link java.io.Serializable Serializable}, invoca el método
	 * {@link sbaui.util.SerializableClonator#clone(Object) SerializableClonator.clone(Object}
	 * <li>.
	 * 
	 * @throws CloneNotSupportedException
	 *             si no se pudo realizar ninguno de los pasos anteriores.
	 */
	public static <T> T clone(T o) {
		if (o == null) {
			return null;
		}

		try {
			if (Cloneable.class.isAssignableFrom(o.getClass()))
				return tryCloneable(o);
		} catch (CloneNotSupportedException e) {
			// do nothing
		}

		try {
			if (Serializable.class.isAssignableFrom(o.getClass())) {
				return tryCloneSerializable(o);
			}
			throw new IllegalArgumentException("Error clonando el objeto: el objeto no es serializable");
		} catch (CloneNotSupportedException e) {
			throw new IllegalArgumentException("Error clonando el objeto: " + e.getMessage(), e);
		}
	}

	private static <T> T tryCloneSerializable(T o) throws CloneNotSupportedException {
		try {
			return cloneBySerialization(o);
		} catch (Exception e) {
			CloneNotSupportedException cE = new CloneNotSupportedException();
			cE.initCause(e);
			throw cE;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T tryCloneable(T o) throws CloneNotSupportedException {
		try {
			return (T) o.getClass().getMethod(CLONE_METHOD_NAME, (Class[]) null).invoke(o, (T[]) null);
		} catch (Exception e) {
			throw new CloneNotSupportedException();
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T cloneBySerialization(T value) {
		try {
			ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outBuffer);
			out.writeObject(value);

			ByteArrayInputStream inBuffer = new ByteArrayInputStream(outBuffer.toByteArray());
			ObjectInputStream in = new ObjectInputStream(inBuffer);
			return (T) in.readObject();
		} catch (NotSerializableException nsE) {
			throw new IllegalArgumentException(nsE.toString());
		} catch (ClassNotFoundException cE) {
			throw new UnsupportedOperationException(cE.getMessage());
		} catch (IOException ioE) {
			throw new UnsupportedOperationException(ioE.getMessage());
		}
	}

	private static final String CLONE_METHOD_NAME = "clone";
}