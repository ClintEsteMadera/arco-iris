package ar.uba.dc.thesis.dao;

import java.util.Set;

import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public interface SelfHealingScenarioDao {

	public Set<SelfHealingScenario> getAllScenarios();
}
