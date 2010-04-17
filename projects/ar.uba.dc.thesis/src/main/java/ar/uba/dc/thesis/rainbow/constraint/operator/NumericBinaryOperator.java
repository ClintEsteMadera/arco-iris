package ar.uba.dc.thesis.rainbow.constraint.operator;

import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.Validatable;

/**
 * This class is intended to provide a type-safe way to specify operators.
 * 
 */
public enum NumericBinaryOperator implements Validatable {

	LESS_THAN("<") {
		@Override
		public boolean performOperation(Number value1, Number value2) {
			return value1.floatValue() < value2.floatValue();
		}
	},
	LESS_THAN_OR_EQUALS("<=") {
		@Override
		public boolean performOperation(Number value1, Number value2) {
			return value1.floatValue() <= value2.floatValue();
		}
	},
	GREATER_THAN(">") {
		@Override
		public boolean performOperation(Number value1, Number value2) {
			return value1.floatValue() > value2.floatValue();
		}
	},
	GREATER_THAN_OR_EQUALS(">=") {
		@Override
		public boolean performOperation(Number value1, Number value2) {
			return value1.floatValue() >= value2.floatValue();
		}
	},
	EQUALS("==") {
		@Override
		public boolean performOperation(Number value1, Number value2) {
			return value1.floatValue() == value2.floatValue();
		}
	};

	private final String symbol;

	private NumericBinaryOperator(String symbol) {
		this.symbol = symbol;

		this.validate();
	}

	@Override
	public String toString() {
		return this.symbol;
	}

	public void validate() {
		if (StringUtils.isBlank(this.symbol)) {
			throw new IllegalArgumentException("Operator's symbol cannot be empty");
		}
	}

	public abstract boolean performOperation(Number value1, Number value2);
}