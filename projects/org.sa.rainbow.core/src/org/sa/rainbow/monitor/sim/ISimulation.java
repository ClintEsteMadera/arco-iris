/**
 * Created April 23, 2006
 */
package org.sa.rainbow.monitor.sim;

import java.util.List;
import java.util.Properties;

import org.sa.rainbow.monitor.sim.SimulationRunner.SimPoint;

/**
 * Interface to a simulation system.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface ISimulation {

	public static final long SIM_TIME_WINDOW = 100;  // ms
	public static final String KEY_ELEMENTS = "elements";

	/**
	 * If simulation is capable of prediction, what mode of property prediction
	 * is requested.
	 */ 
	public static enum PredictionMode { PERFECT, IMPERFECT };

	/**
	 * Requests the simulation to simulate the system for some number of steps.
	 * @param steps  number of steps to simulate
	 */
	public void simulate (int steps);

	/**
	 * Requests the simulation to refresh its states, basically resetting all
	 * the original values of the system elements ONLY.
	 */
	public void refresh ();

	/**
	 * Requests the simulation to reset its properties, basically resetting all
	 * the original property values.
	 */
	public void reset ();

	/**
	 * Terminates the simulation.
	 */
	public void terminate ();

	/**
	 * Predictive simulation at some delta duration from present sim time.
	 * @param delta   delta duration in milliseconds.
	 * @param simpts  relevant sublist of simulation points to this prediction
	 * @param mode    perfect or imperfect prediction?
	 * @return Properties  the set of properties predicted at now + delta duration
	 */
	public Properties predict (List<SimPoint> simpts, long delta, PredictionMode mode);

}
