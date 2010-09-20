package ar.uba.dc.thesis.rainbow.constraint.numerical;

import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

public enum Quantifier {

	IN_AVERAGE("Avg") {
		@Override
		public String getExpPropertyPrefix() {
			return RainbowModel.EXP_AVG_KEY;
		}
	},
	SUM("Σ") {
		@Override
		public String getExpPropertyPrefix() {
			return RainbowModelWithScenarios.EXP_SUM_KEY;
		}
	};

	private String showableText;

	private Quantifier(String showableText) {
		this.showableText = showableText;
	}

	@Override
	public String toString() {
		return this.showableText;
	}

	public abstract String getExpPropertyPrefix();
}
