package ar.uba.dc.thesis.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.instance.BooleanLiteralConstraint;

public class TestScenarioEnvironmentDao implements ScenarioEnvironmentDao {

	private final Collection<Environment> environments = new HashSet<Environment>();

	// TODO: Include all the other concerns?
	public TestScenarioEnvironmentDao() {
		this.environments.add(createNormalEnvironment());
		this.environments.add(createHighLoadEnvironment());
		this.environments.add(createAnyEnvironment());
	}

	public Collection<Environment> getAllEnvironments() {
		return this.environments;
	}

	public Environment getEnvironment(String name) {
		for (Environment environment : environments) {
			if (environment.getName().equals(name)) {
				return environment;
			}
		}
		throw new RuntimeException("Environment " + name + " not defined.");
	}

	// TODO: setear las condiciones de los environments(deben ser excluyentes)
	private Environment createNormalEnvironment() {
		Map<Concern, Double> normalConcernMultiplierMap = new HashMap<Concern, Double>();
		normalConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.5D);
		normalConcernMultiplierMap.put(Concern.SERVER_LOAD, 0.5D);
		Environment normalEnv = new Environment("NORMAL", Collections.singletonList(BooleanLiteralConstraint.TRUE),
				normalConcernMultiplierMap);
		return normalEnv;
	}

	private Environment createHighLoadEnvironment() {
		Map<Concern, Double> highLoadConcernMultiplierMap = new HashMap<Concern, Double>();
		highLoadConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.8D);
		highLoadConcernMultiplierMap.put(Concern.SERVER_LOAD, 0.2D);
		Environment highLoadEnv = new Environment("HIGH LOAD",
				Collections.singletonList(BooleanLiteralConstraint.TRUE), highLoadConcernMultiplierMap);
		return highLoadEnv;
	}

	private static Environment createAnyEnvironment() {
		Map<Concern, Double> equallyDistributedWeights = new HashMap<Concern, Double>();
		// TODO: let user defined weights
		Concern[] values = Concern.values();
		Double aWeight = (double) 1 / values.length;
		for (Concern concern : values) {
			equallyDistributedWeights.put(concern, aWeight);
		}
		return new Environment("Any", Collections.<Constraint> emptyList(), equallyDistributedWeights);
	}

}
