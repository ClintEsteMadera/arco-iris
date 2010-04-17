package ar.uba.dc.thesis.dao;

import java.io.File;
import java.util.List;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class FileSelfHealingScenarioDao implements SelfHealingScenarioDao {

	private static final String SCENARIO_SPEC_PATH = "customize.scenario.path";

	public List<SelfHealingScenario> getAllScenarios() {
		@SuppressWarnings("unused")
		File scenarioSpec = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow
				.property(SCENARIO_SPEC_PATH));

		throw new RuntimeException("This method is not implemented yet");
	}
}
