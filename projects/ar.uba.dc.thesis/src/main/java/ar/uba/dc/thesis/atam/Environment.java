package ar.uba.dc.thesis.atam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

/**
 * FIXME Extract the heuristic calculation to another object
 */
public class Environment extends ThesisPojo {

	private static final int HISTORY_SIZE = 10;

	private final String name;

	private final List<? extends Constraint> conditions;

	private final Map<Concern, Double> weights;

	private final HeuristicType heuristicType;

	private final List<Boolean> history;

	public Environment(String name, List<? extends Constraint> conditions, Map<Concern, Double> weights) {
		super();
		this.name = name;
		this.conditions = conditions;
		this.weights = weights;

		// TODO: Let the history size and heuristicType to be configured by the user and update validate() accordingly
		this.history = new ArrayList<Boolean>(HISTORY_SIZE);
		this.heuristicType = HeuristicType.MOST;

		this.validate();
	}

	public String getName() {
		return name;
	}

	public List<? extends Constraint> getConditions() {
		return conditions;
	}

	public Map<Concern, Double> getWeights() {
		return weights;
	}

	public boolean holds(RainbowModelWithScenarios rainbowModelWithScenarios) {
		boolean holds = true;

		for (Constraint constraint : this.getConditions()) {
			holds = holds && constraint.holds(rainbowModelWithScenarios, HeuristicType.MOST);
		}
		holds = this.getHeuristicAccordingToHistory(holds);

		this.addToHistory(holds);

		return holds;
	}

	public void validate() {
		if (StringUtils.isBlank(this.getName())) {
			throw new IllegalArgumentException("Environment's name cannot be empty");
		}
		for (Constraint condition : this.getConditions()) {
			condition.validate();
		}
		if (this.getWeights() == null) {
			throw new IllegalArgumentException("Concern-Multiplier map cannot be null");
		}
	}

	/**
	 * Uses an heuristic (configured by the user) to decide if, based on the history, a current calculation of the
	 * underlying constraints holds or not.<br>
	 * The possible values are specified in {@link HeuristicType}
	 * 
	 * @param currentConstraintsEvaluation
	 *            the current evaluation of the underlying constraints, to be quantified with the history.
	 * @return a (hopefully) more accurate value than the last one obtained recently (i.e.
	 *         <code>currentConstraintsEvaluation</code>)
	 */
	private boolean getHeuristicAccordingToHistory(boolean currentConstraintsEvaluation) {
		return this.heuristicType.run(currentConstraintsEvaluation, this.history);
	}

	/**
	 * Adds the value to the history. If the history has reached its maximum size, then the oldest value is overriden
	 * (FIFO Cache)
	 * 
	 * @param holds
	 */
	private void addToHistory(boolean holds) {
		if (this.history.size() == HISTORY_SIZE) {
			this.history.add(0, holds);
		} else {
			this.history.add(holds);
		}
	}

	public enum HeuristicType {
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

}