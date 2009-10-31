package ar.uba.dc.thesis.rainbow;

import java.util.Map;

import ar.uba.dc.thesis.acme.Architecture;
import ar.uba.dc.thesis.repository.SelfHealingScenarioRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

// TODO: implementar ConstraintEvaluator!
public class ConstraintEvaluator {

	private Map<Constraint, SelfHealingScenario> scenarioPerConstraint;

	/**
	 * The repair handler I need to invoke in the event of some constraint not holding anymore.
	 */
	private final RepairHandler repairHandler;

	/**
	 * The amount of time (in milliseconds) I need to wait before asking the architecture for
	 * changes
	 */
	private final long checkInterval;

	/**
	 * The constraint evaluator knows about the whole underlying architecture and can talk to the
	 * repair handler as well
	 */
	@SuppressWarnings("unused")
	private final Architecture architecture;

	public ConstraintEvaluator(Architecture architecture, RepairHandler repairHandler, long checkInterval) {
		super();
		this.architecture = architecture;
		this.repairHandler = repairHandler;
		this.checkInterval = checkInterval;
	}

	public void addScenarioPerConstraint(Constraint constraint, SelfHealingScenario scenario) {
		this.scenarioPerConstraint.put(constraint, scenario);
	}

	public void start() {
		// avoid a senseless infinite loop
		if (!SelfHealingScenarioRepository.getEnabledScenarios().isEmpty()) {
			while (true) {
				/*
				 * note that I ask for the enabled scenarios all the time because this subset can
				 * change dynamically
				 */
				for (SelfHealingScenario scenario : SelfHealingScenarioRepository.getEnabledScenarios()) {
					Constraint constraint = scenario.getResponseMeasure().getConstraint();
					if (!this.holds(constraint)) {
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

	/**
	 * This functionality will be provided by Rainbow framework
	 * 
	 * @param constraint
	 *            an ACME constraint, parseable by Rainbow.
	 * @return whether the constraint passed as parameter holds within the architecture or not.
	 */
	private boolean holds(Constraint constraint) {
		return true;
	}

}
