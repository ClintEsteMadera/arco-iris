package commons.utils;

/**
 * Clase de utileria para conversiones de tipos.
 * 
 * @author P.Pastorino
 */
public class ConversionUtilities {

	public static Character toCharacter(Object value) {
		if (value != null) {
			if (value instanceof Character) {
				return (Character) value;
			}
			final String s = value.toString();
			if (s.length() > 0)
				return new Character(s.charAt(0));
			return new Character(Character.MIN_VALUE);
		}
		return null;
	}

	public static Number toNumber(Object value) {
		if (value != null) {
			if (value instanceof Number) {
				return (Number) value;
			}
			return Double.valueOf(value.toString());
		}
		return null;
	}

	public static Boolean toBoolean(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof String) {
			final String s = (String) value;
			if (s.equals("true") || s.equals("yes") || s.equals("si")) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue() != 0 ? Boolean.TRUE : Boolean.FALSE;
		}
		return Boolean.valueOf(value.toString());
	}

	public static boolean toBoolean(Object value, boolean defaultValue) {
		Boolean ret = toBoolean(value);
		return ret != null ? ret.booleanValue() : defaultValue;
	}

	public static long toLong(Object value, long defaultValue) {
		Number ret = toNumber(value);
		return ret != null ? ret.longValue() : defaultValue;
	}

	public static int toInt(Object value, int defaultValue) {
		Number ret = toNumber(value);
		return ret != null ? ret.intValue() : defaultValue;
	}

	public static char toChar(Object value, char defaultValue) {
		Character ret = toCharacter(value);
		return ret != null ? ret.charValue() : defaultValue;
	}

	public static Object toClass(Object value, Class<?> targetClass) {
		if (targetClass.isAssignableFrom(Integer.class)) {
			return new Integer(toInt(value, 0));
		} else if (targetClass.isAssignableFrom(Long.class)) {
			return new Long(toLong(value, 0));
		} else if (targetClass.isAssignableFrom(Character.class)) {
			return new Character(toChar(value, (char) 0));
		} else if (targetClass.isAssignableFrom(Boolean.class)) {
			return Boolean.valueOf(toBoolean(value, false));
		} else if (targetClass.isAssignableFrom(String.class)) {
			return value.toString();
		}
		return null;
	}
}
