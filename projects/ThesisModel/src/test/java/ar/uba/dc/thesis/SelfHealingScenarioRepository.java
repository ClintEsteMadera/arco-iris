package ar.uba.dc.thesis;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.acme.Component;
import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class SelfHealingScenarioRepository {

	public static SelfHealingScenario USABILITY_SCENARIO = createUsabilityScenario();

	public static SelfHealingScenario HEAVY_LOAD_SCENARIO = createHeavyLoadScenario();

	static {
		HEAVY_LOAD_SCENARIO.addRepairStrategySpec(RepairStrategySpecRepository.getDisableVideosRepairStrategy());
		HEAVY_LOAD_SCENARIO
				.addRepairStrategySpec(RepairStrategySpecRepository.getDisableDynamicContentRepairStrategy());

	}

	private static SelfHealingScenario createUsabilityScenario() {
		String scenarioName = "Page loaded with all content enabled";
		String stimulusSource = "Simple User";
		String environment = "Normal";
		Component pagerRenderer = ComponentRepository.getPageRenderer();
		Collection<Artifact> components = Collections.<Artifact> singletonList(pagerRenderer);
		String response = "Page loaded with all content enabled";
		ResponseMeasure responseMeasure = new ResponseMeasure("Page loaded with all content enabled", pagerRenderer
				.getName()
				+ ".allContentEnabled == true");
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(scenarioName, Concern.RESPONSE_TIME, stimulusSource, pagerRenderer
				.getOperation(ComponentRepository.RENDER_PAGE_OPERATION_NAME), environment, components, response,
				responseMeasure, archDecisions, enabled, priority);
	}

	private static SelfHealingScenario createHeavyLoadScenario() {
		String scenarioName = "Page loaded - Heavy Load";
		String stimulusSource = "Simple User";
		String environment = "Heavy Load";
		Component pageRenderer = ComponentRepository.getPageRenderer();
		Collection<Artifact> components = Collections.<Artifact> singletonList(pageRenderer);
		String response = "Page fully loaded";
		ResponseMeasure responseMeasure = new ResponseMeasure("Page loaded in less than 5 seconds", pageRenderer
				.getName()
				+ ".responseTime<= 5000ms");
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(scenarioName, Concern.RESPONSE_TIME, stimulusSource, pageRenderer
				.getOperation(ComponentRepository.RENDER_PAGE_OPERATION_NAME), environment, components, response,
				responseMeasure, archDecisions, enabled, priority);
	}

}
