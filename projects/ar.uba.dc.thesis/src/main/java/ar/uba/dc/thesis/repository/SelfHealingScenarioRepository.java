package ar.uba.dc.thesis.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.component.znn.Proxy;
import ar.uba.dc.thesis.component.znn.Proxy.GetActiveServersAmount;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.Constraint;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class SelfHealingScenarioRepository {

	// TODO Agregar al menos otro escenario de acuerdo a lo escrito en el paper de znn

	public static SelfHealingScenario SERVER_COST_SCENARIO = createServerCostScenario();

	public static List<SelfHealingScenario> scenarios = new ArrayList<SelfHealingScenario>();

	static {
		SERVER_COST_SCENARIO.addRepairStrategySpec(RepairStrategySpecRepository.getReduceOverallCostRepairStrategy());
		scenarios.add(SERVER_COST_SCENARIO);
	}

	private static SelfHealingScenario createServerCostScenario() {
		String scenarioName = "Server Cost Scenario";
		String stimulusSource = "A Server cost analizer";
		Proxy proxy = ComponentRepository.getProxy();
		GetActiveServersAmount stimulus = (GetActiveServersAmount) proxy.getOperation(GetActiveServersAmount.class
				.getSimpleName());
		String environment = "Normal";
		String response = "Active servers amount";
		ResponseMeasure responseMeasure = new ResponseMeasure("Active servers amount is within threshold",
				new Constraint(proxy.getName() + ".getActiveServersAmount() < 3"));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 2;

		return new SelfHealingScenario(scenarioName, Concern.NUMBER_OF_ACTIVE_SERVERS, stimulusSource, stimulus,
				environment, proxy, response, responseMeasure, archDecisions, enabled, priority);
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
