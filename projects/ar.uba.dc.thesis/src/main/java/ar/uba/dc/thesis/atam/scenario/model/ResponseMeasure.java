package ar.uba.dc.thesis.atam.scenario.model;

import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public class ResponseMeasure extends ThesisPojo {

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

	public boolean holds4Scoring(RainbowModelWithScenarios rainbowModelWithScenarios, double concernDiffAfterStrategy) {
		return this.constraint.holds4Scoring(rainbowModelWithScenarios, concernDiffAfterStrategy);
	}

	public void validate() {
		// Do nothing
	}

}