/**
 * Created December 19, 2006.
 */
package org.sa.rainbow.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class to store a pair of Strings, one for the type of something, and the
 * other for the name of something.  This can be used with a AttributeValueTriple
 * to form a type-name-value triple relationship.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class TypeNamePair extends Pair<String,String> {

	private static final long serialVersionUID = -2640549607677352253L;

	private static Pattern m_mappingPairPattern = null;

	/**
	 * Main Constructor for creating a pair of strings.
	 */
	public TypeNamePair (String type, String name) {
		super(type.intern(), name);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.util.Pair#clone()
	 */
	@Override
	public Object clone() {
		return (TypeNamePair )super.clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return secondValue() + ":" + firstValue();
	}

	public String type () {
		return firstValue();
	}
	public void setType (String type) {
		setFirstValue(type);
	}

	public String name () {
		return secondValue();
	}
	public void setName (String name) {
		setSecondValue(name);
	}

	/**
	 * Parses a string of the form "name":"type" into a Pair object.
	 *  
	 * @param str  the string to parse, must be of form n:t
	 * @return TypeNamePair  the resulting pair object whose type is t and name is n
	 */
	public static TypeNamePair parsePair (String str) {
		if (m_mappingPairPattern == null) {
			m_mappingPairPattern = Pattern.compile("(.+?):(.+?)");
		}
	
		Matcher m = m_mappingPairPattern.matcher(str);
		String name = null;
		String type = null;
		if (m.matches()) {  // got 2 groups matched
			name = m.group(1).trim();
			type = m.group(2).trim();
		}
	
		return new TypeNamePair (type, name);
	}

}
