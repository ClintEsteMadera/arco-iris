package ar.uba.dc.thesis.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import ar.uba.dc.thesis.atam.scenario.model.DefaultEnvironment;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.dao.SelfHealingConfigurationDao;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class SelfHealingConfigurationRepository implements SelfHealingConfigurationChangeListener {

	private final List<SelfHealingScenario> enabledScenarios = new ArrayList<SelfHealingScenario>();

	private final SelfHealingConfigurationDao selfHealingConfigurationDao;

	private final Collection<SelfHealingConfigurationChangeListener> listeners;

	public SelfHealingConfigurationRepository(SelfHealingConfigurationDao selfHealingScenarioDao) {
		super();
		this.selfHealingConfigurationDao = selfHealingScenarioDao;
		this.keepOnlyEnabledScenarios();
		this.listeners = new HashSet<SelfHealingConfigurationChangeListener>();
		this.selfHealingConfigurationDao.register(this);
	}

	/**
	 * <b>Important Note:</b>It is assumed that we are not using this Repository for holding scenarios for more than one
	 * instance of the application.<br>
	 */
	public Collection<SelfHealingScenario> getEnabledScenarios() {
		return this.enabledScenarios;
	}

	public List<Environment> getAllNonDefaultEnvironments() {
		return this.selfHealingConfigurationDao.getAllNonDefaultEnvironments();
	}

	public Environment getEnvironment(String name) {
		return this.selfHealingConfigurationDao.getEnvironment(name);
	}

	public Environment getDefaultEnvironment() {
		return DefaultEnvironment.getInstance();
	}

	public void register(SelfHealingConfigurationChangeListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void selfHealingConfigurationHasChanged() {
		for (SelfHealingConfigurationChangeListener listener : this.listeners) {
			listener.selfHealingConfigurationHasChanged();
		}
	}

	private void keepOnlyEnabledScenarios() {
		for (SelfHealingScenario scenario : this.selfHealingConfigurationDao.getAllScenarios()) {
			if (scenario.isEnabled()) {
				this.enabledScenarios.add(scenario);
			}
		}
	}
}