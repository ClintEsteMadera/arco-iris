package ar.uba.dc.thesis.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class provides a similar funcionality as some methods provided by {@link java.util.Collections} but using
 * instances of the Java Standard Collections API instead of obscure inner classes that are not usually handled by some
 * frameworks as, for instance, XStream.
 * 
 */
public final class Collections {

	/**
	 * This class is not intended to be subclassed.
	 */
	private Collections() {
		super();
	}

	/**
	 * Returns an {@link ArrayList} with the elements passed as parameter.<br>
	 * This avoids the boiler plate code of having to instantiate the list first and write one line of code for each
	 * element we want to add.
	 * 
	 * @param element
	 *            the elements to be stored in the returned list.
	 * @return an instance of {@link ArrayList} containing the specified objects.
	 */
	public static <T> List<T> createList(T... elements) {
		List<T> list = new ArrayList<T>(elements.length);
		for (T element : elements) {
			list.add(element);
		}
		return list;
	}

	/**
	 * Returns an {@link ArrayList} with the elements passed as parameter.<br>
	 * This avoids the boiler plate code of having to instantiate the list first and write one line of code for each
	 * element we want to add.
	 * 
	 * @param element
	 *            the elements to be stored in the returned list.
	 * @return an instance of {@link ArrayList} containing the specified objects.
	 */
	public static <T> Set<T> createSet(T... elements) {
		Set<T> list = new HashSet<T>(elements.length);
		for (T element : elements) {
			list.add(element);
		}
		return list;
	}
}
