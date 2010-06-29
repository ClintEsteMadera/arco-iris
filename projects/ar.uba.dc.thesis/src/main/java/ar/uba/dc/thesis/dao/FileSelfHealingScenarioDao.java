package ar.uba.dc.thesis.dao;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingScenarioPersister;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class FileSelfHealingScenarioDao implements SelfHealingScenarioDao {

	private static final String SCENARIO_SPEC_PATH = "customize.scenarios.path";

	public List<SelfHealingScenario> getAllScenarios() {
		File scenarioSpec = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow
				.property(SCENARIO_SPEC_PATH));
		SelfHealingScenarioPersister persister = new SelfHealingScenarioPersister();
		SelfHealingConfiguration scenariosConfig = persister.readFromFile(scenarioSpec.getAbsolutePath());
		return scenariosConfig.getScenarios() != null ? scenariosConfig.getScenarios()
				: new ArrayList<SelfHealingScenario>();
	}
}
