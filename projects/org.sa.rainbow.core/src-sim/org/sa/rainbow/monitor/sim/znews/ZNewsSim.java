/**
 * Created April 23, 2006
 */
package org.sa.rainbow.monitor.sim.znews;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.sa.rainbow.model.Model;
import org.sa.rainbow.monitor.sim.AbstractSim;
import org.sa.rainbow.monitor.sim.ISimulatedTargetSystem;
import org.sa.rainbow.monitor.sim.QueuingTheoryUtil;

/**
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 * 
 * Simulation of a Test system with alpha component connected via conn to Beta component.
 */
public class ZNewsSim extends AbstractSim {

	private String m_sysName = null;
	private int m_clientCnt = 0;
	private int m_serverCnt = 0;
	private int m_reqSize = 0;
	private int m_respSize = 0;
	private double m_alpha = 0.0;

	/**
	 * Default Constructor
	 */
	public ZNewsSim(ISimulatedTargetSystem runner, Properties props) {
		super("ZNews Simulation System", runner, props);

		m_sysName = "ZNewsSys";
		m_clientCnt = intProp("client.count");
		m_serverCnt = intProp("server.count");
		m_reqSize = intProp("perf.request.size");
		m_respSize = intProp("perf.response.size");
		m_alpha = doubleProp("alpha");

		m_thread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.monitor.sim.AbstractSim#runAction()
	 */
	@Override
	protected void runAction() {
		// perform one sim step
		double accumServerRespTme = 0.0;
		List<String> activeList = new ArrayList<String>();
		for (int i = 0; i < m_serverCnt; i++) { // count active servers
			String elemName = m_sysName + ".s" + i;
			// check if arch enabled
			if (booleanProp(elemName + "." + Model.PROPKEY_ARCH_ENABLED)) { // yep!
				activeList.add(elemName);
			}
		}
		int serverFactor = Math.max(activeList.size(), 1);
		for (String elemName : activeList) { // iterate active servers
			// compute arrival rate
			int arrRate = computeArrivalRate(elemName, "perf.arrival.rate", serverFactor);
			m_props.setProperty(elemName + ".perf.arrival.rate", String.valueOf(arrRate));
			// compute server utilization
			int fidelity = intProp(elemName + ".fidelity");
			double svcTime = doubleProp(elemName + ".perf.service.time") * fidelity;
			int repl = intProp(elemName + ".replication");
			double util = (arrRate * svcTime / 1000.0) / repl;
			if (m_logger.isDebugEnabled()) {
				m_logger.debug("** " + elemName + ".load <== " + util);
			}
			m_runner.changeProperty(elemName + ".load", String.valueOf(util));
			// compute server response time
			double respTime = 1000.0 * QueuingTheoryUtil.responseTime(repl, util, arrRate, svcTime / 1000.0);
			m_props.setProperty(elemName + ".perf.response.time", String.valueOf(respTime));
			if (m_logger.isDebugEnabled()) {
				m_logger.debug("** " + elemName + ".perf.response.time <== " + respTime);
			}
			if (booleanProp("perf.load.balanced")) { // accumulate server resp times
				accumServerRespTme += respTime;
			}
		}
		double globalAvgERT = 0;
		for (int i = 0; i < m_clientCnt; i++) { // iterate the clients
			String connElemName = m_sysName + ".conn" + i;
			String elemName = m_sysName + ".c" + i;
			// compute connection delay, accounting for both directions
			int bwUp = intProp(connElemName + ".bandwidth.up");
			int bwDown = intProp(connElemName + ".bandwidth.down");
			// int reqps = intProp(elemName + ".perf.arrival.rate");
			double connDelay = 1000.0/* (ms/s) */* m_reqSize / (bwUp / 10.0/* (b/B) */) + m_respSize
					/ (bwDown / 10.0/* (b/B) */);
			// compute current instantaneous latency
			double sRespTime = doubleProp(elemName + ".perf.service.time") + connDelay;
			if (booleanProp("perf.load.balanced")) { // average all server resp times
				sRespTime += accumServerRespTme / serverFactor;
			} else {
				String tgtServer = m_props.getProperty(elemName + ".connection.target");
				sRespTime += doubleProp(tgtServer + ".perf.response.time");
			}
			if (m_logger.isDebugEnabled()) {
				m_logger.debug("** " + elemName + ".perf.response.time <== " + sRespTime);
			}
			// compute average experienced response time
			double avgERT = doubleProp(elemName + ".experRespTime");
			avgERT = m_alpha * sRespTime + (1 - m_alpha) * avgERT;
			globalAvgERT += avgERT;
			m_runner.changeProperty(elemName + ".experRespTime", String.valueOf(avgERT));
		}
	}

	private int computeArrivalRate(String srv, String prop, int serverCount) {
		int rv = 0;
		for (int i = 0; i < m_clientCnt; i++) {
			String elemName = m_sysName + ".c" + i;
			if (booleanProp("perf.load.balanced")) {
				// load is balanced across servers, assume equally...
				// compute arrival rate of each server as average of total client arrival rates
				rv += intProp(elemName + "." + prop) / serverCount;
			} else {
				// load is not balanced across servers, so compute by connection target
				if (m_props.getProperty(elemName + ".connection.target").equals(srv)) {
					rv += intProp(elemName + "." + prop);
				}
			}
		}
		if (rv == 0) {
			throw new RuntimeException("Null Arrival Rate!");
		}
		return rv;
	}
}
