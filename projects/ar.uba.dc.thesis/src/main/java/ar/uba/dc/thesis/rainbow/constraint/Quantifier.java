package ar.uba.dc.thesis.rainbow.constraint;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

public enum Quantifier {

	FOR_ALL {
		@Override
		public boolean holds(Constraint constraint, List<Number> values) {
			boolean holds = true;
			for (Number value : values) {
				holds = holds && constraint.holds(value);
			}
			return holds;
		}
	},
	AVERAGE {
		@Override
		public boolean holds(Constraint constraint, List<Number> values) {
			double sum = NumberUtils.DOUBLE_ZERO;
			for (Number number : values) {
				sum += number.doubleValue();
			}
			Number average = values.isEmpty() ? sum : sum / values.size();
			boolean holds = constraint.holds(average);
			Oracle.instance().writeEvaluatorPanel(
					logger,
					"Holds for average " + average + " of " + constraint.getFullyQualifiedPropertyName() + "? " + holds
							+ "!!!!");
			return holds;
		}
	};
	private final static RainbowLogger logger = RainbowLoggerFactory.logger(Quantifier.class);

	public abstract boolean holds(Constraint constraint, List<Number> values);
}
