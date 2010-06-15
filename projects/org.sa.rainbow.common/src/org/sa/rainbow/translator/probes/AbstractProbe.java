/**
 * Created November 22, 2006.
 */
package org.sa.rainbow.translator.probes;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.core.error.BadLifecycleStepException;
import org.sa.rainbow.health.RainbowCloudEventHandler;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

/**
 * The AbstractProbe provides the probe State member and the transition of that
 * state when a lifecycle method is executed.  It also provides the identifier
 * information as well as the data queue.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class AbstractProbe implements IProbe {

	protected RainbowLogger m_logger = null;
	protected List<String> m_queue = null;
	protected Map<String,Object> m_configParams = null;

	private String m_name = null;
	private String m_location = null;
	private String m_type = null;
	private Kind m_kind = null;
	private State m_state = State.NULL;

	/**
	 * Main Constuctor that initializes the ID of this Probe.
	 * @param id    the unique identifier of the Probe
	 * @param type  the type name of the Probe
	 * @param kind  one of the enumerated {@link IProbe.Kind}s designating how
	 *     this Probe would be handled by the ProbeBusRelay
	 */
	public AbstractProbe (String id, String type, Kind kind) {
		m_logger = RainbowLoggerFactory.logger(getClass());
		m_kind = kind;
		m_queue = Collections.synchronizedList(new LinkedList<String>());
		m_configParams = Collections.synchronizedMap(new HashMap<String,Object>());
		setID(id);
		setType(type);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#id()
	 */
	public String id () {
		return m_name + LOCATION_SEP + m_location;
	}

	/**
	 * Sets the ID of this Probe only if ID was <code>null</code>.
	 * Not accessible to probe users.
	 * @param id
	 */
	protected void setID (String id) {
		if (m_name == null) {
			// find the "@" index
			int atIdx = id.indexOf(LOCATION_SEP);
			if (atIdx == -1) {  // no @ symbol, use NULL_LOCATION
				m_name = id;
				m_location = NULL_LOCATION;
			} else {
				m_name = id.substring(0, atIdx);
				m_location = id.substring(atIdx+1);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#name()
	 */
	public String name () {
		return m_name;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#location()
	 */
	public String location () {
		return m_location;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbe#type()
	 */
	public String type () {
		return m_type;
	}

	protected void setType (String type) {
		m_type = type;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbe#kind()
	 */
	public Kind kind() {
		return m_kind;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#create()
	 */
	public synchronized void create () {
		if (m_state != State.NULL)
			throw new BadLifecycleStepException("Cannot " + Lifecycle.CREATE + " when Probe State is " + m_state);

		m_state = State.INACTIVE;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#activate()
	 */
	public synchronized void activate () {
		if (m_state != State.INACTIVE)
			throw new BadLifecycleStepException("Cannot " + Lifecycle.ACTIVATE + " when Probe State is " + m_state);

		m_state = State.ACTIVE;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#deactivate()
	 */
	public synchronized void deactivate () {
		if (m_state != State.ACTIVE)
			throw new BadLifecycleStepException("Cannot " + Lifecycle.DEACTIVATE + " when Probe State is " + m_state);

		m_state = State.INACTIVE;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#destroy()
	 */
	public synchronized void destroy () {
		if (m_state != State.INACTIVE)
			throw new BadLifecycleStepException("Cannot " + Lifecycle.DESTROY + " when Probe State is " + m_state);

		m_configParams.clear();
		m_configParams = null;
		m_queue.clear();
		m_queue = null;
		m_state = State.NULL;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#lcTransition(org.sa.rainbow.translator.probes.relay.IProbe.Lifecycle)
	 */
	public void lcTransition (Lifecycle lc) {
		if (lc == Lifecycle.CREATE) {
			create();
		} else if (lc == Lifecycle.ACTIVATE) {
			activate();
		} else if (lc == Lifecycle.DEACTIVATE) {
			deactivate();
		} else if (lc == Lifecycle.DESTROY) {
			destroy();
		} else {  // don't know what this is...
			throw new BadLifecycleStepException("Probe lifecycle command '" + lc + "' unrecognized!");
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#lcState()
	 */
	public State lcState () {
		return m_state;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.relay.IProbe#isActive()
	 */
	public boolean isActive () {
		return m_state == State.ACTIVE;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbe#isAlive()
	 */
	public boolean isAlive () {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbe#configure(java.util.Map)
	 */
	public void configure (Map<String,Object> configParams) {
		m_configParams.putAll(configParams);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbe#reportData(java.lang.String)
	 */
	public void reportData (String data) {
		if (m_queue != null) m_queue.add(data);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbe#receiveData()
	 */
	public String receiveData () {
		if (m_queue == null) return null;
		return m_queue.remove(0);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.IProbe#queueEmpty()
	 */
	public boolean dataPending () {
		if (m_queue == null) return false;
		return !m_queue.isEmpty();
	}

	protected void log (String txt) {
		String msg = "P[" + name() + "] " + txt;
		RainbowCloudEventHandler.sendTranslatorLog(msg);
		// avoid duplicate output in the master's process
		if (! Rainbow.isMaster()) m_logger.info(msg);
	}

}
