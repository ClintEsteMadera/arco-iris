package ar.uba.dc.arcoiris.util.sim.graphics;

import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

public enum GraphicQuantifier {

	SUM() {
		@Override
		public Number getValue(List<Number> values) {
			double sum = NumberUtils.DOUBLE_ZERO;
			for (Number number : values) {
				sum += number.doubleValue();
			}
			return sum;
		}
	},
	AVERAGE() {
		@Override
		public Number getValue(List<Number> values) {
			Number sum = GraphicQuantifier.SUM.getValue(values);
			return values.isEmpty() ? 0.0d : sum.doubleValue() / values.size();
		}
	};

	public abstract Number getValue(List<Number> values);

}
