package ar.uba.dc.thesis.znn.gauges;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sa.rainbow.AttributeValueTripleWithStimulus;
import org.sa.rainbow.translator.gauges.RegularPatternGauge;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.Util;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * Gauge for estimating end-to-end response time of a server request after being invocated by an stimulus, as
 * experienced by a proxy client.
 * 
 * @author Gabriel Tursi
 */
public class End2EndRespTimeGaugeWithStimulus extends RegularPatternGauge {

	public static final String NAME = "G - End-End Resp Time With Stimulus";
	/** Sample window to compute an average latency */
	public static final int AVG_SAMPLE_WINDOW = 5;

	/** List of values reported by this Gauge */
	private static final String[] valueNames = { "end2endRespTimeWithStimulus(*)" };
	private static final String DEFAULT = "DEFAULT";

	private Map<String, Queue<Double>> m_historyMap = null;
	private Map<String, Double> m_cumulationMap = null;

	/**
	 * Main constructor.
	 */
	public End2EndRespTimeGaugeWithStimulus(String id, long beaconPeriod, TypeNamePair gaugeDesc,
			TypeNamePair modelDesc, List<AttributeValueTriple> setupParams, List<ValuePropertyMappingPair> mappings) {

		super(NAME, id, beaconPeriod, gaugeDesc, modelDesc, setupParams, mappings);

		m_historyMap = new HashMap<String, Queue<Double>>();
		m_cumulationMap = new HashMap<String, Double>();

		addPattern(DEFAULT, Pattern.compile("\\[(.+)\\]<(.+)>\\s+(.+?):([0-9.]+)ms\\s<stimulus:(.+)>"));
	}

	@Override
	protected void initProperty(String name, Object value) {
		// no prop to init, do nothing
	}

	@Override
	protected void doMatch(String matchName, Matcher m) {
		if (matchName == DEFAULT) {
			// acquire the next set of ping RTT data, we care for the average
			// String tstamp = m.group(1);
			// String id = m.group(2);
			String host = m.group(3);
			if (host.equals(""))
				return;
			double dur = Double.parseDouble(m.group(4));
			String stimulusName = m.group(5);
			String hostAndStimulus = host + ":" + stimulusName;
			// setup data struct for host:stimulusName if new
			if (!m_historyMap.containsKey(hostAndStimulus)) {
				m_historyMap.put(hostAndStimulus, new LinkedList<Double>());
				m_cumulationMap.put(hostAndStimulus, 0.0);
			}

			Queue<Double> history = m_historyMap.get(hostAndStimulus);
			double cumulation = m_cumulationMap.get(hostAndStimulus);
			// add value to cumulation and enqueue
			cumulation += dur;
			history.offer(dur);
			if (history.size() > AVG_SAMPLE_WINDOW) {
				// if queue size reached window size, then
				// dequeue and delete oldest value and report average
				cumulation -= history.poll();
			}
			m_cumulationMap.put(hostAndStimulus, cumulation); // store updated cumulation
			dur = cumulation / history.size();
			if (m_logger.isDebugEnabled())
				m_logger.debug(id() + ": " + cumulation + ", hist" + Arrays.toString(history.toArray()));

			// update connection in model with latency in seconds
			for (String valueName : valueNames) {
				// massage value name for mapping purposes
				valueName = valueName.replace("*", host);
				if (m_mappings.containsKey(valueName)) {
					// ZNewsSys.c0.experRespTime
					String pExperRespTime = m_modelDesc.name() + Util.DOT + m_mappings.get(valueName);
					if (m_logger.isTraceEnabled())
						m_logger.trace("Updating " + pExperRespTime + " using " + valueName + " = " + dur);
					eventHandler().reportValue(
							new AttributeValueTripleWithStimulus(pExperRespTime, valueName, dur, stimulusName));
				}
			}
		}
	}

}
