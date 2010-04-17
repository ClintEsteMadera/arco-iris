package ar.uba.dc.thesis.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.uba.dc.thesis.dao.SelfHealingScenarioDao;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class SelfHealingScenarioRepository {

	private final List<SelfHealingScenario> scenarios;

	private final List<SelfHealingScenario> enabledScenarios;

	private final SelfHealingScenarioDao dao;

	public SelfHealingScenarioRepository(SelfHealingScenarioDao selfHealingScenarioDao) {
		super();
		this.dao = selfHealingScenarioDao;
		this.scenarios = this.dao.getAllScenarios();
		this.enabledScenarios = this.selectEnabledScenarios();
	}

	private List<SelfHealingScenario> selectEnabledScenarios() {
		List<SelfHealingScenario> enabledScenarios = new ArrayList<SelfHealingScenario>();

		for (SelfHealingScenario scenario : this.scenarios) {
			if (scenario.isEnabled()) {
				enabledScenarios.add(scenario);
			}
		}
		return enabledScenarios;
	}

	public Collection<SelfHealingScenario> getAllScenarios() {
		return this.scenarios;
	}

	/**
	 * <b>Important Note:</b>It is assumed that we are not using this Repository for holding scenarios for more than
	 * one application.<br>
	 */
	public Collection<SelfHealingScenario> getEnabledScenarios() {
		return this.enabledScenarios;
	}
}