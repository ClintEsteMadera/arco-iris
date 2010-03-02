package ar.uba.dc.thesis.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.constraint.instance.NumericBinaryRelationalConstraint;
import ar.uba.dc.thesis.rainbow.constraint.operator.NumericBinaryOperator;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public final class SelfHealingScenarioRepository {

	// public static SelfHealingScenario SERVER_COST_SCENARIO;

	public static SelfHealingScenario CLIENT_RESPONSE_TIME_SCENARIO;

	private static final String SCENARIO_SPEC_PATH = "customize.scenario.path";

	public static List<SelfHealingScenario> scenarios;

	static {
		createMockScenarios();
		// loadScenariosFromFile();
	}

	/**
	 * This class is not intended to be extended.
	 */
	private SelfHealingScenarioRepository() {
		super();
	}

	/**
	 * It is assumed that we are not using this Repository for holding scenarios for more than one application.<br>
	 * <b>Be careful!</b>
	 */
	public static Collection<SelfHealingScenario> getEnabledScenarios() {
		// TODO evitar recorrer toda la lista, mantener una lista actualizada de escenarios habilitados
		List<SelfHealingScenario> enabledScenarios = new ArrayList<SelfHealingScenario>();
		for (SelfHealingScenario scenario : scenarios) {
			if (scenario.isEnabled()) {
				enabledScenarios.add(scenario);
			}
		}
		return enabledScenarios;
	}

	private static void createMockScenarios() {
		// SERVER_COST_SCENARIO = createServerCostScenario();
		CLIENT_RESPONSE_TIME_SCENARIO = createClientResponseTimeScenario();

		// SERVER_COST_SCENARIO.addRepairStrategySpec(new RepairStrategySpecification("reduceOverallCost", NO_PARAMS));
		CLIENT_RESPONSE_TIME_SCENARIO.addRepairStrategy("simpleReduceResponseTime");

		scenarios = new ArrayList<SelfHealingScenario>();
		// scenarios.add(SERVER_COST_SCENARIO);
		scenarios.add(CLIENT_RESPONSE_TIME_SCENARIO);
	}

	/**
	 * TODO: This method will be used when we finish the XML parser for scenarios.
	 */
	@SuppressWarnings("unused")
	private static List<SelfHealingScenario> loadScenariosFromFile() {
		@SuppressWarnings("unused")
		File scenarioSpec = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow
				.property(SCENARIO_SPEC_PATH));
		// TODO: Transformar de XML a una Lista de Escenarios.
		return Collections.emptyList();
	}

	@SuppressWarnings("unused")
	private static SelfHealingScenario createServerCostScenario() {
		String scenarioName = "Server Cost Scenario";
		String stimulusSource = "A Server cost analizer";
		Artifact artifact = ArtifactRepository.getProxy();
		String stimulus = "GetActiveServersAmountOperation";
		Environment environment = ScenarioEnvironmentRepository.NORMAL;
		String response = "Active servers amount";
		ResponseMeasure responseMeasure = new ResponseMeasure("Active servers amount is within threshold",
				new NumericBinaryRelationalConstraint("activeServersAmount", NumericBinaryOperator.LESS_THAN, 3));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 2;

		return new SelfHealingScenario(1L, scenarioName, Concern.NUMBER_OF_ACTIVE_SERVERS, stimulusSource, stimulus,
				environment, artifact, response, responseMeasure, archDecisions, enabled, priority);
	}

	private static SelfHealingScenario createClientResponseTimeScenario() {
		String scenarioName = "Client Experienced Response Time Scenario";
		String stimulusSource = "Any Client requesting news content";
		Artifact artifact = ArtifactRepository.getClient();
		String stimulus = "GetNewsContentClientStimulus";
		Environment environment = ScenarioEnvironmentRepository.NORMAL;
		String response = "Requested News Content";
		ResponseMeasure responseMeasure = new ResponseMeasure("Experienced response time is within threshold",
				new NumericBinaryRelationalConstraint("experRespTime", NumericBinaryOperator.LESS_THAN, 10000));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(2L, scenarioName, Concern.RESPONSE_TIME, stimulusSource, stimulus, environment,
				artifact, response, responseMeasure, archDecisions, enabled, priority);
	}
}