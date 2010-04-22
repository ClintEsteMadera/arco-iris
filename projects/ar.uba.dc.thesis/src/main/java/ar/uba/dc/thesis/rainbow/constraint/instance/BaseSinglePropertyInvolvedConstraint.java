package ar.uba.dc.thesis.rainbow.constraint.instance;

import org.acmestudio.acme.model.IAcmeModel;
import org.acmestudio.basicmodel.element.property.AcmeProperty;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.SinglePropertyInvolvedConstraint;

public abstract class BaseSinglePropertyInvolvedConstraint extends ThesisPojo implements
		SinglePropertyInvolvedConstraint {

	private final Artifact artifact;

	public BaseSinglePropertyInvolvedConstraint(Artifact artifact) {
		super();
		this.artifact = artifact;
	}

	/**
	 * Uses the Property Name, in addition to the System and Type Name present in this Constraint's artifact, and looks
	 * for that particular property in the Acme Model.
	 * 
	 * @param acmeModel
	 *            the model where to look for the property
	 * @param propertyName
	 *            the name of the property (it will be prepended with the system name and the type name of the property,
	 *            both taken from the artifact present in this constraint)
	 * @return
	 */
	protected AcmeProperty findAcmePropertyInAcme(IAcmeModel acmeModel, String propertyName) {
		AcmeProperty property = (AcmeProperty) acmeModel.findNamedObject(acmeModel, propertyName);
		if (property == null) {
			throw new RuntimeException("Could not find in the model the property '" + propertyName + "'");
		}
		return property;
	}

	public void validate() {
		if (this.artifact == null) {
			throw new RuntimeException("The artifact involved in this constraint cannot be null");
		}
	}

}
