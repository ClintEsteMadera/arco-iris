/**
 * Created December 14, 2006.
 */
package org.sa.rainbow.model.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.standalone.resource.StandaloneLanguagePackHelper;
import org.apache.log4j.Level;
import org.sa.rainbow.core.IDisposable;
import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.model.GaugeInstanceDescription;
import org.sa.rainbow.model.Model;
import org.sa.rainbow.monitor.SystemDelegate;
import org.sa.rainbow.scenario.model.ArcoIrisModel;
import org.sa.rainbow.translator.gauges.GaugeConsumerEventHandler;
import org.sa.rainbow.translator.gauges.IGaugeConsumer;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.Util;
import org.sa.rainbow.util.ValuePropertyMappingPair;

import ar.uba.dc.arcoiris.atam.scenario.model.Stimulus;
import ar.uba.dc.arcoiris.selfhealing.AttributeValueTripleWithStimulus;

/**
 * Coordinates the creation and deletion of, and coordination with, gauges. Communication with the gauges occur across
 * the Gauge Event Bus. In addition, every host is assumed to have a RainbowDelegate to act on behalf of this
 * coordinator on that remote machine.
 * <p>
 * At initiation, the coordinator reads a gauge specification file (most likely separate from the Acme description of
 * the target system) and causes the deployment of the necessary gauges on the appropriate locations (hosts). This
 * coordinator keeps track of what gauges are created and active, and is the contact point to obtain a reference to the
 * IGauge.
 * <p>
 * Algorithm:
 * <ol>
 * <li>Read gauge specs (done in {@link org.sa.rainbow.core.Rainbow <code>Rainbow</code>})
 * <li>Obtain set of gauge types, and issue event to connect to those
 * <li>Create all gauge instances ({@link org.sa.rainbow.model.manager.ModelManager <code>ModelManager</code>} takes
 * care of this by invoking initGauges())
 * <li>Start handling gauge reports of values
 * <li>Monitor beacons...
 * <li>Clean-up: delete gauges (actually, would already be done by the {@link org.sa.rainbow.translator.RainbowDelegate
 * <code>RainbowDelegate</code>}!)
 * </ol>
 */
public class ArcoIrisGaugeCoordinator implements IDisposable, IGaugeConsumer {

	private RainbowLogger m_logger = null;
	private String m_id = null;
	private SystemDelegate m_sys = null;
	/** Stores, by gauge ID, a hash to the Gauge instance */
	private Map<String, GaugeInstanceDescription> m_id2Inst = null;
	private GaugeConsumerEventHandler m_eventHandler = null;
	private State m_state = State.ALL_GAUGES_UNINIT;

