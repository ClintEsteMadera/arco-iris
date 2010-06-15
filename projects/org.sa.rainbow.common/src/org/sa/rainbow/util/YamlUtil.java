/**
 * Created January 31, 2007.
 */
package org.sa.rainbow.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ho.yaml.Yaml;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.model.DescriptionAttributes;
import org.sa.rainbow.model.EffectorDescription;
import org.sa.rainbow.model.GaugeDescription;
import org.sa.rainbow.model.GaugeInstanceDescription;
import org.sa.rainbow.model.GaugeTypeDescription;
import org.sa.rainbow.model.Model;
import org.sa.rainbow.model.ProbeDescription;
import org.sa.rainbow.model.UtilityPreferenceDescription;
import org.sa.rainbow.model.UtilityPreferenceDescription.UtilityAttributes;
import org.sa.rainbow.translator.effectors.IEffector;
import org.sa.rainbow.translator.probes.IProbe;

/**
 * This utility class provides methods for parsing specific Yaml files for
 * Rainbow.  The class is non-instantiable.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class YamlUtil {

	/**
	 * Retrieves the utility definitions, then<ul>
	 * <li> store the weights
	 * <li> store the utility functions
	 * <li> for each tactic, store respective tactic attribute vectors.
	 * @return UtilityPreferenceDescription  the data structure of utility definitions.
	 */
	@SuppressWarnings("unchecked")
	public static UtilityPreferenceDescription loadUtilityPrefs () {
		UtilityPreferenceDescription prefDesc = new UtilityPreferenceDescription();

		Map<String,Map<String,Map>> utilityDefMap = null;
		try {
			File defFile = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow.property(Rainbow.PROPKEY_UTILITY_PATH));
			Object o = Yaml.load(defFile);
			Util.logger().trace("Utiltiy Def Yaml file loaded: " + o.toString());
			utilityDefMap = (Map )o;
		} catch (FileNotFoundException e) {
			Util.logger().error("Loading Utiltiy Def Yaml file failed!", e);
		}

		// store weights
		Map<String,Map> weightMap = utilityDefMap.get("weights");
		for (Map.Entry<String,Map> e : weightMap.entrySet()) {
			Map<String,Double> kvMap = new HashMap<String,Double>();
			double sum = 0.0;
			for (Object k : e.getValue().keySet()) {
				Object v = e.getValue().get(k);
				if (k instanceof String && v instanceof Number) {
					kvMap.put((String )k, ((Number )v).doubleValue());
					sum += ((Number )v).doubleValue();
				}
			}
			if (sum < 1.0 || sum > 1.0) {  // issue warning
				Util.logger().warn("Weights for " + e.getKey() + " did NOT sum to 1!");
			}
			prefDesc.weights.put(e.getKey(), kvMap);
		}
		Util.logger().trace(" - Weights collected: " + prefDesc.weights);

		// create utility functions
		Map<String,Map> utilMap = utilityDefMap.get("utilities");
		for (String k : utilMap.keySet()) {
			Map vMap = utilMap.get(k);
			UtilityAttributes ua = new UtilityAttributes();
			ua.label = (String )vMap.get("label");
			ua.mapping = (String )vMap.get("mapping");
			ua.desc = (String )vMap.get("description");
			ua.values = (Map<Number,Number> )vMap.get("utility");
			prefDesc.utilities.put(k, ua);
		}
		Util.logger().trace(" - Utility functions collected: " + prefDesc.utilities);

		Map<String,Map> vectorMap = utilityDefMap.get("vectors");
		for (String k : vectorMap.keySet()) {
			prefDesc.attributeVectors.put(k, (Map<String,Object> )vectorMap.get(k));
		}
		Util.logger().trace(" - Utility attribute vectors collected: " + prefDesc.attributeVectors);

		return prefDesc;
	}

	@SuppressWarnings("unchecked")
	public static GaugeDescription loadGaugeSpecs (Model model) {
		GaugeDescription gd = new GaugeDescription();

		Map<String,Map<String,Map>> gaugeSpecMap = null;
		try {
			File gaugeSpec = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow.property(Rainbow.PROPKEY_GAUGES_PATH));
			Object o = Yaml.load(gaugeSpec);
			Util.logger().trace("Gauge Spec Yaml file loaded: " + o.toString());
			gaugeSpecMap = (Map )o;
		} catch (FileNotFoundException e) {
			Util.logger().error("Loading Gauge Spec Yaml file failed!", e);
		}

		// store gauge type descriptions
		Map<String,Map> typeMap = gaugeSpecMap.get("gauge-types");
		for (Map.Entry<String,Map> typeSpec : typeMap.entrySet()) {
			// map type name to Gauge type desc
			String gaugeType = typeSpec.getKey();
			Map<String,Object> attrMap = typeSpec.getValue();  // get attribute map
			// get comment
			String typeComment = (String )attrMap.get("comment");
			// populate type description
			GaugeTypeDescription gaugeTypeSpec = new GaugeTypeDescription(gaugeType, typeComment);
			gd.typeSpec.put(gaugeType, gaugeTypeSpec);
			// get mappings of reported values
			Map<String,String> values = (Map<String,String> )attrMap.get("values");
			for (Map.Entry<String,String> value : values.entrySet()) {
				String valName = value.getKey();
				String valType = value.getValue();
				gaugeTypeSpec.addReportedValue(new TypeNamePair(valType, valName));
			}
			// get mappings of setup params
			Map<String,Map> params = (Map<String,Map> )attrMap.get("setupParams");
			for (Map.Entry<String,Map> param : params.entrySet()) {
				String pname = param.getKey();
				Map<String,Object> paramAttr = param.getValue();
				String ptype = (String )paramAttr.get("type");
				Object pdefault = paramAttr.get("default");
				if (! ptype.equals("String")) {
					pdefault = Util.parseObject(pdefault.toString(), ptype);
				}
				if (pdefault != null && pdefault instanceof String) {
					pdefault = Util.evalTokens((String )pdefault);
				}
				gaugeTypeSpec.addSetupParam(new AttributeValueTriple(ptype, pname, pdefault));
			}
			// get mappings of config params
			params = (Map<String,Map> )attrMap.get("configParams");
			for (Map.Entry<String,Map> param : params.entrySet()) {
				String pname = param.getKey();
				Map<String,Object> paramAttr = param.getValue();
				String ptype = (String )paramAttr.get("type");
				Object pdefault = paramAttr.get("default");
				if (! ptype.equals("String")) {
					pdefault = Util.parseObject(pdefault.toString(), ptype);
				}
				if (pdefault != null && pdefault instanceof String) {
					pdefault = Util.evalTokens((String )pdefault);
				}
				gaugeTypeSpec.addConfigParam(new AttributeValueTriple(ptype, pname, pdefault));
			}
		}
		Util.logger().trace(" - Gauge Types collected: " + gd.typeSpec.keySet());

		// store gauge instances
		Map<String,Map> instanceMap = gaugeSpecMap.get("gauge-instances");
		for (Map.Entry<String,Map> instSpec : instanceMap.entrySet()) {
			// map name to Gauge instance
			String gaugeName = instSpec.getKey();
			Map<String,Object> attrMap = instSpec.getValue();  // get attribute map
			// get type name, model description, comment
			String gaugeType = (String )attrMap.get("type");
			TypeNamePair modelDesc = TypeNamePair.parsePair((String )attrMap.get("model"));
			String instComment = (String )attrMap.get("comment");
			// populate instance description
			GaugeTypeDescription gaugeTypeSpec = gd.typeSpec.get(gaugeType);
			GaugeInstanceDescription gaugeInstSpec = gaugeTypeSpec.makeInstance(gaugeName, instComment);
			gaugeInstSpec.setModelDesc(modelDesc);
			gd.instSpec.put(gaugeName, gaugeInstSpec);
			// get mappings of mappings
			Map<String,String> mappings = (Map<String,String> )attrMap.get("mappings");
			for (Map.Entry<String,String> mapping : mappings.entrySet()) {
				String valName = Util.evalTokens(mapping.getKey());
				if (model != null) {
					valName = Util.evalModelProperties(valName, model);
				}
				String propName = mapping.getValue();
				gaugeInstSpec.addMapping(new ValuePropertyMappingPair(valName, propName));
			}
			// get mappings of setup values and store in setup param info
			Map<String,Object> values = (Map<String,Object> )attrMap.get("setupValues");
			for (Map.Entry<String,Object> param : values.entrySet()) {
				String paramName = param.getKey();
				Object paramValue = param.getValue();
				if (paramValue != null) {  // set new value
					if (paramValue instanceof String) {
						paramValue = Util.evalTokens((String )paramValue);
					}
					AttributeValueTriple setupParam = gaugeInstSpec.findSetupParam(paramName);
					if (setupParam != null) {
						setupParam.setValue(paramValue);
					}
				}
			}
			// get mappings of config values and store in config param info
			values = (Map<String,Object> )attrMap.get("configValues");
			for (Map.Entry<String,Object> param : values.entrySet()) {
				String paramName = param.getKey();
				Object paramValue = param.getValue();
				if (paramValue != null) {  // set new value
					if (paramValue instanceof String) {
						paramValue = Util.evalTokens((String )paramValue);
					}
					AttributeValueTriple configParam = gaugeInstSpec.findConfigParam(paramName);
					if (configParam != null) {
						configParam.setValue(paramValue);
					}
				}
			}
		}
		Util.logger().trace(" - Gauge Instances collected: " + gd.instSpec.keySet());

		return gd;
	}

	@SuppressWarnings("unchecked")
	public static EffectorDescription loadEffectorDesc () {
		EffectorDescription ed = new EffectorDescription();

		Map effectorMap = null;
		try {
			File effectorFile = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow.property(Rainbow.PROPKEY_EFFECTORS_PATH));
			Object o = Yaml.load(effectorFile);
			Util.logger().trace("Effector Desc Yaml file loaded: " + o.toString());
			effectorMap = (Map )o;
		} catch (FileNotFoundException e) {
			Util.logger().error("Loading Effector Desc Yaml file failed!", e);
		}

		// acquire "variable" declarations and store as rainbow properties
		Map<String,String> varMap = (Map<String,String> )effectorMap.get("vars");
		for (Map.Entry<String,String> varPair : varMap.entrySet()) {
			Rainbow.instance().setProperty(varPair.getKey(), Util.evalTokens(varPair.getValue()));
		}

		// store effector description info
		Map<String,Map> effMap = (Map<String,Map> )effectorMap.get("effectors");
		for (Map.Entry<String,Map> effInfo : effMap.entrySet()) {
			EffectorDescription.EffectorAttributes ea = new EffectorDescription.EffectorAttributes();

			// get effector name
			ea.name = effInfo.getKey();
			Map<String,Object> attrMap = effInfo.getValue();  // get attribute map
			// get location and effector type
			ea.location = Util.evalTokens((String )attrMap.get("location"));
			ea.kindName = (String )attrMap.get("type");
			ea.kind = IEffector.Kind.valueOf(ea.kindName.toUpperCase());
			Map<String,Object> addlInfoMap = (Map<String,Object> )attrMap.get(ea.infoPropName());
			extractArrays(ea, addlInfoMap);
			ed.effectors.add(ea);
		}
		Util.logger().trace(" - Effectors collected: " + ed.effectors);

		return ed;
	}

	@SuppressWarnings("unchecked")
	public static ProbeDescription loadProbeDesc () {
		ProbeDescription ed = new ProbeDescription();

		Map probeMap = null;
		try {
			File probeFile = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow.property(Rainbow.PROPKEY_PROBES_PATH));
			Object o = Yaml.load(probeFile);
			Util.logger().trace("Probe Desc Yaml file loaded: " + o.toString());
			probeMap = (Map )o;
		} catch (FileNotFoundException e) {
			Util.logger().error("Loading Probe Desc Yaml file failed!", e);
		}

		// acquire "variable" declarations and store as rainbow properties
		Map<String,String> varMap = (Map<String,String> )probeMap.get("vars");
		for (Map.Entry<String,String> varPair : varMap.entrySet()) {
			Rainbow.instance().setProperty(varPair.getKey(), Util.evalTokens(varPair.getValue()));
		}

		// store probe description info
		Map<String,Map> pbMap = (Map<String,Map> )probeMap.get("probes");
		for (Map.Entry<String,Map> pbInfo : pbMap.entrySet()) {
			ProbeDescription.ProbeAttributes pa = new ProbeDescription.ProbeAttributes();

			// get probe name
			pa.name = pbInfo.getKey();
			Map<String,Object> attrMap = pbInfo.getValue();  // get attribute map
			// get location, alias, and probe type
			pa.location = Util.evalTokens((String )attrMap.get("location"));
			pa.alias = (String )attrMap.get("alias");
			pa.kindName = (String )attrMap.get("type");
			pa.kind = IProbe.Kind.valueOf(pa.kindName.toUpperCase());
			Map<String,Object> addlInfoMap = (Map<String,Object> )attrMap.get(pa.infoPropName());
			extractArrays(pa, addlInfoMap);
			ed.probes.add(pa);
		}
		Util.logger().trace(" - Probe collected: " + ed.probes);

		return ed;
	}

	/**
	 * Acquires additional info (key-value pairs) based on the element Kind.
	 * @param attr     the DescriptionAttributes object to populate
	 * @param infoMap  the map of key-value info pairs
	 */
	public static void extractArrays(DescriptionAttributes attr, Map<String,Object> infoMap) {
		List<String> arrayKeys = new ArrayList<String>();
		for (Map.Entry<String,Object> pair : infoMap.entrySet()) {
			if (pair.getKey().endsWith(".length")) {  // store just the key
				arrayKeys.add(pair.getKey().replace(".length", ""));
			}
			String valStr = String.valueOf(pair.getValue());
			attr.info.put(pair.getKey(), Util.evalTokens(valStr));
		}
		/* Get any key-value pair named "key.length", remove it, find all
		 * key.# items, and construct an array out of the list of values
		 */
		for (String arrayKey: arrayKeys) {
			int length = Integer.parseInt(attr.info.remove(arrayKey + ".length"));
			String[] valArray = new String[length];  // new array
			for (int i=0; i < length; ++i) {  // store item in array
				String itemKey = arrayKey + Util.DOT + i;
				if (attr.info.containsKey(itemKey)) {
					valArray[i] = attr.info.remove(itemKey);
				}
			}
			attr.arrays.put(arrayKey, valArray);  // store array
		}
	}

}
