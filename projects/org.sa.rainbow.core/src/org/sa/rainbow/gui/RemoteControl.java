/**
 * Created April 3, 2007.
 */
package org.sa.rainbow.gui;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.monitor.SystemDelegate;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.Util;

/**
 * Provides utilities to communicate with delegate processes remotely and
 * issue control commands to them.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class RemoteControl {

	public static final int WAKER_RESTART = 0;
	public static final int WAKER_KILL = 1;

	private static final String AUTH_KEY = "AUTH 6144037128";
	private static int svcPort = Rainbow.RAINBOW_SERVICE_PORT;
	private static RainbowLogger logger = null;

	static {
		String svcPortStr = Rainbow.property(Rainbow.PROPKEY_SERVICE_PORT);
		if (svcPortStr != null) {
			svcPort = Integer.parseInt(svcPortStr);
		}
		logger = RainbowLoggerFactory.logger(RemoteControl.class);
	}

	private static class Waker implements Runnable {
		private String m_hostname;
		private int m_cmd;
		public Waker (String hostname, int cmd) {
			m_hostname = hostname;
			m_cmd = cmd;
		}
		public void run () {
			try {
				Socket socket = new Socket(m_hostname, svcPort);
				BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

				int key = m_cmd;
				out.write(key);
				out.flush();

				socket.shutdownInput();
				socket.shutdownOutput();
				socket.close();
			} catch (UnknownHostException e) {
				logger.error("Waker can't find host!", e);
			} catch (IOException e) {
				logger.error("Waker encountered I/O exception!", e);
			}
		}
	}

	private static class Updater implements Runnable {
		private String m_hostname;
		private File m_dir;
		private String m_filename;
		public Updater (String hostname, File dir, String filename) {
			m_hostname = hostname;
			m_dir = dir;
			m_filename = filename;
		}
		public void run () {
			// see if updating localhost, in which case no socket use required
			if (m_hostname.equalsIgnoreCase("localhost")) {
				File updateFile = new File(m_dir, m_filename);
				// save content to the update directory
				File tgtFile = Util.getRelativeToPath(Rainbow.instance().getBasePath().getParentFile(), Rainbow.RAINBOW_UPDATE_DIR);
				tgtFile = new File(tgtFile, m_filename);
				tgtFile.getParentFile().mkdirs();  // create any parent directories
				if (tgtFile.exists()) {  // delete it first
					if (! tgtFile.delete()) {  // unable to continue
						System.err.print("Existing file " + tgtFile.getName() + " could NOT be deleted, update aborted!");
						return;
					}
				}
				try {
					// now create the file anew
					if (! tgtFile.createNewFile()) {
						System.err.println("Could not create file " + tgtFile.getCanonicalPath() + ", update aborted!");
						return;
					}
					// file creation succeeded, write content
					FileInputStream fin = new FileInputStream(updateFile);
					BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(tgtFile));
					byte[] bytes = new byte[Util.MAX_BYTES];
					while (fin.available() > 0) {
						int cnt = fin.read(bytes, 0, Math.min(fin.available(), Util.MAX_BYTES));
						fout.write(bytes, 0, cnt);
						fout.flush();
					}
					fout.close();
				} catch (IOException e) {
					System.err.println("Error saving update archive! " + e.getMessage());
					return;
				}
				// cause Rainbow component to restart
				Rainbow.signalTerminate(Rainbow.ExitState.RESTART);
				return;
			} else if (m_hostname.equals("*")) {  // update ALL delegates
				if (!Rainbow.inSimulation()) {
					SystemDelegate sys = (SystemDelegate )Oracle.instance().targetSystem();
					for (String hostname : sys.delegateLocations()) {
						if (Rainbow.property(Rainbow.PROPKEY_DEPLOYMENT_LOCATION).equals(hostname)) {
							// don't do update on self
							Oracle.instance().writeOraclePanel(logger, "Software Update: skipping self.");
							continue;
						} else {
							sendViaSocket(hostname);
						}
					}
				}
			} else {  // otherwise, update the single named host
				sendViaSocket(m_hostname);
			}
		}

		/**
		 * Sends update file via socket to the designated hostname.
		 * @param hostname  Name of host to send update file to.
		 */
		private void sendViaSocket (String hostname) {
			StringBuffer msg = new StringBuffer("Software Update: ");
			msg.append("host ").append(hostname).append(", ");
			int cnter = 0;
			try {
				// connect to socket
				Socket socket = new Socket(hostname, svcPort);
				BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line = null;

				// grab and transport update
				File file = new File(m_dir, m_filename);
				if (! file.exists()) {
					System.err.println("Can't find " + m_filename + " in " + m_dir + "!");
					return;
				}
				if (m_dir != null) {  // check for parent being any of the deploy dirs
					String dirName = m_dir.getName();
					if (dirName.equals(Rainbow.RAINBOW_BIN_DIR)
							|| dirName.equals(Rainbow.RAINBOW_JAR_CONTRIB_DIR)
							|| dirName.equals(Rainbow.RAINBOW_CONFIG_PATH)) {
						// better include the dir name
						m_filename = dirName + Util.FILESEP + m_filename;
					}
				}

				// initiate protocol
				String sendStr = AUTH_KEY;
//				logger.info("<= " + sendStr);
				writeln(out, sendStr);
				sendStr = "CMD update";
				logger.info("<= " + sendStr);
				writeln(out, sendStr);
				out.flush();
				line = in.readLine();  // expect "ACP update"
				logger.info("=> " + line);
				// - compute md5sum of file and send data
				long size = file.length();
				FileInputStream fin = new FileInputStream(file);
				sendStr = "DATA " + size + "|" + m_filename + "|" + Util.computeMD5Sum(fin);
				logger.info("<= " + sendStr);
				writeln(out, sendStr);
				out.flush();
				fin.close();
				fin = new FileInputStream(file);
				byte[] bytes = new byte[Util.MAX_BYTES];
				while (fin.available() > 0) {
					int cnt = fin.read(bytes, 0, Math.min(fin.available(), Util.MAX_BYTES));
					out.write(bytes, 0, cnt);
					out.flush();
					cnter += cnt;
				}
				msg.append("wrote ").append(cnter).append(" bytes");
				cnter = 0;
				line = in.readLine();  // expect "ACK update"
				logger.info("=> " + line);
				socket.shutdownInput();
				socket.shutdownOutput();
				socket.close();
			} catch (UnknownHostException e) {
				logger.error("Updater can't find host!", e);
			} catch (IOException e) {
				logger.error("Updater encountered I/O exception!", e);
			} finally {
				if (cnter > 0) {  // error happened, report count
					msg.append("wrote ").append(cnter).append(" bytes");
				}
			}
			Oracle.instance().writeOraclePanel(logger, msg.toString());
		}
	};

	private static class Restarter implements Runnable {
		private String m_hostname;
		private String m_cmd;
		public Restarter (String hostname, String cmd) {
			m_hostname = hostname;
			m_cmd = cmd;
		}
		public void run () {
			try {
				Socket socket = new Socket(m_hostname, svcPort);
				BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line = null;

				// initiate protocol
				String sendStr = AUTH_KEY;
				logger.info("<= " + sendStr);
				writeln(out, sendStr);
				sendStr = "CMD " + m_cmd;
				logger.info("<= " + sendStr);
				writeln(out, sendStr);
				out.flush();
				line = in.readLine();  // expect "ACK sleep/restart"
				logger.info("=> " + line);

				socket.shutdownInput();
				socket.shutdownOutput();
				socket.close();
			} catch (UnknownHostException e) {
				logger.error("Restarter can't find host!", e);
			} catch (IOException e) {
				logger.error("Restarter encountered I/O exception!", e);
			}
		}
	};

	/**
	 * Awakens a sleeping RainbowDelegate's RainDropD thread.
	 * Command value is one of '0' restarts the RainDropD, '1' kills it.
	 * @param m_hostname  name of target host to awaken
	 * @param m_cmd       command value to issue to waker
	 */
	public static void waker (String hostname, int cmd) {
		new Thread(new Waker(hostname, cmd)).start();
	}

	/**
	 * Updates the software on the Rainbow Delegate's host.
	 * @param m_hostname
	 * @param m_cmd
	 */
	public static void updater (String hostname, File dir, String filename) {
		new Thread(new Updater(hostname, dir, filename)).start();
	}

	/**
	 * Restarts (restart), sleeps (sleep), or destructs (stop) the Rainbow component.
	 * @param m_hostname
	 * @param m_cmd
	 */
	public static void restarter (String hostname, String cmd) {
		new Thread(new Restarter(hostname, cmd)).start();
	}

	private static byte[] NL_BYTE = "\n".getBytes();
	private static void writeln (OutputStream out, String text) throws IOException {
		out.write(text.getBytes());
		out.write(NL_BYTE);
	}

}
