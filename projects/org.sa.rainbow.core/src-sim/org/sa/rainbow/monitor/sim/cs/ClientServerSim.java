/**
 * Created April 23, 2006
 */
package org.sa.rainbow.monitor.sim.cs;

import java.util.Properties;

import org.sa.rainbow.monitor.sim.AbstractSim;
import org.sa.rainbow.monitor.sim.ISimulatedTargetSystem;
import org.sa.rainbow.monitor.sim.QueuingTheoryUtil;

/**
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 *
 * Simulation of a Client-Server system with j clients (j=6) and 2 server pools
 * where each server pool p_i has a replication cound k_i.  The simulation is
 * done by iteratively evaluating a queuing network model...
 */
public class ClientServerSim extends AbstractSim {

	private int m_clientCnt = 0;
	private int m_srvGrpCnt = 0;
	private int m_reqSize = 0;
	private int m_respSize = 0;
	private double m_alpha = 0.0;

	/**
	 * Default Constructor
	 */
	public ClientServerSim (ISimulatedTargetSystem runner, Properties props) {
		super("Client-Server Simulation System", runner, props);

		m_clientCnt = intProp("client.count");
		m_srvGrpCnt = intProp("sg.count");
		m_reqSize = intProp("perf.request.size");
		m_respSize = intProp("perf.response.size");
		m_alpha = doubleProp("alpha");

		m_thread.start();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.sim.AbstractSim#runAction()
	 */
	@Override
	protected void runAction () {
		// perform one sim step
		for (int i=1; i <= m_srvGrpCnt; i++) {  // iterate srv groups
			String elemName = "sg" + i;
			// compute total arrival rate
			int arrRate = sumForServerGroup(elemName, "perf.arrival.rate");
			m_props.setProperty(elemName + ".perf.arrival.rate", String.valueOf(arrRate));
			// compute server utilization
			int svcTime = intProp(elemName + ".perf.service.time");
			int repl = intProp(elemName + ".replication");
			double util = (arrRate * svcTime / 1000.0) / repl;
			m_runner.changeProperty(elemName + ".load", String.valueOf(util));
			// compute server response time
			double respTime = 1000.0 * QueuingTheoryUtil.responseTime(repl, util, arrRate, svcTime/1000.0);
			m_props.setProperty(elemName + ".perf.response.time", String.valueOf(respTime));

//			Debug.traceln(elemName + " repl, load, response time == " + repl + ", " + util + ", " + respTime);
		}
		for (int i=1; i <= m_clientCnt; i++) {  // iterate the clients
			String elemName = "c" + i;
			// compute connection delay
			int bw = intProp(elemName + ".connection.bandwidth");
			int connDelay = (int )(1000.0*(m_reqSize+m_respSize)) / (bw*2);
			m_props.setProperty(elemName + ".connection.delay", String.valueOf(connDelay));
			// compute current instantaneous latency
			String tgtSGrp = m_props.getProperty(elemName + ".connection.target");
			double sRespTime = doubleProp(tgtSGrp + ".perf.response.time");
			double latency = connDelay * 2 + sRespTime;
			m_props.setProperty(elemName + ".perf.latency", String.valueOf(latency));
			// compute average latency
			double avgLat = doubleProp(elemName + ".latency");
			avgLat = m_alpha * latency + (1-m_alpha) * avgLat;
			m_runner.changeProperty(elemName + ".latency", String.valueOf(avgLat));

//			Debug.traceln(elemName + " target, latency, avg == " + tgtSGrp + ", " + latency + ", " + avgLat);
		}
	}

	private int sumForServerGroup (String grp, String prop) {
		int rv = 0;
		for (int i=1; i <= m_clientCnt; i++) {
			String elemName = "c" + i;
			if (m_props.getProperty(elemName + ".connection.target").equals(grp)) {
				rv += intProp(elemName + "." + prop);
			}
		}
		return rv;
	}

}
