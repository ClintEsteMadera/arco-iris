/**
 * Created November 1, 2006.
 */
package org.sa.rainbow.monitor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.sa.rainbow.core.IDisposable;
import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.core.error.NotImplementedException;
import org.sa.rainbow.event.EventTypeFilter;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.event.IProtocolAction;
import org.sa.rainbow.event.IRainbowEventListener;
import org.sa.rainbow.event.IRainbowMessage;
import org.sa.rainbow.event.RainbowEventAdapter;
import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.health.RainbowCloudEventHandler;
import org.sa.rainbow.health.RainbowCloudRef;
import org.sa.rainbow.health.IRainbowHealthProtocol.CloudType;
import org.sa.rainbow.translator.effectors.EffectorRef;
import org.sa.rainbow.translator.effectors.IEffector;
import org.sa.rainbow.translator.effectors.IEffectorProtocol;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.Util;

/**
 * This class acts as the conceptual delegate for the real target system,
 * providing interfaces for properties from the system to be propagated to
 * the model.
 * <p>
 * Design Intent: <ul>
 * <li> The SystemDelegate allows Rainbow (through the ModelManager) to query
 * properties of the system... or shouldn't this be done through Gauges?
 * <li> Properties monitored by Rainbow actually propagate from the Gauges
 * through the GaugeCoordinator
 * <li> The SystemDelegate allows Rainbow to acquire references to effectors
 * provided by the target system
 * </ul>
 * <p>
 * HISTORY: <ul>
 * <li>[January 17, 2007]<br>
 *   - Changed class from PhantomSystem to SystemDelegate, merging "Translator"
 *     capabilities.
 * <li>
 * </ul>
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class SystemDelegate implements IDisposable, TargetSystem {

	public static enum State {
		UNCONNECTED, CONNECTED, DELEGATE_STARTED, TERMINATED
	}

	public static final String NAME = "Target System Delegate";

	private static final String DELEGATE_CLASS = "org.sa.rainbow.translator.RainbowDelegate";

	private RainbowLogger m_logger = null;
	private State m_state = State.UNCONNECTED;
	private List<String> m_locations = null;
	private Map<String,RainbowCloudRef> m_idCloudMap = null;
	private Map<String,RainbowCloudRef> m_locationDelegateMap = null;
	private Map<String,RainbowCloudRef> m_locationPbRelayMap = null;
	private Map<String,IEffector> m_id2Effectors = null;
	private IRainbowEventListener m_healthListener = null;
	private IRainbowEventListener m_effectorListener = null;

	/**
	 * Default constructor.
	 */
	public SystemDelegate () {
		m_logger = RainbowLoggerFactory.logger(getClass());
		// determine the expected list of remote locations
		m_locations = new ArrayList<String>();
		int cnt = Integer.parseInt(Rainbow.property(Rainbow.PROPKEY_TARGET_LOCATION + Util.SIZE_SFX));
		for (int i=0; i < cnt; ++i) {
			m_locations.add(Rainbow.property(Rainbow.PROPKEY_TARGET_LOCATION + Util.DOT + i));
		}
		m_idCloudMap = new HashMap<String,RainbowCloudRef>();
		m_locationDelegateMap = new HashMap<String,RainbowCloudRef>();
		m_locationPbRelayMap = new HashMap<String,RainbowCloudRef>();
		m_id2Effectors = new HashMap<String,IEffector>();
		String id = Util.genID(getClass());
		m_healthListener = healthListener(id);
		m_effectorListener = effectorListener(id);

		Rainbow.eventService().listen(IEventService.TOPIC_RAINBOW_HEALTH, m_healthListener);
		Rainbow.eventService().listen(IEventService.TOPIC_EFFECTOR_BUS, m_effectorListener);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose() {
		// send termination signal if necessary
		if (m_locationDelegateMap.size() > 0) {
			signalTerminate();  // send signal and wait a little
			log("Waiting a little for RainbowDelegate termination...");
			Beacon beacon = new Beacon(5*IRainbowRunnable.LONG_SLEEP_TIME);
			beacon.mark();
			while (m_locationDelegateMap.size() > 0 && !beacon.periodElapsed()) {
				Util.pause(IRainbowRunnable.SLEEP_TIME);
			}
			synchronized (m_locationDelegateMap) {
				m_locationDelegateMap.clear();
			}
			synchronized (m_locationPbRelayMap) {  // clear probe bus list, too
				m_locationPbRelayMap.clear();
			}
		}

		Rainbow.eventService().unlisten(m_effectorListener);
		Rainbow.eventService().unlisten(m_healthListener);

		synchronized (m_idCloudMap) {
			m_idCloudMap.clear();
		}
		synchronized (m_id2Effectors) {
			m_id2Effectors.clear();
		}
		log("terminated.");
		m_state = State.TERMINATED;

		// null-out data members
		m_idCloudMap = null;
		m_locationDelegateMap = null;
		m_locationPbRelayMap = null;
		m_id2Effectors = null;
		m_logger = null;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#isDisposed()
	 */
	public boolean isDisposed() {
		return isTerminated();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.TargetSystem#connect()
	 */
	public void connect () {
		if (m_state == State.UNCONNECTED) {
			m_state = State.CONNECTED;
			log("connecting to RainbowDelegates...");
		}
		try {
			Class<?> delegateClass = Class.forName(DELEGATE_CLASS);
			// don't need to keep track of delegate, since communicating via Rainbow Health Bus
			delegateClass.newInstance();
			Oracle.instance().writeTranslatorPanel(m_logger, "[SysD] Found LOCAL delegate " + DELEGATE_CLASS);
		} catch (ClassNotFoundException e) {     // ignore
		} catch (SecurityException e) {          // ignore
		} catch (IllegalArgumentException e) {   // ignore
		} catch (IllegalAccessException e) {     // ignore
		} catch (InstantiationException e) {
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.TargetSystem#isTerminated()
	 */
	public boolean isTerminated () {
		return m_state == State.TERMINATED;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.TargetSystem#getProperties()
	 */
	public Properties getProperties () {
		throw new UnsupportedOperationException("This method should never be called on SystemDelegate!");
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.TargetSystem#getChangedProperties()
	 */
	public Map<String, Object> getChangedProperties () {
		throw new UnsupportedOperationException("This method should never be called on SystemDelegate!");
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.TargetSystem#changeProperty(java.lang.String, java.lang.Object)
	 */
	public boolean changeProperty (String iden, Object value) {
		throw new UnsupportedOperationException("This method should never be called on SystemDelegate!");
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.TargetSystem#queryProperty(java.lang.String)
	 */
	public Object queryProperty (String iden) {
		throw new NotImplementedException("SystemDelegate.queryProperty() missing!");
	}

	
	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.TargetSystem#predictProperty(java.lang.String, long, org.sa.rainbow.monitor.TargetSystem.StatType)
	 */
	public Object predictProperty(String iden, long dur, StatType type) {
		throw new NotImplementedException("SystemDelegate.predictProperty() missing!");
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.TargetSystem#findServices(java.lang.String, java.util.Map)
	 */
	public Set<?> findServices (String type, Map<String, String> filters) {
		// TODO: ignore the filters for now
		throw new NotImplementedException("SystemDelegate.findServices() missing!");
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.TargetSystem#getEffector(java.lang.String, java.lang.String)
	 */
	public IEffector getEffector (String name, String target) {
		// search among list of available effectors using provided name and target
		String id = Util.genID(name, target);
		if (m_logger.isDebugEnabled())
			m_logger.debug("[SysD]: getEffector() called, composed ID: " + id);
		IEffector effector = m_id2Effectors.get(id.toLowerCase());
		if (effector == null) {
			// convert target into IP4 number (current Rainbow standard representation)
			if (target.equalsIgnoreCase("localhost")) {
				String localhost = Rainbow.property(Rainbow.PROPKEY_DEPLOYMENT_LOCATION);
				try {
					id = Util.genID(name, InetAddress.getByName(localhost).getHostAddress());
					if (m_logger.isDebugEnabled())
						m_logger.debug("[SysD]: getEffector() retries ID: " + id);
					effector = m_id2Effectors.get(id.toLowerCase());
				} catch (UnknownHostException e) {
					// ignore error
				}
			} else {
				try {
					id = Util.genID(name, InetAddress.getByName(target).getHostAddress());
					if (m_logger.isDebugEnabled())
						m_logger.debug("[SysD]: getEffector() retries ID: " + id);
					effector = m_id2Effectors.get(id.toLowerCase());
				} catch (UnknownHostException e) {
					// ignore error
				}
			}
		}
		if (m_logger.isDebugEnabled())
			m_logger.debug("[SysD]: Effector retrieved: " + (effector == null? "NULL" : effector.id()));
		return (effector == null) ? IEffector.NULL_EFFECTOR : effector;
	}

	public State state () {
		return m_state;
	}

	/**
	 * Returns whether the RainbowDelegate from all the known locations have
	 * reported their own creation.
	 * @return boolean  <code>true</code> if all locations have reported creation,
	 *     <code>false</code> otherwise.
	 */
	public boolean allLocationsCreated () {
		boolean allCreated = true;
		for (String loc : m_locations) {
			synchronized (m_locationDelegateMap) {
				if (! m_locationDelegateMap.containsKey(loc)) {  // noooo!
					allCreated = false;
					break;
				}
			}
		}
		return allCreated;
	}
	/**
	 * Returns whether the ProbeBusRelay of all known RainbowDelegates have
	 * reported creation, meaning RainbowDelegates are ready to monitor.
	 * @return boolean  <code>true</code> if all locations have their
	 *     ProbeBusRelay report creation, <code>false</code> otherwise.
	 */
	public boolean allLocationsReady () {
		boolean allReady = true;
		for (String loc : m_locations) {
			synchronized (m_locationPbRelayMap) {
				if (! m_locationPbRelayMap.containsKey(loc)) {  // noooo!
					allReady = false;
					break;
				}
			}
		}
		return allReady;
	}
	public boolean existsDelegateAtLocation (String loc) {
		return m_locationDelegateMap.containsKey(loc);
	}
	public String[] delegateLocations () {
		return m_locationDelegateMap.keySet().toArray(new String[0]);
	}

	/**
	 * Checks the health of current list of Rainbow clouds.
	 * @return boolean  flag indicating whether any cloud may have been "unhealthy"
	 */
	public boolean checkCloudHealth () {
		boolean healthy = true;
		for (RainbowCloudRef cloud : new ArrayList<RainbowCloudRef>(m_idCloudMap.values())) {
			if (cloud.beacon().isExpired()) {
				// report expiration, and remove from the list of existing delegates
				m_logger.error("Rainbow Cloud ID " + cloud.id() + " may have died, removing!");
				m_idCloudMap.remove(cloud.id());  // should NOT cause concurrent mod exception
				if (cloud.type() == CloudType.DELEGATE) {
					// set unstarted & remove cloud
					cloud.setStarted(false);
					m_locationDelegateMap.remove(cloud.location());
				}
			} else {
				if (cloud.type() == CloudType.DELEGATE && !cloud.isStarted()
						&& m_state == State.CONNECTED) {
					// good to start the RainbowDelegate
					log("Signalling delegate " + cloud.id() + " to start");
					RainbowCloudEventHandler.startCloud(cloud.id(), cloud.location());
					cloud.setStarted(true);
				}
			}
		}
		return healthy;
	}

	/**
	 * Sends the terminate signal to all Rainbow delegates.
	 */
	public void signalTerminate () {
		Oracle.instance().writeSystemPanel(m_logger, "*** Sending Delegates The TERMINATE Signal! ***");
		List<RainbowCloudRef> clouds = null;
		synchronized (m_idCloudMap) {
			clouds = new ArrayList<RainbowCloudRef>(m_idCloudMap.values());
		}
		for (RainbowCloudRef cloud : clouds) {
			// terminate only the delegates
			if (cloud.type() == IRainbowHealthProtocol.CloudType.DELEGATE) {
				RainbowCloudEventHandler.terminateCloud(cloud.id(), cloud.location());
			}
		}
	}

	/**
	 * Sends a kill signal to all the probes.
	 */
	public void signalKillProbes () {
		Oracle.instance().writeSystemPanel(m_logger, "=-~ Sending KILL Signal to Probes! ~-=");
		List<RainbowCloudRef> clouds = null;
		synchronized (m_idCloudMap) {
			clouds = new ArrayList<RainbowCloudRef>(m_idCloudMap.values());
		}
		for (RainbowCloudRef cloud : clouds) {
			// send kill to delegates only
			if (cloud.type() == IRainbowHealthProtocol.CloudType.DELEGATE) {
				RainbowCloudEventHandler.killProbes(cloud.location());
			}
		}
	}

	/**
	 * Sends a kill signal to all the probes.
	 */
	public void signalStartProbes () {
		Oracle.instance().writeSystemPanel(m_logger, "=-~ Sending START Signal to Probes! ~-=");
		List<RainbowCloudRef> clouds = null;
		synchronized (m_idCloudMap) {
			clouds = new ArrayList<RainbowCloudRef>(m_idCloudMap.values());
		}
		for (RainbowCloudRef cloud : clouds) {
			// send start to delegates only
			if (cloud.type() == IRainbowHealthProtocol.CloudType.DELEGATE) {
				RainbowCloudEventHandler.startProbes(cloud.location());
			}
		}
	}

	private IRainbowEventListener effectorListener (String id) {
		return new RainbowEventAdapter(id) {
			@EventTypeFilter(eventTypes={IProtocolAction.REPORT_OUTCOME})
			public void onMessage(IRainbowMessage msg) {
				String id = (String )msg.getProperty(IEffectorProtocol.ID);
				if (id == null) return;
				// An IEffector has reported outcome
				EffectorRef effector = (EffectorRef )m_id2Effectors.get(id.toLowerCase());
				if (effector != null && effector.outcome() == IEffector.Outcome.UNKNOWN) {
					// found the IEffector reference, fetch outcome state and set it
					String outcomeStr = (String )msg.getProperty(IEffectorProtocol.OUTCOME);
					IEffector.Outcome outcome = IEffector.Outcome.valueOf(outcomeStr);
					effector.setOutcome(outcome);
				}
			}
		};
	}

	private IRainbowEventListener healthListener (String id) {
		return new RainbowEventAdapter(id) {
			@EventTypeFilter(eventTypes={
					IProtocolAction.BEACON,
					IProtocolAction.REPORT_CREATED,
					IProtocolAction.REPORT_DELETED
			})
			public void onMessage(IRainbowMessage msg) {
				String id = (String )msg.getProperty(IRainbowHealthProtocol.ID);
				IProtocolAction action = IProtocolAction.valueOf((String )msg.getProperty(IProtocolAction.ACTION));
				switch (action) {
				case REPORT_CREATED:
					String typeStr = (String )msg.getProperty(IRainbowHealthProtocol.CLOUD_TYPE);
					if (typeStr == null) return;  // don't know nor care about the cloud type
					IRainbowHealthProtocol.CloudType type = IRainbowHealthProtocol.CloudType.valueOf(typeStr);
					String location = (String )msg.getProperty(IRainbowHealthProtocol.LOCATION);
					long beaconPer = (Long )msg.getProperty(IRainbowHealthProtocol.BEACON_PERIOD);
					// store reference to this Rainbow Cloud's info
					RainbowCloudRef cloud = new RainbowCloudRef(id, type, location, beaconPer);
					m_idCloudMap.put(id, cloud);
					switch (type) {
					case DELEGATE:  // store delegate reference by "location"
						synchronized (m_locationDelegateMap) {
							m_locationDelegateMap.put(location, cloud);
						}
						break;
					case PB_RELAY:  // store probe bus reference by "location"
						synchronized (m_locationPbRelayMap) {
							m_locationPbRelayMap.put(location, cloud);
						}
						break;
					case GAUGE:
					case PROBE:
						break;
					case EFFECTOR:
						String refID = id;
						String svcName = (String )msg.getProperty(IRainbowHealthProtocol.SERVICE_NAME);
						String effKindStr = (String )msg.getProperty(IRainbowHealthProtocol.EFFECTOR_KIND);
						IEffector.Kind effKind = IEffector.Kind.valueOf(effKindStr);
						IEffector eff = new EffectorRef(refID, location, svcName, effKind);
						synchronized (m_id2Effectors) {
							m_id2Effectors.put(refID.toLowerCase(), eff);  // store effector
						}
						break;
					}
					log("Received creation notification from " + type + " ID " + id);
					break;
				case BEACON:
					// update beacon timestamp
					id = (String )msg.getProperty(IRainbowHealthProtocol.ID);
					cloud = m_idCloudMap.get(id);
					if (cloud != null) {
						cloud.beacon().mark();
//						long b = cloud.beacon().mark();
//						Util.logger().info("Beacon received for Cloud ID " + id + " at " + b);
					}
					break;
				case REPORT_DELETED:
					// remve reference from location-cloud mapping
					id = (String )msg.getProperty(IRainbowHealthProtocol.ID);
					cloud = m_idCloudMap.remove(id);
					if (cloud != null
							&& cloud.type() == IRainbowHealthProtocol.CloudType.DELEGATE) {
						m_locationDelegateMap.remove(cloud.location());
					}
					log("Received deletion notification from Cloud ID " + id);
					break;
				}
			}
		};
	}

	private void log (String msg) {
		if (m_logger.isInfoEnabled()) m_logger.info("[SysD] " + msg);
	}

}
