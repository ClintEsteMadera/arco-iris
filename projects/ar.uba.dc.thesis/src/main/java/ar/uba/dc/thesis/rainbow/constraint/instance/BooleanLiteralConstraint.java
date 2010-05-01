package ar.uba.dc.thesis.rainbow.constraint.instance;

import org.acmestudio.acme.model.IAcmeModel;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.atam.Environment.HeuristicType;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public class BooleanLiteralConstraint extends ThesisPojo implements Constraint {

	private final boolean literalValue;

	public static final BooleanLiteralConstraint TRUE = new BooleanLiteralConstraint(Boolean.TRUE);

	public static final BooleanLiteralConstraint FALSE = new BooleanLiteralConstraint(Boolean.FALSE);

	private BooleanLiteralConstraint(boolean literalValue) {
		super();
		this.literalValue = literalValue;
	}

	public boolean holds(IAcmeModel acmeModel, HeuristicType heuristicType) {
		return this.literalValue;
	}

	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios, HeuristicType most) {
		return this.literalValue;
	}

	public void validate() {
		// do nothing
	}

	@Override
	public String toString() {
		return Boolean.toString(this.literalValue);
	}

}
