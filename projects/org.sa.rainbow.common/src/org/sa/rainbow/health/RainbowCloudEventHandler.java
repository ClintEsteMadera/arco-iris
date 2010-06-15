/**
 * Created January 18, 2007.
 */
package org.sa.rainbow.health;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.event.EventServiceManager;
import org.sa.rainbow.event.IEventService;
import org.sa.rainbow.event.IProtocolAction;
import org.sa.rainbow.event.IRainbowMessage;
import org.sa.rainbow.event.RainbowEventAdapter;
import org.sa.rainbow.translator.effectors.IEffector;
import org.sa.rainbow.translator.probes.IProbe;
import org.sa.rainbow.translator.probes.IProbeProtocol;

/**
 * This class defines a handler for a "Rainbow Cloud," an element of the Rainbow
 * framework, which participates in the IRainbowHealthProtocol.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class RainbowCloudEventHandler extends RainbowEventAdapter {

	/**
	 * Main Constructor.
	 * @param cloud  reference to a RainbowCloud
	 */
	public RainbowCloudEventHandler (RainbowCloudRef cloud) {
		super(cloud.id());
	}

	public static boolean startCloud (String id, String location) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.START.name());
		if (id != null) {
			msg.setProperty(IRainbowHealthProtocol.ID, id);
		}
		msg.setProperty(IRainbowHealthProtocol.LOCATION, location);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	/**
	 * Starts remote delegates, without specifying location, since it's not
	 * known at the initialization time of Rainbow.
	 * @return boolean  <code>true</code> if event sending succeeds, <code>false</code> otherwise.
	 */
	public static boolean startDelegates () {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.START.name());
		msg.setProperty(IRainbowHealthProtocol.CLOUD_TYPE, IRainbowHealthProtocol.CloudType.DELEGATE.name());
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	public static boolean stopCloud (String id, String location) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.STOP.name());
		msg.setProperty(IRainbowHealthProtocol.ID, id);
		msg.setProperty(IRainbowHealthProtocol.LOCATION, location);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	public static boolean terminateCloud (String id, String location) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.TERMINATE.name());
		msg.setProperty(IRainbowHealthProtocol.ID, id);
		msg.setProperty(IRainbowHealthProtocol.LOCATION, location);
		msg.setProperty(IRainbowHealthProtocol.EXIT_VALUE, String.valueOf(Rainbow.exitValue()));
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	public static boolean reportCreated (String id,
			IRainbowHealthProtocol.CloudType type, String location, long beaconPer) {

		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_CREATED.name());
		msg.setProperty(IRainbowHealthProtocol.ID, id);
		msg.setProperty(IRainbowHealthProtocol.CLOUD_TYPE, type.name());
		msg.setProperty(IRainbowHealthProtocol.LOCATION, location);
		msg.setProperty(IRainbowHealthProtocol.BEACON_PERIOD, beaconPer);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	public static boolean reportEffectorCreated (String id, String location,
			String svcName, IEffector.Kind kind) {

		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_CREATED.name());
		msg.setProperty(IRainbowHealthProtocol.ID, id);
		msg.setProperty(IRainbowHealthProtocol.CLOUD_TYPE, IRainbowHealthProtocol.CloudType.EFFECTOR.name());
		msg.setProperty(IRainbowHealthProtocol.LOCATION, location);
		msg.setProperty(IRainbowHealthProtocol.SERVICE_NAME, svcName);
		msg.setProperty(IRainbowHealthProtocol.EFFECTOR_KIND, kind.name());
		msg.setProperty(IRainbowHealthProtocol.BEACON_PERIOD, 0L);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	public static boolean reportProbeCreated (String id, String location,
			String svcName, String alias, IProbe.Kind kind) {

		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_CREATED.name());
		msg.setProperty(IRainbowHealthProtocol.ID, id);
		msg.setProperty(IRainbowHealthProtocol.CLOUD_TYPE, IRainbowHealthProtocol.CloudType.PROBE.name());
		msg.setProperty(IRainbowHealthProtocol.LOCATION, location);
		msg.setProperty(IRainbowHealthProtocol.SERVICE_NAME, svcName);
		msg.setProperty(IProbeProtocol.TYPE, alias);
		msg.setProperty(IRainbowHealthProtocol.PROBE_KIND, kind.name());
		msg.setProperty(IRainbowHealthProtocol.BEACON_PERIOD, 0L);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	public static boolean reportDeleted (String id) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.REPORT_DELETED.name());
		msg.setProperty(IRainbowHealthProtocol.ID, id);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	/**
	 * Sends Translator log message so that Rainbow Master can update the
	 * Translator GUI panel.
	 * @param txt  log text
	 * @return boolean  <code>true</code> if send succeeded, <code>false</code> otherwise.
	 */
	public static boolean sendTranslatorLog (String txt) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.LOG_TRANSLATOR.name());
		msg.setProperty(IRainbowHealthProtocol.TEXT, txt);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	/**
	 * Sends EventService log message so that Rainbow Master can update the
	 * Event GUI panel.
	 * @param txt  log text
	 * @return boolean  <code>true</code> if send succeeded, <code>false</code> otherwise.
	 */
	public static boolean sendEventLog (String txt) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.LOG_EVENT.name());
		msg.setProperty(IRainbowHealthProtocol.TEXT, txt);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	public static boolean sendBeacon (String id) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.BEACON.name());
		msg.setProperty(IRainbowHealthProtocol.ID, id);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	public static boolean killProbes (String location) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.KILL_PROBES.name());
		msg.setProperty(IRainbowHealthProtocol.LOCATION, location);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

	public static boolean startProbes (String location) {
		IRainbowMessage msg = EventServiceManager.instance().createMessage(IEventService.TOPIC_RAINBOW_HEALTH);
		msg.setProperty(IProtocolAction.ACTION, IProtocolAction.START_PROBES.name());
		msg.setProperty(IRainbowHealthProtocol.LOCATION, location);
		return EventServiceManager.instance().send(IEventService.TOPIC_RAINBOW_HEALTH, msg);
	}

}
