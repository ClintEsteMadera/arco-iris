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
import ar.uba.dc.thesis.rainbow.constraint.instance.NumericBinaryRelationalConstraint;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;
import ar.uba.dc.thesis.repository.ArtifactRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class TestSelfHealingScenarioDao implements SelfHealingScenarioDao {

	// private static SelfHealingScenario SERVER_COST_SCENARIO;

	private static SelfHealingScenario CLIENT_RESPONSE_TIME_SCENARIO;

	private final List<SelfHealingScenario> scenarios;

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
		// SERVER_COST_SCENARIO = createServerCostScenario();
		CLIENT_RESPONSE_TIME_SCENARIO = createClientResponseTimeScenario();

		// SERVER_COST_SCENARIO.addRepairStrategySpec(new RepairStrategySpecification("reduceOverallCost", NO_PARAMS));
		CLIENT_RESPONSE_TIME_SCENARIO.addRepairStrategy("QuickDirtyReduceResponseTime");

		// scenarios.add(SERVER_COST_SCENARIO);
		testScenarios.add(CLIENT_RESPONSE_TIME_SCENARIO);

		return testScenarios;
	}

	@SuppressWarnings("unused")
	private SelfHealingScenario createServerCostScenario() {
		String scenarioName = "Server Cost Scenario";
		String stimulusSource = "A Server cost analizer";
		Artifact artifact = ArtifactRepository.getProxy();
		String stimulus = "GetActiveServersAmountOperation";
		Set<Environment> environments = new HashSet<Environment>();
		environments.add(environmentDao.getEnvironment("NORMAL"));
		String response = "Active servers amount";
		ResponseMeasure responseMeasure = new ResponseMeasure("Active servers amount is within threshold",
				new NumericBinaryRelationalConstraint(artifact, "activeServersAmount", NumericBinaryOperator.LESS_THAN,
						3));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 2;

		return new SelfHealingScenario(1L, scenarioName, Concern.SERVER_COST, stimulusSource, stimulus,
				environments, artifact, response, responseMeasure, archDecisions, enabled, priority);
	}

	private SelfHealingScenario createClientResponseTimeScenario() {
		String scenarioName = "Client Experienced Response Time Scenario";
		String stimulusSource = "Any Client requesting news content";
		Artifact artifact = ArtifactRepository.getClient();
		String stimulus = "GetNewsContentClientStimulus";
		Set<Environment> environments = new HashSet<Environment>();
		environments.add(environmentDao.getEnvironment("NORMAL"));
		String response = "Requested News Content";
		ResponseMeasure responseMeasure = new ResponseMeasure("Experienced response time is within threshold",
				new NumericBinaryRelationalConstraint(artifact, "experRespTime", NumericBinaryOperator.LESS_THAN, 1));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(2L, scenarioName, Concern.RESPONSE_TIME, stimulusSource, stimulus, environments,
				artifact, response, responseMeasure, archDecisions, enabled, priority);
	}
}