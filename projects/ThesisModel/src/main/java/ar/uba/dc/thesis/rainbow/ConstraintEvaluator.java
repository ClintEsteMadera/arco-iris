package ar.uba.dc.thesis.rainbow;

import java.util.Map;

import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class ConstraintEvaluator {

	// TODO: implementar ConstraintEvaluator!

	// TODO alimentarlo de las responseMeasure de los AtamScenario
	private Map<Constraint, SelfHealingScenario> scenarioPerConstraint;

	private final RepairHandler repairHandler;

	private final long checkInterval;

	public ConstraintEvaluator(RepairHandler repairHandler, long checkInterval) {
		super();
		this.repairHandler = repairHandler;
		this.checkInterval = checkInterval;
	}

	public void start() {
		while (true) {
			for (Constraint constraint : scenarioPerConstraint.keySet()) {
				if (constraint.fails()) {
					repairHandler.repairScenario(scenarioPerConstraint.get(constraint));
				}
			}
			try {
				Thread.sleep(checkInterval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
