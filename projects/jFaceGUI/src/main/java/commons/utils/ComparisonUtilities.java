package commons.utils;

import java.util.Comparator;

/**
 * @author P.Pastorino
 */
public class ComparisonUtilities {

	/**
	 * Verifica la igualdad de ods objetos.
	 * 
	 * @param o1
	 * @param o2
	 * @return <code>(o1 == o2) || (o1 != null && o1.equals(o2))</code>
	 */
	public static boolean equals(Object o1, Object o2) {
		return (o1 == o2) || (o1 != null && o1.equals(o2));
	}

	/**
	 * Verifica la igualdad de ods objetos.
	 * 
	 * @param o1
	 * @param o2
	 * @return <code>o2 == null || equals(o1,o2)</code>
	 */
	public static boolean equalsOrNull(Object o1, Object o2) {
		return o2 == null || equals(o1, o2);
	}

	/**
	 * Verifica la pertenencia a un rango. Retorna <code>true</code> si <code>value &gt= from && value &lt=
	 * to</code><br>
	 * En caso de que <code>from</code> o <code>to</code> sean nulos la comparacion por &gt= o &lt= es siempre
	 * verdadera (es decir que se asumen los valores "- infinto" y "+infinito" respectivamente).<br>
	 * En caso de que <code>value</code> sea nulo retirna <code>true</code> solo si <code>from</code> y
	 * <code>to</code> son nulos.
	 * 
	 * @param comp
	 * @param value
	 * @param from
	 * @param to
	 * @return boolean
	 */
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

	/**
	 * Comparador que retorna 0 si dos elementos son iguales o 1 en otro caso.
	 */
	public static final Comparator EQUALS_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			return ComparisonUtilities.equals(o1, o2) ? 0 : 1;
		}
	};

}
