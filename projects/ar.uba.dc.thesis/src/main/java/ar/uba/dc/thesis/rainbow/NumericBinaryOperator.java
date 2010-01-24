package ar.uba.dc.thesis.rainbow;

import org.acmestudio.acme.core.globals.ExpressionOperator;

/**
 * This class is intended to provide a type-safe way to specify operators.
 * 
 * @author Jonathan
 * 
 */
public enum NumericBinaryOperator {

	LESS_THAN(ExpressionOperator.LESS_THAN_OP),

	LESS_THAN_OR_EQUALS(ExpressionOperator.LESS_OR_EQUAL_OP),

	GREATER_THAN(ExpressionOperator.GREATER_THAN_OP),

	GREATER_THAN_OR_EQUALS(ExpressionOperator.GREATER_OR_EQUAL_OP),

	EQUALS(ExpressionOperator.EQUALS_OP);

	public ExpressionOperator getRainbowExpressionOperator() {
		return rainbowExpressionOperator;
	}

	private NumericBinaryOperator(ExpressionOperator rainbowExpressionOperator) {
		this.rainbowExpressionOperator = rainbowExpressionOperator;
	}

	private final ExpressionOperator rainbowExpressionOperator;
}