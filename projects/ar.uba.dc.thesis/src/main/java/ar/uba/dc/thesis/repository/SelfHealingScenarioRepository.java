package ar.uba.dc.thesis.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.acme.Component;
import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.component.dummy.PageRenderer;
import ar.uba.dc.thesis.component.dummy.PageRenderer.RenderPage;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.Constraint;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class SelfHealingScenarioRepository {

	public static SelfHealingScenario USABILITY_SCENARIO = createUsabilityScenario();

	public static SelfHealingScenario HEAVY_LOAD_SCENARIO = createHeavyLoadScenario();

	public static List<SelfHealingScenario> scenarios = new ArrayList<SelfHealingScenario>();
	static {
		HEAVY_LOAD_SCENARIO.addRepairStrategySpec(RepairStrategySpecRepository.getDisableVideosRepairStrategy());
		HEAVY_LOAD_SCENARIO
				.addRepairStrategySpec(RepairStrategySpecRepository.getDisableDynamicContentRepairStrategy());
		scenarios.add(USABILITY_SCENARIO);
		scenarios.add(HEAVY_LOAD_SCENARIO);
	}

	private static SelfHealingScenario createUsabilityScenario() {
		String scenarioName = "Page loaded with all content enabled";
		String stimulusSource = "Simple User";
		String environment = "Normal";
		PageRenderer pageRenderer = ComponentRepository.getPageRenderer();
		String response = "Page loaded with all content enabled";
		ResponseMeasure responseMeasure = new ResponseMeasure("Page loaded with all content enabled", new Constraint(
				pageRenderer.getName() + ".allContentEnabled == true"));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(scenarioName, Concern.RESPONSE_TIME, stimulusSource, pageRenderer
				.getOperation(RenderPage.class.getSimpleName()), environment, pageRenderer, response, responseMeasure,
				archDecisions, enabled, priority);
	}

	private static SelfHealingScenario createHeavyLoadScenario() {
		String scenarioName = "Page loaded - Heavy Load";
		String stimulusSource = "Simple User";
		String environment = "Heavy Load";
		Component pageRenderer = ComponentRepository.getPageRenderer();
		String response = "Page fully loaded";
		ResponseMeasure responseMeasure = new ResponseMeasure("Page loaded in less than 5 seconds", new Constraint(
				pageRenderer.getName() + ".responseTime<= 5000ms"));
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(scenarioName, Concern.RESPONSE_TIME, stimulusSource, pageRenderer
				.getOperation(RenderPage.class.getSimpleName()), environment, pageRenderer, response, responseMeasure,
				archDecisions, enabled, priority);
	}

	public static Collection<SelfHealingScenario> getEnabledScenarios() {
		//TODO evitar recorrer toda la lista, mantener una lista actualizada de escenarios habilitados
		List<SelfHealingScenario> enabledScenarios = new ArrayList<SelfHealingScenario>();
		for (SelfHealingScenario scenario : scenarios) {
			if (scenario.isEnabled()) {
				enabledScenarios.add(scenario);
			}
		}
		return enabledScenarios;
	}

}
