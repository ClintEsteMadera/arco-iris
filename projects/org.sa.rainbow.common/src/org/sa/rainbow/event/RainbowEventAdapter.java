/**
 * Created February 7, 2007.
 */
package org.sa.rainbow.event;

/**
 * This class
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class RainbowEventAdapter implements IRainbowEventListener {

	private String m_id = null;

	/**
	 * Default Constructor, creates an ID thru this.toString()
	 */
	public RainbowEventAdapter () {
		m_id = this.toString();
	}

	/**
	 * Main Constructor, sets an ID
	 * @param id  a String identifier
	 */
	public RainbowEventAdapter (String id) {
		if (id == null) {
			m_id = this.toString();
		} else {
			m_id = id;
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.event.IRainbowEventListener#id()
	 */
	public String id () {
		return m_id;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.event.IRainbowEventListener#onMessage(org.sa.rainbow.event.IRainbowMessage)
	 */
	public void onMessage(IRainbowMessage msg) {
	}

}
