/**
 * Created December 5, 2006.
 */
package org.sa.rainbow.translator.probes;

import java.util.ArrayList;
import java.util.List;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.event.EventTypeFilter;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.event.IProtocolAction;
import org.sa.rainbow.event.IRainbowMessage;
import org.sa.rainbow.event.RainbowEventAdapter;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.Pair;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * This class handles the probe protocol to support the Gauges' interaction
 * with the probes.  There are primarily six Probe Protocol Actions, with
 * specific relationship between them:
 * <ul>
 * <li> FIND - Find a Probe at a defined <em>location</em> with matching <em>capabilities</em>.<br>
 *      This must be the first protocol message sent, and the FOUND message
 *      received before any of the other messages are available.
 * <li> FOUND - A Probe has been found, an <em>id</em> is returned, with a <em>beacon period</em>.
 * <li> SUBSCRIBE - Subcribe to a Probe of a particular <em>id</em> for a specified <em>duration</em>.
 * <li> UNSUBSCRIBE - Unsubscribe from a Probe of a particular <em>id</em>.<br>
 *      After the specified subscription duration expires, unsubscription
 *      automatically occurs.
 * <li> REPORT_VALUE - A data is reported by the Probe, with <em>id</em> and <em>data</em>.
 * <li> BEACON - A beacon of <em>id</em> and <em>capabilities</em> is sent by the Probe.<br>
 *      If this message is not received within the beacon period, the Probe is
 *      considered to have failed/died.
 * </ul>
 * History:
 *   - [2007.02.07] Changed to extend RainbowEventAdapter, which implements IRainbowEventListener
 *   - [2007.06.06] Removed unused find/subscribe/unsubscribeProbe methods, as
 *     these are not actually used, but we will not touch IProbeProtocol yet.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class ProbeProtocolHandler extends RainbowEventAdapter {

	private IProbeConsumer m_gauge = null;
	private List<Pair<String,String>> m_probeTypes = null;

	/**
	 * Default Constructor.
	 */
	public ProbeProtocolHandler(IProbeConsumer gauge) {
		super(gauge.id());

		m_gauge = gauge;
		m_probeTypes = new ArrayList<Pair<String,String>>();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.event.IRainbowEventListener#onMessage(org.sa.rainbow.event.IRainbowMessage)
	 */
	@EventTypeFilter(eventTypes={
			IProtocolAction.REPORT
	})
	public void onMessage(IRainbowMessage msg) {
		String type = (String )msg.getProperty(IProbeProtocol.TYPE);
		String location = (String )msg.getProperty(IProbeProtocol.LOCATION);
		// decide what to do based on Action
		IProtocolAction action = IProtocolAction.valueOf((String )msg.getProperty(IProtocolAction.ACTION));
		switch (action) {
		case REPORT:  // got report!
			// iterate through probe type list
			boolean found = false;
			for (Pair<String,String> probeType : new ArrayList<Pair<String,String>>(m_probeTypes)) {
				if (type == null || !type.equals(probeType.firstValue())) continue;
				if (probeType.secondValue() != null && !probeType.secondValue().equals(location)) continue;
				found = true;
				break;
			}
			if (!found) return;
			// at this point, data is for this gauge
			String data = (String )msg.getProperty(IProbeProtocol.DATA);
			m_gauge.probeReport(data);
			break;
		}
	}

	/**
	 * Sends to the Probe Bus mapping information between an alias for a probe
	 * and a Gauge ID, allowing a probe to report values properly to a gauge. 
	 * @param location    the deployment location of the probe
	 * @param probeAlias  the alias of the probe
	 * @param gaugeID     the unique Gauge ID deployed on location with the probe
	 * @return boolean  <code>true</code> if send of mapProbeToGauge message succeeded, <code>false</code> otherwise
	 */
	public boolean mapProbeToGauge (String location, String probeAlias, String gaugeID) {
		m_probeTypes.add(new Pair<String,String>(probeAlias, location));
		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_PROBE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.MAP2GAUGE.name());
		msg.setProperty(IProbeProtocol.LOCATION, location);
		msg.setProperty(IProbeProtocol.MAPPING, new ValuePropertyMappingPair(probeAlias, gaugeID).toString());
		return Rainbow.eventService().send(IEventService.TOPIC_PROBE_BUS, msg);
	}

	/**
	 * Sends to the Probe Bus property configurations for the probes identified
	 * by the probe alias at the designated location (or null to receive in all
	 * locations).
	 * @param location  the host location to send message to
	 * @param alias  the type of the probe
	 * @param configParams  the list of {@link AttributeValueTriple} parameters to configure
	 * @return boolean  <code>true</code> if send of configureProbe message succeeded, <code>false</code> otherwise
	 */
	public boolean configureProbe (String location, String alias, List<AttributeValueTriple> configParams) {
		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_PROBE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.CONFIGURE.name());
		msg.setProperty(IProbeProtocol.LOCATION, location);
		msg.setProperty(IProbeProtocol.TYPE, alias);
		msg.setProperty(IProbeProtocol.CONFIG_PARAM + IProbeProtocol.SIZE, configParams.size());
		for (int i=0; i < configParams.size(); ++i) {
			String key = IProbeProtocol.CONFIG_PARAM + Rainbow.USCORE + i;
			msg.setProperty(key, configParams.get(i).toString());
		}
		return Rainbow.eventService().send(IEventService.TOPIC_PROBE_BUS, msg);
	}

}
