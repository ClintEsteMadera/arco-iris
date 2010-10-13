package ar.uba.dc.thesis.common;

import java.util.Iterator;
import java.util.List;

public enum Heuristic {

	ALL("All of the historic evaluations") {
		@Override
		public boolean run(List<Boolean> history) {
			boolean result = true;
			Iterator<Boolean> it = history.iterator();
			while (result && it.hasNext()) {
				result = result && it.next();
			}
			return result;
		}
	},
	MOST("Most of the historic evaluations") {
		@Override
		public boolean run(List<Boolean> history) {
			int holded = 0;
			for (boolean historicalEval : history) {
				if (historicalEval) {
					holded++;
				} else {
					holded--;
				}
			}
			return holded > 0;
		}
	},
	AT_LEAST_ONE("At least one evaluation") {
		@Override
		public boolean run(List<Boolean> history) {
			boolean result = false;
			Iterator<Boolean> it = history.iterator();
			while (!result && it.hasNext()) {
				result = result || it.next();
			}
			return result;
		}
	};

	private final String description;

	Heuristic(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return this.description;
	}

	public abstract boolean run(List<Boolean> history);
}