package commons.validation;

import java.math.BigDecimal;

/**
 * 
 * 
 */
public class NumericConstraints implements Validator<Number> {

	public NumericConstraints(int intDigits, int precision, BigDecimal max, BigDecimal min) {
		super();
		this.intDigits = intDigits;
		this.precision = precision;
		this.max = max;
		this.min = min;
	}

	public NumericConstraints(int intDigits, int precision, BigDecimal max) {
		this(intDigits, precision, max, BigDecimal.ZERO);
	}

	public NumericConstraints(int intDigits, int precision) {
		this(intDigits, precision, buildMax(intDigits, precision));
	}

	public NumericConstraints(int intDigits) {
		this(intDigits, 0);
	}

	public boolean validate(Number objectToValidate, ValidationResult result) {

		boolean valid = true;

		if (this.max != null && this.max.compareTo(new BigDecimal(objectToValidate.toString())) < 0) {
			result.addError(CommonValidationMessages.LESS_THAN_OR_EQUAL, result.getValueDescription(), this.max);
			valid = false;
		} else if (this.min != null && this.min.compareTo(new BigDecimal(objectToValidate.toString())) > 0) {
			result.addError(CommonValidationMessages.GREATER_THAN_OR_EQUAL, result.getValueDescription(), this.max);
			valid = false;
		}

		// TODO: validate precision

		return valid;
	}

	public int getIntDigits() {
		return intDigits;
	}

	public Number getMax() {
		return max;
	}

	public Number getMin() {
		return min;
	}

	public int getPrecision() {
		return precision;
	}

	private static BigDecimal buildMax(int intDigits, int precision) {

		String s = "0";

		if (intDigits > 0) {
			s = getDigitString(intDigits);
		}

		if (precision > 0) {
			s = s + "." + getDigitString(precision);
		}

		return new BigDecimal(s);
	}

	private static String getDigitString(int n) {
		final StringBuilder str = new StringBuilder();
		for (; n > 0; n--) {
			str.append("9");
		}
		return str.toString();
	}

	private BigDecimal min;

	private BigDecimal max;

	private int intDigits;

	private int precision;
}
