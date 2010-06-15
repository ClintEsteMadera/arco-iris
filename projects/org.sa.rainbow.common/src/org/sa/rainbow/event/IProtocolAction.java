/**
 * Created February 9, 2007.
 */
package org.sa.rainbow.event;

/**
 * Common enum for all event protocol actions, allowing common reference to
 * these actions, for use by {@link EventTypeFilter}.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public enum IProtocolAction {

// Common Actions
	BEACON,

// Rainbow HEALTH Protocol Actions
	START, STOP, RESTART, TERMINATE,
	REPORT_CREATED, REPORT_DELETED,
	LOG_TRANSLATOR, LOG_EVENT,
	KILL_PROBES, START_PROBES,

// EFFECTOR Protocol Actions
	EXECUTE,
	REPORT_OUTCOME,

// GAUGE-BUS Protocl Actions
	CREATE,
	DELETE,
	CONFIGURE, REPORT_CONFIGURED,
	REPORT_VALUE, REPORT_MULTIVALUES,
	SUBSCRIBE, UNSUBSCRIBE,
	/** Establish connection to a Gauge Manager of type */
	CONNECT,
	/** Query actions */
	QUERY_VALUE, QUERY_ALLVALUES, QUERY_GAUGESTATE, QUERY_GAUGEMETAINFO,

// PROBE-BUS Protocol Actions
	FIND, FOUND, REPORT,
	/** Action sent to transition a Probe's lifecycle to another phase.
	 */
	LIFECYCLE,
	/* CONFIGURE (shared action on Gauge Bus) */
	/** Action sent by a Gauge to indicate to ProbeBusRelay what Probe should
	 *  be associated with that Gauge.
	 */
	MAP2GAUGE;

	public static final String ACTION = "action";
	public static final String SEND_ALL_LOCATION = "*";

	/**
	 * Returns whether the supplied Action is a report action.
	 * @param action  the Action instance to check
	 * @return <code>true</code> if Action is a REPORT_*, <code>false</code> otherwise.
	 */
	public static boolean isReport (IProtocolAction action) {
		return action == REPORT_CREATED
			|| action == REPORT_DELETED
			|| action == REPORT_CONFIGURED
			|| action == REPORT_VALUE
			|| action == REPORT_MULTIVALUES;
	}

	/**
	 * Returns whether the supplied Action is a query action.
	 * @param action  the Action instance to check
	 * @return <code>true</code> if Action is a QUERY_*, <code>false</code> otherwise.
	 */
	public static boolean isQuery (IProtocolAction action) {
		return action == QUERY_VALUE
			|| action == QUERY_ALLVALUES
			|| action == QUERY_GAUGESTATE
			|| action == QUERY_GAUGEMETAINFO;
	}

	/**
	 * Returns whether the supplied Action is a Rainbow Health Lifecycle action.
	 * @param action  the Action instance to check
	 * @return <code>true</code> if Action is a Rainbow Health Lifecycle action,
	 *         <code>false</code> otherwise.
	 */
	public static boolean isHealthLifecycle (IProtocolAction action) {
		return action == START
			|| action == STOP
			|| action == RESTART
			|| action == TERMINATE;
	}

}
