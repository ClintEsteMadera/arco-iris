package ar.uba.dc.thesis.rainbow.constraint.numerical;

import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.Quantifier;

public abstract class BaseSinglePropertyInvolvedConstraint extends ThesisPojo implements Constraint {

	private static final long serialVersionUID = 1L;

	private final Artifact artifact;

	private final String property;

	private final Quantifier quantifier;

	public BaseSinglePropertyInvolvedConstraint(Quantifier quantifier, Artifact artifact, String property) {
		super();
		this.quantifier = quantifier;
		this.artifact = artifact;
		this.property = property;
	}

	public Artifact getArtifact() {
		return artifact;
	}

	public String getProperty() {
		return property;
	}

	public Quantifier getQuantifier() {
		return quantifier;
	}

	/**
	 * Returns the fully qualified property name prepending the System and Type name of it.
	 */
	public String getFullyQualifiedPropertyName() {
		return this.artifact.getSystemName() + Util.DOT + this.artifact.getName() + Util.DOT + this.property;
	}

	public void validate() {
		if (this.artifact == null) {
			throw new RuntimeException("The artifact involved in this constraint cannot be null");
		}
	}

}
