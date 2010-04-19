package ar.uba.dc.thesis.repository;

import java.util.Collection;

import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.dao.ScenarioEnvironmentDao;

public final class ScenarioEnvironmentRepository {

	private final ScenarioEnvironmentDao scenarioEnvironmentDao;

	public ScenarioEnvironmentRepository(ScenarioEnvironmentDao scenarioEnvironmentDao) {
		super();
		this.scenarioEnvironmentDao = scenarioEnvironmentDao;
	}

	public Collection<Environment> getAllEnvironments() {
		return this.scenarioEnvironmentDao.getAllEnvironments();
	}

	public Environment getEnvironment(String name) {
		return this.scenarioEnvironmentDao.getEnvironment(name);
	}

}
