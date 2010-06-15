/**
 * Created January 18, 2007.
 */
package org.sa.rainbow.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.TypeNamePair;

/**
 * This Class captures the information in a Gauge Type description.
 * The captured information can be used to create Gauge Instance descriptions.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class GaugeTypeDescription {

	protected String m_typeName = null;
	protected String m_typeComment = null;
	/** Stores, by type name, a hash of type-name pairs. */
	protected Map<String,TypeNamePair> m_reportedValues = null;
	/** Stores, by name, a hash of type-name and any default value of the setup parameters. */
	protected Map<String,AttributeValueTriple> m_setupParams = null;
	/** Stores, by name, a hash of the type-name and any default value of the configuration parameters. */
	protected Map<String,AttributeValueTriple> m_configParams = null;

	/**
	 * Main Constructor.
	 */
	public GaugeTypeDescription (String gaugeType, String typeComment) {
		m_typeName = gaugeType;
		m_typeComment = typeComment;
		m_reportedValues = new HashMap<String,TypeNamePair>();
		m_setupParams = new HashMap<String,AttributeValueTriple>();
		m_configParams = new HashMap<String,AttributeValueTriple>();
	}

	public GaugeInstanceDescription makeInstance (String gaugeName, String instComment) {
		// create a Gauge Instance description using type, name, and comments
		GaugeInstanceDescription inst = new GaugeInstanceDescription(m_typeName, gaugeName, m_typeComment, instComment);
		// transfer the set of reported values, setup params, and config params
		for (TypeNamePair valPair : reportedValues()) {
			inst.addReportedValue((TypeNamePair )valPair.clone());
		}
		for (AttributeValueTriple param : setupParams()) {
			inst.addSetupParam((AttributeValueTriple )param.clone());
		}
		for (AttributeValueTriple param : configParams()) {
			inst.addConfigParam((AttributeValueTriple )param.clone());
		}
		return inst;
	}

	public String gaugeType () {
		return m_typeName;
	}

	public String typeComment () {
		return m_typeComment;
	}

	public void addReportedValue (TypeNamePair pair) {
		m_reportedValues.put(pair.name(), pair);
	}

	public TypeNamePair findReportedValue (String name) {
		return m_reportedValues.get(name);
	}

	@SuppressWarnings("unchecked")
	public List<TypeNamePair> reportedValues () {
		List<TypeNamePair> valueList = new ArrayList<TypeNamePair>(m_reportedValues.values());
		Collections.sort(valueList);
		return valueList;
	}

	public void addSetupParam (AttributeValueTriple triple) {
		m_setupParams.put(triple.name(), triple);
	}

	public AttributeValueTriple findSetupParam (String name) {
		return m_setupParams.get(name);
	}

	@SuppressWarnings("unchecked")
	public List<AttributeValueTriple> setupParams () {
		List<AttributeValueTriple> paramList = new ArrayList<AttributeValueTriple>(m_setupParams.values());
		Collections.sort(paramList);
		return paramList;
	}

	public void addConfigParam (AttributeValueTriple triple) {
		m_configParams.put(triple.name(), triple);
	}

	public AttributeValueTriple findConfigParam (String name) {
		return m_configParams.get(name);
	}

	@SuppressWarnings("unchecked")
	public List<AttributeValueTriple> configParams () {
		List<AttributeValueTriple> paramList = new ArrayList<AttributeValueTriple>(m_configParams.values());
		Collections.sort(paramList);
		return paramList;
	}

}
