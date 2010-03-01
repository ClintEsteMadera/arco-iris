package ar.uba.dc.thesis.atam;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public class ScenarioEnvironment extends ThesisPojo {

	private final String name;

	private final List<? extends Constraint> conditions;

	private final Map<Concern, Float> weights;

	public ScenarioEnvironment(String name, List<? extends Constraint> conditions, Map<Concern, Float> weights) {
		super();
		this.name = name;
		this.conditions = conditions;
		this.weights = weights;

		this.validate();
	}

	public String getName() {
		return name;
	}

	public List<? extends Constraint> getConditions() {
		return conditions;
	}

	public Map<Concern, Float> getWeights() {
		return weights;
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
}