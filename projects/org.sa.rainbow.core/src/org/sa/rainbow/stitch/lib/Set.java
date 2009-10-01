/**
 * Created April 27, 2007.
 */
package org.sa.rainbow.stitch.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * This utility class provides useful Set operators for set manipulation.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class Set {

	/**
	 * Returns the size of the provided set.
	 * @param set  the set whose size to query
	 * @return int  the count of items in {@code set}
	 */
	public static int size (java.util.Set<?> set) {
		return set.size();
	}

	/**
	 * Returns the union of the two given sets.
	 * @param <E>   the common type of the Set Collection
	 * @param set1  the first set to include in the union
	 * @param set2  the second set to include in the union
	 * @return java.util.Set<E>  the set resulting from the union
	 */
	public static <E> java.util.Set<E> union (java.util.Set<E> set1, java.util.Set<E> set2) {
		java.util.Set<E> newSet = new HashSet<E>(set1);
		newSet.addAll(set2);
		return newSet;
	}

	/**
	 * Returns the difference subset of {@code mainset} when the members of
	 * {@code subtrahend} are removed.
	 * @param <E>         the common type of the Set Collection
	 * @param mainset     the "source" set
	 * @param subtrahend  the set to remove from the source set
	 * @return java.util.Set<E>  the subset that results from the set operation
	 *   (mainset / subtrahends)
	 */
	public static <E> java.util.Set<E> diff (java.util.Set<E> mainset, java.util.Set<E> subtrahend) {
		java.util.Set<E> resultSet = new HashSet<E>();
		for (E e : mainset) {
			if (! subtrahend.contains(e)) {
				resultSet.add(e);
			}
		}
		return resultSet;
	}

	/**
	 * Returns a random subset of size {@code count} from the given set.
	 * This algorithm puts the supplied set into a list, permutes the list
	 * randomly using Collections.shuffle, then returns the first {@code count}
	 * elements of that list as a set.
	 * @param <E>    the common type of the Set Collection
	 * @param set    the set to pick randomly from
	 * @param count  the number of elements to randomly draw
	 * @return java.util.Set<E>  the resulting random subset
	 */
	public static <E> java.util.Set<E> randomSubset (java.util.Set<E> set, int count) {
		List<E> itemList = new ArrayList<E>(set);
		Collections.shuffle(itemList);
		return new HashSet<E>(itemList.subList(0, count));
	}

}
