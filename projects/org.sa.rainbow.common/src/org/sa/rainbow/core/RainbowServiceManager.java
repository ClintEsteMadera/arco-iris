/**
 * Created March 21, 2007.
 */
package org.sa.rainbow.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.sa.rainbow.core.error.RainbowAbortException;
import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.service.IServiceRegistrant;
import org.sa.rainbow.util.Util;

/**
 * This class handles socket communication on the Rainbow service port 9210.
 * It delegates sockets initiated with commands that have been spoken for by
 * a IServiceRegistrant to the corresponding IServiceRegistrant.
 * For example, it delegates probe socket connections to the SocketRelayStrategy
 * instance.
 * Any other connections must be authenticated first with a key (TO BE refined).
 * For security reasons, socket connections to RainbowDelegates that are not
 * dispatched to the un-authd IServiceRegistrant list (e.g., not a probe socket)
 * are "kosher" iff they originate from either the localhost or the Rainbow
 * master host.  All others are rejected.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class RainbowServiceManager extends AbstractRainbowRunnable {

	public enum Keywords {
		AUTH, CMD, ACK, ACP, DATA, ERR;

		/**
		 * Takes the given string and chop out the keyword + space.
		 * @param str  string to remove keyword from
		 * @return String  the resulting string after removing the keyword and a space
		 */
		public String accept (String str) {
			if (str.startsWith(name())) {  // accept-able
				if (str.length() > name().length()) {
					return str.substring(name().length() + 1);
				} else {  // keyword only, so return empty string
					return "";
				}
			} else {   // not good
				return null;
			}
		}

		/**
		 * Given the input to send, compose a string joining the keyword and
		 * the input.
		 * @param input  the input text to compose
		 * @return String  a composed string beginning with the keyword
		 */
		public String compose (String input) {
			return name() + " " + input;
		}
	}

	/** Thread name for this RainbowServiceManager. */
	public static final String NAME = "Rainbow Service Port";
	/** The "secret" authentication key.  TODO: should work out a more secure socket authentication mechanism later. */
	public static final String AUTH_KEY = "6144037128";
	/** The time-out period to allow a socket to connect and authenticate itself. */
	public static final long CONNECT_TIMEOUT = 10000;  // 10 seconds
	/** The size of the queue of socket connections allowed. */
	public static final int DEFAULT_SOCKET_QUEUE_SIZE = 10;  // 10 sockets in parallel
	/** The value of the error state to return on the socket on failure to comply with socket protocol. */
	public static final int ERR_BAD_STATE = 4;

	private static final String SERVICE_LIST_CLASS = "org.sa.rainbow.service.ServiceRegistrantList";
	private static final String SERVICE_LIST_METHOD = "list";

	private boolean m_closeSocket = false;
	private ServerSocket m_socketServer = null;
	private String[] m_svcRegList = null;
	private Queue<Socket> m_socketDispatchPool = null;
	private Map<String,IServiceRegistrant> m_registrantMap = null;
	private Map<String,IServiceRegistrant> m_authdRegistrantMap = null;

	private class SocketDispatchRunnable implements Runnable {
		private Socket m_socket = null;
		private Beacon m_timer = new Beacon(CONNECT_TIMEOUT);
		public SocketDispatchRunnable (Socket socket) {
			this.m_socket = socket;
		}
		public void run () {
			log("Socket from " + m_socket.getInetAddress() + ":" + m_socket.getPort()
					+ " established on port " + m_socket.getLocalPort());
			m_timer.mark();  // start timer
			// check socket input
			try {
				InputStream sockInRaw = m_socket.getInputStream();
				while (sockInRaw.available() == 0) {
					if (m_timer.periodElapsed()) {  // wait no more, reject
						randomPauseReject(m_socket);
						return;  // end this dispatch thread
					} else {
						try {
							Thread.sleep(IRainbowRunnable.LONG_SLEEP_TIME);
						} catch (InterruptedException e) {
							// intentional ignore
						}
					}
				}
				m_timer.mark();  // reset timer
				// act based on the first line
				StringBuffer readBuf = new StringBuffer();
				String line = null;
				while (line == null) {
					line = Util.readLineFromStream(sockInRaw, readBuf);
					if (m_timer.periodElapsed()) {
						randomPauseReject(m_socket);
						return;
					} else {
						try {
							Thread.sleep(IRainbowRunnable.SLEEP_TIME);
						} catch (InterruptedException e) {
							// intentional ignore
						}
					}
				}  // at this point, line is read, and we're not timed out
				if (line.startsWith(Keywords.CMD.name())) {
					// check if command spoken for in registrant map
					String cmd = Keywords.CMD.accept(line);
					IServiceRegistrant reg = m_registrantMap.get(cmd);
					if (reg != null) {  // yes!
						reg.connected(cmd, m_socket, readBuf);
						// remove socket from dispatch pool
						synchronized (m_socketDispatchPool) {
							m_socketDispatchPool.remove(m_socket);
						}
						return;
					}
				}  // at this point, command not handled by un-authd registrant
				// for remaining connections, must be authenticated and "kosher"
				if (!Rainbow.isMaster()) {  // check for "kosher" socket attempt
					try {
						String remotehost = m_socket.getInetAddress().getHostAddress();
						String masterhost = InetAddress.getByName(Rainbow.property(Rainbow.PROPKEY_MASTER_LOCATION)).getHostAddress();
						String localhost = InetAddress.getLocalHost().getHostAddress();
						boolean kosher = remotehost.equals(localhost)
							|| remotehost.equals(masterhost);
						if (! kosher) {
							randomPauseReject(m_socket);
							return;
						}
					} catch (UnknownHostException e) {
						m_logger.error("Get host address failed!", e);
					}
				}
				if (line.startsWith(Keywords.AUTH.name())) {
					String key = Keywords.AUTH.accept(line);
					if (key.equals(AUTH_KEY)) {  // good to go!
						// get next line and check auth'd registrant map
						m_timer.mark();  // reset timer
						line = null;
						while (line == null) {
							line = Util.readLineFromStream(sockInRaw, readBuf);
							if (m_timer.periodElapsed()) {
								randomPauseReject(m_socket);
								return;
							} else {
								try {
									Thread.sleep(IRainbowRunnable.SLEEP_TIME);
								} catch (InterruptedException e) {
									// intentional ignore
								}
							}
						}  // at this point, line is read, and we're not timed out
						if (line.startsWith(Keywords.CMD.name())) {
							String cmd = Keywords.CMD.accept(line);
							IServiceRegistrant reg = m_authdRegistrantMap.get(cmd);
							if (reg != null) {  // yes!
								reg.connected(cmd, m_socket, readBuf);
								// remove socket from dispatch pool
								synchronized (m_socketDispatchPool) {
									m_socketDispatchPool.remove(m_socket);
								}
								return;
							}
						}
					}
				}  // at this point, socket is NOT authenticated
			} catch (IOException e) {
				m_logger.error("Socket I/O failed!", e);
			}
			randomPauseReject(m_socket);
		};
	};

	private Runnable m_socketAccepterRunnable = new Runnable() {
		public void run () {
			while (!m_closeSocket && m_socketServer != null) {
				try {
					Socket socket = m_socketServer.accept();
					if (socket != null) {
						handleSocketConnection(socket);
					}
					Thread.sleep(IRainbowRunnable.SLEEP_TIME);
				} catch (IOException e) {
					if (!m_closeSocket) {
						// only express error if Rainbow didn't intend to close socket
						m_logger.error("Socket accept or getInputStream failed! " + e.getMessage());
					}
				} catch (InterruptedException e) {  // intentional ignore
				}
			}
		}

		// access by a single thread, the socket accepter
		private void handleSocketConnection (Socket socket) {
			// check if pool size reached
			if (m_socketDispatchPool.size() >= DEFAULT_SOCKET_QUEUE_SIZE) {
				// pool size reached, wait for timeout period, then kill the earlier socket
				Util.pause(CONNECT_TIMEOUT);
				while (m_socketDispatchPool.size() >= DEFAULT_SOCKET_QUEUE_SIZE) {
					Socket toKill = null;
					synchronized (m_socketDispatchPool) {
						toKill = m_socketDispatchPool.poll();
					}
					threadedRandomPauseReject(toKill);
				}
			} // at this point, pool size below limit
			m_socketDispatchPool.offer(socket);
			SocketDispatchRunnable runnable = new SocketDispatchRunnable(socket);
			String name = NAME + " Socket Dispatcher " +
				socket.getInetAddress() + ":" + socket.getPort();
			new Thread(Rainbow.instance().getThreadGroup(), runnable, name).start();
		}
	};


	/**
	 * Main Constructor.
	 */
	RainbowServiceManager () {
		super(NAME);

		m_socketDispatchPool = new LinkedList<Socket>();
		m_registrantMap = new HashMap<String,IServiceRegistrant>();
		m_authdRegistrantMap = new HashMap<String,IServiceRegistrant>();
		String sPortStr = Rainbow.property(Rainbow.PROPKEY_SERVICE_PORT, String.valueOf(Rainbow.RAINBOW_SERVICE_PORT));
		if (sPortStr != null) {
			int sPort = Integer.parseInt(sPortStr);
			try {
				m_socketServer = new ServerSocket(sPort);
				log("accepting on socket " + sPort);
			} catch (IOException e) {
				m_logger.error("Creating ServerSocket failed!", e);
			}
		}
		if (m_socketServer == null) {  // something bad happened??
			throw new RainbowAbortException("Could not setup socket server for " + NAME + "!");
		}
		// Retrieve list of service registrants
		try {
			Class<?> listClass = Class.forName(SERVICE_LIST_CLASS);
			Method method = listClass.getMethod(SERVICE_LIST_METHOD, new Class[0]);
			m_svcRegList = (String[] )method.invoke(null, new Object[0]);
		} catch (ClassNotFoundException e) {     // ignore
		} catch (SecurityException e) {          // ignore
		} catch (NoSuchMethodException e) {      // ignore 
		} catch (IllegalArgumentException e) {   // ignore
		} catch (IllegalAccessException e) {     // ignore
		} catch (InvocationTargetException e) {  // ignore
		} finally {
			// process error here
			if (m_svcRegList == null) {
				m_logger.error("Search for Rainbow Service Registrant List failed! " + SERVICE_LIST_CLASS);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose () {
		m_closeSocket = true;  // stop socket accepter thread
		if (m_socketServer != null) {
			try {
				m_socketServer.close();
			} catch (IOException e) {  // ignore error
			}
			m_socketServer = null;
		}
		if (m_socketDispatchPool.size() > 0) {
			synchronized (m_socketDispatchPool) {
				for (Socket toKill : m_socketDispatchPool) {
					threadedRandomPauseReject(toKill);
				}
				m_socketDispatchPool.clear();
			}
		}
		m_registrantMap.clear();
		m_authdRegistrantMap.clear();

		// null-out data members
		m_socketAccepterRunnable = null;
		m_svcRegList = null;
		m_registrantMap = null;
		m_authdRegistrantMap = null;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#start()
	 */
	@Override
	public void start () {
		super.start();
		new Thread(Rainbow.instance().getThreadGroup(), m_socketAccepterRunnable,
				NAME + " Socket Accepter").start();
		for (String clazz : m_svcRegList) {
			try {
				Class<?> svcClass = Class.forName(clazz);
				IRainbowRunnable svc = (IRainbowRunnable )svcClass.newInstance();
				svc.start();
			} catch (ClassNotFoundException e) {  // ignore
			} catch (InstantiationException e) {  // ignore
			} catch (IllegalAccessException e) {  // ignore
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#log(java.lang.String)
	 */
	@Override
	protected void log (String txt) {
		m_logger.info("[RbSP] " + txt);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#runAction()
	 */
	@Override
	protected void runAction () {
		
	}

	/*package*/ void registerCommand (String cmd, IServiceRegistrant reg, boolean authRequired) {

		if (authRequired) {
			m_authdRegistrantMap.put(cmd, reg);
		} else {
			m_registrantMap.put(cmd, reg);
		}
	}

	private void randomPauseReject (final Socket socket) {
		if (m_logger != null) log("Rejecting socket " + socket.getInetAddress() + ":" + socket.getPort());
		// reject socket silently, pausing briefly
		Util.pause((long )(1000*Math.random()+1000));
		try {
			socket.shutdownInput();
			socket.shutdownOutput();
			socket.close();
			// remove socket from dispatch pool
			synchronized (m_socketDispatchPool) {
				m_socketDispatchPool.remove(socket);
			}
		} catch (IOException e) {
			// ignore error
		}
	}

	private void threadedRandomPauseReject (final Socket socket) {
		new Thread(Rainbow.instance().getThreadGroup(), new Runnable() {
			public void run () {
				randomPauseReject(socket);
			}
		}, NAME + " Socket Rejector").start();
	}

}
