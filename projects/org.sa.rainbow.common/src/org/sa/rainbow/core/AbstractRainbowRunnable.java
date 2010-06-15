/**
 * Created April 8, 2006
 */
package org.sa.rainbow.core;

import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;


/**
 * Convenience abstract class that handles the usual thread start/stop/terminate
 * steps, so subclass need not worry about those unless specific actions
 * at those junctions are needed
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class AbstractRainbowRunnable implements IRainbowRunnable {

	protected RainbowLogger m_logger = null;

	private Thread m_thread = null;
	private String m_name = null;
	private long m_sleepTime = SLEEP_TIME;
	private State m_threadState = State.RAW;
	private State m_nextState = State.RAW;
	private boolean m_restarting = false;

	/**
	 * Default Constructor with name for the thread.
	 * @param name  Name of the Thread
	 */
	public AbstractRainbowRunnable (String name) {
		m_logger = RainbowLoggerFactory.logger(getClass());
		m_name = name;
		m_thread = new Thread(Rainbow.instance().getThreadGroup(), this, m_name);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#isDisposed()
	 */
	public boolean isDisposed() {
		return isTerminated();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IRainbowRunnable#name()
	 */
	public String name() {
		return m_name;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IRainbowRunnable#start()
	 */
	public void start () {
		if (m_thread == null || m_threadState == State.TERMINATED) return;

		switch (m_threadState) {
		case RAW:  // note yet started
			m_threadState = State.STARTED;
			m_thread.start();
			log(m_name + " started.");
			break;
		case STOPPED:
			m_threadState = State.STARTED;
			log(m_name + " started.");
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IRainbowRunnable#stop()
	 */
	public void stop() {
		transition(State.STOPPED);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IRainbowRunnable#restart()
	 */
	public void restart() {
		m_restarting = true;
		transition(State.STOPPED);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IRainbowRunnable#terminate()
	 */
	public void terminate() {
		transition(State.TERMINATED);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IRainbowRunnable#state()
	 */
	public State state () {
		return m_threadState;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IRainbowRunnable#isTerminated()
	 */
	public boolean isTerminated() {
		return m_threadState == State.TERMINATED;
	}

	/**
	 * Pauses the execution of the runnable, preserving any states until it is
	 * resumed again by call to start.
	 */
	protected void doStop () {
		if (m_thread == null || m_threadState == State.RAW || m_threadState == State.TERMINATED) return;

		m_threadState = State.STOPPED;
		log(m_name + " stopped.");
	}

	/**
	 * Stops the runnable thread and causes exit from fun.
	 */
	protected void doTerminate () {
		dispose();
		log(m_name + " terminated.");
		finalTerminate();
	}

	/**
	 * This method is only to be invoked if overriding the doTerminate method().
	 */
	protected void finalTerminate () {
		m_threadState = State.TERMINATED;
		m_logger = null;
		m_name = null;
		m_thread = null;
	}

	protected long sleepTime () {
		return m_sleepTime;
	}
	protected void setSleepTime (long time) {
		if (time < SLEEP_TIME) {
			time = SLEEP_TIME;
		}
		m_sleepTime = time;
	}

	/**
	 * Returns whether this runnable should terminate.  If <code>true</code>,
	 * causes the doTerminate to be called.
	 * <p>
	 * The default is query Rainbow to determine if we should terminate.
	 * Override to define special conditions.
	 * @return boolean  <code>true</code> if runnable should terminate, <code>false</code> otherwise
	 */
	protected boolean shouldTerminate () {
		return Rainbow.shouldTerminate();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run () {
		Thread currentThread = Thread.currentThread();
		while (m_thread == currentThread) {
			try {
				Thread.sleep(m_sleepTime);
			} catch (InterruptedException e) {
				// intentional ignore
			}
			if (m_threadState == State.STARTED) {  // only process if started
				if (shouldTerminate()) {
					// time to stop RainbowRunnable as well
					doTerminate();
				} else {
					try {
						runAction();
					} catch (Throwable t) {
						// make sure Rainbow can dispose despite error... by terminating
						m_logger.fatal("Runtime error, terminating runnable " + m_name + "!", t);
						terminate();
						// cause a Rainbow restart if we're the event service, or slave
						if (this instanceof IEventService
								|| !Rainbow.isMaster()) {
							Rainbow.signalTerminate(Rainbow.ExitState.RESTART);
						}
					}
				}
			}
			// check whether to advance state, not if terminated
			if (m_threadState != m_nextState && m_threadState != State.TERMINATED) {
				switch (m_nextState) {
				case STARTED:
					start();
					break;
				case STOPPED:
					doStop();
					break;
				case TERMINATED:
					doTerminate();
					break;
				}
				if (m_restarting && m_nextState == State.STOPPED) {
					m_nextState = State.STARTED;  // cause restart next
					m_restarting = false;
				} else {
					m_nextState = m_threadState;
				}
			}
		}
	}

	protected Thread activeThread () {
		return m_thread;
	}

	protected abstract void log (String txt);
	protected abstract void runAction (); 

	/**
	 * Sets the next State to transition this Runnable to; with appropriate guard.
	 * @param state  new runnable state
	 */
	private void transition(State nextState) {
		// rule out prohibited transitions, basically TERMINATED->* and *->RAW
		if (m_threadState == State.TERMINATED || nextState == State.RAW) return;
		if (m_restarting && m_threadState != State.STOPPED) {
			m_restarting = false;
			return;
		}

		m_nextState = nextState;
	}

}
