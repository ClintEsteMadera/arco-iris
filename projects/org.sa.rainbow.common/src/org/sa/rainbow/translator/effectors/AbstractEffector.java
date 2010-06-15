/**
 * Created November 4, 2006.
 */
package org.sa.rainbow.translator.effectors;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.health.RainbowCloudEventHandler;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

/**
 * Abstract definition of the effector with common methods to simplify
 * effector implementations.  Note that this is NOT a runnable since a
 * RainbowDelegate thread or a Strategy Executor thread is expected to execute
 * the effector.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class AbstractEffector implements IEffector {

	protected RainbowLogger m_logger = null;
	private String m_id = null;
	private String m_name = null;
	private Kind m_kind = null;

	/**
	 * Main Constructor that subclass should call.
	 * @param refID  the location-unique reference identifier to match this IEffector
	 * @param name   the name used to label this IEffector
	 * @param kind   the implementation type of this IEffector
	 */
	public AbstractEffector (String refID, String name, Kind kind) {
		m_logger = RainbowLoggerFactory.logger(getClass());
		m_id = refID;
		m_name = name;
		m_kind = kind;
		log("+ " + refID);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.effectors.IEffector#id()
	 */
	public String id () {
		return m_id;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.effectors.IEffector#service()
	 */
	public String service () {
		return m_name;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.effectors.IEffector#type()
	 */
	public Kind kind() {
		return m_kind;
	}

	protected void log (String txt) {
		String msg = "E[" + service() + "] " + txt;
		RainbowCloudEventHandler.sendTranslatorLog(msg);
		// avoid duplicate output in the master's process
		if (! Rainbow.isMaster()) m_logger.info(msg);
	}

}
