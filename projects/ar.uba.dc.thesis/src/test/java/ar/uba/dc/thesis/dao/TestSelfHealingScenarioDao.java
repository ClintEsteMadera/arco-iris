package ar.uba.dc.thesis.dao;

import java.util.Arrays;
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

	// This value is the same as the one specified in ZNewsSys.acme
	private static final double MAX_UTIL = 0.75;

	// This value is the same as the one specified in ZNewsSys.acme
	private static final int THRESHOLD_FIDELITY = 2;

	private static final String REQUESTED_NEWS_CONTENT_DESCRIPTION = "Requested News Content";

	private static final String GET_NEWS_CONTENT_CLIENT_STIMULUS_NAME = "GetNewsContentClientStimulus";

	private static final String NEWS_CONTENT_REQUEST_DESCRIPTION = "Any Client requesting news content";

	private final List<SelfHealingScenario> scenarios;

	// TODO See whether it makes sense or not having *a set* of environments...
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
		// TODO Add more repair strategies for this one
		SelfHealingScenario clientResponseTimeScenario = createClientResponseTimeScenario().addRepairStrategy(
				"SimpleReduceResponseTime").addRepairStrategy("BruteReduceResponseTime");

		SelfHealingScenario serverCostScenario = createServerCostScenario().addRepairStrategy("ReduceOverallCost");

		SelfHealingScenario fidelityScenario = createFidelityScenario().addRepairStrategy("ImproveOverallFidelity");

		return Arrays.asList(clientResponseTimeScenario, serverCostScenario, fidelityScenario);
	}

	private SelfHealingScenario createClientResponseTimeScenario() {
		String scenarioName = "Client Experienced Response Time Scenario";
		String stimulusSource = NEWS_CONTENT_REQUEST_DESCRIPTION;
		Artifact artifact = ArtifactRepository.getClient();
		String stimulus = GET_NEWS_CONTENT_CLIENT_STIMULUS_NAME;
		Set<Environment> environments = Collections.singleton(Environment.ANY_ENVIRONMENT);
		String response = REQUESTED_NEWS_CONTENT_DESCRIPTION;
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
						NumericBinaryOperator.LESS_THAN, MAX_UTIL)));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 2;

		return new SelfHealingScenario(2L, scenarioName, Concern.SERVER_COST, stimulusSource, stimulus, environments,
				artifact, response, responseMeasure, archDecisions, enabled, priority);
	}

	private SelfHealingScenario createFidelityScenario() {
		String scenarioName = "Fidelity Scenario";
		String stimulusSource = NEWS_CONTENT_REQUEST_DESCRIPTION;
		Artifact artifact = ArtifactRepository.getServer();
		String stimulus = GET_NEWS_CONTENT_CLIENT_STIMULUS_NAME;
		Set<Environment> environments = Collections.singleton(Environment.ANY_ENVIRONMENT);
		String response = REQUESTED_NEWS_CONTENT_DESCRIPTION;
		ResponseMeasure responseMeasure = new ResponseMeasure("Content Fidelity is within threshold",
				new SumatoryConstraint(artifact, "fidelity", new NumericBinaryRelationalConstraint(artifact,
						"fidelity", NumericBinaryOperator.LESS_THAN, THRESHOLD_FIDELITY)));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 3;

		return new SelfHealingScenario(3L, scenarioName, Concern.CONTENT_FIDELITY, stimulusSource, stimulus,
				environments, artifact, response, responseMeasure, archDecisions, enabled, priority);
	}
}