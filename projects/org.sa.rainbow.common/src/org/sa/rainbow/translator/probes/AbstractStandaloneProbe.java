/**
 * Created April 11, 2007.
 */
package org.sa.rainbow.translator.probes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.core.RainbowServiceManager;
import org.sa.rainbow.core.ServiceConstants;
import org.sa.rainbow.health.Beacon;

/**
 * This class provides the superclass for a Probe that can be run in a
 * standalone process, or instantiated and run by another process.
 * This probe communicates with Rainbow via the Rainbow Service Port (9210)
 * using the basic Probe-relay lifecycle messages.  The probe also maintains
 * states of the Socket, so that it knows to re-establish socket if connection
 * with the RainbowDelegate has been disconnected.
 * <p>
 * For the Probe lifecycle transitions:
 * "Create" sets up the liveness beacon and establishes initial connection with
 * the RainbowDelegate via the Rainbow service port.
 * "Activate" registers this probe with the RainbowDelegate (reconnecting socket
 * again if it was disconnected), and starts the runnable in the parent class
 * "Deactivate" deregisters this probe with the RainbowDelegate and nullifies
 * the runnable thread in the parent class, which causes the run method to exit;
 * do the deactivate step in the run() cleanup.
 * "Destroy" simply transitions the Probe state, and has no other effects.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class AbstractStandaloneProbe extends AbstractRunnableProbe {

	/** RainbowDelegate host, usually the localhost since every monitored
	 *  machine should have a RainbowDelegate running */
	public static final String RAINBOW_DELEGATE_HOST = "localhost";

	private Socket m_socket = null;
	private BufferedWriter m_sockwriter = null;
	private InputStream m_sockin = null;
	private Beacon m_beacon = null;  // setup liveness Beacon

	/**
	 * Main Constructor.
	 * @param id  the ID to use for the Probe instance
	 * @param type  the type name, or alias, of this Probe
	 * @param sleepTime  the duration to sleep each cycle,
	 *     defaults to {@linkplain IRainbowRunnable.LONG_SLEEP_TIME}
	 */
	public AbstractStandaloneProbe (String id, String type, long sleepTime) {
		super(id, type, Kind.JAVA, IRainbowRunnable.LONG_SLEEP_TIME);

		m_beacon = new Beacon(INTERNAL_BEACON_DURATION);

		// Logger init should not occur in the standalone probe
//		if (m_logger.getClass() == RainbowLogger.NoOpRainbowLogger.class) {
//			// initialize with local dir defaults and re-instantiate m_logger
//			System.setProperty(Rainbow.PROPKEY_TARGET_NAME, "standalone");
//			ARainbowLoggerFactory.reset();
//			m_logger = RainbowLoggerFactory.logger(getClass());
//		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.AbstractRunnableProbe#create()
	 */
	@Override
	public synchronized void create() {
		super.create();
		connectRainbowDelegate();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.AbstractRunnableProbe#activate()
	 */
	@Override
	public synchronized void activate () {
		if (m_socket == null) {  // first need to connect
			connectRainbowDelegate();
		}
		register();  // register Probe with RainbowDelegate
		m_beacon.mark();
		super.activate();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.probes.AbstractRunnableProbe#deactivate()
	 */
	@Override
	public synchronized void deactivate() {
		super.deactivate();
		// de-register Probe and disconnect from Rainbow service port to clean things up
		deregister();
		disconnect();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run () {
		/* Repeatedly execute until probe is deactivated (which means it may
		 * still be reactivated later) or destroyed by unsetting the thread.
		 */
		Thread currentThread = Thread.currentThread();
		while (thread() == currentThread && isActive() && !shouldTerminate()) {
			runAction();
			try {
				Thread.sleep(sleepTime());
			} catch (InterruptedException e) {
				// intentional ignore
			}
		}
		deactivate();
	}

	protected abstract void runAction ();

	/**
	 * Establishes socket connection with local RainbowDelegate to initialize
	 * for Probe reporting.
	 */
	protected void connectRainbowDelegate () {
		try {
			m_socket = new Socket(RAINBOW_DELEGATE_HOST, Rainbow.RAINBOW_SERVICE_PORT);
			m_sockwriter = new BufferedWriter(new OutputStreamWriter(m_socket.getOutputStream()));
			m_sockin = m_socket.getInputStream();

			// initialize the socket connection to report probe events
			writeSocket(RainbowServiceManager.Keywords.CMD.compose(ServiceConstants.SVC_CMD_RELAY));
		} catch (UnknownHostException e) {
			m_logger.error(id() + " can't find host!", e);
			tallyError();
		} catch (IOException e) {
			m_logger.error(id() + " encountered I/O exception!", e);
			tallyError();
		}
	}

	/**
	 * Returns whether this probe should terminate, determined by whether a
	 * Probe kill signal is received from the RainbowDelegate.
	 * @return boolean  <code>true</code> if Probe should terminate, <code>false</code> otherwise
	 */
	protected boolean shouldTerminate () {
		if (m_socket == null) return true;

		boolean terminate = 
			m_socket.isClosed() || !m_socket.isConnected() ||
			m_beacon.isExpired();
		try {
			if (m_sockin.available() > 0) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(m_sockin));
				String line = reader.readLine();
				if (KILL_SIGNAL.startsWith(line)) {
					terminate = true;
					// send kill ack
					writeSocket(KILL_ACK);
				} else if (DEREGISTER_SIGNAL.startsWith(line)) {
					disconnect();  // disconnect to cause run to terminate, and deactivate
				}
			}
		} catch (IOException e) {
			m_logger.error("Probe read from socket failed!", e);
		}
		return terminate;
	}

	/**
	 * Sends communication text over socket to register this Probe with the
	 * RainbowDelegate.
	 */
	protected void register () {
		if (m_socket == null) return;

		StringBuffer regBuf = new StringBuffer();
		regBuf.append(PROBE_REGISTER_PREFIX).append(type()).append(PROBE_CMD_DELIMITER);
		regBuf.append(" ").append(id());
		writeSocket(regBuf.toString());
	}

	/**
	 * Announces a Probe event with the RainbowDelegate.
	 * @param msg  the message to announce
	 */
	protected void announce (String msg) {
		if (m_socket == null) {  // connect, then register
			activate();
			if (m_socket == null) return;
		}

		StringBuffer regBuf = new StringBuffer();
		regBuf.append(PROBE_ANNOUNCE_PREFIX).append(type()).append(PROBE_CMD_DELIMITER);
		regBuf.append(" ").append(msg);
		writeSocket(regBuf.toString());
	}

	/**
	 * Sends communication text over socket to deregister this Probe from the
	 * RainbowDelegate.
	 */
	protected void deregister () {
		if (m_socket == null) return;

		StringBuffer regBuf = new StringBuffer();
		regBuf.append(PROBE_DEREGISTER_PREFIX).append(type()).append(PROBE_CMD_DELIMITER);
		writeSocket(regBuf.toString());
	}

	/**
	 * Disconnects the socket of this Probe.
	 */
	protected void disconnect () {
		if (m_socket == null) return;

		try {
			if (m_sockwriter != null) m_sockwriter.close();
		} catch (IOException e) {  // ignore
		}
		try {
			if (m_sockin != null) m_sockin.close();
		} catch (IOException e) {  // ignore
		}
		try {
			m_socket.shutdownInput();
			m_socket.shutdownOutput();
			m_socket.close();
		} catch (IOException e) {
			m_logger.error("Disconnect failed!", e);
		}

		// nullify the socket to ensure re-connect if announce occurs
		m_socket = null;
		m_sockin = null;
		m_sockwriter = null;
	}

	private void writeSocket (String txt) {
		if (m_sockwriter == null || (m_socket != null && m_socket.isClosed())) return;

		try {
			m_sockwriter.write(txt);
			m_sockwriter.newLine();
			m_sockwriter.flush();
			m_beacon.mark();
		} catch (IOException e) {
			m_logger.error("Probe write failed!", e);
			try {
				m_socket.close();
			} catch (IOException e1) {  // ignore
			}
		}
	}

}
