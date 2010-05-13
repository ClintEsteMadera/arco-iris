package ar.uba.dc.thesis.dao;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;

public class TestEnvironmentDao implements EnvironmentDao {

	private final Collection<Environment> environments = new HashSet<Environment>();

	private static final List<Constraint> EMPTY_CONDITION_LIST = Collections.emptyList();

	public TestEnvironmentDao() {
		this.environments.add(createNormalEnvironment());
		this.environments.add(createHighLoadEnvironment());
	}

	public Collection<Environment> getAllNonDefaultEnvironments() {
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
		normalConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.6D);
		normalConcernMultiplierMap.put(Concern.CONTENT_FIDELITY, 0.3D);
		normalConcernMultiplierMap.put(Concern.SERVER_COST, 0.1D);
		Environment normalEnv = new Environment("NORMAL", EMPTY_CONDITION_LIST, normalConcernMultiplierMap);
		return normalEnv;
	}

	private Environment createHighLoadEnvironment() {
		Map<Concern, Double> highLoadConcernMultiplierMap = new HashMap<Concern, Double>();
		highLoadConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.8D);
		highLoadConcernMultiplierMap.put(Concern.SERVER_COST, 0.2D);
		highLoadConcernMultiplierMap.put(Concern.CONTENT_FIDELITY, 0.0D); // this one SHOULD exist in the map

		Environment highLoadEnv = new Environment("HIGH LOAD", EMPTY_CONDITION_LIST, highLoadConcernMultiplierMap);
		return highLoadEnv;
	}
}
