package ar.uba.dc.thesis;

import ar.uba.dc.thesis.acme.Architecture;
import ar.uba.dc.thesis.rainbow.ConstraintEvaluator;
import ar.uba.dc.thesis.rainbow.OrderedRepairStrategySelector;
import ar.uba.dc.thesis.rainbow.RepairHandler;
import ar.uba.dc.thesis.repository.ArchitectureRepository;

public class Demo {

	private static final int DEFAULT_CHECK_INTERVAL = 10000;

	public static void main(String[] args) {
		Architecture architecture = ArchitectureRepository.getZnnArchitecture();
		RepairHandler repairHandler = new RepairHandler(architecture, new OrderedRepairStrategySelector());
		new ConstraintEvaluator(architecture, repairHandler, DEFAULT_CHECK_INTERVAL).start();
	}
}