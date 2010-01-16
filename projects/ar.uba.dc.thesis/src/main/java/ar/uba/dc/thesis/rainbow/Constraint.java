package ar.uba.dc.thesis.rainbow;

import org.acmestudio.basicmodel.element.AcmeComponent;
import org.acmestudio.basicmodel.model.AcmeModel;

public interface Constraint {

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
	void createAndAddAcmeRule(String ruleName, AcmeModel acmeModel, final AcmeComponent acmeComponent);

	/**
	 * Validates the instance and throws an Exception (typically {@link IllegalArgumentException} if something is wrong.
	 */
	void validate();
}
