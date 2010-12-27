package ar.uba.dc.thesis.rainbow.constraint.numerical;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.common.ArcoIrisDomainObject;
import ar.uba.dc.thesis.common.validation.Assert;
import ar.uba.dc.thesis.common.validation.ValidationError;
import ar.uba.dc.thesis.common.validation.ValidationException;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public abstract class BaseSinglePropertyInvolvedConstraint extends ArcoIrisDomainObject implements Constraint {

	private static final long serialVersionUID = 1L;

	private static final String VALIDATION_MSG_PROPERTY = "The property involved in the comparison cannot be blank";

	private static final String VALIDATION_MSG_ARTIFACT = "The artifact cannot be empty";

	private static final String EMPTY_STRING = "";

	private static final String NULL = "null";

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
	public final void validate() {
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		String constraintId = this.getDescriptiveIdForReporting();

		Assert.notNullAndValid(this.artifact, constraintId + VALIDATION_MSG_ARTIFACT, validationErrors);

		Assert.notBlank(this.property, constraintId + VALIDATION_MSG_PROPERTY, validationErrors);

		validationErrors.addAll(this.collectValidationErrors());

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
	}

	protected String getDescriptiveIdForReporting() {
		String constraintId = this.getFullyQualifiedPropertyName() + ": ";
		if (StringUtils.isBlank(constraintId) || constraintId.contains(NULL)) {
			constraintId = EMPTY_STRING;
		}
		return constraintId;
	}

	/**
	 * This method is intended to assure validation is implemented among all subclasses.
	 * <p>
	 * <b>Importante Note:</b> Do not invoke {@link #validate()}, if you do so, there will be an infinite loop because
	 * {@link #validate()} invokes this method.
	 */
	protected abstract List<ValidationError> collectValidationErrors();
}
