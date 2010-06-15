/**
 * Created November 1, 2006.
 */
package org.sa.rainbow.translator.gauges;

import java.util.List;

import org.sa.rainbow.core.Identifiable;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * Interface for a gauge...
 * The Identifiable.id() returns the unique ID of this Gauge. 
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IGauge extends Identifiable {

	public static final String SETUP_LOCATION = "targetIP";
	public static final String SETUP_BEACON_PERIOD = "beaconPeriod";
	public static final String SETUP_JAVA_CLASS = "javaClass";
	public static final String CONFIG_PROBE_MAPPING = "targetProbeType";
	public static final String CONFIG_PROBE_MAPPING_LIST = "targetProbeList";
	public static final String CONFIG_SAMPLING_FREQUENCY = "samplingFrequency";
	/** Determines the max number of updates to do per instance of sleep, to
	 *  prevent one Gauge from hogging CPU in case of massive updates. */
	public static final int MAX_UPDATES_PER_SLEEP = 100;

	/**
	 * Returns the Gauge liveness beacon period, in milliseconds.
	 * @return int  the beacon period in milliseconds
	 */
	public long beaconPeriod ();

	/**
	 * Returns the Gauge's type-name description.
	 * @return TypeNamePair  the Gauge description as a type-name pair 
	 */
	public TypeNamePair gaugeDesc ();

	/**
	 * Returns the Gauge's model type-name description.
	 * @return TypeNamePair  the Model description as a type-name pair
	 */
	public TypeNamePair modelDesc ();

	/**
	 * Configures the parameters of this Gauge using the supplied configuration
	 * parameter values.
	 * @param configParams  list of type-name-value triples of configuration parameters
	 * @return boolean  <code>true</code> if configuration succeeds, <code>false</code> otherwise
	 */
	public boolean configureGauge (List<AttributeValueTriple> configParams);

	/**
	 * Causes the IGauge to call configureGauge on itself using its existing
	 * config parameters.  This method is currently used to reconnect to IProbes.
	 * @return boolean  <code>true</code> if configure call succeed, <code>false</code> otherwise
	 */
	public boolean reconfigureGauge ();

	/**
	 * Returns the entire state of this Gauge via the supplied lists.
	 * @param setupParams   the list of setup type-name-value triples
	 * @param configParams  the list of configuration type-name-value triples
	 * @param mappings      the list of value-property mapping pairs
	 * @return boolean  <code>true</code> if query succeeds, <code>false</code> otherwise
	 */
	public boolean queryGaugeState (List<AttributeValueTriple> setupParams,
			List<AttributeValueTriple> configParams,
			List<ValuePropertyMappingPair> mappings);

	/**
	 * Queries for a value identified by the property name.
	 * @param value  the AttributeValueTriple object to contain the value
	 * @return boolean  <code>true</code> if query succeeds, <code>false</code> otherwise
	 */
	public boolean querySingleValue (AttributeValueTriple value);

	/**
	 * Queries for all of the values reported by this Gauge.
	 * @param values  the List of AttributeValueTriple values
	 * @return boolean  <code>true</code> if query succeeds, <code>false</code> otherwise
	 */
	public boolean queryAllValues (List<AttributeValueTriple> values);

}
