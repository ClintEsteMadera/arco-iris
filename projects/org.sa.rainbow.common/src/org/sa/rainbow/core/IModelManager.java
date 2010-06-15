/**
 * Created July 20, 2007.
 */
package org.sa.rainbow.core;

import java.util.Map;
import java.util.Set;

import org.acmestudio.acme.element.IAcmeElementInstance;
import org.acmestudio.acme.element.IAcmeElementType;

/**
 * The RainbowModel Manager manages the architectural and environmental models.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IModelManager {

	public static final String NAME = "Rainbow Model Manager";

	public abstract Object queryElementProperty(String propName,
			IAcmeElementInstance<?, ?> element);

	/**
	 * Returns the count of available services of a certain type without further
	 * filtering of attributes.
	 * @param type  the Acme Element Type
	 * @return the number of such available services
	 */
	public abstract int availableServices(IAcmeElementType<?, ?> type);

	/**
	 * Returns a set of architectural instances of the designated type, matching
	 * an optional filter of key-value attribute pairs.  A match must satisfy
	 * ALL attribute pairs (i.e., filters are conjunctive).  An implied
	 * constraint is that the instance MUST define the property "isArchEnabled"
	 * and the value of that property must be <code>false</code>.
	 * @param type  the Acme Element Type
	 * @param filters  a map of key-value pairs to describe what instances to filter out
	 * @return
	 */
	public abstract Set<IAcmeElementInstance<?,?>> findServices(
			IAcmeElementType<?,?> type, Map<String,String> filters);

}