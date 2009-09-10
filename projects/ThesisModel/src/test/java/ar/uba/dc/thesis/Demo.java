package ar.uba.dc.thesis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.acme.Architecture;
import ar.uba.dc.thesis.acme.Component;
import ar.uba.dc.thesis.acme.Operation;
import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.rainbow.RepairHandler;
import ar.uba.dc.thesis.selfhealing.RepairStrategy;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;
import ar.uba.dc.thesis.selfhealing.SelfHealingTradeoff;

public class Demo {

	private static final String DISABLE_FLASH_OPERATION_NAME = "disableFlash";

	private static final String DISABLE_VIDEOS_OPERATION_NAME = "disableVideos";

	private static final String PAGE_RENDERER_NAME = "Page Renderer";

	private static final String RENDER_PAGE_OPERATION_NAME = "renderPage";

	private static Architecture architecture;

	public static void main(String[] args) {
		createArchitecture();
		createRepairStrategies();

		SelfHealingScenario heavyLoadScenario = getHeavyLoadScenario();
		SelfHealingScenario usabilityScenario = getUsabilityScenario();

		addRepairStrategy(heavyLoadScenario, usabilityScenario, "disableVideos");
		addRepairStrategy(heavyLoadScenario, usabilityScenario,
				"disableDynamicContent");

		// TODO Continue from here... with...?
		// Supongamos que se rompio heavyLoadScenario y lo detectamos:
		RepairHandler.getInstance().repairScenario(heavyLoadScenario);

	}

	private static void createArchitecture() {
		architecture = new Architecture();
		architecture.addComponent(getPageRenderer());
	}

	private static void addRepairStrategy(SelfHealingScenario scenarioToRepair,
			SelfHealingScenario affectedScenario, String repairStrategyName) {
		RepairStrategy strategy = RepairStrategyRepository
				.getRepairStrategy(repairStrategyName);

		SelfHealingTradeoff tradeoff = new SelfHealingTradeoff(Collections
				.singletonList(affectedScenario), Collections
				.<SelfHealingScenario> emptyList());

		scenarioToRepair.addRepairStrategy(strategy, tradeoff);
	}

	private static SelfHealingScenario getUsabilityScenario() {
		String scenarioName = "Page loaded with all content enabled";
		String stimulusSource = "Simple User";
		String environment = "Normal";
		Component pagerRenderer = architecture.getComponent(PAGE_RENDERER_NAME);
		Collection<Artifact> components = Collections
				.<Artifact> singletonList(pagerRenderer);
		String response = "Page loaded with all content enabled";
		ResponseMeasure responseMeasure = new ResponseMeasure(
				"Page loaded with all content enabled", PAGE_RENDERER_NAME
						+ ".allContentEnabled == true");
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(scenarioName, Concern.RESPONSE_TIME,
				stimulusSource, pagerRenderer
						.getOperation(RENDER_PAGE_OPERATION_NAME), environment,
				components, response, responseMeasure, archDecisions, enabled,
				priority);
	}

	private static SelfHealingScenario getHeavyLoadScenario() {
		String scenarioName = "Page loaded - Heavy Load";
		String stimulusSource = "Simple User";
		String environment = "Heavy Load";
		Component pageRenderer = getPageRenderer();
		Collection<Artifact> components = Collections
				.<Artifact> singletonList(pageRenderer);
		String response = "Page fully loaded";
		ResponseMeasure responseMeasure = new ResponseMeasure(
				"Page loaded in less than 5 seconds", PAGE_RENDERER_NAME
						+ ".responseTime<= 5000ms");
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(scenarioName, Concern.RESPONSE_TIME,
				stimulusSource, pageRenderer
						.getOperation(RENDER_PAGE_OPERATION_NAME), environment,
				components, response, responseMeasure, archDecisions, enabled,
				priority);
	}

	private static Component getPageRenderer() {
		Collection<Operation> operations = new ArrayList<Operation>();
		operations.add(new Operation(RENDER_PAGE_OPERATION_NAME));
		return new Component(PAGE_RENDERER_NAME, operations);
	}

	private static void createRepairStrategies() {
		String code = PAGE_RENDERER_NAME + "." + DISABLE_VIDEOS_OPERATION_NAME
				+ ";" + PAGE_RENDERER_NAME + "." + DISABLE_FLASH_OPERATION_NAME;
		RepairStrategy disableDynamicContent = new RepairStrategy(
				"disableDynamicContent", code);

		RepairStrategyRepository.addRepairStrategy(disableDynamicContent);

		code = PAGE_RENDERER_NAME + "." + DISABLE_VIDEOS_OPERATION_NAME + ";";
		RepairStrategy disableVideos = new RepairStrategy("disableVideos", code);

		RepairStrategyRepository.addRepairStrategy(disableVideos);
	}

}
