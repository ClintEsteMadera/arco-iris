/**
 * Created January 16, 2007.
 * Renamed from ProbeCoupledGauge to RegularPatternGauge April 11, 2007.
 * Modification eliminates the need to hold reference to a Probe.
 */
package org.sa.rainbow.translator.gauges;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * This class implements the common methods for a Gauge that processes Probe
 * reports using one or more regular patterns.  Report strings are queued for
 * thread processing.  Probe beacon expiration isn't used by this type of Gauge.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class RegularPatternGauge extends AbstractGauge {

	protected Queue<String> m_lines = null;
	private Map<String,Pattern> m_patternMap = null;

	/**
	 * Main Constructor the Gauge that is hardwired to the Probe.
	 * @param threadName  the name of the Gauge thread
	 * @param id  the unique ID of the Gauge
	 * @param beaconPeriod  the liveness beacon period of the Gauge
	 * @param gaugeDesc  the type-name description of the Gauge
	 * @param modelDesc  the type-name description of the Model the Gauge updates
	 * @param setupParams  the list of setup parameters with their values
	 * @param mappings  the list of Gauge Value to Model Property mappings
	 */
	public RegularPatternGauge (String threadName, String id, long beaconPeriod,
			TypeNamePair gaugeDesc, TypeNamePair modelDesc,
			List<AttributeValueTriple> setupParams,
			List<ValuePropertyMappingPair> mappings) {

		super(threadName, id, beaconPeriod, gaugeDesc, modelDesc, setupParams, mappings);

		m_lines = new LinkedList<String>();
		m_patternMap = new HashMap<String,Pattern>();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.AbstractGauge#dispose()
	 */
	@Override
	public void dispose () {
		m_lines.clear();
		m_patternMap.clear();

		// null-out data members
		m_lines = null;
		m_patternMap = null;

		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.AbstractGauge#probeReport(java.lang.String)
	 */
	@Override
	public void probeReport (String data) {
		if (m_lines == null) return;

		m_lines.offer(data);

		super.probeReport(data);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.translator.gauges.AbstractGauge#runAction()
	 */
	@Override
	protected void runAction() {
		Matcher m = null;
		String name = null;
		int cnt = MAX_UPDATES_PER_SLEEP;
		while (m_lines.size() > 0 && cnt-- > 0) {
			String line = m_lines.poll();
			// process the line for stats
			//log("Got line: " + line);
			for (Map.Entry<String,Pattern> e : m_patternMap.entrySet()) {
				name = e.getKey();
				m = e.getValue().matcher(line);
				if (m.matches()) {
					break;
				}
			}
			if (m != null && m.matches()) {
				doMatch(name, m);
			}
		}

		super.runAction();
	}

	protected void addPattern (String matchName, Pattern p) {
		m_patternMap.put(matchName.intern(), p);
	}

	protected abstract void doMatch (String matchName, Matcher m);

}
