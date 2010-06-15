/**
 * Created February 9, 2007.
 */
package org.sa.rainbow.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows event registrants to designate the type of events,
 * in terms of an event protocol Action, that they "subscribe" to on the event
 * bus.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventTypeFilter {

	IProtocolAction[] eventTypes ();
}
