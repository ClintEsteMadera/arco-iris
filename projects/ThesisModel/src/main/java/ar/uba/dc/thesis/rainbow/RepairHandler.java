package ar.uba.dc.thesis.rainbow;

import java.util.ArrayList;
import java.util.List;

import ar.uba.dc.thesis.RepairStrategyRepository;
import ar.uba.dc.thesis.acme.Architecture;
import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.selfhealing.RepairStrategySpecification;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class RepairHandler {

	private final IRepairStrategySelector repairStrategySelector;

	private final Architecture architecture;

	public RepairHandler(Architecture architecture, IRepairStrategySelector repairStrategySelector) {
		this.architecture = architecture;
		this.repairStrategySelector = repairStrategySelector;
	}

	public void repairScenario(SelfHealingScenario scenario) {
		RepairStrategySpecification spec = repairStrategySelector.selectRepairStrategyFor(scenario);
		if (spec != null) {
			List<Artifact> params = new ArrayList<Artifact>(spec.getParams().length);
			for (String artifactName : spec.getParams()) {
				params.add(architecture.getComponent(artifactName));
			}

			RepairStrategyRepository.getRepairStrategy(spec.getRepairStrategyName()).execute(params);
		}
	}
}
