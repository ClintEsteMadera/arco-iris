package ar.uba.dc.arcoiris.dao;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.arcoiris.atam.scenario.model.AnyEnvironment;
import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.atam.scenario.model.ResponseMeasure;
import ar.uba.dc.arcoiris.atam.scenario.model.Stimulus;
import ar.uba.dc.arcoiris.qa.Concern;
import ar.uba.dc.arcoiris.rainbow.constraint.numerical.NumericBinaryRelationalConstraint;
import ar.uba.dc.arcoiris.rainbow.constraint.numerical.Quantifier;
import ar.uba.dc.arcoiris.rainbow.constraint.operator.NumericBinaryOperator;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;
import ar.uba.dc.arcoiris.selfhealing.repair.SpecificRepairStrategies;
import ar.uba.dc.arcoiris.util.Collections;

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
		SelfHealingScenario serverCostScenario = createServerCostScenario();

		return Collections.createList(clientResponseTimeScenario, serverCostScenario);
	}

	private SelfHealingScenario createClientResponseTimeScenario() {
		String scenarioName = "Client Experienced Response Time Scenario";
		Artifact artifact = TestArtifactDao.CLIENT;
		Stimulus stimulus = new Stimulus(GET_NEWS_CONTENT_CLIENT_STIMULUS_NAME, NEWS_CONTENT_REQUEST_DESCRIPTION);
		List<AnyEnvironment> environments = Collections.createList(AnyEnvironment.getInstance());
		String response = REQUESTED_NEWS_CONTENT_DESCRIPTION;
		ResponseMeasure responseMeasure = new ResponseMeasure("Experienced response time is within threshold",
				new NumericBinaryRelationalConstraint(artifact, "experRespTime", NumericBinaryOperator.LESS_THAN,
						THRESHOLD_RESPONSE_TIME));
		boolean enabled = true;
		int priority = 1;
		SpecificRepairStrategies repairStrategies = new SpecificRepairStrategies("VariedReduceResponseTime");

		return new SelfHealingScenario(0L, scenarioName, Concern.RESPONSE_TIME, stimulus, environments, artifact,
				response, responseMeasure, enabled, priority, repairStrategies);
	}

	private SelfHealingScenario createServerCostScenario() {
		String scenarioName = "Server Cost Scenario";
		Artifact artifact = TestArtifactDao.SERVER;
		Stimulus stimulus = Stimulus.ANY;
		List<AnyEnvironment> environments = Collections.createList(AnyEnvironment.getInstance());
		String response = "The proper response for the request";
		ResponseMeasure responseMeasure = new ResponseMeasure("Active servers amount is within threshold",
				new NumericBinaryRelationalConstraint(Quantifier.SUM, artifact, "cost",
						NumericBinaryOperator.LESS_THAN, THRESHOLD_SERVER_COST));

		boolean enabled = true;
		int priority = 2;
		SpecificRepairStrategies repairStrategies = new SpecificRepairStrategies("ReduceOverallCost");

		return new SelfHealingScenario(1L, scenarioName, Concern.SERVER_COST, stimulus, environments, artifact,
				response, responseMeasure, enabled, priority, repairStrategies);
	}
}