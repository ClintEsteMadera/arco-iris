package ar.uba.dc.thesis.rainbow;

import org.acmestudio.acme.core.globals.ExpressionOperator;

/**
 * This class is intended to provide a type-safe way to specify operators.
 * 
 * @author Jonathan
 * 
 */
public enum NumericBinaryOperator {
	LESS_THAN() {
		@Override
		public ExpressionOperator getRainbowExpressionOperator() {
			return ExpressionOperator.LESS_THAN_OP;
		}
	},
	LESS_THAN_OR_EQUALS() {
		@Override
		public ExpressionOperator getRainbowExpressionOperator() {
			return ExpressionOperator.LESS_OR_EQUAL_OP;
		}
	},
	GREATER_THAN() {
		@Override
		public ExpressionOperator getRainbowExpressionOperator() {
			return ExpressionOperator.GREATER_THAN_OP;
		}
	},
	GREATER_THAN_OR_EQUALS() {
		@Override
		public ExpressionOperator getRainbowExpressionOperator() {
			return ExpressionOperator.GREATER_OR_EQUAL_OP;
		}
	},
	EQUALS() {
		@Override
		public ExpressionOperator getRainbowExpressionOperator() {
			return ExpressionOperator.EQUALS_OP;
		}
	};

	public abstract ExpressionOperator getRainbowExpressionOperator();
}
