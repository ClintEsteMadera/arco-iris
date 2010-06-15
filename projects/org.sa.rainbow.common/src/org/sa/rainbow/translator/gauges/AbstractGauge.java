/**
 * Created November 2, 2006.
 */
package org.sa.rainbow.translator.gauges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.sa.rainbow.core.AbstractRainbowRunnable;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.event.IProtocolAction;
import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.health.RainbowCloudEventHandler;
import org.sa.rainbow.translator.probes.IProbeConsumer;
import org.sa.rainbow.translator.probes.ProbeProtocolHandler;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.Pair;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.Util;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * Abstract definition of gauge with common methods to simplify gauge
 * implementations.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class AbstractGauge extends AbstractRainbowRunnable
implements IGauge, IProbeConsumer {

	private String m_id = null;
	private GaugeEventHandler m_gaugeEventHandler = null;

	/** Used to determine when to fire off beacon to consumers;
	 * the period will be set by the Gauge implementation subclase */
	protected Beacon m_gaugeBeacon = null;
	protected TypeNamePair m_gaugeDesc = null;
	protected TypeNamePair m_modelDesc = null;
	protected Map<String,AttributeValueTriple> m_setupParams = null;
	protected Map<String,AttributeValueTriple> m_configParams = null;
	protected Map<String,String> m_mappings = null;
	protected Map<String,AttributeValueTriple> m_lastValues = null;
	// for probe interaction
	protected ProbeProtocolHandler m_pbHandler = null;
	protected Beacon m_probeBeacon = null;  // to track probe liveness

	/**
	 * Main Constructor for the Gauge.
	 * @param threadName  the name of the Gauge thread
	 * @param id  the unique ID of the Gauge
	 * @param beaconPeriod  the liveness beacon period of the Gauge
	 * @param gaugeDesc  the type-name description of the Gauge
	 * @param modelDesc  the type-name description of the Model the Gauge updates
	 * @param setupParams  the list of setup parameters with their values
	 * @param mappings  the list of Gauge Value to Model Property mappings
	 */
	public AbstractGauge (String threadName, String id, long beaconPeriod,
			TypeNamePair gaugeDesc, TypeNamePair modelDesc,
			List<AttributeValueTriple> setupParams,
			List<ValuePropertyMappingPair> mappings) {

		super(threadName);

		m_id = id;
		m_gaugeBeacon = new Beacon(beaconPeriod);
		m_gaugeDesc = gaugeDesc;
		m_modelDesc = modelDesc;

		m_setupParams = new HashMap<String,AttributeValueTriple>();
		m_configParams = new HashMap<String,AttributeValueTriple>();
		m_mappings = new HashMap<String,String>();
		m_lastValues = new HashMap<String,AttributeValueTriple>();

		// store the setup parameters
		for (AttributeValueTriple param : setupParams) {
			m_setupParams.put(param.name(), param);
		}
		// store the mapping info
		for (ValuePropertyMappingPair mapping : mappings) {
			m_mappings.put(mapping.valueName(), mapping.propertyName());
		}

		// register on the Gauge Bus
		m_gaugeEventHandler = new GaugeEventHandler(this);
		Rainbow.eventService().listen(IEventService.TOPIC_GAUGE_BUS, m_gaugeEventHandler);

		// register this Gauge with Rainbow, and report created
		Rainbow.registerGauge(this);
		m_gaugeEventHandler.reportCreated();
		m_gaugeBeacon.mark();

		// setup probe stuff, register on the Probe Bus
		m_probeBeacon = new Beacon();
		m_pbHandler = new ProbeProtocolHandler(this);
		Rainbow.eventService().listen(IEventService.TOPIC_PROBE_BUS, m_pbHandler);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose () {
		m_gaugeEventHandler.reportDeleted();

		Rainbow.eventService().unlisten(m_gaugeEventHandler);

		m_setupParams.clear();
		m_configParams.clear();
		m_mappings.clear();
		m_lastValues.clear();

		// null-out data members
		m_gaugeEventHandler = null;
//		m_id = null;  // keep value for log output
		m_gaugeDesc = null;
		m_modelDesc = null;
		m_setupParams = null;
		m_configParams = null;
		m_mappings = null;
		m_lastValues = null;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGauge#id()
	 */
	public String id () {
		return m_id;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGauge#beaconPeriod()
	 */
	public long beaconPeriod () {
		return m_gaugeBeacon.period();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGauge#gaugeDesc()
	 */
	public TypeNamePair gaugeDesc () {
		return m_gaugeDesc;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGauge#modelDesc()
	 */
	public TypeNamePair modelDesc () {
		return m_modelDesc;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGauge#configureGauge(java.util.List)
	 */
	public boolean configureGauge (List<AttributeValueTriple> configParams) {
		for (AttributeValueTriple triple : configParams) {
			m_configParams.put(triple.name(), triple);
			if (triple.name().equals(CONFIG_SAMPLING_FREQUENCY)) {  // set the runner timer directly
				setSleepTime((Long )triple.value());
			}
			if (triple.name().equals(CONFIG_PROBE_MAPPING)) {  // set target probe type
				pubProbeGaugeMapping((String )triple.value());
			}
			if (triple.name().equals(CONFIG_PROBE_MAPPING_LIST)) {  // set list of target probe types
				StringTokenizer tokens = new StringTokenizer((String )triple.value(), ",");
				while (tokens.hasMoreTokens()) {
					pubProbeGaugeMapping(tokens.nextToken().trim());
				}
			}
			if (m_mappings.keySet().contains(triple.name())) {  // a property value
				initProperty(triple.name(), triple.value());
			}
		}
		m_gaugeEventHandler.reportConfigured(configParams);
		return true;
	}
	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGauge#reconfigureGauge()
	 */
	public boolean reconfigureGauge() {
		return configureGauge(new ArrayList<AttributeValueTriple>(m_configParams.values()));
	}

	abstract protected void initProperty (String name, Object value);

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGauge#queryGaugeState(java.util.List, java.util.List, java.util.List)
	 */
	public boolean queryGaugeState (List<AttributeValueTriple> setupParams,
			List<AttributeValueTriple> configParams, List<ValuePropertyMappingPair> mappings) {
		// transfer the setup params
		for (AttributeValueTriple param : m_setupParams.values()) {
			setupParams.add(param);
		}
		// transfer the configu params
		for (AttributeValueTriple param : m_configParams.values()) {
			configParams.add(param);
		}
		// transfer the mappings
		for (Map.Entry<String,String> mapping : m_mappings.entrySet()) {
			mappings.add(new ValuePropertyMappingPair(mapping.getKey(), mapping.getValue()));
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGauge#querySingleValue(org.sa.rainbow.util.AttributeValueTriple)
	 */
	public boolean querySingleValue (AttributeValueTriple value) {
		if (m_lastValues.containsKey(value.attribute())) {
			value.setSecondValue(m_lastValues.get(value.attribute()));
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGauge#queryAllValues(java.util.List)
	 */
	public boolean queryAllValues (List<AttributeValueTriple> values) {
		for (AttributeValueTriple value : m_lastValues.values()) {
			values.add(value);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbeConsumer#setProbeBeaconPeriod(long)
	 */
	public void setProbeBeaconPeriod (long period) {
		m_probeBeacon.setPeriod(period);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbeConsumer#probeBeacon()
	 */
	public void probeBeacon () {
		m_probeBeacon.mark();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbeConsumer#probeReport(java.lang.String)
	 */
	public void probeReport (String data) {
		probeBeacon();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#log(java.lang.String)
	 */
	@Override
	protected void log (String txt) {
		String msg = "G[" + id() + "] " + txt;
		RainbowCloudEventHandler.sendTranslatorLog(msg);
		// avoid duplicate output in the master's process
		if (! Rainbow.isMaster()) m_logger.info(msg);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#runAction()
	 */
	@Override
	protected void runAction () {
		// report Gauge's beacon
		if (m_gaugeBeacon.periodElapsed()) {
			// send beacon signal to Rainbow
			m_gaugeEventHandler.sendBeacon();
			m_gaugeBeacon.mark();
		}
	}

	protected GaugeEventHandler eventHandler () {
		return m_gaugeEventHandler;
	}

	/**
	 * Assuming target location is stored as setup parameter IGauge.SETUP_LOCATION,
	 * this method returns the value of that location.
	 * @return String  the string indicating the deployment location
	 */
	protected String deploymentLocation () {
		return (String )m_setupParams.get(SETUP_LOCATION).value();
	}


	/**
	 * Given a probe type, sends message IProtocolAction.MAP2GAUGE on probe bus
	 * to map probes of that type to this gauge.  Probe Type may have location
	 * info, so that gets parsed and passed along.
	 * @param probeType  the probe type identifier
	 */
	private void pubProbeGaugeMapping(String probeType) {
		// check if probe type contains location info
		Pair<String,String> nameLocPair = Util.decomposeID(probeType);
		if (nameLocPair.secondValue() == null) {  // default: host-bound
			nameLocPair.setSecondValue(deploymentLocation());
		} else if (nameLocPair.secondValue().equals(IProtocolAction.SEND_ALL_LOCATION)) {
			nameLocPair.setSecondValue(null);  // null to notify all location
		}
		if (m_logger.isDebugEnabled())
			m_logger.debug("Notify P-G map: " + nameLocPair.firstValue() + "@" +
					nameLocPair.secondValue() + " :: " + m_id);
		m_pbHandler.mapProbeToGauge(nameLocPair.secondValue(), nameLocPair.firstValue(), m_id);
	}

}
