/**
 * Created December 21, 2006.
 */
package org.sa.rainbow.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class maps a pair of Strings, one for a value name, and the other for
 * the corresponding property name.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class ValuePropertyMappingPair extends Pair<String,String> {

	private static final long serialVersionUID = 1805792037182268437L;

	private static Pattern m_mappingPairPattern = null;

	/**
	 * Main Constructor for creating a mapping pair of value name to prop name.
	 */
	public ValuePropertyMappingPair (String valueName, String propertyName) {
		super(valueName, propertyName);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.util.Pair#clone()
	 */
	@Override
	public Object clone() {
		return (ValuePropertyMappingPair )super.clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return firstValue() + "::" + secondValue();
	}

	public String valueName () {
		return firstValue();
	}
	public void setValueName (String valName) {
		setFirstValue(valName);
	}

	public String propertyName () {
		return secondValue();
	}
	public void setPropertyName (String propName) {
		setSecondValue(propName);
	}

	/**
	 * Parses a string of the form "valueN"::"propN" into a Pair object.
	 *  
	 * @param str  the string to parse, must be of form v:p
	 * @return ValuePropertyMappingPair  the resulting pair object whose value name is v and property name is p
	 */
	public static ValuePropertyMappingPair parseMappingPair (String str) {
		if (m_mappingPairPattern == null) {
			m_mappingPairPattern = Pattern.compile("(.+?)::(.+?)");
		}
	
		Matcher m = m_mappingPairPattern.matcher(str);
		String valName = null;
		String propName = null;
		if (m.matches()) {  // got 2 groups matched
			valName = m.group(1).trim();
			propName = m.group(2).trim();
		}
	
		return new ValuePropertyMappingPair (valName, propName);
	}

}
