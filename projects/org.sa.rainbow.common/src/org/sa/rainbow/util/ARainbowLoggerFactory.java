/**
 * Created January 15, 2007.
 */
package org.sa.rainbow.util;

import java.util.WeakHashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * A substantive Logger service factory.
 * 
 * This class handles default values and initialization of the underlying log4j.
 * It stores a, perhaps redundant, instance of the resulting RainbowLogger.
 * 
 * The Util class stores an instance of the logger instance because all other
 * classes can access it for the logger service.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class ARainbowLoggerFactory extends RainbowLoggerFactory {

    /**
     * Initializes the debug object, setting up the debug file writer. The debug
     * file defaults to ${AcmeStudio.debug.filename}, which is specified in the
     * plugin resource file.
     */
    private static void initialize () {
        RainbowLoggerFactory.setLoggerFactory(new ARainbowLoggerFactory());
        PropertyConfigurator.configure(Util.defineLoggerProperties());
    }

    /**
     * Re-initializes debug object by resetting the Logger.
     */
    public static void reset () {
        LogManager.shutdown ();
        initialize();
    }


	private WeakHashMap<Logger,ARainbowLogger> m_loggerMap = new WeakHashMap<Logger,ARainbowLogger>();

	/* (non-Javadoc)
	 * @see org.sa.rainbow.util.RainbowLoggerFactory#factoryGetLogger(java.lang.Class)
	 */
	@Override
	protected RainbowLogger factoryGetLogger (Class<?> c) {
		Logger logger = Logger.getLogger(c);
		return internalGetLogger(logger);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.util.RainbowLoggerFactory#factoryGetLogger(java.lang.String)
	 */
	@Override
	protected RainbowLogger factoryGetLogger (String name) {
		Logger logger = Logger.getLogger(name);
		return internalGetLogger(logger);
	}

	private RainbowLogger internalGetLogger (Logger logger) {
		ARainbowLogger rainbowLogger = m_loggerMap.get(logger);
		if(rainbowLogger == null)
			m_loggerMap.put(logger, rainbowLogger = new ARainbowLogger(logger));
		return rainbowLogger;
	}

	class ARainbowLogger extends RainbowLogger {
		private Logger m_logger = null;
		
		public ARainbowLogger (Logger logger) {
			m_logger = logger;
		}

		@Override
		public void fatal (Object message, Throwable t) {
			m_logger.fatal(message,t);
		}
		@Override
		public void fatal (Object message) {
			m_logger.fatal(message);
		}

		@Override
		public void error (Object message, Throwable t) {
			m_logger.error(message,t);
		}
		@Override
		public void error (Object message) {
			m_logger.error(message);
		}

		@Override
		public void warn (Object message, Throwable t) {
			m_logger.warn(message,t);
		}
		@Override
		public void warn (Object message) {
			m_logger.warn(message);
		}
		
		@Override
		public void info (Object message, Throwable t) {
			m_logger.info(message,t);
		}
		@Override
		public void info (Object message) {
			m_logger.info(message);
		}
		@Override
		public boolean isInfoEnabled() {
			return m_logger.isInfoEnabled();
		}

		@Override
		public void debug (Object message, Throwable t) {
			m_logger.debug(message,t);
		}
		@Override
		public void debug (Object message) {
			m_logger.debug(message);
		}
		@Override
		public boolean isDebugEnabled() {
			return m_logger.isDebugEnabled();
		}

		@Override
		public void trace (Object message, Throwable t) {
			m_logger.trace(message,t);
		}
		@Override
		public void trace (Object message) {
			m_logger.trace(message);
		}
		@Override
		public boolean isTraceEnabled() {
			return m_logger.isTraceEnabled();
		}

	}

}
