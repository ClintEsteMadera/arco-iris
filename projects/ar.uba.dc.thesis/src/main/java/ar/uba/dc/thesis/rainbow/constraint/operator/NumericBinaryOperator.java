package ar.uba.dc.thesis.rainbow.constraint.operator;

import org.acmestudio.acme.core.globals.ExpressionOperator;
import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.Validatable;

/**
 * This class is intended to provide a type-safe way to specify operators.
 * 
 */
public enum NumericBinaryOperator implements Validatable {

	LESS_THAN("<", ExpressionOperator.LESS_THAN_OP),

	LESS_THAN_OR_EQUALS("<=", ExpressionOperator.LESS_OR_EQUAL_OP),

	GREATER_THAN(">", ExpressionOperator.GREATER_THAN_OP),

	GREATER_THAN_OR_EQUALS(">=", ExpressionOperator.GREATER_OR_EQUAL_OP),

	EQUALS("==", ExpressionOperator.EQUALS_OP);

	private final String symbol;

	private final ExpressionOperator rainbowExpressionOperator;

	public ExpressionOperator getRainbowExpressionOperator() {
		return rainbowExpressionOperator;
	}

	private NumericBinaryOperator(String symbol, ExpressionOperator rainbowExpressionOperator) {
		this.symbol = symbol;
		this.rainbowExpressionOperator = rainbowExpressionOperator;

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
		if (this.getRainbowExpressionOperator() == null) {
			throw new IllegalArgumentException("Rainbow Expression Operator cannot be null");
		}
	}
}