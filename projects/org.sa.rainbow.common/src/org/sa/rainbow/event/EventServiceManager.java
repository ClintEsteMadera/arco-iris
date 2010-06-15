/**
 * Created June 28, 2006.
 */
package org.sa.rainbow.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.health.RainbowCloudEventHandler;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;


/**
 * An EventServiceManager manages event publish/subscribe/delivery for the
 * Rainbow infrastructure.  The design intent is to decouple Rainbow from any
 * particular event implementation, so that one can choose to use JMS, CEI,
 * RMI, etc., as the underlying "event bus" and delivery mechanism.
 * <p>
 * The implementation currently favors JMS and assumes JBoss provider.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class EventServiceManager {

	private static final String SERVICE_LIST_CLASS = "org.sa.rainbow.event.EventServiceList";
	private static final String SERVICE_LIST_METHOD = "list";
	private static final int IDX_LABEL = 0;
	private static final int IDX_CLASS = 1;
	private static final int IDX_TIMEOUT = 2;
	/** SORTED LIST used to determine what messages to filter out of m_logger traces */
	private static final String[] FILTERED_ACTIONS = {
		IProtocolAction.BEACON.name(),
		IProtocolAction.LOG_EVENT.name(),
		IProtocolAction.LOG_TRANSLATOR.name()
	};

	/** Singleton instance */
	private static EventServiceManager m_instance = null;

	private static class NoOpEventService implements IEventService {
		public boolean initialize () {
			
			return true;
		}
		public void dispose () {
		}
		public boolean isDisposed () {
			return false;
		}
		public void pause () {
		}
		public void resume () {
		}
		public IRainbowMessage createMessage (String topic) {
			return null;
		}
		public void listen (String topic, IRainbowEventListener listener) {
		}
		public void unlisten (IRainbowEventListener listener) {
		}
		public boolean send (String topic, IRainbowMessage msg) {
			return true;
		}
		public boolean readyForTermination() {
			return true;
		}
		public Mode mode () {
			return Mode.NONE;
		}
		public IRainbowMessage importFromString (String topic, String str) {
			return null;
		}
	}

	public static IEventService instance () {
		if (m_instance == null && !Rainbow.shouldTerminate()) {
			m_instance = new EventServiceManager();
			m_instance.initialize();
			RainbowCloudEventHandler.sendEventLog("EventServiceManager initialized!");
		}
		return (m_instance==null) ? null : m_instance.m_eventService;
	}

	public static void dispose () {
		if (m_instance != null && m_instance.m_eventService != null) {
			if (! m_instance.m_eventService.isDisposed()) {
				m_instance.m_eventService.dispose();

				synchronized (m_instance.m_methodMap) {
					m_instance.m_methodMap.clear();
				}
				// null-out data members
				m_instance.m_eventServiceList = null;
				m_instance.m_methodMap = null;
			}
			m_instance = null;
		}
	}

	public static boolean isNotLogFiltered (String action) {
		return Arrays.binarySearch(FILTERED_ACTIONS, action) < 0;
	}

	/**
	 * Returns list of actions from listener's onMessage method annotation.
	 * @param listener  the IRainbowEventListener
	 * @return List     the list of IProtocolActions
	 */
	public static List<IProtocolAction> getAnnotatedActions (IRainbowEventListener listener) {
		List<IProtocolAction> actions = new ArrayList<IProtocolAction>();
		for (Map.Entry<String,Class<?>[]> me : m_instance.m_methodMap.entrySet()) {
			try {
				Method m = listener.getClass().getMethod(me.getKey(), me.getValue());
				if (m != null && m.isAnnotationPresent(EventTypeFilter.class)) {
					// get the EventTypeFilter annotation and extract the type list
					EventTypeFilter filter = (EventTypeFilter )m.getAnnotation(EventTypeFilter.class);
					if (filter != null) {
						Collections.addAll(actions, filter.eventTypes());
					}
				}
			} catch (SecurityException e) {
				m_instance.m_logger.error("Get method violated security!", e);
			} catch (NoSuchMethodException e) {
				m_instance.m_logger.error("Get method failed!", e);
			}
		}
		return actions;
	}

	private RainbowLogger m_logger = null;
	private IEventService m_eventService = null;
	private String[][] m_eventServiceList = null;
	private Map<String,Class<?>[]> m_methodMap = null;

	/**
	 * Non-intantiable (publicly) constructor.
	 *
	 * TODO: Allow reinitialization of EventService when new ones become available while Rainbow is still running?
	 */
	private EventServiceManager () {
		m_logger = RainbowLoggerFactory.logger(getClass());
		// Retrieve list of event services
		try {
			Class<?> listClass = Class.forName(SERVICE_LIST_CLASS);
			Method method = listClass.getMethod(SERVICE_LIST_METHOD, new Class[0]);
			m_eventServiceList = (String[][] )method.invoke(null, new Object[0]);
		} catch (ClassNotFoundException e) {     // ignore
		} catch (SecurityException e) {          // ignore
		} catch (NoSuchMethodException e) {      // ignore 
		} catch (IllegalArgumentException e) {   // ignore
		} catch (IllegalAccessException e) {     // ignore
		} catch (InvocationTargetException e) {  // ignore
		} finally {
			// process error here
			if (m_eventServiceList == null) {
				m_logger.error("Search for Event Service List failed! " + SERVICE_LIST_CLASS);
			}
		}
		// initialize Rainbow listener interface method map
		m_methodMap = new HashMap<String,Class<?>[]>();
		Method[] methods = IRainbowEventListener.class.getMethods();
		for (Method method : methods) {
			m_methodMap.put(method.getName(), method.getParameterTypes());
		}
	}

	private void initialize () {
		// First, look to see if a specific event service has been defined
		String eventSvcProp = Rainbow.property(Rainbow.PROPKEY_EVENT_SERVICE).toUpperCase().intern();
		if (eventSvcProp != null) {  // a specific event service chosen
			for (String[] eList : m_eventServiceList) {
				if (eList[IDX_LABEL] == eventSvcProp) {  // found service to init
					m_logger.debug("EventService designated '" + eventSvcProp + "' and found.");
					try {
						Class<?> eSvcClass = Class.forName(eList[IDX_CLASS]);
						IEventService eSvc = (IEventService )eSvcClass.newInstance();
						if (eSvc.initialize()) {
							m_eventService = eSvc;
							break;
						}
					} catch (ClassNotFoundException e) {  // ignore
					} catch (InstantiationException e) {  // ignore
					} catch (IllegalAccessException e) {  // ignore
					}
				}
			}
		}
		if (m_eventService == null) {
			// Attempt to initialize event services in the given order,
			// stopping at the first availability and success.
			for (int i=0; i < m_eventServiceList.length; ++i) {
				try {
					// if any error with class intantiation, then try another one
					Class<?> eSvcClass = Class.forName(m_eventServiceList[i][IDX_CLASS]);
					final IEventService eSvc = (IEventService )eSvcClass.newInstance();
					// try initializing for at MOST N seconds
					final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
					final ScheduledFuture<Boolean> initHandler = scheduler.schedule(new Callable<Boolean>() {
						public Boolean call() throws Exception {
							return eSvc.initialize();
						}
					}, 0, TimeUnit.SECONDS);
					final String svcName = m_eventServiceList[i][IDX_LABEL];
					final int timeout = Integer.parseInt(m_eventServiceList[i][IDX_TIMEOUT]);
					scheduler.schedule(new Runnable() {
						public void run() {
							if (!scheduler.isShutdown()) {
								m_logger.info("Cancelling init of "+ svcName + "...");
							}
							initHandler.cancel(true);
						}
					}, timeout, TimeUnit.SECONDS);
					Boolean gotIt = null;
					try {
						gotIt = initHandler.get();
					} catch (CancellationException e) {
						// ignore, but attempt to kill scheduler thread
						scheduler.shutdownNow();
					}
					if (gotIt != null) {
						scheduler.shutdown();  // clean up scheduler threads
						if (gotIt.booleanValue()) {
							m_eventService = eSvc;
							break;
						}
					}
				} catch (ClassNotFoundException e) {  // ignore
				} catch (InstantiationException e) {  // ignore
				} catch (IllegalAccessException e) {  // ignore
				} catch (InterruptedException e) {    // ignore
				} catch (ExecutionException e) {      // ignore
				}
			}
		}
		// check if there is _any_ event service
		if (m_eventService == null) {  // uh oh!
			// try final, backup no-op event service
			m_logger.warn("Initializing a no-op Event Service!");
			m_eventService = new NoOpEventService();
			if (!m_eventService.initialize()) {
				m_logger.error("Something seriously wrong! No Event Service could be instantiated!!");
			}
		}
	}

}
