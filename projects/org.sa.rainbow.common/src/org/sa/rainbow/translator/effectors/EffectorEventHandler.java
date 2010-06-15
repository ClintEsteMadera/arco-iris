/**
 * Created January 18, 2007.
 */
package org.sa.rainbow.translator.effectors;

import org.sa.rainbow.RainbowConstants;
import org.sa.rainbow.event.EventServiceManager;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.event.IProtocolAction;
import org.sa.rainbow.event.IRainbowMessage;
import org.sa.rainbow.event.RainbowEventAdapter;

/**
 * This class defines a handler for an effector, which participates in the
 * IEffectorProtocol.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class EffectorEventHandler extends RainbowEventAdapter {

	/**
	 * Main Constructor.
	 * @param id  an identifier string for this handler
	 */
	public EffectorEventHandler (IEffector effector) {
		super(effector.id());
	}

	/**
	 * Executes the effector.
	 * @param name      a unique name within the target location
	 * @param location  location identifier required for remote delegates to filter applicability
	 * @param args      the list of argument for effector execution
	 * @return boolean  <code>true</code> if event sending succeeds, <code>false</code> otherwise.
	 */
	public static boolean executeEffector (String name, String location, String[] args) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_EFFECTOR_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.EXECUTE.name());
		msg.setProperty(IEffectorProtocol.NAME, name);
		msg.setProperty(IEffectorProtocol.LOCATION, location);
		msg.setProperty(IEffectorProtocol.ARGUMENT + IEffectorProtocol.SIZE, args.length);
		for (int i=0; i < args.length; ++i) {
			String key = IEffectorProtocol.ARGUMENT + RainbowConstants.USCORE + i;
			msg.setProperty(key, args[i]);
		}
		return EventServiceManager.instance().send(IEventService.TOPIC_EFFECTOR_BUS, msg);
	}

	public static boolean reportOutcome (String id, IEffector.Outcome outcome) {

		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_EFFECTOR_BUS);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_OUTCOME.name());
		msg.setProperty(IEffectorProtocol.ID, id);
		msg.setProperty(IEffectorProtocol.OUTCOME, outcome.name());
		return EventServiceManager.instance().send(IEventService.TOPIC_EFFECTOR_BUS, msg);
	}

//	public static boolean sendBeacon (String id) {
//		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_EFFECTOR_BUS);
//		msg.setProperty(IEffectorProtocol.ACTION, IEffectorProtocol.Action.BEACON.name());
//		msg.setProperty(IEffectorProtocol.ID, id);
//		return EventServiceManager.instance().send(IEventService.TOPIC_EFFECTOR_BUS, msg);
//	}

}
