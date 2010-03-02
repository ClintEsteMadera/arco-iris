package ar.uba.dc.thesis.rainbow.constraint.instance;

import java.util.Arrays;

import org.acmestudio.acme.core.resource.IAcmeResource;
import org.acmestudio.acme.element.IAcmeComponent;
import org.acmestudio.acme.element.IAcmeDesignRule.DesignRuleType;
import org.acmestudio.acme.rule.node.IExpressionNode;
import org.acmestudio.acme.rule.node.NumericBinaryRelationalExpressionNode;
import org.acmestudio.acme.rule.node.NumericLiteralNode;
import org.acmestudio.acme.rule.node.ReferenceNode;
import org.acmestudio.basicmodel.element.AcmeDesignRule;
import org.acmestudio.basicmodel.model.AcmeModel;
import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.SinglePropertyInvolvedConstraint;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;

/**
 * TODO: We can contemplate an abstraction for the operands so that we do not force an specific order (i.e. left side =
 * property, right side=literal value)
 */
public class NumericBinaryRelationalConstraint extends ThesisPojo implements SinglePropertyInvolvedConstraint {

	private static final String SPACE = " ";

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

	public void createAndAddAcmeRule(String ruleName, AcmeModel acmeModel, final IAcmeComponent acmeComponent) {
		// The following adds the rule to the component, among several other things...
		/*
		 * try { createdRule = acmeComponent.createDesignRule(ruleName); } catch (final AcmeIllegalNameException e) {
		 * logger.error("Error creating rule from Scenario", e); throw new RuntimeException(e); }
		 */
		IAcmeResource context = acmeComponent.getContext();
		AcmeDesignRule createdRule = new AcmeDesignRule(context, acmeModel, ruleName);
		final NumericBinaryRelationalExpressionNode expression = new NumericBinaryRelationalExpressionNode(context);
		// an integer is an special case of a float, therefore, we can use always FLOAT
		expression.setExpressionType(NumericBinaryRelationalExpressionNode.FLOAT_TYPE);

		// TODO: the "context" param used to be NULL, if this doesn't work, change it back to NULL
		final ReferenceNode leftHandOperand = new ReferenceNode(context);
		leftHandOperand.setReference(Arrays.asList(acmeComponent.getQualifiedName(), this.getProperty()));
		expression.setFirstChild(leftHandOperand);
		expression.setOperator(this.getBinaryOperator().getRainbowExpressionOperator());

		final IExpressionNode rightHandOperand = new NumericLiteralNode(this.getValue(), context);
		expression.setSecondChild(rightHandOperand);

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
		this.getBinaryOperator().validate();
	}

	@Override
	public String toString() {
		return new StringBuffer().append("(").append(this.getProperty()).append(SPACE).append(this.getBinaryOperator())
				.append(SPACE).append(this.getValue()).append(")").toString();
	}

}
