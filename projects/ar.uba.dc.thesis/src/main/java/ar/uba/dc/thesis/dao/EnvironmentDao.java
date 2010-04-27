package ar.uba.dc.thesis.dao;

import java.util.Collection;

import ar.uba.dc.thesis.atam.Environment;

public interface EnvironmentDao {

	Collection<Environment> getAllNonDefaultEnvironments();

	Environment getEnvironment(String name);

	Environment getDefaultEnvironment();
}
