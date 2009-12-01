package ar.uba.dc.thesis.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.component.znn.Client;
import ar.uba.dc.thesis.component.znn.Proxy;
import ar.uba.dc.thesis.component.znn.Client.GetNewsContentClientOperation;
import ar.uba.dc.thesis.component.znn.Proxy.GetActiveServersAmountOperation;
import ar.uba.dc.thesis.component.znn.Proxy.GetNewsContentProxyOperation;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.Constraint;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class SelfHealingScenarioRepository {

	public static SelfHealingScenario SERVER_COST_SCENARIO = createServerCostScenario();

	public static SelfHealingScenario CLIENT_RESPONSE_TIME_SCENARIO = createClientResponseTimeScenario();

	public static List<SelfHealingScenario> scenarios = new ArrayList<SelfHealingScenario>();

	static {
		SERVER_COST_SCENARIO.addRepairStrategySpec(RepairStrategySpecRepository.getReduceOverallCostRepairStrategy());

		CLIENT_RESPONSE_TIME_SCENARIO.addRepairStrategySpec(RepairStrategySpecRepository
				.getSimpleReduceResponseTimeRepairStrategy());

		scenarios.add(SERVER_COST_SCENARIO);
		scenarios.add(CLIENT_RESPONSE_TIME_SCENARIO);
	}

	private static SelfHealingScenario createServerCostScenario() {
		String scenarioName = "Server Cost Scenario";
		String stimulusSource = "A Server cost analizer";
		Proxy artifact = ComponentRepository.getProxy();
		GetActiveServersAmountOperation stimulus = (GetActiveServersAmountOperation) artifact
				.getOperation(GetActiveServersAmountOperation.class);
		String environment = "Normal";
		String response = "Active servers amount";
		ResponseMeasure responseMeasure = new ResponseMeasure("Active servers amount is within threshold",
				new Constraint(artifact.getName() + ".getActiveServersAmount() < 3"));
		// TODO tipamos la constraint para evitar parsear el string? Ejemplo:
		// new Constraint(artifact.getName(), "getActiveServersAmount()", "<", "3"));
		// el operador deberia ser un ExpressionNode?
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 2;

		return new SelfHealingScenario(scenarioName, Concern.NUMBER_OF_ACTIVE_SERVERS, stimulusSource, stimulus,
				environment, artifact, response, responseMeasure, archDecisions, enabled, priority);
	}

	private static SelfHealingScenario createClientResponseTimeScenario() {
		String scenarioName = "Client Experienced Response Time Scenario";
		String stimulusSource = "Any Client requesting news content";
		Client artifact = ComponentRepository.getClient();
		GetNewsContentProxyOperation stimulus = (GetNewsContentProxyOperation) artifact
				.getOperation(GetNewsContentClientOperation.class);
		String environment = "Normal";
		String response = "Requested News Content";
		ResponseMeasure responseMeasure = new ResponseMeasure("Experienced response time is within 4 seconds",
				new Constraint(artifact.getName() + ".getExperienceResponseTime() < 4"));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(scenarioName, Concern.RESPONSE_TIME, stimulusSource, stimulus, environment,
				artifact, response, responseMeasure, archDecisions, enabled, priority);
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

}
