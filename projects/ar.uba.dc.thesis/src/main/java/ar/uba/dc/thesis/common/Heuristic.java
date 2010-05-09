package ar.uba.dc.thesis.common;

import java.util.Iterator;
import java.util.List;

public enum Heuristic {

	ALL {
		@Override
		public boolean run(boolean currentConstraintsEvaluation, List<Boolean> history) {
			boolean result = currentConstraintsEvaluation;
			Iterator<Boolean> it = history.iterator();
			while (result && it.hasNext()) {
				result = result && it.next();
			}
			return result;
		}
	},
	MOST {
		@Override
		public boolean run(boolean currentConstraintsEvaluation, List<Boolean> history) {
			int holded = currentConstraintsEvaluation ? 1 : 0;
			int notHolded = currentConstraintsEvaluation ? 0 : 1;
			for (boolean historicalItem : history) {
				if (historicalItem) {
					holded++;
				} else {
					notHolded++;
				}
			}
			return holded > notHolded;
		}
	},
	AT_LEAST_ONE {
		@Override
		public boolean run(boolean currentConstraintsEvaluation, List<Boolean> history) {
			boolean result = currentConstraintsEvaluation;
			Iterator<Boolean> it = history.iterator();
			while (!result && it.hasNext()) {
				result = result || it.next();
			}
			return result;
		}
	};

	public abstract boolean run(boolean currentConstraintsEvaluation, List<Boolean> history);
}