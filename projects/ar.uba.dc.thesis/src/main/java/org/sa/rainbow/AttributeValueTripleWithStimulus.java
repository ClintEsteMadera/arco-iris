package org.sa.rainbow;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sa.rainbow.util.AttributeValueTriple;
import org.sa.rainbow.util.TypeNamePair;
import org.sa.rainbow.util.Util;

public class AttributeValueTripleWithStimulus extends AttributeValueTriple {

	private static final long serialVersionUID = 1L;

	private static Pattern m_valueTriplePattern;

	private final String stimulus;

	public AttributeValueTripleWithStimulus(String type, String name, Object value, String stimulus) {
		super(type, name, value);
		this.stimulus = stimulus;
	}

	public AttributeValueTripleWithStimulus(TypeNamePair attr, Object value, String stimulus) {
		super(attr, value);
		this.stimulus = stimulus;
	}

	public String getStimulus() {
		return stimulus;
	}

	@Override
	public String toString() {
		return super.toString() + " (stimulus: " + this.getStimulus() + ")";
	}

	/**
	 * Parses a string of the form "name"[:"type"][=value] into a Triple object, using the supplied defaultType as the
	 * value type if no type was parsed.
	 * 
	 * @param str
	 *            the string to parse, must be of form n[:t][=v]
	 * @param defaultType
	 *            the default type to use for a parsed value "type" is blank
	 * @return AttributeValueTriple the resulting pair object whose attribute has type t and name n, and whose value is
	 *         a primitive object of v
	 */
	public static AttributeValueTriple parseValueTriple(String val, String defaultType) {
		if (m_valueTriplePattern == null) {
			m_valueTriplePattern = Pattern.compile("(.+?)(:(.+?))?(=(.+?))?.*<stimulus:(.+)>");
		}

		Matcher m = m_valueTriplePattern.matcher(val);
		String type = defaultType;
		String name = null;
		String valStr = null;
		String stimulusName = null;
		if (m.matches()) { // got three matched + 2 option groups
			name = m.group(1).trim();
			if (m.group(3) != null) { // type component exists
				type = m.group(3).trim();
			}
			if (m.group(5) != null) { // value component exists
				valStr = m.group(5).trim();
			}
			if (m.group(6) != null) { // stimulus name exists
				stimulusName = m.group(6).trim();
			}
		}

		// parse value
		Object value = null;
		if (valStr != null) {
			if (type == DEFAULT_TYPE) {
				value = valStr;
			} else {
				value = Util.parseObject(valStr, type);
			}
		}
		return new AttributeValueTripleWithStimulus(type, name, value, stimulusName);
	}

}