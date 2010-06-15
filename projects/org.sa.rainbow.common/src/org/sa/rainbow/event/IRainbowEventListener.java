/**
 * Created November 26, 2006.
 */
package org.sa.rainbow.event;

import org.sa.rainbow.core.Identifiable;

/**
 * This interface defines an event listener in the Rainbow framework.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IRainbowEventListener extends Identifiable {

	public void onMessage (IRainbowMessage msg);

}
