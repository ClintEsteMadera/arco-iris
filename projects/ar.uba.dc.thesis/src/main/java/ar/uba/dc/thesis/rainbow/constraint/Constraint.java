package ar.uba.dc.thesis.rainbow.constraint;

import org.acmestudio.acme.element.IAcmeComponent;
import org.acmestudio.basicmodel.element.AcmeDesignRule;
import org.acmestudio.basicmodel.model.AcmeModel;

import ar.uba.dc.thesis.common.Validatable;

public interface Constraint extends Validatable {

	/**
	 * Creates an ACME rule based on the provided parameters and sets that rule to itself.
	 * 
	 * @param ruleName
	 *            the name of the rule. Cannot be null.
	 * @param acmeModel
	 *            the model where the rule will be checked.
	 * @param acmeComponent
	 *            the main component involved on this rule.
	 */
	void createAndAddAcmeRule(String ruleName, AcmeModel acmeModel, final IAcmeComponent acmeComponent);

	/**
	 * Provides the Acme design rule. This object is usually used for evaluating constraints in some particular
	 * architectural model
	 * 
	 * @return the Acme design rule
	 */
	AcmeDesignRule getAcmeDesignRule();
}
