package ar.uba.dc.thesis.znn.probes;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.translator.probes.AbstractRunnableProbe;
import org.sa.rainbow.util.Util;

/**
 * A probe that acts like an end-user client connecting to the newssite, to help determine client's experienced response
 * time, taking into account the corresponding simulated stimulus.
 */
public class ClientProxyProbeWithStimulus extends AbstractRunnableProbe {

	public static final String PROBE_TYPE = "clientproxywithstimulus";

	private final String[] m_tgtUrls;

	private final String[] m_stimulusNames;

	/**
	 * Constructor to supply with array of target URLs.
	 * 
	 * @param id
	 *            the unique name@location identifier of the IProbe
	 * @param sleepTime
	 *            milliseconds to sleep per cycle
	 * @param args
	 *            String array of target URLs against which to check time
	 */
	public ClientProxyProbeWithStimulus(String id, long sleepTime, String[] args) {
		super(id, PROBE_TYPE, Kind.JAVA, sleepTime);
		if ((args.length % 2) != 0) {
			throw new IllegalArgumentException("Provide the stimulus name for each url");
		}
		int urlsCounter = args.length / 2;
		m_tgtUrls = new String[urlsCounter];
		m_stimulusNames = new String[urlsCounter];
		for (int i = 0; i < urlsCounter; i++) {
			m_tgtUrls[i] = args[i * 2];
			m_stimulusNames[i] = args[i * 2 + 1];
		}
	}

	public void run() {
		byte[] bytes = new byte[Util.MAX_BYTES];

		/*
		 * Repeatedly execute until probe is deactivated (which means it may still be reactivated later) or destroyed by
		 * unsetting the thread.
		 */
		Beacon timer = new Beacon(IRainbowRunnable.LONG_SLEEP_TIME); // use this to determine to report
		Thread currentThread = Thread.currentThread();
		while (thread() == currentThread && isActive()) {
			for (int i = 0; i < m_tgtUrls.length; i++) {
				String urlStr = m_tgtUrls[i];
				String stimulusName = m_stimulusNames[i];
				timer.mark();
				try {
					URL url = new URL(urlStr);
					HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int cnter = 0;
					long startTime = System.currentTimeMillis();
					int length = httpConn.getContentLength();
					BufferedInputStream in = new BufferedInputStream(httpConn.getInputStream());
					while (in.available() > 0 || cnter < length) {
						int cnt = in.read(bytes);
						baos.write(bytes, 0, cnt);
						cnter += cnt;
						if (cnter < length) {
							if (timer.periodElapsed()) { // make an intermediate report
								// ESTO NO LO LEE EL GAUGE, VER SI VALE LA PENA DEJARLO
								// String rpt = "[" + Util.probeLogTimestamp() + "] " + url.getHost() + ":"
								// + (System.currentTimeMillis() - startTime) + "ms";
								// reportData(rpt);
								// Util.dataLogger().info(rpt);
								// timer.mark(); // reset timer
							}
							try { // sleep just a little as to not saturate CPU
								Thread.sleep(IRainbowRunnable.SHORT_SLEEP_TIME);
							} catch (InterruptedException e) {
								// intentional ignore
							}
						}
					}
					long endTime = System.currentTimeMillis();
					// TODO evaluar la ubicacion del estimulo en el string a loggear:
					String rpt = "[" + Util.probeLogTimestamp() + "]<" + id() + "> " + url.getHost() + "<stimulus:"
							+ stimulusName + ">:" + (endTime - startTime) + "ms";
					reportData(rpt);
					Util.dataLogger().info(rpt);
					if (m_logger.isTraceEnabled()) {
						m_logger.trace("Content-type: " + httpConn.getContentType());
						m_logger.trace("Content-length: " + httpConn.getContentLength());
						m_logger.trace(baos.toString());
						m_logger.trace("ClientProxyProbe " + id() + " queues \"" + rpt + "\"");
					}
					httpConn.disconnect();
				} catch (MalformedURLException e) {
					m_logger.error("Bad URL provided in Probe Spec?", e);
					tallyError();
				} catch (IOException e) {
					m_logger.error("HTTP connection error!", e);
					tallyError();
				}
			}
			try {
				Thread.sleep(sleepTime());
			} catch (InterruptedException e) {
				// intentional ignore
			}
		}
	}
}
