package org.sa.rainbow.monitor.sim;

/**
 * This class fixes a bug in the superclass.
 * 
 * @see #changeProperty(String, Object)
 * 
 */
public class SimulationRunnerWithScenarios extends SimulationRunner {

	/**
	 * This method fixes the one in the superclass since it takes into consideration the system can be terminated by the
	 * moment we want to perform a change in a property.
	 */
	@Override
	public boolean changeProperty(String iden, Object value) {
		if (isTerminated()) {
			return false;
		} else {
			return super.changeProperty(iden, value);
		}
	}
}
