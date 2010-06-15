/**
 * Created November 28, 2006.
 */
package org.sa.rainbow.event;

import org.sa.rainbow.core.IDisposable;

/**
 * Public interface to the Rainbow Event Service providers.
 * Currently, two are implemented, a file-based, and a JMS-based event service.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public interface IEventService extends IDisposable {

	/**
	 * The Mode of the event service, mainly to distinguish distributed event-
	 * (message) based vs. single-process queue-based
	 */
	public enum Mode {
		MESSAGE, QUEUE, NONE
	}

	public static final String TOPIC_PROBE_BUS = "topic/probeBus";
	public static final String TOPIC_GAUGE_BUS = "topic/gaugeBus";
	public static final String TOPIC_EFFECTOR_BUS = "topic/effectorBus";
	public static final String TOPIC_RAINBOW_HEALTH = "topic/rainbowHealth";
	public static final String[] TOPIC_LIST = {
		TOPIC_PROBE_BUS, TOPIC_GAUGE_BUS, TOPIC_EFFECTOR_BUS, TOPIC_RAINBOW_HEALTH
	};

	/**
	 * Attempts initialization of the event service, and returns false if the
	 * initialization fails.
	 * @return boolean  <code>true</code> if initialization succeeds, <code>false</code> otherwise.
	 */
	public boolean initialize ();

	/**
	 * Pauses the Event Service so that no new event delivery is made.
	 * New events published are simply queued until the Event Service is
	 * resumed.
	 */
	public void pause ();

	/**
	 * Resumes the Event Service; all queued events are sent to subscribers.
	 */
	public void resume ();

	public IRainbowMessage createMessage (String topic);

	public boolean send (String topic, IRainbowMessage msg);

	public void listen (String topic, final IRainbowEventListener listener);

	public void unlisten (IRainbowEventListener listener);

	/**
	 * Returns whether the event service is ready to be terminated.
	 * @return boolean  <code>true</code> if the event service can be terminated, <code>false</code> otherwise
	 */
	public boolean readyForTermination ();

	public Mode mode ();

	/**
	 * Imports a single line of string to an IRainbowMessage.  Each line of
	 * input contains a number of key-value pairs, in the form:
	 *   (key = value;)*{newline}
	 * @param topic  the topic for which to create message
	 * @param str  the string to import from
	 * @return IRainbowMessage  the newly created IRainbowMessage object.
	 */
	public IRainbowMessage importFromString (String topic, String str);

}
