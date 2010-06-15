/**
 * Created December 5, 2006.
 */
package org.sa.rainbow.translator.probes;

import org.sa.rainbow.core.Identifiable;

/**
 * This interface defines methods for the ProbeProtocolHandler to interact
 * with the AbstractGauge.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IProbeConsumer extends Identifiable {

	/**
	 * Sets the beacon period of target Probe in milliseconds.  Used by the
	 * gauge to determine whether its Probe is still functioning.
	 * 
	 * @param period  the period in milliseconds between beacons
	 */
	public void setProbeBeaconPeriod (long period);

	/**
	 * Marks a beacon receipt from the Probe; clears a timer.
	 */
	public void probeBeacon ();

	/**
	 * Reports a string of data to the Gauge.
	 * 
	 * @param data  the string of the Probe data
	 */
	public void probeReport (String data);

}
