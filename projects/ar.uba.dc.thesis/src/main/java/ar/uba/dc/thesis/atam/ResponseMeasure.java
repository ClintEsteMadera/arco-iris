package ar.uba.dc.thesis.atam;

import org.acmestudio.acme.model.IAcmeModel;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

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
		this(description, constraint, HeuristicType.MOST);
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

	public boolean holds(IAcmeModel acmeModel) {
		return this.constraint.holds(acmeModel, this.heuristicType);
	}

	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios) {
		return this.constraint.holds(rainbowModelWithScenarios, this.heuristicType);
	}

	public void validate() {
		// Do nothing
	}

}