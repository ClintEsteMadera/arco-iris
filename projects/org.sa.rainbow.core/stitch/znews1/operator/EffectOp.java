/**
 * Created August 25, 2006.
 */
package znews1.operator;

import java.util.HashMap;
import java.util.Map;

import org.sa.rainbow.core.Rainbow;

/**
 * Effector library for the Apache single-server version of the ZNews example
 * system, modified November 3, 2006.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class EffectOp {

	public static void setFidelity (Object server, int fid) {
		Map<String,String> pairs = new HashMap<String,String>();
		pairs.put("fidelity", String.valueOf(fid));
		Rainbow.instance().sysOpProvider().changeState("setFidelity", server, pairs);
	}

}
