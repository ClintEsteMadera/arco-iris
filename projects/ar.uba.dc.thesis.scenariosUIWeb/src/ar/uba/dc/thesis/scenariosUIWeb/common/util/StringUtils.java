/*
 * $Id: StringUtils.java,v 1.6 2009/04/30 18:37:13 cvsuribe Exp $
 *
 * Licencia de Caja de Valores S.A., Versión 1.0
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */
package ar.uba.dc.thesis.scenariosUIWeb.common.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Utilidades para la manipulación de Strings en forma eficiente, y operaciones que complementan el
 * API de la clase {@link String}.
 * @author H. Adrián Uribe
 * @version $Revision: 1.6 $ $Date: 2009/04/30 18:37:13 $
 */
public final class StringUtils {

	/**
	 * Concatena varios Strings de la forma más eficiente posible.
	 * @param strings Strings a concatenar.
	 * @return String concatenado.
	 */
	public static String concat(String... strings) {
		int len = 0;
		for (String str : strings) {
			len += str == null ? NULL_LENGTH : str.length();
		}

		StringBuilder sb = new StringBuilder(len);
		for (String str : strings) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static String trim(String str) {
		String result = null;
		if (str != null) {
			str = str.trim();
			result = str.length() == 0 ? null : str;
		}
		return result;
	}

	public static String rtrim(String str) {
		String result = null;
		if (str != null) {
			int last = str.length() - 1;
			int index = last;
			while (index >= 0 && str.charAt(index) <= ' ') {
				--index;
			}

			if (index == last) {
				result = str;
			} else {
				result = index < 0 ? "" : str.substring(0, index + 1);
			}
		}
		return result;
	}

	/**
	 * Comprime secuencias de 1 ó más de caracteres de espaciado en un carácter de espacio.
	 * @param str Valor a compactar.
	 * @return Valor compactado.
	 */
	public static String squeeze(String str) {
		String result = null;
		if (str != null) {
			// To-Do: Optimize internal StringBuilder allocation
			result = SQUEEZE_PATTERN.matcher(str).replaceAll(" ");
		}
		return result;
	}

	/**
	 * Comprime secuencias de 1 ó más de caracteres de espaciado en un carácter de espacio, o las
	 * suprime en caso de encontrarse entre 2 caracteres que no sean ambos alfanuméricos.
	 * @param str Valor a compactar.
	 * @return Valor compactado.
	 */
	public static String squeezeFully(String str) {
		String result = null;
		if (str != null) {
			// To-Do: Optimize internal StringBuilder allocation
			str = SQUEEZE_PATTERN.matcher(str).replaceAll(" ");
			for (Pattern element : SQUEEZE_FULLY_PATTERNS) {
				str = element.matcher(str).replaceAll("$1");
			}
			result = str;
		}
		return result;
	}

	public static String toString(Class<?> clss, Object... attrValPairs) {
		String className = null;
		int len = 0;
		if (clss != null) {
			className = clss.getName();
			len = className.length() + ATTR_BEGIN.length() + ATTR_END.length();
		}

		int index = attrValPairs.length / 2;
		len += (index - 1) * ATTR_SEPARATOR.length();
		len += index * ATTR_VALUE_SEPARATOR.length();

		Object val;
		for (index = 0; index < attrValPairs.length; index++) {
			val = attrValPairs[index];
			if (val == null) {
				len += NULL_LENGTH;
			} else {
				if (val instanceof Collection<?>) {
					val = toString((Collection<?>) val);
				} else if (val.getClass().isArray()) {
					val = toString((Object[]) val);
				} else {
					val = toString(val);
				}
				len += ((String) val).length();
				attrValPairs[index] = val;
			}
		}

		StringBuilder sb = new StringBuilder(len);
		if (className != null) {
			sb.append(className);
			sb.append(ATTR_BEGIN);
		}
		for (index = 0; index < attrValPairs.length; index += 2) {
			if (index != 0) {
				sb.append(ATTR_SEPARATOR);
			}
			sb.append(attrValPairs[index]);
			sb.append(ATTR_VALUE_SEPARATOR);
			sb.append(attrValPairs[index + 1]);
		}
		if (className != null) {
			sb.append(ATTR_END);
		}
		assert sb.length() == len;
		return sb.toString();
	}

	public static String toString(Object[] array) {
		String result = null;
		if (array != null) {
			if (array.length == 0) {
				result = ARR_EMPTY;
			} else {
				int len =
				        ARR_BEGIN.length() + ARR_END.length() + (array.length - 1)
				                * ARR_SEPARATOR.length();

				Object val;
				Object[] strArray = new String[array.length];
				int index;
				for (index = 0; index < array.length; index++) {
					val = array[index];
					if (val == null) {
						len += NULL_LENGTH;
					} else {
						if (val instanceof Collection<?>) {
							val = toString((Collection<?>) val);
						} else if (val.getClass().isArray()) {
							val = toString((Object[]) val);
						} else {
							val = toString(val);
						}
						len += ((String) val).length();
						strArray[index] = val;
					}
				}

				StringBuilder sb = new StringBuilder(len);
				sb.append(ARR_BEGIN);
				for (index = 0; index < strArray.length; index++) {
					if (index != 0) {
						sb.append(ARR_SEPARATOR);
					}
					sb.append(strArray[index]);
				}
				sb.append(ARR_END);
				assert sb.length() == len : sb;
				result = sb.toString();
			}
		}
		return result;
	}

	public static String toString(Collection<?> collection) {
		String result = null;
		if (collection != null) {
			if (collection.isEmpty()) {
				result = ARR_EMPTY;
			} else {
				int len =
				        ARR_BEGIN.length() + ARR_END.length() + (collection.size() - 1)
				                * ARR_SEPARATOR.length();

				Object val;
				Object[] strArray = new String[collection.size()];
				Iterator<?> iterator = collection.iterator();
				int index;
				for (index = 0; index < strArray.length; index++) {
					val = iterator.next();
					if (val == null) {
						len += NULL_LENGTH;
					} else {
						if (val instanceof Collection<?>) {
							val = toString((Collection<?>) val);
						} else if (val.getClass().isArray()) {
							val = toString((Object[]) val);
						} else {
							val = toString(val);
						}
						len += ((String) val).length();
						strArray[index] = val;
					}
				}

				StringBuilder sb = new StringBuilder(len);
				sb.append(ARR_BEGIN);
				for (index = 0; index < strArray.length; index++) {
					if (index != 0) {
						sb.append(ARR_SEPARATOR);
					}
					sb.append(strArray[index]);
				}
				sb.append(ARR_END);
				assert sb.length() == len;
				result = sb.toString();
			}
		}
		return result;
	}

	public static String toString(Object obj) {
		if (obj instanceof Date) {
			return DateUtils.toString((Date) obj);
		}
		if (obj instanceof Calendar) {
			return DateUtils.toString((Calendar) obj);
		}
		return String.valueOf(obj);
	}

	private static final String ATTR_BEGIN = ":{";

	private static final String ATTR_SEPARATOR = "; ";

	private static final String ATTR_VALUE_SEPARATOR = "=";

	private static final String ATTR_END = "}";

	private static final String ARR_BEGIN = "[";

	private static final String ARR_SEPARATOR = ", ";

	private static final String ARR_END = "]";

	private static final String ARR_EMPTY = "[]";

	// Determines the amount of space used by StringBuilder.append(null);
	private static final int NULL_LENGTH = new StringBuilder("").append((String) null).length();

	private static final Pattern SQUEEZE_PATTERN = Pattern.compile("\\s+", Pattern.DOTALL);

	private static final Pattern[] SQUEEZE_FULLY_PATTERNS =
	        { Pattern.compile("(\\W) ", Pattern.DOTALL), Pattern.compile(" (\\W)", Pattern.DOTALL), };
}
