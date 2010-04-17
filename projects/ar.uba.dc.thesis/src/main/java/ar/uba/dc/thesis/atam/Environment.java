package ar.uba.dc.thesis.atam;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acmestudio.acme.model.IAcmeModel;
import org.apache.commons.lang.StringUtils;

import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public class Environment extends ThesisPojo {

	private final String name;

	private final List<? extends Constraint> conditions;

	private final Map<Concern, Double> weights;

	public static final Environment ANY = createAnyEnvironment();

	public Environment(String name, List<? extends Constraint> conditions, Map<Concern, Double> weights) {
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

	public Map<Concern, Double> getWeights() {
		return weights;
	}

	public boolean holds(IAcmeModel acmeModel) {
		boolean holds = true;
		for (Constraint constraint : this.getConditions()) {
			holds = holds && constraint.holds(acmeModel);
		}
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

	private static Environment createAnyEnvironment() {
		Map<Concern, Double> equallyDistributedWeights = new HashMap<Concern, Double>();
		Concern[] values = Concern.values();
		Double aWeight = (double) 1 / values.length;
		for (Concern concern : values) {
			equallyDistributedWeights.put(concern, aWeight);
		}
		return new Environment("Any", Collections.<Constraint> emptyList(), equallyDistributedWeights);
	}
}