package ar.uba.dc.thesis.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.thesis.rainbow.constraint.numerical.SumatoryConstraint;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;
import ar.uba.dc.thesis.repository.ArtifactRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class TestSelfHealingScenarioDao implements SelfHealingScenarioDao {

	private static SelfHealingScenario SERVER_COST_SCENARIO;

	private static SelfHealingScenario CLIENT_RESPONSE_TIME_SCENARIO;

	private final List<SelfHealingScenario> scenarios;

	// TODO See if it makes sense or not having *a set* of environments...
	private final Set<Environment> environments = new HashSet<Environment>();

	private final EnvironmentDao environmentDao;

	public TestSelfHealingScenarioDao(EnvironmentDao environmentDao) {
		super();
		this.environmentDao = environmentDao;
		this.scenarios = this.createTestScenarios();
		for (SelfHealingScenario scenario : scenarios) {
			this.environments.addAll(scenario.getEnvironments());
		}
	}

	public List<SelfHealingScenario> getAllScenarios() {
		return scenarios;
	}

	public Set<Environment> getAllEnvironments() {
		return environments;
	}

	private List<SelfHealingScenario> createTestScenarios() {
		List<SelfHealingScenario> testScenarios = new ArrayList<SelfHealingScenario>();
		CLIENT_RESPONSE_TIME_SCENARIO = createClientResponseTimeScenario();
		CLIENT_RESPONSE_TIME_SCENARIO.addRepairStrategy("QuickDirtyReduceResponseTime");

		SERVER_COST_SCENARIO = createServerCostScenario();
		SERVER_COST_SCENARIO.addRepairStrategy("ReduceOverallCost");

		testScenarios.add(CLIENT_RESPONSE_TIME_SCENARIO);
		testScenarios.add(SERVER_COST_SCENARIO);

		return testScenarios;
	}

	private SelfHealingScenario createClientResponseTimeScenario() {
		String scenarioName = "Client Experienced Response Time Scenario";
		String stimulusSource = "Any Client requesting news content";
		Artifact artifact = ArtifactRepository.getClient();
		String stimulus = "GetNewsContentClientStimulus";
		Set<Environment> environments = Collections.singleton(Environment.DEFAULT_ENVIRONMENT);
		String response = "Requested News Content";
		ResponseMeasure responseMeasure = new ResponseMeasure("Experienced response time is within threshold",
				new NumericBinaryRelationalConstraint(artifact, "experRespTime", NumericBinaryOperator.LESS_THAN, 1));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(2L, scenarioName, Concern.RESPONSE_TIME, stimulusSource, stimulus, environments,
				artifact, response, responseMeasure, archDecisions, enabled, priority);
	}

	private SelfHealingScenario createServerCostScenario() {
		String scenarioName = "Server Cost Scenario";
		String stimulusSource = "Anyone";
		Artifact artifact = ArtifactRepository.getServer();
		String stimulus = "ANY";
		Set<Environment> environments = Collections.singleton(environmentDao.getEnvironment("NORMAL"));
		String response = "The proper response for the request";
		ResponseMeasure responseMeasure = new ResponseMeasure("Active servers amount is within threshold",
				new SumatoryConstraint(artifact, "load", new NumericBinaryRelationalConstraint(artifact, "load",
						NumericBinaryOperator.LESS_THAN, 3)));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 2;

		return new SelfHealingScenario(1L, scenarioName, Concern.SERVER_COST, stimulusSource, stimulus, environments,
				artifact, response, responseMeasure, archDecisions, enabled, priority);
	}
}