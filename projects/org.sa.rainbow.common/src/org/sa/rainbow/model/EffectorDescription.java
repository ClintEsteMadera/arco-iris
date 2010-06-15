/**
 * Created January 31, 2007.
 */
package org.sa.rainbow.model;

import java.util.SortedSet;
import java.util.TreeSet;

import org.sa.rainbow.translator.effectors.IEffector;

/**
 * This class holds effector description information parsed from its description
 * file (usu. Yaml).
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class EffectorDescription {

	public static class EffectorAttributes extends DescriptionAttributes {
		public IEffector.Kind kind = null;
	}

	public SortedSet<EffectorAttributes> effectors = null;

	/**
	 * Default Constructor.
	 */
	public EffectorDescription() {
		effectors = new TreeSet<EffectorAttributes>();
	}

}
