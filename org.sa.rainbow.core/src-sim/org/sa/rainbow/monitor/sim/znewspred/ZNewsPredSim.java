/**
 * Created September 22, 2008
 */
package org.sa.rainbow.monitor.sim.znewspred;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.sa.rainbow.model.Model;
import org.sa.rainbow.monitor.sim.AbstractSim;
import org.sa.rainbow.monitor.sim.ISimulatedTargetSystem;
import org.sa.rainbow.monitor.sim.QueuingTheoryUtil;
import org.sa.rainbow.monitor.sim.SimulationRunner.SimPoint;

/**
 * Simulation of a Znn.com-variant based on queueing network, but with
 * predictive ability added.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class ZNewsPredSim extends AbstractSim {

	private String m_sysName = null;
	private int m_clientCnt = 0;
	private int m_serverCnt = 0;
	private int m_reqSize = 0;
	private int m_respSize = 0;
	private double m_alpha = 0.0;
	/** Stores predictive properties at integral time windows from start of sim */
	private Map<Long,Properties> m_timedProps = null;

	/**
	 * Default Constructor
	 */
	public ZNewsPredSim (ISimulatedTargetSystem runner, Properties props) {
		super("ZNews Simulation System", runner, props);

		m_sysName = "ZNewsSys";
		m_clientCnt = intProp("client.count");
		m_serverCnt = intProp("server.count");
		m_reqSize = intProp("perf.request.size");
		m_respSize = intProp("perf.response.size");
		m_alpha = doubleProp("alpha");
		m_timedProps = new HashMap<Long,Properties>();

		m_thread.start();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.sim.AbstractSim#runAction()
	 */
	@Override
	protected void runAction () {
		// perform one sim step
		Properties propsToUpdate = doOneSimStep(m_props);
		// invoke change properties on the props to change
		for (Map.Entry<Object,Object> e : propsToUpdate.entrySet()) {
			m_runner.changeProperty(e.getKey().toString(), e.getValue());
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.sim.ISimulation#predict(java.util.List, long, org.sa.rainbow.monitor.sim.ISimulation.PredictionMode)
	 */
	@Override
	public Properties predict (List<SimPoint> simpts, long delta, PredictionMode mode) {
		if (mode == PredictionMode.PERFECT) {
			return perfectlyPredict(simpts, delta);
		} else {
			return imperfectlyPredict(simpts, delta);
		}
	}

	private Properties perfectlyPredict (List<SimPoint> simpts, long delta) {
		// find least nearest time window
		long elapsed = m_runner.elapsedTime();
		boolean inCurTimeWin = delta < SIM_TIME_WINDOW;
		long timewin = ((elapsed + delta) / SIM_TIME_WINDOW) * SIM_TIME_WINDOW;
		// fetch, or create new, properties set
		Properties props = m_timedProps.get(timewin);
		if (props == null) {
			if (inCurTimeWin) {
				props = new Properties();
				for (Map.Entry<Object,Object> e: m_props.entrySet()) {
					props.put(e.getKey(), e.getValue());
				}
			} else {
				// create new set of properties based on previous sim window
				props = new Properties();
				Properties prevProps = perfectlyPredict(simpts, delta - SIM_TIME_WINDOW);
				for (Map.Entry<Object,Object> e: prevProps.entrySet()) {
					props.put(e.getKey(), e.getValue());
				}
			}
			m_timedProps.put(timewin, props);
		}
		// apply relevant sim points
		for (SimPoint pt : simpts) {
			if (timewin - pt.simTime <= SIM_TIME_WINDOW) {
				props.setProperty(pt.identifier, pt.value);
			}
		}
		// take a step of simulation using present set of properties
		Properties propsToUpdate = doOneSimStep(props);
		for (Map.Entry<Object,Object> e : propsToUpdate.entrySet()) {
			props.setProperty(e.getKey().toString(), e.getValue().toString());
		}
		return props;
	}

	private Properties imperfectlyPredict (List<SimPoint> simpts, long delta) {
		// find least nearest time window
		long elapsed = m_runner.elapsedTime();
		long future = elapsed + delta;
		// use current conditions as basis
		Properties props = new Properties();
		for (Map.Entry<Object,Object> e: m_props.entrySet()) {
			props.put(e.getKey(), e.getValue());
		}
		// apply all relevant sim points
		for (SimPoint pt : simpts) {
			if (pt.simTime <= future) {
				m_logger.info("Predicting... setting " + pt.identifier + " = " + pt.value);
				props.setProperty(pt.identifier, pt.value);
			}
		}
		// take a step of simulation using current properties
		Properties propsToUpdate = doOneSimStep(props);
		for (Map.Entry<Object,Object> e : propsToUpdate.entrySet()) {
			String k = e.getKey().toString();
			if (k.endsWith(".experRespTime")) {
				String stub = k.substring(0, k.indexOf(".experRespTime"));
				// exponential avg in property has no meaning in an arbitrary future time point
				props.setProperty(k, props.getProperty(stub + ".instExperRespTime"));
			} else {
				props.setProperty(k, e.getValue().toString());
			}
			m_logger.info("Updated prop " + k + " = " + props.getProperty(k));
		}
		return props;
	}

	private Properties doOneSimStep (Properties props) {
		Properties toUpdate = new Properties();
		double accumServerRespTme = 0.0;
		List<String> activeList = new ArrayList<String>();
		for (int i=0; i < m_serverCnt; i++) {  // count active servers
			String elemName = m_sysName + ".s" + i;
			// check if arch enabled
			if (booleanProp(props, elemName + "." + Model.PROPKEY_ARCH_ENABLED)) {  // yep!
				activeList.add(elemName);
			}
		}
		int serverFactor = Math.max(activeList.size(),1);
		for (String elemName : activeList) {  // iterate active servers
			// compute arrival rate
			int arrRate = computeArrivalRate(props, elemName, "perf.arrival.rate", serverFactor);
			props.setProperty(elemName + ".perf.arrival.rate", String.valueOf(arrRate));
			// compute server utilization
			int fidelity = intProp(props, elemName + ".fidelity");
			double svcTime = doubleProp(props, elemName + ".perf.service.time") * fidelity;
			int repl = intProp(props, elemName + ".replication");
			double util = (arrRate * svcTime / 1000.0) / repl;
			if (m_logger.isDebugEnabled()) m_logger.debug("** "+ elemName + ".load <== " + util);
			toUpdate.setProperty(elemName + ".load", String.valueOf(util));
			// compute server response time
			double respTime = 1000.0 * QueuingTheoryUtil.responseTime(repl, util, arrRate, svcTime/1000.0);
			props.setProperty(elemName + ".perf.response.time", String.valueOf(respTime));
			if (m_logger.isDebugEnabled()) m_logger.debug("** "+ elemName + ".perf.response.time <== " + respTime);
			if (booleanProp(props, "perf.load.balanced")) {  // accumulate server resp times
				accumServerRespTme += respTime;
			}
		}
		for (int i=0; i < m_clientCnt; i++) {  // iterate the clients
			String connElemName = m_sysName + ".conn" + i;
			String elemName = m_sysName + ".c" + i;
			// compute connection delay, accounting for both directions
			int bwUp = intProp(props, connElemName + ".bandwidth.up");
			int bwDown = intProp(props, connElemName + ".bandwidth.down");
			//int reqps = intProp(elemName + ".perf.arrival.rate");
			double connDelay = 1000.0/*(ms/s)*/ *
					m_reqSize/(bwUp/10.0/*(b/B)*/) + m_respSize/(bwDown/10.0/*(b/B)*/);
			// compute current instantaneous latency
			double sRespTime = doubleProp(props, elemName + ".perf.service.time") + connDelay;
			if (booleanProp(props, "perf.load.balanced")) {  // average all server resp times
				sRespTime += accumServerRespTme / serverFactor;
			} else {
				String tgtServer = props.getProperty(elemName + ".connection.target");
				sRespTime += doubleProp(props, tgtServer + ".perf.response.time");
			}
			if (m_logger.isDebugEnabled()) m_logger.debug("** "+ elemName + ".perf.response.time <== " + sRespTime);
			props.setProperty(elemName + ".instExperRespTime", String.valueOf(sRespTime));
			// compute average experienced response time
			double avgERT = doubleProp(props, elemName + ".experRespTime");
			avgERT = m_alpha * sRespTime + (1-m_alpha) * avgERT;
			toUpdate.setProperty(elemName + ".experRespTime", String.valueOf(avgERT));
		}

		return toUpdate;
	}

	private int computeArrivalRate (Properties props, String srv, String prop, int serverCount) {
		int rv = 0;
		for (int i=0; i < m_clientCnt; i++) {
			String elemName = m_sysName + ".c" + i;
			if (booleanProp(props, "perf.load.balanced")) {
				// load is balanced across servers, assume equally...
				// compute arrival rate of each server as average of total client arrival rates
				rv += intProp(props, elemName + "." + prop) / serverCount;
			} else {
				// load is not balanced across servers, so compute by connection target
				if (props.getProperty(elemName + ".connection.target").equals(srv)) {
					rv += intProp(props, elemName + "." + prop);
				}
			}
		}
		return rv;
	}

}
