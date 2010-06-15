/**
 * Created December 21, 2006.
 */
package org.sa.rainbow.translator.gauges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.event.EventTypeFilter;
import org.sa.rainbow.event.IProtocolAction;
import org.sa.rainbow.event.IRainbowMessage;
import org.sa.rainbow.event.RainbowEventAdapter;
import org.sa.rainbow.health.RainbowCloudEventHandler;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * This class handles the gauge protocol to support the Gauge Manager's
 * interaction with the Gauge and Gauge Consumer.
 * <br><br>
 * History:
 *   - [2006.12.21] Split from original GaugeProtocolHandler.
 *   - [2007.02.07] Changed to extend RainbowEventAdapter, which implements
 *     IRainbowEventListener
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class GaugeManagerEventHandler extends RainbowEventAdapter {

	private IGaugeManager m_manager = null;
	private RainbowLogger m_logger = null;

	/**
	 * Constructor accepting an IGaugeManager.
	 * @param manager  the IGaugeManager for which to handle the Gauge protocol
	 */
	public GaugeManagerEventHandler (IGaugeManager manager) {
		super(manager.id());

		m_logger = RainbowLoggerFactory.logger(getClass());
		m_manager = manager;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.event.IRainbowEventListener#onMessage(org.sa.rainbow.event.IRainbowMessage)
	 */
	@EventTypeFilter(eventTypes={
			IProtocolAction.CONNECT,
			IProtocolAction.CREATE,
			IProtocolAction.DELETE,
			IProtocolAction.QUERY_GAUGEMETAINFO
	})
	public void onMessage(IRainbowMessage msg) {
		// retrieve any ID returned
		String id = (String )msg.getProperty(IGaugeProtocol.ID);
		// determine if gauge type of request matches this manager
		String typeName = (String )msg.getProperty(IGaugeProtocol.GAUGE_TYPE);
		boolean isManaged = m_manager.managesType(typeName);

		// decide what to do based on Action
		IProtocolAction action = IProtocolAction.valueOf((String )msg.getProperty(IProtocolAction.ACTION));
		if (action == IProtocolAction.CONNECT) {  // connect request
			String str = "GM[" + m_manager.gaugeType() + "] manages type '" + typeName + "'? " + isManaged;
			RainbowCloudEventHandler.sendTranslatorLog(str);
			if (m_logger.isInfoEnabled()) m_logger.info(str);
		}
		if (!isManaged) return;

		switch (action) {
		case CREATE:
			// obtain setup params
			List<AttributeValueTriple> setupParams = new ArrayList<AttributeValueTriple>();
			int cnt = (Integer )msg.getProperty(IGaugeProtocol.SETUP_PARAM + IGaugeProtocol.SIZE);
			for (int i=0; i < cnt; ++i) {
				String key = IGaugeProtocol.SETUP_PARAM + Rainbow.USCORE + i;
				AttributeValueTriple triple = AttributeValueTriple.parseValueTriple((String )msg.getProperty(key));
				if (triple.name().equals(IGauge.SETUP_LOCATION)) {
					// check if target location applicable
					String targetIP = (String )triple.value();
					if (!targetIP.equals(Rainbow.property(Rainbow.PROPKEY_DEPLOYMENT_LOCATION)))
						return;  // NOPE! don't proceed with create
				}
				setupParams.add(triple);
			}
			// retrieve gauge and model info
			String gaugeName = (String )msg.getProperty(IGaugeProtocol.GAUGE_NAME);
			String modelType = (String )msg.getProperty(IGaugeProtocol.MODEL_TYPE);
			String modelName = (String )msg.getProperty(IGaugeProtocol.MODEL_NAME);
			TypeNamePair modelDesc = new TypeNamePair(modelType, modelName);
			// obtain value-property mappings
			List<ValuePropertyMappingPair> mappings = new ArrayList<ValuePropertyMappingPair>();
			cnt = (Integer )msg.getProperty(IGaugeProtocol.MAPPING + IGaugeProtocol.SIZE);
			for (int i=0; i < cnt; ++i) {
				String key = IGaugeProtocol.MAPPING + Rainbow.USCORE + i;
				mappings.add(ValuePropertyMappingPair.parseMappingPair((String )msg.getProperty(key)));
			}
			if (m_logger.isTraceEnabled())
				m_logger.trace("GM[" + m_manager.gaugeType() + "] creating Gauge "
						+ gaugeName + " for model " + modelDesc.toString()
						+ " with setup params " + Arrays.toString(setupParams.toArray())
						+ " and mappings " + Arrays.toString(mappings.toArray()));
			m_manager.createGauge(gaugeName, modelDesc, setupParams, mappings);
			break;
		case DELETE:
			m_manager.deleteGauge(id);
			break;
		case QUERY_GAUGEMETAINFO:
			// obtain meta config params
			List<TypeNamePair> metaConfigParams= new ArrayList<TypeNamePair>();
			cnt = (Integer )msg.getProperty(IGaugeProtocol.META_CONFIG_PARAM + IGaugeProtocol.SIZE);
			for (int i=0; i < cnt; ++i) {
				String key = IGaugeProtocol.META_CONFIG_PARAM + Rainbow.USCORE + i;
				metaConfigParams.add(TypeNamePair.parsePair((String )msg.getProperty(key)));
			}
			// obtain meta values
			List<TypeNamePair> metaValues = new ArrayList<TypeNamePair>();
			cnt = (Integer )msg.getProperty(IGaugeProtocol.META_VALUE + IGaugeProtocol.SIZE);
			for (int i=0; i < cnt; ++i) {
				String key = IGaugeProtocol.META_VALUE + Rainbow.USCORE + i;
				metaValues.add(TypeNamePair.parsePair((String )msg.getProperty(key)));
			}
			m_manager.queryGaugeMetaInfo(metaConfigParams, metaValues);
			break;
		}
	}

}
