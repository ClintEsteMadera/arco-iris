/**
 * Created December 21, 2006.
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
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * This class handles the gauge protocol to support the Gauge Consumer's
 * interaction with the Gauge and Gauge Manager.
 * <br><br>
 * History:
 *   - [2006.12.21] Split from original GaugeProtocolHandler.
 *   - [2007.02.07] Changed to extend RainbowEventAdapter, which implements
 *     IRainbowEventListener
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class GaugeConsumerEventHandler extends RainbowEventAdapter {

	private IGaugeConsumer m_consumer = null;

	/**
	 * Constructor accepting an IGaugeConsumer.
	 * @param consumer  the IGaugeConsumer for which to handle the Gauge protocol
	 */
	public GaugeConsumerEventHandler (IGaugeConsumer consumer) {
		super(consumer.id());

		m_consumer = consumer;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.event.IRainbowEventListener#onMessage(org.sa.rainbow.event.IRainbowMessage)
	 */
	@EventTypeFilter(eventTypes={
			IProtocolAction.BEACON,
			IProtocolAction.REPORT_CREATED,
			IProtocolAction.REPORT_DELETED,
			IProtocolAction.REPORT_CONFIGURED,
			IProtocolAction.REPORT_VALUE,
			IProtocolAction.REPORT_MULTIVALUES
	})
	public void onMessage (IRainbowMessage msg) {
		// retrieve any ID returned
		String id = (String )msg.getProperty(IGaugeProtocol.ID);
		if (id == null) return;  // need not continue without an ID
		// decide what to do based on Action
		IProtocolAction action = IProtocolAction.valueOf((String )msg.getProperty(IProtocolAction.ACTION));
		if (action == IProtocolAction.BEACON) {  // received beacon!
			Long beaconPer = (Long )msg.getProperty(IGaugeProtocol.BEACON_PERIOD);
			m_consumer.gaugeBeacon(id, beaconPer);
		} else if (IProtocolAction.isReport(action)) {
			// a report, retrieve the report-common attributes
			String gaugeType = (String )msg.getProperty(IGaugeProtocol.GAUGE_TYPE);
			String gaugeName = (String )msg.getProperty(IGaugeProtocol.GAUGE_NAME);
			TypeNamePair gaugeDesc = new TypeNamePair(gaugeType, gaugeName);
			String modelType = (String )msg.getProperty(IGaugeProtocol.MODEL_TYPE);
			String modelName = (String )msg.getProperty(IGaugeProtocol.MODEL_NAME);
			TypeNamePair modelDesc = new TypeNamePair(modelType, modelName);
			switch (action) {
			case REPORT_CREATED:
				// received Gauge-created message + beacon
				m_consumer.onReportCreated(id, gaugeDesc, modelDesc);
				long per = (Long )msg.getProperty(IGaugeProtocol.BEACON_PERIOD);
				m_consumer.gaugeBeacon(id, per);
				break;
			case REPORT_DELETED:
				// received Gauge-deleted message
				m_consumer.onReportDeleted(id, gaugeDesc, modelDesc);
				break;
			case REPORT_CONFIGURED:
				// received Gauge-configured message
				List<AttributeValueTriple> configParams = new ArrayList<AttributeValueTriple>();
				int cnt = (Integer )msg.getProperty(IGaugeProtocol.CONFIG_PARAM + IGaugeProtocol.SIZE);
				for (int i=0; i < cnt; ++i) {
					String key = IGaugeProtocol.CONFIG_PARAM + Rainbow.USCORE + i;
					configParams.add(AttributeValueTriple.parseValueTriple((String )msg.getProperty(key)));
				}
				m_consumer.onReportConfigured(id, gaugeDesc, modelDesc, configParams);
				break;
			case REPORT_VALUE:
				// got single-value report!
				AttributeValueTriple value = AttributeValueTriple.parseValueTriple((String )msg.getProperty(IGaugeProtocol.VALUE));
				m_consumer.onReportValue(id, gaugeDesc, modelDesc, value);
				break;
			case REPORT_MULTIVALUES:
				// got multi-values report!
				List<AttributeValueTriple> values = new ArrayList<AttributeValueTriple>();
				cnt = (Integer )msg.getProperty(IGaugeProtocol.VALUE + IGaugeProtocol.SIZE);
				for (int i=0; i < cnt; ++i) {
					String key = IGaugeProtocol.VALUE + Rainbow.USCORE + i;
					values.add(AttributeValueTriple.parseValueTriple((String )msg.getProperty(key)));
				}
				m_consumer.onReportMultipleValues(id, gaugeDesc, modelDesc, values);
				break;
			}
		}
	}


	/**
	 * Connects to a GaugeManager by posting message on the GaugeBus for a type
	 * of Gauge.
	 * 
	 * @param location  the String identifying the location
	 * @param gaugeDesc  the Gauge type-name pair
	 * @param modelDesc  the model type-name pair
	 */
	public boolean connectManager (String location, String gaugeType) {
		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.CONNECT.name());
		msg.setProperty(IGaugeProtocol.LOCATION, location);
		msg.setProperty(IGaugeProtocol.GAUGE_TYPE, gaugeType);
		return Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
	}

	/**
	 * Creates by posting message on the GaugeBus a Gauge residing at location
	 * for the gauge type and gauge name, model type and model name.
	 * 
	 * @param location  the String identifying the location
	 * @param gaugeDesc  the Gauge type-name pair
	 * @param modelDesc  the model type-name pair
	 */
	public boolean createGauge (String location, TypeNamePair gaugeDesc,
			TypeNamePair modelDesc, List<AttributeValueTriple> setupParams,
			List<ValuePropertyMappingPair> mappings) {
		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.CREATE.name());
		msg.setProperty(IGaugeProtocol.LOCATION, location);
		msg.setProperty(IGaugeProtocol.GAUGE_TYPE, gaugeDesc.type());
		msg.setProperty(IGaugeProtocol.GAUGE_NAME, gaugeDesc.name());
		msg.setProperty(IGaugeProtocol.MODEL_TYPE, modelDesc.type());
		msg.setProperty(IGaugeProtocol.MODEL_NAME, modelDesc.name());
		msg.setProperty(IGaugeProtocol.SETUP_PARAM + IGaugeProtocol.SIZE, setupParams.size());
		for (int i=0; i < setupParams.size(); ++i) {
			String key = IGaugeProtocol.SETUP_PARAM + Rainbow.USCORE + i;
			msg.setProperty(key, setupParams.get(i).toString());
		}
		msg.setProperty(IGaugeProtocol.MAPPING + IGaugeProtocol.SIZE, mappings.size());
		for (int i=0; i < mappings.size(); ++i) {
			String key = IGaugeProtocol.MAPPING + Rainbow.USCORE + i;
			msg.setProperty(key, mappings.get(i).toString());
		}
		return Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
	}

	/**
	 * Deletes by posting message on the GaugeBus a Gauge identified by the ID
	 * and the gauge type and gauge name, model type and model name.
	 * 
	 * @param id         the unique String identifier of the Gauge
	 * @param gaugeDesc  the Gauge type-name pair
	 * @param modelDesc  the model type-name pair
	 */
	public boolean deleteGauge (String id, TypeNamePair gaugeDesc, TypeNamePair modelDesc) {
		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.DELETE.name());
		msg.setProperty(IGaugeProtocol.ID, id);
		msg.setProperty(IGaugeProtocol.GAUGE_TYPE, gaugeDesc.type());
		msg.setProperty(IGaugeProtocol.GAUGE_NAME, gaugeDesc.name());
		msg.setProperty(IGaugeProtocol.MODEL_TYPE, modelDesc.type());
		msg.setProperty(IGaugeProtocol.MODEL_NAME, modelDesc.name());
		return Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
	}

	/**
	 * Configures by posting message on the GaugeBus a Gauge identified by the
	 * ID and the gauge type and gauge name, model type and model name, using
	 * the supplied List of configuration parameters.
	 * 
	 * @param id         the unique String identifier of the Gauge
	 * @param gaugeDesc  the Gauge type-name pair
	 * @param modelDesc  the model type-name pair
	 * @param configParams  the List of configuration parameters to use
	 */
	public boolean configureGauge (String id, TypeNamePair gaugeDesc,
			TypeNamePair modelDesc, List<AttributeValueTriple> configParams) {
		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.CONFIGURE.name());
		msg.setProperty(IGaugeProtocol.ID, id);
		msg.setProperty(IGaugeProtocol.GAUGE_TYPE, gaugeDesc.type());
		msg.setProperty(IGaugeProtocol.GAUGE_NAME, gaugeDesc.name());
		msg.setProperty(IGaugeProtocol.MODEL_TYPE, modelDesc.type());
		msg.setProperty(IGaugeProtocol.MODEL_NAME, modelDesc.name());
		msg.setProperty(IGaugeProtocol.CONFIG_PARAM + IGaugeProtocol.SIZE, configParams.size());
		for (int i=0; i < configParams.size(); ++i) {
			String key = IGaugeProtocol.CONFIG_PARAM + Rainbow.USCORE + i;
			msg.setProperty(key, configParams.get(i).toString());
		}
		return Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
	}

	/**
	 * Subscribes to the Gauge with the supplied ID for the given duration.
	 * 
	 * @param id  the ID of the Gauge to subscribe to
	 * @param duration  the duration of time in seconds for the subscription to remain effective
	 */
	public boolean subscribeGauge (String id, int duration) {
		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.SUBSCRIBE.name());
		msg.setProperty(IGaugeProtocol.ID, id);
		msg.setProperty(IGaugeProtocol.SUBSCRIPTION_DUR, duration);
		return Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
	}

	public boolean unsubscribeGauge (String id) {
		IRainbowMessage msg = Rainbow.eventService().createMessage(IEventService.TOPIC_GAUGE_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.UNSUBSCRIBE.name());
		msg.setProperty(IGaugeProtocol.ID, id);
		return Rainbow.eventService().send(IEventService.TOPIC_GAUGE_BUS, msg);
	}

}
 