package ar.uba.dc.thesis.atam;

import org.acmestudio.acme.model.IAcmeModel;

import ar.uba.dc.thesis.atam.Environment.HeuristicType;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.SinglePropertyInvolvedConstraint;

public class ResponseMeasure extends ThesisPojo {

	private final String description;

	private final SinglePropertyInvolvedConstraint constraint;

	private final HeuristicType heuristicType;

	public ResponseMeasure(String description, SinglePropertyInvolvedConstraint constraint, HeuristicType heuristicType) {
		super();
		this.description = description;
		this.constraint = constraint;
		this.heuristicType = heuristicType;
		this.validate();
	}

	public ResponseMeasure(String description, SinglePropertyInvolvedConstraint constraint) {
		super();
		this.description = description;
		this.constraint = constraint;
		this.heuristicType = HeuristicType.MOST;
		this.validate();
	}

	public String getPropertyMeasured() {
		return this.getConstraint().getFullyQualifiedPropertyName();
	}

	public String getDescription() {
		return this.description;
	}

	public SinglePropertyInvolvedConstraint getConstraint() {
		return this.constraint;
	}

	public void validate() {
		// Do nothing
	}

	public boolean holds(IAcmeModel acmeModel, String involvedArtifactName) {
		return this.constraint.holds(acmeModel, involvedArtifactName);
	}

	public boolean holds(IAcmeModel acmeModel) {
		return this.constraint.holds(acmeModel, this.heuristicType);
	}
}