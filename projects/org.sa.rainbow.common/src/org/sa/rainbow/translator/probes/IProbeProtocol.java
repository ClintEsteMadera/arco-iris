/**
 * Created December 5, 2006.
 */
package org.sa.rainbow.translator.probes;

import org.sa.rainbow.core.Rainbow;

/**
 * This interface defines common strings and commands required for the Probe
 * Protocol.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IProbeProtocol {

	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String TYPE = "alias";
	public static final String LOCATION = "location";
	public static final String LIFECYCLE = "lifecycle";
	public static final String CAPABILITY = "capability";
	public static final String SUBSCRIPTION_DUR = "subsDur";
	public static final String BEACON_PERIOD = "beaconPer";
	public static final String CONFIG_PARAM = "configParam";  // name[:type][=value]
	public static final String MAPPING = "mapping";  // probeAlias::gaugeID
	public static final String DATA = "data";
	public static final String SIZE = Rainbow.USCORE + "size";

}
