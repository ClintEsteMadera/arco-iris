package ar.uba.dc.thesis.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.Quantifier;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;
import ar.uba.dc.thesis.repository.ArtifactRepository;

public class TestEnvironmentDao implements EnvironmentDao {

	private final Collection<Environment> environments = new HashSet<Environment>();

	private static final List<Constraint> HIGH_RESPONSE_TIME_CONDITIONS_LIST = new ArrayList<Constraint>(0);

	private static final List<Constraint> NORMAL_RESPONSE_TIME_CONDITIONS_LIST = new ArrayList<Constraint>(0);

	private static final int HIGH_LOAD_RESPONSE_TIME_THRESHOLD = 800; /* ms. */

	public TestEnvironmentDao() {
		this.environments.add(createNormalEnvironment());
		this.environments.add(createHighLoadEnvironment());
		Constraint highResponseTimeCondition = new NumericBinaryRelationalConstraint(Quantifier.IN_AVERAGE,
				ArtifactRepository.getClient(), "experRespTime", NumericBinaryOperator.GREATER_THAN,
				HIGH_LOAD_RESPONSE_TIME_THRESHOLD);
		HIGH_RESPONSE_TIME_CONDITIONS_LIST.add(highResponseTimeCondition);
		Constraint normalResponseTimeCondition = new NumericBinaryRelationalConstraint(Quantifier.IN_AVERAGE,
				ArtifactRepository.getClient(), "experRespTime", NumericBinaryOperator.LESS_THAN,
				HIGH_LOAD_RESPONSE_TIME_THRESHOLD);
		NORMAL_RESPONSE_TIME_CONDITIONS_LIST.add(normalResponseTimeCondition);
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

	// TODO setear las condiciones de los environments(deben ser excluyentes)
	private Environment createNormalEnvironment() {
		Map<Concern, Double> normalConcernMultiplierMap = new HashMap<Concern, Double>();
		normalConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.333D);
		normalConcernMultiplierMap.put(Concern.CONTENT_FIDELITY, 0.333D);
		normalConcernMultiplierMap.put(Concern.SERVER_COST, 0.333D);
		Environment normalEnv = new Environment("NORMAL", NORMAL_RESPONSE_TIME_CONDITIONS_LIST,
				normalConcernMultiplierMap);
		return normalEnv;
	}

	private Environment createHighLoadEnvironment() {
		Map<Concern, Double> highLoadConcernMultiplierMap = new HashMap<Concern, Double>();
		highLoadConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.7D);
		highLoadConcernMultiplierMap.put(Concern.SERVER_COST, 0.2D);
		highLoadConcernMultiplierMap.put(Concern.CONTENT_FIDELITY, 0.1D); // this one SHOULD exist in the map

		Environment highLoadEnv = new Environment("HIGH LOAD", HIGH_RESPONSE_TIME_CONDITIONS_LIST,
				highLoadConcernMultiplierMap);
		return highLoadEnv;
	}
}
