/**
 * Created July 24, 2007.
 */
package org.sa.rainbow.monitor.sim;

/**
 * Allows properties of the "system" to be changed and effected as needed.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface ISimulatedTargetSystem {

	/**
	 * Used in Simulation Run to change the value of a property.
	 * @param iden   the name identifier of the property
	 * @param value  the value to set the property to
	 * @return boolean  <code>true</code> if property change succeeds, <code>false</code> otherwise.
	 */
	public boolean changeProperty (String iden, Object value);

	/**
	 * Basically same function as changeProperty(), but done with
	 * a random delay to simulate randomness in system-level changes.
	 * @param iden   the name identifier of the property
	 * @param value  the value to set the property to
	 * @return boolean  <code>true</code> if property change succeeds, <code>false</code> otherwise.
	 */
	public boolean effectProperty (String iden, Object value);

	/**
	 * Returns the number of milliseconds elapsed in a timed simulation,
	 * accounting for pauses.
	 * @return long  net milliseconds of simulation time elapsed.
	 */
	public long elapsedTime ();
}
