package commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Clonator {

	private static final String CLONE_METHOD_NAME = "clone";

	private Clonator() {
		super();
	}

	/**
	 * Clones an object according to the following criteria:<br>
	 * <li>If the object implements {@link java.lang.Cloneable Cloneable}, it invokes {@link Cloneable#clone()} using
	 * reflection.</li> <li>If not but in turn it implements {@link java.io.Serializable}, this method tries to clonate
	 * the object by serializing and deserializing it using the standard Serialization mechanism.<li>.
	 * 
	 * @throws CloneNotSupportedException
	 *             if neither of the above steps could be performed.
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
			throw new IllegalArgumentException("Error when cloning object: the class " + o.getClass().getName()
					+ " does not implement Serializable");
		} catch (CloneNotSupportedException e) {
			throw new IllegalArgumentException("Error when cloning object: " + e.getMessage(), e);
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
}