package ar.uba.dc.thesis.dao;

import java.util.List;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

/**
 * This dao only delegates to the specific dao.
 * 
 */
public class TestSelfHealingConfigurationDao implements SelfHealingConfigurationDao {

	private final TestEnvironmentDao environmentDao;

	private final TestSelfHealingScenarioDao scenarioDao;

	private TestArtifactDao artifactDao;

	public TestSelfHealingConfigurationDao() {
		super();
		this.environmentDao = new TestEnvironmentDao();
		this.scenarioDao = new TestSelfHealingScenarioDao();
		this.artifactDao = new TestArtifactDao();
	}

	@Override
	public List<SelfHealingScenario> getAllScenarios() {
		return this.scenarioDao.getAllScenarios();
	}

	@Override
	public List<Environment> getAllNonDefaultEnvironments() {
		return this.environmentDao.getAllNonDefaultEnvironments();
	}

	@Override
	public Environment getEnvironment(String name) {
		return this.environmentDao.getEnvironment(name);
	}

	@Override
	public List<Artifact> getAllArtifacts() {
		return this.artifactDao.getAllArtifacts();
	}
}