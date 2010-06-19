package ar.uba.dc.thesis.rainbow.constraint;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Level;
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
			log(Level.INFO, "Holds " + constraint.getFullyQualifiedPropertyName() + " for all instances? " + holds
					+ "!!!!");
			return holds;
		}
	},
	IN_AVERAGE {
		@Override
		public boolean holds(Constraint constraint, List<Number> values) {
			double sum = NumberUtils.DOUBLE_ZERO;
			for (Number number : values) {
				sum += number.doubleValue();
			}
			Number average = values.isEmpty() ? sum : sum / values.size();
			boolean holds = constraint.holds(average);
			log(Level.INFO, "Holds for average " + average + " of " + constraint.getFullyQualifiedPropertyName() + "? "
					+ holds + "!!!!");
			return holds;
		}
	},
	IN_SUM {
		@Override
		public boolean holds(Constraint constraint, List<Number> values) {
			double sum = NumberUtils.DOUBLE_ZERO;
			for (Number number : values) {
				sum += number.doubleValue();
			}
			boolean holds = constraint.holds(sum);
			log(Level.INFO, "Holds for sum " + sum + " of " + constraint.getFullyQualifiedPropertyName() + "? " + holds
					+ "!!!!");
			return holds;
		}
	};
	private final static RainbowLogger logger = RainbowLoggerFactory.logger(Quantifier.class);

	public abstract boolean holds(Constraint constraint, List<Number> values);

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEvaluatorPanel(logger, level, txt, t);
	}
}
