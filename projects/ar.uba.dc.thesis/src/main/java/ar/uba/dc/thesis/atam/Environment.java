package ar.uba.dc.thesis.atam;

import java.util.Map;
import java.util.Set;

import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.Constraint;

public class Environment {

	public Environment(String name, Set<Constraint> conditions, Map<Concern, Float> weights) {
		super();
		this.name = name;
		this.conditions = conditions;
		this.weights = weights;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Constraint> getConditions() {
		return conditions;
	}

	public void setConditions(Set<Constraint> conditions) {
		this.conditions = conditions;
	}

	public Map<Concern, Float> getWeights() {
		return weights;
	}

	public void setWeights(Map<Concern, Float> weights) {
		this.weights = weights;
	}

	private String name;

	private Set<Constraint> conditions;

	private Map<Concern, Float> weights;

}
