package commons.gui.model.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import commons.validation.NumericConstraints;

/**
 * Parametros para la edicion de numeros.
 * 
 * @author P.Pastorino
 */
public class NumberEditParameters implements Cloneable {

	public boolean grouping = DEFAULT_GROUPING;

	public int maxIntDigits = DEFAULT_MAX_INT_DIGITS;

	public int maxFractionDigits = DEFAULT_MAX_FRACTION_DIGITS;

	public int minFractionDigits = DEFAULT_MIN_FRACTION_DIGITS;

	public boolean allowsNegative = DEFAULT_ALLOWS_NEGATIVE;

	public Number prototype;

	public Number columnPrototype;

	@Override
	public String toString() {
		return new StringBuffer(getClass().getName()).append(" [grouping=").append(grouping).append(" maxIntDigits=")
				.append(maxIntDigits).append(" maxFractionDigits=").append(maxFractionDigits).append(
						" minFractionDigits=").append(minFractionDigits).append(" allowsNegative=").append(
						allowsNegative).append(" prototype=").append(prototype).append("]").toString();
	}

	public NumberEditParameters(NumericConstraints c) {
		this.maxIntDigits = c.getIntDigits();
		this.allowsNegative = c.getMin() == null || c.getMin().doubleValue() < 0;
		this.maxFractionDigits = c.getPrecision();
	}

	public NumberEditParameters() {
	}

	public NumberEditParameters(int maxIntDigits) {
		this.maxIntDigits = maxIntDigits;
		this.maxFractionDigits = 0;
	}

	public NumberEditParameters(int maxIntDigits, int maxFractionDigits) {
		this.maxIntDigits = maxIntDigits;
		this.maxFractionDigits = maxFractionDigits;
		this.minFractionDigits = maxFractionDigits;
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new UnsupportedOperationException(e.getMessage());
		}
	}

	public void copy(NumberEditParameters p) {
		this.grouping = p.grouping;
		this.maxIntDigits = p.maxIntDigits;
		this.maxFractionDigits = p.maxFractionDigits;
		this.minFractionDigits = p.minFractionDigits;
		this.allowsNegative = p.allowsNegative;
		this.prototype = p.prototype;
		this.columnPrototype = p.columnPrototype;
	}

	public NumberEditParameters(NumberEditParameters p) {
		copy(p);
	}

	public Number getPrototype() {
		if (prototype != null) {
			return prototype;
		}

		final String fraction = maxFractionDigits == 0 ? "" : maxFractionDigits < 0 ? FRACTION : FRACTION.substring(0,
				maxFractionDigits + 1);

		String integer;
		if (allowsNegative) {
			integer = maxIntDigits < 0 ? NEGATIVE_INT : NEGATIVE_INT.substring(0, maxIntDigits + 1);
		} else {
			integer = maxIntDigits < 0 ? POSITIVE_INT : POSITIVE_INT.substring(0, maxIntDigits);
		}
		return new BigDecimal(integer + fraction);
	}

	public DecimalFormat getDecimalFormat(Class numberClass) {

		final boolean isInteger = isInteger(numberClass);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();

		if (!DEFAULT_DECIMAL_SEPARATOR_COMMA) {
			symbols.setDecimalSeparator('.');
			symbols.setGroupingSeparator(',');
		} else {
			symbols.setDecimalSeparator(',');
			symbols.setGroupingSeparator('.');
		}

		DecimalFormat format = new DecimalFormat("", symbols);

		if (isInteger) {
			format.setDecimalSeparatorAlwaysShown(false);
			format.setParseIntegerOnly(true);
			format.setMaximumFractionDigits(0);
		} else {
			if (maxFractionDigits >= 0) {
				format.setMaximumFractionDigits(maxFractionDigits);
			}
			if (minFractionDigits >= 0) {
				format.setMinimumFractionDigits(minFractionDigits);
			}
		}

		format.setGroupingUsed(grouping);
		format.setMinimumIntegerDigits(1);

		if (numberClass.equals(BigDecimal.class)) {
			format.setParseBigDecimal(true);
		}

		return format;
	}

	public DecimalFormat getExportableFormat(Class numberClass) {

		final boolean isInteger = isInteger(numberClass);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();

		symbols.setDecimalSeparator('.');
		DecimalFormat format = new DecimalFormat("", symbols);

		if (isInteger) {
			format.setDecimalSeparatorAlwaysShown(false);
			format.setParseIntegerOnly(true);
			format.setMaximumFractionDigits(0);
		} else {
			if (maxFractionDigits >= 0) {
				format.setMaximumFractionDigits(maxFractionDigits);
			}
			if (minFractionDigits >= 0) {
				format.setMinimumFractionDigits(minFractionDigits);
			}
		}

		format.setGroupingUsed(false);
		format.setMinimumIntegerDigits(1);

		return format;
	}

	private static boolean isInteger(Class valueClass) {
		return Integer.class.isAssignableFrom(valueClass) || Long.class.isAssignableFrom(valueClass)
				|| Short.class.isAssignableFrom(valueClass) || BigInteger.class.isAssignableFrom(valueClass);
	}

	/**
	 * Flag para activar el agrupamiento por miles
	 */
	public static boolean DEFAULT_GROUPING;

	/**
	 * Flag para setear ',' como separador decimal.
	 */
	public static boolean DEFAULT_DECIMAL_SEPARATOR_COMMA;

	static int DEFAULT_MAX_INT_DIGITS;

	static int DEFAULT_MAX_FRACTION_DIGITS;

	static int DEFAULT_MIN_FRACTION_DIGITS;

	static boolean DEFAULT_ALLOWS_NEGATIVE;

	static {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		DEFAULT_DECIMAL_SEPARATOR_COMMA = symbols.getDecimalSeparator() == ',';
		DEFAULT_GROUPING = true;
		DEFAULT_MAX_INT_DIGITS = -1;
		DEFAULT_MAX_FRACTION_DIGITS = -1;
		DEFAULT_MIN_FRACTION_DIGITS = -1;
		DEFAULT_ALLOWS_NEGATIVE = false;
	};

	// constantes utilizadas para obtener prototipos.
	private static final String POSITIVE_INT = "99999999999999999999999";

	private static final String NEGATIVE_INT = "-99999999999999999999999";

	private static final String FRACTION = ".55555555555555555";
}
