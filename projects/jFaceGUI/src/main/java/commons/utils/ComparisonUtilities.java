package commons.utils;

import java.util.Comparator;

public class ComparisonUtilities {

	public static final Comparator<Comparable> DEFAULT_COMPARATOR = new Comparator<Comparable>() {
		@SuppressWarnings("unchecked")
		public int compare(Comparable o1, Comparable o2) {
			if (o1 != null) {
				return o1.compareTo(o2);
			} else if (o2 != null) {
				return o2.compareTo(o1);
			}
			return 0;
		}
	};

	public static final Comparator EQUALS_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ComparisonUtilities.equals(o1, o2) ? 0 : 1;
		}
	};

	/**
	 * @return <code>(o1 == o2) || (o1 != null && o1.equals(o2))</code>
	 */
	public static boolean equals(Object o1, Object o2) {
		return (o1 == o2) || (o1 != null && o1.equals(o2));
	}

	/**
	 * @return <code>o2 == null || equals(o1,o2)</code>
	 */
	public static boolean equalsOrNull(Object o1, Object o2) {
		return o2 == null || equals(o1, o2);
	}

	public static <T> boolean inRange(Comparator<T> comp, T value, T from, T to) {
		if (value == null) {
			return from == null && to == null;
		}
		return (from == null || comp.compare(value, from) >= 0) && (to == null || comp.compare(value, to) <= 0);
	}

	public static <T> boolean inRange(Comparable<T> value, T from, T to) {
		if (value == null) {
			return from == null && to == null;
		}
		return (from == null || value.compareTo(from) >= 0) && (to == null || value.compareTo(to) <= 0);
	}

	public static boolean inArray(Object value, Object[] array) {
		if (array == null) {
			return value == null;
		}
		for (int i = 0; i < array.length; i++) {
			if (equals(value, array[i])) {
				return true;
			}
		}
		return false;
	}

	public static boolean like(String value, String expr) {
		if (expr == null) {
			return true;
		}
		if (value == null) {
			return false;
		}
		return value.toLowerCase().indexOf(expr.toLowerCase()) >= 0;
	}
}
