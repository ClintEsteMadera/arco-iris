/**
 * Created January 31, 2007.
 */
package org.sa.rainbow.translator.effectors;

import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.util.Util;

/**
 * This class acts as a reference to the actual, but remote, IEffector object.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class EffectorRef implements IEffector {

	private String m_refID = null;
	private String m_location = null;
	private String m_svcName = null;
	private Kind m_type = null;
	private Beacon m_timeout = null;
	private Outcome m_outcome = null;

	/**
	 * Default Constructor
	 */
	public EffectorRef (String refID, String location, String svcName, Kind kind) {
		m_refID = refID;
		m_location = location;
		m_svcName = svcName;
		m_type = kind;
		m_timeout = new Beacon(IRainbowRunnable.LONG_SLEEP_TIME);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.effectors.IEffector#id()
	 */
	public String id () {
		return m_refID;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.effectors.IEffector#service()
	 */
	public String service () {
		return m_svcName;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.effectors.IEffector#type()
	 */
	public Kind kind () {
		return m_type;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.effectors.IEffector#execute(java.lang.String[])
	 */
	public Outcome execute (String[] args) {
		// NOTE: This method should be entered by the Executor thread
		m_outcome = Outcome.UNKNOWN;
		boolean rv = EffectorEventHandler.executeEffector(m_svcName, m_location, args);
		if (rv) {
			// IEffector execution blocks for a period of time, then times-out
			m_timeout.mark();
			while (m_outcome == Outcome.UNKNOWN && !m_timeout.isExpired()) {
				Util.pause(IRainbowRunnable.SLEEP_TIME);
			}
			if (m_timeout.isExpired()) {
				m_outcome = Outcome.TIMEOUT;
			}
		} else {
			m_outcome = Outcome.CONFOUNDED;
		}
		return m_outcome;
	}

	/**
	 * Sets the execution outcome, used by the SystemDelegate object to inform
	 * this IEffector reference of the completion of its execution.
	 * @param outcome  the execution {@link IEffector.Outcome Outcome}
	 */
	public void setOutcome (Outcome outcome) {
		// NOTE: This method should be entered by the RainbowEvent thread
		m_timeout.mark();  // to make sure we don't time out
		m_outcome = outcome;
	}
	public Outcome outcome () {
		return m_outcome;
	}

}
