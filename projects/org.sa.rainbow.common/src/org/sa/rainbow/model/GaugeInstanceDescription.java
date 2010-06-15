/**
 * Created January 18, 2007.
 */
package org.sa.rainbow.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sa.rainbow.health.Beacon;
import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.ValuePropertyMappingPair;

/**
 * This Class captures the information in a Gauge Instance description, though
 * instantiated first from the Gauge Type description.  This description helps
 * to track a Gauge from creation through termination.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class GaugeInstanceDescription extends GaugeTypeDescription {

	public static enum State {
		UNINITIALIZED, CREATING, CREATED, CONFIGURING,
		/** Although a Gauge can technically be configured, but not yet alive,
		 *  for simplicity, we'll consider configured to be alive.  Pausing
		 *  a Gauge is separately handled from these lifecycle states. */
		ALIVE,
		TERMINATED 
	}

	public static final String LOCATION_PARAM_NAME = "targetIP";

	private String m_instName = null;
	private String m_instComment = null;
	private TypeNamePair m_modelDesc = null;
	private Map<String,ValuePropertyMappingPair> m_mappings = null;

	private String m_id = null;
	private State m_state = State.UNINITIALIZED;
	private Beacon m_beacon = null;

	/**
	 * Main Constructor.
	 */
	public GaugeInstanceDescription (String gaugeType, String gaugeName,
			String typeComment, String instComment) {

		super(gaugeType, typeComment);
		m_instName = gaugeName;
		m_instComment = instComment;
		m_mappings = new HashMap<String,ValuePropertyMappingPair>();
		m_beacon = new Beacon();
	}

	public String gaugeName () {
		return m_instName;
	}

	public String instanceComment () {
		return m_instComment;
	}

	public TypeNamePair modelDesc () {
		return m_modelDesc;
	}

	public void setModelDesc (TypeNamePair modelDesc) {
		m_modelDesc = modelDesc;
	}

	public void addMapping (ValuePropertyMappingPair mapping) {
		m_mappings.put(mapping.valueName(), mapping);
	}

	public ValuePropertyMappingPair findMapping (String name) {
		return m_mappings.get(name);
	}

	@SuppressWarnings("unchecked")
	public List<ValuePropertyMappingPair> mappings () {
		List<ValuePropertyMappingPair> mapList = new ArrayList<ValuePropertyMappingPair>(m_mappings.values());
		Collections.sort(mapList);
		return mapList;
	}

	public String id () {
		return m_id;
	}
	public void setID (String id) {
		m_id = id;
	}

	/**
	 * Assuming the location info is always set via the setup parameter "targetIP",
	 * this method returns the value of that location.
	 * @return String  the string indicating the location of the target host
	 */
	public String location () {
		String location = null;
		AttributeValueTriple loc = findSetupParam(LOCATION_PARAM_NAME);
		if (loc != null) {
			location = (String )loc.value();
		}
		return location;
	}

	public Beacon beacon () {
		return m_beacon;
	}

	public State state () {
		return m_state;
	}
	public void setState (State newState) {
		m_state = newState;
	}
	public boolean notYetCreated () {
		return m_state.compareTo(State.CREATING) < 0;
	}
	public boolean notYetConfigured () {
		return m_state.compareTo(State.CREATED) >= 0
			&& m_state.compareTo(State.CONFIGURING) < 0;
	}
	public boolean isAlive () {
		return m_state == State.ALIVE;
	}

}
