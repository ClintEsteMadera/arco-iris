package ar.uba.dc.arcoiris.atam.scenario.model;

import ar.uba.dc.arcoiris.common.ArcoIrisDomainObject;
import ar.uba.dc.arcoiris.rainbow.constraint.Constraint;
import ar.uba.dc.arcoiris.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;

public class ResponseMeasure extends ArcoIrisDomainObject {

	private static final long serialVersionUID = 1L;

	private String description;

	private Constraint constraint;

	public ResponseMeasure() {
		super();
		this.description = "";
		// FIXME This is a hack!
		this.constraint = new NumericBinaryRelationalConstraint();
	}

	public ResponseMeasure(String description, Constraint constraint) {
		super();
		this.description = description;
		this.constraint = constraint;

		this.validate();
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Constraint getConstraint() {
		return this.constraint;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public void validate() {
		if (this.constraint != null) {
			this.constraint.validate();
		}
	}
}