package ar.uba.dc.arcoiris.dao;

import java.util.List;

import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.repository.SelfHealingConfigurationChangeListener;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public interface SelfHealingConfigurationDao {

	public List<SelfHealingScenario> getAllScenarios();

	public List<Environment> getAllEnvironments();

	public Environment getEnvironment(String name);

	public List<Artifact> getAllArtifacts();

	public void register(SelfHealingConfigurationChangeListener listener);
}
