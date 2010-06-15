/**
 * Created December 19, 2006.
 */
package org.sa.rainbow.translator.gauges;

import java.util.List;

import org.sa.rainbow.core.Identifiable;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.TypeNamePair;

/**
 * The interface of a Gauge Consumer.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IGaugeConsumer extends Identifiable {

	public static enum State {
		ALL_GAUGES_UNINIT,
		/** All Gauges have been created */
		ALL_GAUGES_CREATED,
		/** All Gauges are configured and ready */
		ALL_GAUGES_READY,
		/** All Gauges have been deleted */
		ALL_GAUGES_DELETED
	}

	public void onReportValue (String id, TypeNamePair gaugeDesc,
			TypeNamePair modelDesc, AttributeValueTriple value);

	public void onReportMultipleValues (String id, TypeNamePair gaugeDesc,
			TypeNamePair modelDesc, List<AttributeValueTriple> values);

	public void onReportCreated (String id, TypeNamePair gaugeDesc, TypeNamePair modelDesc);

	public void onReportDeleted (String id, TypeNamePair gaugeDesc, TypeNamePair modelDesc);

	public void onReportConfigured (String id, TypeNamePair gaugeDesc,
			TypeNamePair modelDesc, List<AttributeValueTriple> configParams);

	/**
	 * Marks a beacon receipt from the Gauge; clears a timer.  At the same time,
	 * sets the beacon period of the consumer, which period is used by the
	 * consumer to determine whether its Gauge is still alive.
	 * @param id      the unique ID of the Gauge
	 * @param period  the period in milliseconds between beacons
	 */
	public void gaugeBeacon (String id, long period);

}
