/**
 * Created October 11, 2006.
 */
package org.sa.rainbow.monitor.sim;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.sa.rainbow.core.error.NotImplementedException;
import org.sa.rainbow.monitor.sim.SimulationRunner.SimPoint;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

/**
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 *
 * Abstract parent class for other simulation runners that contain common
 * members and methods to aid simulation.
 */
public abstract class AbstractSim implements ISimulation, Runnable {

	public static final long SLEEP_TIME = ISimulation.SIM_TIME_WINDOW;  // ms

	protected RainbowLogger m_logger = null;
	protected Thread m_thread = null;
	protected ISimulatedTargetSystem m_runner = null;
	protected Properties m_props = null;
	protected Map<String,String> m_initAllProps = null;
	protected Map<String,String> m_initElemProps = null;
	protected int m_simSteps = 0;
	protected int m_simStepIncr = 0;

	/**
	 * Main Constructor
	 */
	public AbstractSim (String name, ISimulatedTargetSystem runner, Properties props) {
		m_logger = RainbowLoggerFactory.logger(getClass());
		m_thread = new Thread(this, name);
		m_runner = runner;
		m_props = props;
		m_initAllProps = new HashMap<String,String>();
		m_initElemProps = new HashMap<String,String>();

		// grab all props whose key begins with any of the named elements
		List<String> elements = new ArrayList<String>();
		StringTokenizer eTokens = new StringTokenizer(m_props.getProperty(KEY_ELEMENTS), ",");
		while (eTokens.hasMoreTokens()) {
			elements.add(eTokens.nextToken().trim());
		}
		for (Object k : m_props.keySet()) {
			String key = (String )k;
			String value = m_props.getProperty(key);
			m_initAllProps.put(key, value);
			// init architecture as well
			m_runner.changeProperty(key, value);

			int idx = key.indexOf(".");
			if (idx > -1 && elements.contains(key.substring(0, idx))) {  // begins with element
				m_initElemProps.put(key, m_props.getProperty(key));
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run () {
		Thread currentThread = Thread.currentThread();
		while (m_thread == currentThread) {
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				// intentionally ignore
			}
			if (m_thread != null && m_simStepIncr > 0) {
				runAction();

				m_simSteps++;
				synchronized (this) {
					m_simStepIncr--;
				}
			}
		}
	}

	abstract protected void runAction ();

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.sim.ISimulation#refresh()
	 */
	public void refresh () {
		for (String key : m_initElemProps.keySet()) {
			m_runner.changeProperty(key, m_initElemProps.get(key));
		}
		m_logger.info("<System states refreshed>");
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.sim.ISimulation#reset()
	 */
	public void reset() {
		for (String key : m_initAllProps.keySet()) {
			m_props.setProperty(key, m_initAllProps.get(key));
			// affect architecture as well
			m_runner.changeProperty(key, m_initAllProps.get(key));
		}
		m_logger.info("<System states RESET>");
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.sim.ISimulation#simulate(int)
	 */
	public synchronized void simulate (int steps) {
		m_simStepIncr = steps;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.sim.ISimulation#terminate()
	 */
	public void terminate () {
		m_thread = null;

		// write out property to another file
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			BufferedWriter awriter = new BufferedWriter(new OutputStreamWriter(baos));
			List<String> l = new LinkedList<String>();
			for (Enumeration<Object> k = m_props.keys(); k.hasMoreElements(); ) {
				l.add((String )k.nextElement());
			}
			Collections.sort(l);
			for (String k : l) {
				awriter.write(k + "=" + m_props.getProperty(k));
				awriter.newLine();
			}
			awriter.flush();
			m_logger.info("--- Outputing simulation properties ---");
			m_logger.info(baos.toString());
			m_logger.info("***************************************");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.monitor.sim.ISimulation#predict(java.util.List, long, org.sa.rainbow.monitor.sim.ISimulation.PredictionMode)
	 */
	public Properties predict(List<SimPoint> simpts, long delta, PredictionMode mode) {
		throw new NotImplementedException("Simulation system does not support prediction!");
	}
	
	protected int intProp (String name) {
		return Integer.parseInt(m_props.getProperty(name));
	}

	protected double doubleProp (String name) {
		return Double.parseDouble(m_props.getProperty(name));
	}

	protected boolean booleanProp (String name) {
		return Boolean.parseBoolean(m_props.getProperty(name));
	}

	protected int intProp (Properties props, String name) {
		return Integer.parseInt(props.getProperty(name));
	}

	protected double doubleProp (Properties props, String name) {
		return Double.parseDouble(props.getProperty(name));
	}

	protected boolean booleanProp (Properties props, String name) {
		return Boolean.parseBoolean(props.getProperty(name));
	}

}
