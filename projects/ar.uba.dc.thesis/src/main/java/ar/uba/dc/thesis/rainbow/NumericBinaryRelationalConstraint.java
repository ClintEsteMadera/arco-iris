package ar.uba.dc.thesis.rainbow;

import java.util.Arrays;

import org.acmestudio.acme.core.resource.IAcmeResource;
import org.acmestudio.acme.element.IAcmeDesignRule.DesignRuleType;
import org.acmestudio.acme.rule.node.IExpressionNode;
import org.acmestudio.acme.rule.node.NumericBinaryRelationalExpressionNode;
import org.acmestudio.acme.rule.node.NumericLiteralNode;
import org.acmestudio.acme.rule.node.ReferenceNode;
import org.acmestudio.basicmodel.element.AcmeComponent;
import org.acmestudio.basicmodel.element.AcmeDesignRule;
import org.acmestudio.basicmodel.model.AcmeModel;
import org.apache.commons.lang.StringUtils;

/**
 * TODO: We can contemplate an abstraction for the operands so that we do not force an specific order (i.e. left side =
 * property, right side=literal value)
 * 
 * @author Jonathan
 */
public class NumericBinaryRelationalConstraint implements Constraint {

	// private static RainbowLogger logger = RainbowLoggerFactory.logger(NumericBinaryRelationalConstraint.class);

	private final String property;
	private final NumericBinaryOperator binaryOperator;
	private final long value;
	private AcmeDesignRule acmeDesignRule;

	public NumericBinaryRelationalConstraint(String property, NumericBinaryOperator binaryOperator, long value) {
		super();
		this.property = property;
		this.binaryOperator = binaryOperator;
		this.value = value;

		this.validate();
	}

	public void createAndAddAcmeRule(String ruleName, AcmeModel acmeModel, final AcmeComponent acmeComponent) {
		AcmeDesignRule createdRule = null;
		// ESTO AGREGA LA RULE AL MODELO!!! (borrar este comentario cuando te convenzas de que esto no va...)
		/*
		 * try { createdRule = acmeComponent.createDesignRule(ruleName); } catch (final AcmeIllegalNameException e) {
		 * logger.error("Error creating rule from Scenario", e); throw new RuntimeException(e); }
		 */
		IAcmeResource context = acmeComponent.getContext();
		createdRule = new AcmeDesignRule(context, acmeModel, ruleName);
		final NumericBinaryRelationalExpressionNode expression = new NumericBinaryRelationalExpressionNode(context);
		// an integer is an special case of a float, therefore, we can use always FLOAT
		expression.setExpressionType(NumericBinaryRelationalExpressionNode.FLOAT_TYPE);

		// TODO: the "context" param used to be NULL, is this doesn't work, change it back to NULL
		final ReferenceNode leftHandOperand = new ReferenceNode(context);
		leftHandOperand.setReference(Arrays.asList("self", this.getProperty()));
		expression.setFirstChild(leftHandOperand);

		expression.setOperator(this.getBinaryOperator().getRainbowExpressionOperator());

		final IExpressionNode rightHandOperand = new NumericLiteralNode(this.getValue(), context);
		expression.setSecondChild(rightHandOperand);

		// TODO is it always "invariant" or can it be "heuristic" too?
		createdRule.setDesignRuleType(DesignRuleType.INVARIANT);
		createdRule.setDesignRuleExpression(expression);

		this.acmeDesignRule = createdRule;
	}

	public AcmeDesignRule getAcmeDesignRule() {
		return acmeDesignRule;
	}

	public String getProperty() {
		return property;
	}

	public NumericBinaryOperator getBinaryOperator() {
		return binaryOperator;
	}

	public long getValue() {
		return value;
	}

	public void validate() {
		if (StringUtils.isBlank(this.getProperty())) {
			throw new IllegalArgumentException("The property involved in the comparison cannot be blank");
		}
		if (this.getBinaryOperator() == null) {
			throw new IllegalArgumentException("The binary comparator involved in the comparison cannot be null");
		}
	}
}
