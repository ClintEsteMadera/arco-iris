package ar.uba.dc.thesis.atam.scenario.model;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public class ResponseMeasure extends ThesisPojo {

	private static final long serialVersionUID = 1L;

	private final String description;

	private final Constraint constraint;

	public ResponseMeasure(String description, Constraint constraint) {
		super();
		this.description = description;
		this.constraint = constraint;
		this.validate();
	}

	public String getDescription() {
		return this.description;
	}

	public Constraint getConstraint() {
		return this.constraint;
	}

	@Override
	public void validate() {
		// Do nothing
	}

}