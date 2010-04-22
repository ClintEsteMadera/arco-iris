package ar.uba.dc.thesis.rainbow.constraint.instance;

import org.acmestudio.acme.model.IAcmeModel;
import org.acmestudio.basicmodel.element.property.AcmeProperty;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.SinglePropertyInvolvedConstraint;

public abstract class BaseSinglePropertyInvolvedConstraint extends ThesisPojo implements
		SinglePropertyInvolvedConstraint {

	private static final String DOT = ".";

	private final Artifact artifact;

	private final String property;

	public BaseSinglePropertyInvolvedConstraint(Artifact artifact, String property) {
		super();
		this.artifact = artifact;
		this.property = property;
	}

	protected AcmeProperty findAcmePropertyInAcme(IAcmeModel acmeModel) {
		AcmeProperty property = (AcmeProperty) acmeModel
				.findNamedObject(acmeModel, this.getFullyQualifiedPropertyName());
		if (property == null) {
			throw new RuntimeException("Could not find in the model the property '"
					+ this.getFullyQualifiedPropertyName() + "'");
		}
		return property;
	}

	/**
	 * Returns the full qualified property name prepending the System and Type name of it.
	 */
	public String getFullyQualifiedPropertyName() {
		return this.artifact.getSystemName() + DOT + this.artifact.getTypeName() + DOT + this.property;
	}

	public void validate() {
		if (this.artifact == null) {
			throw new RuntimeException("The artifact involved in this constraint cannot be null");
		}
	}

}
