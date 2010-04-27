package ar.uba.dc.thesis.repository;

import java.util.Collection;

import ar.uba.dc.thesis.atam.Environment;
import ar.uba.dc.thesis.dao.EnvironmentDao;

public final class EnvironmentRepository {

	private final EnvironmentDao scenarioEnvironmentDao;

	public EnvironmentRepository(EnvironmentDao scenarioEnvironmentDao) {
		super();
		this.scenarioEnvironmentDao = scenarioEnvironmentDao;
	}

	public Collection<Environment> getAllNonDefaultEnvironments() {
		return this.scenarioEnvironmentDao.getAllNonDefaultEnvironments();
	}

	public Environment getEnvironment(String name) {
		return this.scenarioEnvironmentDao.getEnvironment(name);
	}

	public Environment getDefaultEnvironment() {
		return this.scenarioEnvironmentDao.getDefaultEnvironment();
	}

}