	/**
	 * Default Constructor.
	 */
	public ArcoIrisGaugeCoordinator() {
		m_logger = RainbowLoggerFactory.logger(getClass());
		m_id = Util.genID(getClass());
		m_sys = (SystemDelegate) Oracle.instance().targetSystem();
		m_id2Inst = new HashMap<String, GaugeInstanceDescription>();
		Rainbow.instance().loadGaugeDesc(Oracle.instance().rainbowModel());
		m_eventHandler = new GaugeConsumerEventHandler(this);
		Rainbow.eventService().listen(IEventService.TOPIC_GAUGE_BUS, m_eventHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose() {
		// notify all created gauges to delete
		for (GaugeInstanceDescription instSpec : new ArrayList<GaugeInstanceDescription>(m_id2Inst.values())) {
			synchronized (instSpec) {
				m_eventHandler.deleteGauge(instSpec.id(), new TypeNamePair(instSpec.gaugeType(), instSpec.gaugeName()),
						instSpec.modelDesc());
			}
		}
		// wait only a little bit for gauge deletion notifications
		log(Level.TRACE, "Waiting a little for Gauge Deletion notifications...");
		Beacon beacon = new Beacon(3 * IRainbowRunnable.LONG_SLEEP_TIME);
		beacon.mark();
		while (m_state != State.ALL_GAUGES_DELETED && !beacon.periodElapsed()) {
			Util.pause(IRainbowRunnable.SLEEP_TIME);
		}

		Rainbow.eventService().unlisten(m_eventHandler);
		m_id2Inst.clear();
		log(Level.TRACE, "terminated.");

		// null-out data members
		beacon = null;
		m_state = null;
		m_id2Inst = null;
		m_sys = null;
		m_eventHandler = null; // marks this instance disposed
		m_logger = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#isDisposed()
	 */
	public boolean isDisposed() {
		return m_eventHandler == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.Identifiable#id()
	 */
	public String id() {
		return m_id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.translator.gauges.IGaugeConsumer#gaugeBeacon(java.lang.String, long)
	 */
	public void gaugeBeacon(String id, long period) {
		// Util.logger().info("BEACON: Gauge ID[" + id + "]");
		GaugeInstanceDescription instSpec = m_id2Inst.get(id);
		if (instSpec != null) {
			synchronized (instSpec) {
				// set beacon value
				instSpec.beacon().setPeriod(period);
				// mark beacon
				instSpec.beacon().mark();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.translator.gauges.IGaugeConsumer#onReportCreated(java.lang.String,
	 * org.sa.rainbow.util.TypeNamePair, org.sa.rainbow.util.TypeNamePair)
	 */
	public void onReportCreated(String id, TypeNamePair gaugeDesc, TypeNamePair modelDesc) {
		// mark gauge created, meaning ready for configure
		GaugeInstanceDescription instSpec = Rainbow.instance().gaugeDesc().instSpec.get(gaugeDesc.name());
		if (instSpec != null) {
			boolean proceed = false; // avoids nested synchronized block
			synchronized (instSpec) {
				// make sure we have a match
				if (instSpec.gaugeType().equals(gaugeDesc.type()) && instSpec.modelDesc().equals(modelDesc)) {
					// matched gauge! store ID and add Gauge to created-map
					log(Level.DEBUG, "Gauge instance CREATED ID[" + id + "]");
					instSpec.setID(id);
					instSpec.setState(GaugeInstanceDescription.State.CREATED);
					proceed = true;
				}
			}
			if (proceed) {
				synchronized (m_id2Inst) {
					m_id2Inst.put(id, instSpec);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.translator.gauges.IGaugeConsumer#onReportConfigured(java.lang.String,
	 * org.sa.rainbow.util.TypeNamePair, org.sa.rainbow.util.TypeNamePair, java.util.List)
	 */
	public void onReportConfigured(String id, TypeNamePair gaugeDesc, TypeNamePair modelDesc,
			List<AttributeValueTriple> configParams) {
		// mark gauge alive, meaning configured and ready to report
		GaugeInstanceDescription instSpec = m_id2Inst.get(id);
		if (instSpec != null) {
			synchronized (instSpec) {
				// make sure we have a gauge match
				if (instSpec.gaugeName().equals(gaugeDesc.name()) && instSpec.modelDesc().equals(modelDesc)) {
					// matched gauge! mark configured
					log(Level.DEBUG, "Gauge instance CONFIGURED ID[" + id + "]");
					instSpec.setState(GaugeInstanceDescription.State.ALIVE);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.translator.gauges.IGaugeConsumer#onReportDeleted(java.lang.String,
	 * org.sa.rainbow.util.TypeNamePair, org.sa.rainbow.util.TypeNamePair)
	 */
	public void onReportDeleted(String id, TypeNamePair gaugeDesc, TypeNamePair modelDesc) {
		GaugeInstanceDescription instSpec = m_id2Inst.get(id);
		if (instSpec != null) {
			boolean remove = false; // avoids nested synchronized block
			synchronized (instSpec) {
				// make sure we have a gauge match
				if (instSpec.gaugeName().equals(gaugeDesc.name()) && instSpec.modelDesc().equals(modelDesc)) {
					// matched gauge! mark terminated
					log(Level.DEBUG, "Gauge DELETED ID[" + id + "]");
					instSpec.setState(GaugeInstanceDescription.State.TERMINATED);
					remove = true;
				}
			}
			if (remove) {
				// remove gauge instance spec from created-map
				synchronized (m_id2Inst) {
					m_id2Inst.remove(id);
					if (m_id2Inst.size() == 0) {
						m_state = State.ALL_GAUGES_DELETED;
						log(Level.DEBUG, "All Gauges DELETED!");
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.translator.gauges.IGaugeConsumer#onReportValue(java.lang.String,
	 * org.sa.rainbow.util.TypeNamePair, org.sa.rainbow.util.TypeNamePair, org.sa.rainbow.util.AttributeValueTriple)
	 */
	public void onReportValue(String id, TypeNamePair gaugeDesc, TypeNamePair modelDesc, AttributeValueTriple value) {
		GaugeInstanceDescription instSpec = m_id2Inst.get(id);
		if (instSpec != null) {
			synchronized (instSpec) {
				// make sure we have a gauge match
				if (instSpec.gaugeName().equals(gaugeDesc.name()) && instSpec.modelDesc().equals(modelDesc)) {
					// matched gauge! update rainbow model
					updateRainbowModelWith(value);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.translator.gauges.IGaugeConsumer#onReportMultipleValues(java.lang.String,
	 * org.sa.rainbow.util.TypeNamePair, org.sa.rainbow.util.TypeNamePair, java.util.List)
	 */
	public void onReportMultipleValues(String id, TypeNamePair gaugeDesc, TypeNamePair modelDesc,
			List<AttributeValueTriple> values) {
		GaugeInstanceDescription instSpec = m_id2Inst.get(id);
		if (instSpec != null) {
			synchronized (instSpec) {
				// make sure we have a gauge match
				if (instSpec.gaugeName().equals(gaugeDesc.name()) && instSpec.modelDesc().equals(modelDesc)) {
					// matched gauge! update rainbow model
					log(Level.DEBUG, "Gauge ID[" + id + "]");
					for (AttributeValueTriple value : values) {
						this.updateRainbowModelWith(value);
					}
				}
			}
		}
	}

	/**
	 * Method called by the ModelManager thread (repeatedly) to check Gauge health.
	 */
	public void checkGauges() {
		for (GaugeInstanceDescription instSpec : Rainbow.instance().gaugeDesc().instDescList()) {
			String removeId = null; // avoids nested synchronized block
			synchronized (instSpec) {
				if (m_id2Inst.containsKey(instSpec.id()) && instSpec.isAlive() && instSpec.beacon().isExpired()) {
					log(Level.ERROR, "GC: Gauge ID[" + instSpec.id() + "] seems to have died, removing!");
					removeId = instSpec.id();
				}
			}
			if (removeId != null) {
				synchronized (m_id2Inst) {
					m_id2Inst.remove(removeId);
				}
			}
		}
	}

	/**
	 * Method called via the ModelManager thread to issue create gauge events to the GaugeManagers to cause creation of
	 * Gauges.
	 * <p>
	 * Design Note: This method may be called at anytime by the ModelManager.
	 */
	public void createGauges() {
		boolean allCreated = true;
		for (GaugeInstanceDescription instSpec : Rainbow.instance().gaugeDesc().instDescList()) {
			synchronized (instSpec) {
				if (instSpec.notYetCreated()) {
					allCreated = false;
					// attempt to issue create event to create the Gauge
					String location = instSpec.location();
					if (location != null && m_sys.existsDelegateAtLocation(location)) {
						instSpec.setState(GaugeInstanceDescription.State.CREATING);
						if (m_logger.isTraceEnabled())
							log(Level.TRACE, "GC: creating gauge '" + instSpec.gaugeName() + "'");
						m_eventHandler.createGauge(location,
								new TypeNamePair(instSpec.gaugeType(), instSpec.gaugeName()), instSpec.modelDesc(),
								instSpec.setupParams(), instSpec.mappings());
					}
				} else if (instSpec.notYetConfigured()) {
					allCreated = false;
				} else if (instSpec.state() == GaugeInstanceDescription.State.TERMINATED) {
					// at this point, if location once again exists, then reset state
					if (m_sys.existsDelegateAtLocation(instSpec.location())) {
						instSpec.setState(GaugeInstanceDescription.State.UNINITIALIZED);
						// this state hopefully triggers Gauge re-creation next round
					}
				} // otherwise, this gauge is creating/created
			}
		}
		// if all gauges are configured, and the created list size is the same
		if (m_state.compareTo(State.ALL_GAUGES_CREATED) < 0 && allCreated
				&& Rainbow.instance().gaugeDesc().instSpec.size() == m_id2Inst.size()) {
			// mark all gauges created
			m_state = State.ALL_GAUGES_CREATED;
			log(Level.DEBUG, "All Gauges CREATED!");
		}
	}

	/**
	 * Method called via the ModelManager thread to issue configure gauge events to, and thus configure, the Gauges that
	 * have been created.
	 * <p>
	 * Design Note: This method is called by the ModelManager ONLY after all expected target locations have been
	 * created; after that, this method may be called whenever. This design allows gauges to communicate with probes
	 * across Delegate boundaries.
	 */
	public void configureGauges() {
		boolean allConfigured = true;
		for (GaugeInstanceDescription instSpec : Rainbow.instance().gaugeDesc().instDescList()) {
			synchronized (instSpec) {
				if (instSpec.notYetConfigured()) {
					allConfigured = false;
					// configure the gauge, if ID is set
					if (instSpec.id() != null) {
						instSpec.setState(GaugeInstanceDescription.State.CONFIGURING);
						if (m_logger.isTraceEnabled())
							log(Level.TRACE, "configuring gauge '" + instSpec.gaugeName() + "'");
						// retrieve any states of the mapped model properties
						String modelName = instSpec.modelDesc().name();
						for (ValuePropertyMappingPair vp : instSpec.mappings()) {
							String prop = modelName + Util.DOT + vp.propertyName();
							Object val = Oracle.instance().rainbowModel().getProperty(prop);
							if (val != null) { // add to config list
								IAcmeProperty propVal = (IAcmeProperty) val;
								try {
									String valStr = StandaloneLanguagePackHelper.defaultLanguageHelper()
											.propertyValueToString(propVal.getValue(), null);
									AttributeValueTriple configTriple = new AttributeValueTriple(
											AttributeValueTriple.DEFAULT_TYPE, vp.valueName(), valStr);
									instSpec.addConfigParam(configTriple);
								} catch (Exception e) { // ignore??
								}
							}
						}
						m_eventHandler.configureGauge(instSpec.id(),
								new TypeNamePair(instSpec.gaugeType(), instSpec.gaugeName()), instSpec.modelDesc(),
								instSpec.configParams());
					}
				} else if (!instSpec.isAlive()) {
					allConfigured = false;
				} // otherwise, this gauge is configured
			}
		}
		// if all gauges are configured, and the created list size is the same
		if (m_state.compareTo(State.ALL_GAUGES_READY) < 0 && allConfigured
				&& Rainbow.instance().gaugeDesc().instSpec.size() == m_id2Inst.size()) {
			// mark all gauges ready
			m_state = State.ALL_GAUGES_READY;
			log(Level.TRACE, "All Gauges READY!");
		}
	}

	private void updateRainbowModelWith(AttributeValueTriple value) {
		String updMsg = IRainbowHealthProtocol.DATA_MODEL_PROPERTY + value.type() + "=" + value.value();
		Model rainbowModel = Oracle.instance().rainbowModel();

		if (value instanceof AttributeValueTripleWithStimulus) {
			Stimulus stimulus = ((AttributeValueTripleWithStimulus) value).getStimulus();
			((ArcoIrisModel) rainbowModel).updateProperty(value.type(), value.value(), stimulus);
			updMsg += " / stimulus: " + stimulus;
		} else {
			// Rainbow's usual behavior
			rainbowModel.updateProperty(value.type(), value.value());
		}
		log(Level.INFO, updMsg);
		Util.dataLogger().info(updMsg);
	}

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEnginePanel(m_logger, level, "[GC] " + txt, t);
	}
}
