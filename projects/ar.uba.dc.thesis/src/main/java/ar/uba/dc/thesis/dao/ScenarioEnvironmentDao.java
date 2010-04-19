package ar.uba.dc.thesis.dao;

import java.util.Collection;

import ar.uba.dc.thesis.atam.Environment;

public interface ScenarioEnvironmentDao {

	Collection<Environment> getAllEnvironments();

	Environment getEnvironment(String name);

}
