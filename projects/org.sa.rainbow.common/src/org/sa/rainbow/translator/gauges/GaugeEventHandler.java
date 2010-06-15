/**
 * Created December 7, 2006.
 */
package org.sa.rainbow.translator.gauges;

import java.util.ArrayList;
import java.util.List;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.event.EventTypeFilter;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.event.IProtocolAction;
import org.sa.rainbow.event.IRainbowMessage;
import org.sa.rainbow.event.RainbowEventAdapter;
import org.sa.rainbow.util.AttributeValueTriple;

/**
 * This class handles the gauge protocol to support the Gauge's interaction
 * with the Gauge Manager and Consumer.
 * <br><br>
 * History:
 *   - [2006.12.21] Split into Gauge[|Consumer|Manager]EventHandler.
 *   - [2007.02.07] Changed to extend RainbowEventAdapter, which implements
 *     IRainbowEventListener
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class GaugeEventHandler extends RainbowEventAdapter {

	private IGauge m_gauge = null;

	/**
	 * Constructor accepting an IGauge.
	 * @param gauge  the IGauge for which to handle the Gauge protocol
	 */
	public GaugeEventHandler (IGauge gauge) {
		super(gauge.id());

		m_gauge = gauge;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.event.IRainbowEventListener#onMessage(org.sa.rainbow.event.IRainbowMessage)
	 */
	@EventTypeFilter(eventTypes={
			IProtocolAction.CONFIGURE,
			IProtocolAction.QUERY_GAUGESTATE,
			IProtocolAction.QUERY_VALUE,
			IProtocolAction.QUERY_ALLVALUES
	})
	public void onMessage (IRainbowMessage msg) {
		String id = (String )msg.getProperty(IGaugeProtocol.ID);
		if (m_gauge == null) return;
		if (!m_gauge.id().equals(id)) return;  // mismatched ID

		// decide what to do based on Action
		IProtocolAction action = IProtocolAction.valueOf((String )msg.getProperty(IProtocolAction.ACTION));
		switch (action) {
		case CONFIGURE:
			// received config request
			List<AttributeValueTriple> configParams = new ArrayList<AttributeValueTriple>();
			int cnt = (Integer )msg.getProperty(IGaugeProtocol.CONFIG_PARAM + IGaugeProtocol.SIZE);
			for (int i=0; i < cnt; ++i) {
				String key = IGaugeProtocol.CONFIG_PARAM + Rainbow.USCORE + i;
				configParams.add(AttributeValueTriple.parseValueTriple((String )msg.getProperty(key)));
			}
			m_gauge.configureGauge(configParams);
			break;
		case QUERY_GAUGESTATE:
			// received gauge-state query request
			break;
		case QUERY_VALUE:
			// received value query request
			break;
		case QUERY_ALLVALUES:
			// received all-values query request
			break;
		}
	}

	public boolean reportCreated () {
		if (m_gauge == null) return false;

		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_CREATED.name());
		msg.setProperty(IGaugeProtocol.BEACON_PERIOD, m_gauge.beaconPeriod());
		setCommonGaugeAttributes(msg);
		Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
		return true;
	}

	public boolean reportDeleted () {
		if (m_gauge == null) return false;

		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_DELETED.name());
		setCommonGaugeAttributes(msg);
		Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
		return true;
	}

	public boolean reportConfigured (List<AttributeValueTriple> configParams) {
		if (m_gauge == null) return false;

		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_CONFIGURED.name());
		setCommonGaugeAttributes(msg);
		msg.setProperty(IGaugeProtocol.CONFIG_PARAM + IGaugeProtocol.SIZE, configParams.size());
		for (int i=0; i < configParams.size(); ++i) {
			String key = IGaugeProtocol.CONFIG_PARAM + Rainbow.USCORE + i;
			msg.setProperty(key, configParams.get(i).toString());
		}
		Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
		return true;
	}

	public boolean reportValue (AttributeValueTriple value) {
		if (m_gauge == null) return false;

		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_VALUE.name());
		setCommonGaugeAttributes(msg);
		msg.setProperty(IGaugeProtocol.VALUE, value.toString());
		Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
		return true;
	}

	public boolean reportMultipleValues (List<AttributeValueTriple> values) {
		if (m_gauge == null) return false;

		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_MULTIVALUES.name());
		setCommonGaugeAttributes(msg);
		msg.setProperty(IGaugeProtocol.VALUE + IGaugeProtocol.SIZE, values.size());
		for (int i=0; i < values.size(); ++i) {
			String key = IGaugeProtocol.VALUE + Rainbow.USCORE + i;
			msg.setProperty(key, values.get(i).toString());
		}
		Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
		return true;
	}

	public boolean sendBeacon () {
		if (m_gauge == null) return false;

		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.BEACON.name());
		msg.setProperty(IGaugeProtocol.ID, m_gauge.id());
		msg.setProperty(IGaugeProtocol.BEACON_PERIOD, m_gauge.beaconPeriod());
		Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
		return true;
	}

	private void setCommonGaugeAttributes (IRainbowMessage msg) {
		msg.setProperty(IGaugeProtocol.ID, m_gauge.id());
		msg.setProperty(IGaugeProtocol.GAUGE_TYPE, m_gauge.gaugeDesc().type());
		msg.setProperty(IGaugeProtocol.GAUGE_NAME, m_gauge.gaugeDesc().name());
		msg.setProperty(IGaugeProtocol.MODEL_TYPE, m_gauge.modelDesc().type());
		msg.setProperty(IGaugeProtocol.MODEL_NAME, m_gauge.modelDesc().name());
	}

}
