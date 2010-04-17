package ar.uba.dc.thesis.rainbow.constraint.instance;

import org.acmestudio.acme.core.resource.IAcmeResource;
import org.acmestudio.acme.element.IAcmeComponent;
import org.acmestudio.acme.element.IAcmeDesignRule.DesignRuleType;
import org.acmestudio.acme.model.IAcmeModel;
import org.acmestudio.acme.rule.node.BooleanLiteralNode;
import org.acmestudio.basicmodel.element.AcmeDesignRule;
import org.acmestudio.basicmodel.model.AcmeModel;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public class BooleanLiteralConstraint extends ThesisPojo implements Constraint {

	private static final String[] EQUALS_HASH_CODE_EXCLUDED_FIELDS = new String[] { "acmeDesignRule" };

	private final boolean literalValue;

	private AcmeDesignRule acmeDesignRule;

	public static final BooleanLiteralConstraint TRUE = new BooleanLiteralConstraint(Boolean.TRUE);

	public static final BooleanLiteralConstraint FALSE = new BooleanLiteralConstraint(Boolean.FALSE);

	private BooleanLiteralConstraint(boolean literalValue) {
		super();
		this.literalValue = literalValue;
	}

	public void createAndAddAcmeRule(String ruleName, AcmeModel acmeModel, IAcmeComponent acmeComponent) {
		IAcmeResource context = acmeComponent.getContext();
		AcmeDesignRule createdRule = new AcmeDesignRule(context, acmeModel, ruleName);
		final BooleanLiteralNode expression = new BooleanLiteralNode(context);
		expression.setValue(this.literalValue);

		createdRule.setDesignRuleType(DesignRuleType.INVARIANT);
		createdRule.setDesignRuleExpression(expression);

		this.acmeDesignRule = createdRule;
	}

	public AcmeDesignRule getAcmeDesignRule() {
		return this.acmeDesignRule;
	}

	public boolean holds(IAcmeModel acmeModel) {
		return this.literalValue;
	}

	public void validate() {
		// do nothing
	}

	@Override
	protected String[] getEqualsAndHashCodeExcludedFields() {
		return EQUALS_HASH_CODE_EXCLUDED_FIELDS;
	}

	@Override
	public String toString() {
		return Boolean.toString(this.literalValue);
	}
}
