package ar.uba.dc.thesis.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;
import ar.uba.dc.thesis.util.Collections;

public class TestEnvironmentDao {
	private static final int HIGH_LOAD_RESPONSE_TIME_THRESHOLD = 800; /* ms. */

	private final List<Environment> environments;

	public TestEnvironmentDao() {
		super();
		this.environments = new ArrayList<Environment>();
		this.environments.add(createNormalEnvironment());
		this.environments.add(createHighLoadEnvironment());
	}

	public List<Environment> getAllEnvironments() {
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
		SortedMap<Concern, Double> normalConcernMultiplierMap = new TreeMap<Concern, Double>();
		normalConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.333D);
		normalConcernMultiplierMap.put(Concern.CONTENT_FIDELITY, 0.333D);
		normalConcernMultiplierMap.put(Concern.SERVER_COST, 0.333D);

		Constraint normalResponseTimeCondition = new NumericBinaryRelationalConstraint(TestArtifactDao.CLIENT,
				"experRespTime", NumericBinaryOperator.LESS_THAN, HIGH_LOAD_RESPONSE_TIME_THRESHOLD);

		List<Constraint> normalResponseTimeConditions = Collections.createList(normalResponseTimeCondition);

		return new Environment(0L, "NORMAL", normalResponseTimeConditions, normalConcernMultiplierMap);
	}

	private Environment createHighLoadEnvironment() {
		SortedMap<Concern, Double> highLoadConcernMultiplierMap = new TreeMap<Concern, Double>();
		highLoadConcernMultiplierMap.put(Concern.RESPONSE_TIME, 0.7D);
		highLoadConcernMultiplierMap.put(Concern.SERVER_COST, 0.2D);
		highLoadConcernMultiplierMap.put(Concern.CONTENT_FIDELITY, 0.1D); // this one SHOULD exist in the map

		Constraint highResponseTimeCondition = new NumericBinaryRelationalConstraint(TestArtifactDao.CLIENT,
				"experRespTime", NumericBinaryOperator.GREATER_THAN, HIGH_LOAD_RESPONSE_TIME_THRESHOLD);

		List<Constraint> highResponseTimeConditions = Collections.createList(highResponseTimeCondition);

		return new Environment(1L, "HIGH LOAD", highResponseTimeConditions, highLoadConcernMultiplierMap);
	}
}
