package ar.uba.dc.thesis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.acme.Operation;
import ar.uba.dc.thesis.atam.ArchitecturalDecision;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.ResponseMeasure;
import ar.uba.dc.thesis.qa.Concern;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class Demo {

	private static final String LOAD_HOME_PAGE = "loadHomePage";

	public static void main(String[] args) {
		SelfHealingScenario scenario = getSelfHealingScenario();

		// TODO Continue from here...
	}

	private static SelfHealingScenario getSelfHealingScenario() {
		String scenarioName = "Home Page - Heavy Load";
		String stimulusSource = "Simple User";
		String environment = "Heavy Load";
		Artifact webServer = getWebServer();
		List<Artifact> artifacts = Collections.singletonList(webServer);
		String response = "Home Page fully loaded";
		ResponseMeasure responseMeasure = new ResponseMeasure(
				"Home Page loaded in less than 10 seconds", "invariant");
		List<ArchitecturalDecision> archDecisions = Collections.emptyList();
		boolean enabled = true;
		int priority = 1;

		return new SelfHealingScenario(scenarioName, Concern.RESPONSE_TIME, stimulusSource,
				webServer.getOperation(LOAD_HOME_PAGE), environment, artifacts, response,
				responseMeasure, archDecisions, enabled, priority);
	}

	private static Artifact getWebServer() {
		Collection<Operation> operations = new ArrayList<Operation>();
		operations.add(new Operation(LOAD_HOME_PAGE));
		return new Artifact("Web Server", operations);
	}
}
