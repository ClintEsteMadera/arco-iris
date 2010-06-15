/**
 * Created January 15, 2007.
 */
package org.sa.rainbow.util;


/**
 * This class instantiates a logger service for Rainbow, defaulting to a no-op
 * logger, which can be replaced by an implementation package.
 * 
 * This pattern is copied from Nicholas Sherman's implementation for Acme.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class RainbowLoggerFactory {

    private static RainbowLoggerFactory m_loggerFactory = null;
    static {
        m_loggerFactory = new NoOpRainbowLoggerFactory();
    }
    public static RainbowLogger logger (Class<?> c) {
        return m_loggerFactory.factoryGetLogger(c);
    }
    public static RainbowLogger logger (String name) {
        return m_loggerFactory.factoryGetLogger(name);
    }

    protected abstract RainbowLogger factoryGetLogger (Class<?> c);
    protected abstract RainbowLogger factoryGetLogger (String name);

    public static class NoOpRainbowLoggerFactory extends RainbowLoggerFactory {
        RainbowLogger.NoOpRainbowLogger logger = new RainbowLogger.NoOpRainbowLogger();
        
        protected RainbowLogger factoryGetLogger (Class<?> c) {
            return logger;
        }
        protected RainbowLogger factoryGetLogger (String name) {
            return logger;
        }
    }
    public static void setLoggerFactory (RainbowLoggerFactory factory) {
        m_loggerFactory = factory;
    }

}
