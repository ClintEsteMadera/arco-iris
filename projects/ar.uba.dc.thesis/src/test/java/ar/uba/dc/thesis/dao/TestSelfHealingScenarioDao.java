package ar.uba.dc.thesis.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ar.uba.dc.thesis.atam.scenario.model.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.atam.scenario.model.DefaultEnvironment;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.atam.scenario.model.ResponseMeasure;
import ar.uba.dc.thesis.atam.scenario.model.Stimulus;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;
import ar.uba.dc.thesis.util.Collections;

public class TestSelfHealingScenarioDao {

	private static final int THRESHOLD_RESPONSE_TIME = 500; /* ms. */

	// This value is the same as the one specified in ZNewsSys.acme
	private static final int THRESHOLD_SERVER_COST = 4;

	private static final String REQUESTED_NEWS_CONTENT_DESCRIPTION = "Requested News Content";

	private static final String GET_NEWS_CONTENT_CLIENT_STIMULUS_NAME = "GetNewsContentClientStimulus";

	private static final String NEWS_CONTENT_REQUEST_DESCRIPTION = "Any Client requesting news content";

	private final List<SelfHealingScenario> scenarios;

	private final List<Environment> environments = new ArrayList<Environment>();

	public TestSelfHealingScenarioDao() {
		super();
		this.scenarios = this.createTestScenarios();
		for (SelfHealingScenario scenario : scenarios) {
			this.environments.addAll(scenario.getEnvironments());
		}
	}

	public List<SelfHealingScenario> getAllScenarios() {
		return scenarios;
	}

	public List<Environment> getAllEnvironments() {
		return environments;
	}

	private List<SelfHealingScenario> createTestScenarios() {
		SelfHealingScenario clientResponseTimeScenario = createClientResponseTimeScenario();
		clientResponseTimeScenario.addRepairStrategy("VariedReduceResponseTime");

		SelfHealingScenario serverCostScenario = createServerCostScenario();
		serverCostScenario.addRepairStrategy("ReduceOverallCost");

		return Collections.createList(clientResponseTimeScenario, serverCostScenario);
	}

	private SelfHealingScenario createClientResponseTimeScenario() {
		String scenarioName = "Client Experienced Response Time Scenario";
		Artifact artifact = TestArtifactDao.CLIENT;
		Stimulus stimulus = new Stimulus(GET_NEWS_CONTENT_CLIENT_STIMULUS_NAME, NEWS_CONTENT_REQUEST_DESCRIPTION);
		List<DefaultEnvironment> environments = Collections.createList(DefaultEnvironment.getInstance());
		String response = REQUESTED_NEWS_CONTENT_DESCRIPTION;
		ResponseMeasure responseMeasure = new ResponseMeasure("Experienced response time is within threshold",
				new NumericBinaryRelationalConstraint(artifact, "experRespTime", NumericBinaryOperator.LESS_THAN,
						THRESHOLD_RESPONSE_TIME));
		Set<ArchitecturalDecision> archDecisions = new HashSet<ArchitecturalDecision>();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(0L, scenarioName, Concern.RESPONSE_TIME, stimulus, environments, artifact,
				response, responseMeasure, archDecisions, enabled, priority);
	}

	private SelfHealingScenario createServerCostScenario() {
		String scenarioName = "Server Cost Scenario";
		Artifact artifact = TestArtifactDao.SERVER;
		Stimulus stimulus = Stimulus.ANY;
		List<DefaultEnvironment> environments = Collections.createList(DefaultEnvironment.getInstance());
		String response = "The proper response for the request";
		ResponseMeasure responseMeasure = new ResponseMeasure("Active servers amount is within threshold",
				new NumericBinaryRelationalConstraint(artifact, "cost", NumericBinaryOperator.LESS_THAN,
						THRESHOLD_SERVER_COST, true));
		Set<ArchitecturalDecision> archDecisions = new HashSet<ArchitecturalDecision>();
		boolean enabled = true;
		int priority = 2;

		return new SelfHealingScenario(1L, scenarioName, Concern.SERVER_COST, stimulus, environments, artifact,
				response, responseMeasure, archDecisions, enabled, priority);
	}
}