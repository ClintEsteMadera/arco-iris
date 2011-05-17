package ar.uba.dc.arcoiris.atam.scenario;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;
import ar.uba.dc.arcoiris.selfhealing.priority.DefaultScenarioRelativePriorityAssigner;
import ar.uba.dc.arcoiris.selfhealing.priority.ScenarioRelativePriorityAssigner;

@RunWith(JUnit4.class)
public class RelativePriorityAssignerTest {

	private static final int MAX_ABSOLUTE_PRIORITY = 100;

	private static final int MOST_IMPORTANT_PRIORITY = 1;

	@Test
	public void testMinAndMaxRelativePriority() {
		SelfHealingConfigurationManager configurationManager = new SelfHealingConfigurationManager(
				MAX_ABSOLUTE_PRIORITY);
		ScenarioRelativePriorityAssigner assigner = new DefaultScenarioRelativePriorityAssigner(configurationManager);
		SelfHealingScenario scenario = new SelfHealingScenario();
		scenario.setPriority(MOST_IMPORTANT_PRIORITY);
		double relativePriority = assigner.relativePriority(scenario);
		Assert.assertTrue("Relative Priority must be equals to 1", relativePriority == 1);
		scenario.setPriority(MAX_ABSOLUTE_PRIORITY);
		relativePriority = assigner.relativePriority(scenario);
		Assert.assertEquals("Relative Priority must be equals to 0.01", 0.01, relativePriority, 0);
	}

}
