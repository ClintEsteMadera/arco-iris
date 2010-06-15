/**
 * Created January 16, 2007.
 */
package org.sa.rainbow.translator.gauges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sa.rainbow.core.IDisposable;
import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.core.error.NotImplementedException;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.Util;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * This abstract GaugeManager class implements the common methods and data
 * members for the Gauge subclasses.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class AbstractGaugeManager implements IDisposable, IGaugeManager {

	protected RainbowLogger m_logger = null;
	private String m_id = null;
	/** Interned String indicating the gauge type of this Gauge Manager */
	private String m_gaugeType = null;
	protected Map<String,IGauge> m_gaugeMap = null;
	private GaugeManagerEventHandler m_mgrEventHandler = null;

	/**
	 * Main Constructor.
	 */
	public AbstractGaugeManager (String gaugeType) {
		m_logger = RainbowLoggerFactory.logger(getClass());
		m_id = Util.genID(gaugeType);
		assert gaugeType != null;
		m_gaugeType = gaugeType.intern();
		m_gaugeMap = new HashMap<String,IGauge>();

		m_mgrEventHandler = new GaugeManagerEventHandler(this);
		Rainbow.eventService().listen(IEventService.TOPIC_GAUGE_BUS, m_mgrEventHandler);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose () {
		Rainbow.eventService().unlisten(m_mgrEventHandler);

		// delete all the managed gauges first
		for (String id : new ArrayList<String>(m_gaugeMap.keySet())) {
			deleteGauge(id);
		}
		m_gaugeMap.clear();

		// null-out data members
		m_gaugeMap = null;
		m_gaugeType = null;
		m_mgrEventHandler = null;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#isDisposed()
	 */
	public boolean isDisposed () {
		return m_gaugeMap == null;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.Identifiable#id()
	 */
	public String id() {
		return m_id;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGaugeManager#managesType(java.lang.String)
	 */
	public boolean managesType (String gaugeType) {
		if (gaugeType == null) return false;  // guard against null

		boolean manages = (gaugeType.intern() == m_gaugeType);
		return manages;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGaugeManager#gaugeType()
	 */
	public String gaugeType() {
		return m_gaugeType;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGaugeManager#createGauge(java.lang.String, org.sa.rainbow.util.TypeNamePair, java.util.List, java.util.List)
	 */
	public IGauge createGauge (String gaugeName, TypeNamePair modelDesc,
			List<AttributeValueTriple> setupParams,
			List<ValuePropertyMappingPair> mappings) {

		IGauge gauge = doCreateGauge(gaugeName, modelDesc, setupParams, mappings);
		synchronized (m_gaugeMap) {
			m_gaugeMap.put(gauge.id(), gauge);
		}
		return gauge;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGaugeManager#deleteGauge(java.lang.String)
	 */
	public boolean deleteGauge (String id) {
		boolean rv = false;
		synchronized (m_gaugeMap) {  // one of EventService or Delegate will have to wait
			if (m_gaugeMap.containsKey(id)) {
				IRainbowRunnable runnable = (IRainbowRunnable )m_gaugeMap.remove(id);
				runnable.terminate();
				Util.waitUntilDisposed(runnable);
				rv = true;
			}
		}

		return rv;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGaugeManager#reconfigureGauges()
	 */
	public boolean reconfigureGauges() {
		boolean rv = true;
		List<IGauge> gauges = null;
		synchronized (m_gaugeMap) {
			gauges = new ArrayList<IGauge>(m_gaugeMap.values());
		}
		for (IGauge gauge : gauges) {
			rv &= gauge.reconfigureGauge();
		}
		return rv;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.IGaugeManager#queryGaugeMetaInfo(java.util.List, java.util.List)
	 */
	public boolean queryGaugeMetaInfo (List<TypeNamePair> configParamsMeta,
			List<TypeNamePair> valuesMeta) {
		throw new NotImplementedException("queryGaugeMetaInfo not yet implemented!");
	}

	protected GaugeManagerEventHandler eventHandler () {
		return m_mgrEventHandler;
	}

	protected String generateUID () {
		return Util.genID(m_gaugeType + Util.timestampShort());
	}

	protected abstract IGauge doCreateGauge (String gaugeName, TypeNamePair modelDesc,
			List<AttributeValueTriple> setupParams,
			List<ValuePropertyMappingPair> mappings);

}
