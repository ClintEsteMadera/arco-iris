package ar.uba.dc.thesis.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ar.uba.dc.thesis.dao.SelfHealingScenarioDao;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class SelfHealingScenarioRepository {

	private final List<SelfHealingScenario> enabledScenarios = new ArrayList<SelfHealingScenario>();

	public SelfHealingScenarioRepository(SelfHealingScenarioDao selfHealingScenarioDao) {
		super();
		this.selectEnabledScenarios(selfHealingScenarioDao.getAllScenarios());
	}

	/**
	 * <b>Important Note:</b>It is assumed that we are not using this Repository for holding scenarios for more than
	 * one application.<br>
	 */
	public Collection<SelfHealingScenario> getEnabledScenarios() {
		return this.enabledScenarios;
	}

	private void selectEnabledScenarios(List<SelfHealingScenario> scenarios) {
		for (SelfHealingScenario scenario : scenarios) {
			if (scenario.isEnabled()) {
				this.enabledScenarios.add(scenario);
			}
		}
		return;
	}
}