package ar.uba.dc.thesis.atam;

import ar.uba.dc.thesis.rainbow.Constraint;

public class ResponseMeasure {

	private final String description;

	private final Constraint constraint;

	public ResponseMeasure(String description, Constraint constraint) {
		super();
		this.description = description;
		this.constraint = constraint;
	}

	public String getDescription() {
		return this.description;
	}

	public Constraint getConstraint() {
		return this.constraint;
	}
}