/**
 * Created December 19, 2006.
 */
package org.sa.rainbow.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A pair of type-name attribute, and a value.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class AttributeValueTriple extends Pair<TypeNamePair,Object> {

	private static final long serialVersionUID = 4107943280504716193L;

	public static final String DEFAULT_TYPE = "String";

	private static Pattern m_valueTriplePattern = null;

	/**
	 * Constructor from a pair and an object. 
	 * @param attr  a TypeNamePair describing the attribute
	 * @param v     the value object
	 */
	public AttributeValueTriple (TypeNamePair attr, Object v) {
		super(attr, v);
	}

	/**
	 * Constructor from a triple of type, name, value
	 * @param type  the String describing the type
	 * @param name  the String describing the name
	 * @param v     the value object
	 */
	public AttributeValueTriple (String type, String name, Object v) {
		super(new TypeNamePair(type, name), v);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.util.Pair#clone()
	 */
	@Override
	public Object clone() {
		return (AttributeValueTriple )super.clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String type = type();
		if (type.intern() == DEFAULT_TYPE) {
			type = null;
		}
		Object value = value();
		return name() + (type==null? "" : ":" + type)
			+ (value==null? "" : "=" + value.toString());
	}

	public TypeNamePair attribute () {
		return firstValue();
	}

	public String type () {
		return firstValue().type();
	}
	public void setType (String type) {
		firstValue().setType(type);
	}

	/**
	 * Updates the type name of the value, as well as "reparses" the value
	 * using this new type info, and update the value Object.
	 * @param newType  the new type name
	 * @param valueStr  the string representing the value Object
	 */
	public void updateTypeValue (String newType, String valStr) {
		if (newType == DEFAULT_TYPE) {  // just store the String
			setSecondValue(valStr);
		} else {
			Object val = Util.parseObject(valStr, newType);
			setSecondValue(val);
		}
	}

	public String name () {
		return firstValue().name();
	}
	public void setName (String name) {
		firstValue().setName(name);
	}

	public Object value () {
		return secondValue();
	}
	public void setValue (Object val) {
		setSecondValue(val);
	}

	/**
	 * Parses a string of the form "name"[:"type"][=value] into a Triple object,
	 * which is actually composed of a Pair of TypeNamePair and a Value Object.
	 * This method treats the value as String type by default, if not type
	 * is parsed from the "val" string.  Equivalent to calling:
	 * <blockquote>
	 * <code>AttributeValueTriple.parseValueTriple(val, DEFAULT_TYPE);</code>
	 * </blockquote>
	 * @param str  the string to parse, must be of form n[:t][=v]
	 * @return AttributeValueTriple  the resulting pair object whose attribute
	 *     has type t and name n, and whose value is a primitive object of v
	 */
	public static AttributeValueTriple parseValueTriple (String val) {
		return parseValueTriple(val, DEFAULT_TYPE);
	}

	/**
	 * Parses a string of the form "name"[:"type"][=value] into a Triple object,
	 * using the supplied defaultType as the value type if no type was parsed.
	 * 
	 * @param str  the string to parse, must be of form n[:t][=v]
	 * @param defaultType  the default type to use for a parsed value "type" is blank
	 * @return AttributeValueTriple  the resulting pair object whose attribute
	 *     has type t and name n, and whose value is a primitive object of v
	 */
	public static AttributeValueTriple parseValueTriple (String val, String defaultType) {
		if (m_valueTriplePattern == null) {
			m_valueTriplePattern = Pattern.compile("(.+?)(:(.+?))?(=(.+?))?");
		}
	
		Matcher m = m_valueTriplePattern.matcher(val);
		String type = defaultType;
		String name = null;
		String valStr = null;
		if (m.matches()) {  // got three matched + 2 option groups
			name = m.group(1).trim();
			if (m.group(3) != null) {  // type component exists
				type = m.group(3).trim();
			}
			if (m.group(5) != null) {  // value component exists
				valStr = m.group(5).trim();
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
		return new AttributeValueTriple(type, name, value);
	}

}
