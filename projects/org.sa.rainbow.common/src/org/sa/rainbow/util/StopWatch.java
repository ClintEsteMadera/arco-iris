/**
 * Created April 30, 2009.
 */
package org.sa.rainbow.util;

/**
 * A Timer to mark beginning time and end time, and to read elapsed time.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class StopWatch {

	private boolean m_cycledStartStop = false;
	private long m_startTime = 0L;
	private long m_stopTime = 0L;

	public void start () {
		m_startTime = System.currentTimeMillis();
		m_stopTime = 0L;
		m_cycledStartStop = false;
	}

	public long getStartTime () {
		return m_startTime;
	}

	public void stop () {
		m_stopTime = System.currentTimeMillis();
		m_cycledStartStop = true;
	}

	public long getStopTime () {
		return m_startTime;
	}

	public boolean hasCycled () {
		return m_cycledStartStop;
	}

	public long elapsed () {
		if (m_stopTime > 0L && m_startTime > 0L) {
			return m_stopTime - m_startTime;
		} else {
			if (m_startTime > 0L) {
				return System.currentTimeMillis() - m_startTime;
			} else {
				return 0L;
			}
		}
	}

	public void clear () {
		m_startTime = 0L;
		m_stopTime = 0L;
		m_cycledStartStop = false;
	}

}
