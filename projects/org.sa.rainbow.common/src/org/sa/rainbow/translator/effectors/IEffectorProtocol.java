/**
 * Created February 1, 2007.
 */
package org.sa.rainbow.translator.effectors;

import org.sa.rainbow.core.Rainbow;

/**
 * This interface defines common strings and commands required for the Gauge
 * Protocol.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IEffectorProtocol {

	/** The ID is used for report, since SystemDelegate already knows the provisioning Effector. */
	public static final String ID = "id";
	/** The Name is used in concert with Location for execution, to match Effector. */
	public static final String NAME = "name";
	public static final String LOCATION = "location";
	public static final String OUTCOME = "outcome";
	public static final String ARGUMENT = "argument";
	public static final String SIZE = Rainbow.USCORE + "size";

}
