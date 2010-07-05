package ar.uba.dc.thesis.dao;

import java.io.File;
import java.util.List;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingScenarioPersister;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class FileSelfHealingConfigurationDao implements SelfHealingConfigurationDao {

	private static final String SCENARIO_SPEC_PATH = "customize.scenarios.path";

	private SelfHealingConfiguration scenariosConfig;

	public FileSelfHealingConfigurationDao() {
		super();
		File scenarioSpec = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow
				.property(SCENARIO_SPEC_PATH));
		SelfHealingScenarioPersister persister = new SelfHealingScenarioPersister();
		this.scenariosConfig = persister.readFromFile(scenarioSpec.getAbsolutePath());
	}

	public List<SelfHealingScenario> getAllScenarios() {
		return this.scenariosConfig.getScenarios();
	}

	@Override
	public List<Environment> getAllNonDefaultEnvironments() {
		return this.scenariosConfig.getEnvironments();
	}

	@Override
	public Environment getEnvironment(String name) {
		for (Environment environment : this.getAllNonDefaultEnvironments()) {
			if (environment.getName().equals(name)) {
				return environment;
			}
		}
		throw new RuntimeException("Environment " + name + " not defined.");
	}

	@Override
	public List<Artifact> getAllArtifacts() {
		return this.scenariosConfig.getArtifacts();
	}
}
