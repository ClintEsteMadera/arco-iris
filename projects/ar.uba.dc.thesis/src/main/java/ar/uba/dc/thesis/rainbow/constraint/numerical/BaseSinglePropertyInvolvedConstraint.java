package ar.uba.dc.thesis.rainbow.constraint.numerical;

import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public abstract class BaseSinglePropertyInvolvedConstraint extends ThesisPojo implements Constraint {

	private static final long serialVersionUID = 1L;

	private Artifact artifact;

	private String property;

	public BaseSinglePropertyInvolvedConstraint() {
		super();
		this.artifact = new Artifact();
	}

	public BaseSinglePropertyInvolvedConstraint(Artifact artifact, String property) {
		super();
		this.artifact = artifact;
		this.property = property;
	}

	public Artifact getArtifact() {
		return artifact;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * Returns the fully qualified property name prepending the System and Type name of it.
	 */
	public String getFullyQualifiedPropertyName() {
		return this.artifact.getSystemName() + Util.DOT + this.artifact.getName() + Util.DOT + this.property;
	}

	@Override
	public String getToString() {
		return this.toString();
	}

	@Override
	public void validate() {
		if (this.artifact == null) {
			throw new RuntimeException("The artifact involved in this constraint cannot be null");
		}
	}
}
