/**
 * Created April 7, 2006
 */
package org.sa.rainbow.monitor;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.sa.rainbow.translator.effectors.IEffector;

/**
 * Interface to the Target System, used to access system simulation objects.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface TargetSystem {

	/**
	 * Enum of types of statistical derivation of a property value.
	 */
	public enum StatType {
		/** Get an instantaneous value. */
		SINGLE,
		/** Get the mean value from now through some duration. */
		MEAN,
		/** Get the minimium value between now and some duration. */
		MIN,
		/** Get the maximum value between now and some duration. */
		MAX
	}

	/**
	 * Establishes connection with the target system.
	 * In simulation, this means starting the simulation runner.
	 * For an actual target system, this means starting the delegate thread.
	 */
	public void connect ();

	/**
	 * Returns whether the target system is considered to have been terminated.
	 * @return boolean  <code>true</code> if the system is terminated, <code>false</code> otherwise.
	 */
	public boolean isTerminated ();

	public Properties getProperties ();

	public Map<String,Object> getChangedProperties ();

	/**
	 * Queries the system for the property "iden" and returns the value object.
	 * @param iden  the name identifier of the property
	 * @return Object  the value object of the property
	 */
	public Object queryProperty (String iden);

	/**
	 * Queries the system for the predicted value of the property "iden" at "dur"
	 * milliseconds in the future, requesting the {@link StatType} of the value.
	 * This prediction query is intended to access prediction gauges.  If no
	 * value of the requested kind is available or known, a <code>null</code> is returned.
	 * @param iden  the name identifier of the property
	 * @param dur   predict the property at this many milliseconds into the future 
	 * @param type  apply one of the statistical types of treatments to the value
	 * @return Object  the predicted value object of the requested property
	 */
	public Object predictProperty (String iden, long dur, StatType type);

	/**
	 * This query operation finds instances of a designated type, matching an
	 * optional filter of key-value attributes.
	 * @param type  the name of the service type sought
	 * @param filters  the attribute map of key-value pairs used to filter instances
	 * @return Set<?>  of service instances
	 */
	public Set<?> findServices (String type, Map<String,String> filters);

	/**
	 * Retrieves an effector identified by name and its target location.
	 * @param name  the name of the IEffector
	 * @param target  the target location on which the effector operates
	 * @return IEffector  a handler to the IEffector
	 */
	public IEffector getEffector (String name, String target);

}
