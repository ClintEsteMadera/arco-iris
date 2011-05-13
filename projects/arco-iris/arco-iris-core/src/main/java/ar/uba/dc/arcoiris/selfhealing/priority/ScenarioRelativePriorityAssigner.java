package ar.uba.dc.arcoiris.selfhealing.priority;

import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;

public interface ScenarioRelativePriorityAssigner {

	public abstract int relativePriority(SelfHealingScenario scenario);

}