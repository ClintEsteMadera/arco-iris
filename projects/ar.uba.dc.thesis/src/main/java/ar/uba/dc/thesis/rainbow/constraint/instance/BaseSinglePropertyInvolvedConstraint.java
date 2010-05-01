package ar.uba.dc.thesis.rainbow.constraint.instance;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.SinglePropertyInvolvedConstraint;

public abstract class BaseSinglePropertyInvolvedConstraint extends ThesisPojo implements
		SinglePropertyInvolvedConstraint {

	private final Artifact artifact;

	private final String property;

	public BaseSinglePropertyInvolvedConstraint(Artifact artifact, String property) {
		super();
		this.artifact = artifact;
		this.property = property;
	}

	public Artifact getArtifact() {
		return artifact;
	}

	/**
	 * Uses the Property Name, in addition to the System and Type Name present in this Constraint's artifact, and looks
	 * for that particular property in the Acme Model.
	 * 
	 * @param acmeModel
	 *            the model where to look for the property
	 * @param propertyFullPath
	 *            the name of the property, includes the system and type name of the property,
	 * @return
	 */
	protected Object findAcmePropertyInAcme(RainbowModelWithScenarios rainbowModelWithScenarios, String propertyFullPath) {
		Object property = rainbowModelWithScenarios.getProperty(propertyFullPath);
		if (property == null) {
			throw new RuntimeException("Could not find in the model the property '" + propertyFullPath + "'");
		}
		return property;
	}

	/**
	 * Returns the full qualified property name prepending the System and Type name of it.
	 */
	public String getFullyQualifiedPropertyName() {
		// FIXME this still does not work with types' properties. This seems to be hardcoded in the ACME model (.acme)
		return this.artifact.getSystemName() + Util.DOT + this.artifact.getName() + Util.DOT + this.property;
	}

	public void validate() {
		if (this.artifact == null) {
			throw new RuntimeException("The artifact involved in this constraint cannot be null");
		}
	}

}
