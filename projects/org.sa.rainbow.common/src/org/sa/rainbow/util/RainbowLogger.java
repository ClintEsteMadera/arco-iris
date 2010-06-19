/**
 * Created January 15, 2007.
 */
package org.sa.rainbow.util;

import org.apache.log4j.Level;


/**
 * This class defines the methods of the Logger service for Rainbow.
 * This code is copied from Nicholas Sherman's implementation for Acme.
 *
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public abstract class RainbowLogger {

	/**
	 * We add this method to be able to specify the log level from the caller.
	 *
	 * @param level
	 *            the desired log level
	 * @param message
	 *            the message to log
	 */
	public abstract void log(Level level, String message);

	public abstract void log(Level level, String message, Throwable t);

    public abstract void trace(Object message);
    public abstract void trace(Object message, Throwable t);
    public abstract boolean isTraceEnabled ();
    public abstract void debug(Object message);
    public abstract void debug(Object message, Throwable t);
    public abstract boolean isDebugEnabled ();
    public abstract void info(Object message);
    public abstract void info(Object message, Throwable t);
    public abstract boolean isInfoEnabled ();
    public abstract void warn(Object message);
    public abstract void warn(Object message, Throwable t);
    public abstract void error(Object message);
    public abstract void error(Object message, Throwable t);
    public abstract void fatal(Object message);
    public abstract void fatal(Object message, Throwable t);

    public static class NoOpRainbowLogger extends RainbowLogger {

    	@Override
    	public void log(Level level, String message) {
    	}

    	@Override
    	public void log(Level level, String message, Throwable t) {
    	}

        @Override
        public void trace(Object message, Throwable t) {
        }
        @Override
        public void trace(Object message) {
        }
		@Override
		public boolean isTraceEnabled() {
			return false;
		}

		@Override
        public void debug(Object message, Throwable t) {
        }
        @Override
        public void debug(Object message) {
        }
		@Override
		public boolean isDebugEnabled() {
			return false;
		}

		@Override
        public void info(Object message, Throwable t) {
        }
        @Override
        public void info(Object message) {
        }
		@Override
		public boolean isInfoEnabled() {
			return false;
		}

		@Override
        public void warn(Object message, Throwable t) {
        }
        @Override
        public void warn(Object message) {
        }

        @Override
        public void error(Object message, Throwable t) {
        }
        @Override
        public void error(Object message) {
        }

        @Override
        public void fatal(Object message, Throwable t) {
        }
        @Override
        public void fatal(Object message) {
        }

    }

}
