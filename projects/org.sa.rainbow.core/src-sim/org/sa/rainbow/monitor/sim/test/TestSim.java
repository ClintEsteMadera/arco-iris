/**
 * Created April 23, 2006
 */
package org.sa.rainbow.monitor.sim.test;

import java.util.Properties;

import org.sa.rainbow.monitor.sim.AbstractSim;
import org.sa.rainbow.monitor.sim.ISimulatedTargetSystem;

/**
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 *
 * Simulation of a Test system with alpha component connected via conn to
 * Beta component.
 */
public class TestSim extends AbstractSim {

	/**
	 * Default Constructor
	 */
	public TestSim (ISimulatedTargetSystem runner, Properties props) {
		super("Test Simulation System", runner, props);

		m_thread.start();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.sim.AbstractSim#runAction()
	 */
	@Override
	protected void runAction() {
		// keep increasing the load of Beta by 10%
		String prop = "TestSys.Beta.load";
		double load = doubleProp(prop) + 0.1;
		m_runner.changeProperty(prop, load);
	}

}
