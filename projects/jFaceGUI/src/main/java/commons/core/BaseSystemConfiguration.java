package commons.core;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseSystemConfiguration extends Properties {

	private static final String DEV_ENVIRONMENT = "DEV_ENVIRONMENT";

	private static final String DEV_USER = "DEV_USER";

	private static final String DEV_PASSWORD = "DEV_PASSWORD";

	private static final String SHOW_STACK_TRACES_FOR_ERRORS = "SHOW_STACK_TRACES_FOR_ERRORS";

	private static final Log log = LogFactory.getLog(BaseSystemConfiguration.class);

	private static final long serialVersionUID = 1L;

	protected BaseSystemConfiguration() {
		super();
		try {
			this.load(getClass().getResourceAsStream("/configuration.properties"));
		} catch (Exception e) {
			log.fatal("Error when loading system configuration. " + "Using default configuration...");
			this.initDefaultValues();
		}
	}

	public boolean developmentEnvironment() {
		return getBoolean(DEV_ENVIRONMENT);
	}

	public String developmentUser() {
		return getProperty(DEV_USER);
	}

	public String developmentPassword() {
		return getProperty(DEV_PASSWORD);
	}

	public boolean showStackTraceForErrors() {
		return getBoolean(SHOW_STACK_TRACES_FOR_ERRORS);
	}

	protected Boolean getBoolean(String property) {
		return Boolean.valueOf(getProperty(property));
	}

	/**
	 * Set default values for the properties.
	 */
	protected void initDefaultValues() {
		setProperty(DEV_ENVIRONMENT, Boolean.FALSE.toString());
		setProperty(DEV_USER, "");
		setProperty(DEV_PASSWORD, "");
		setProperty(SHOW_STACK_TRACES_FOR_ERRORS, Boolean.FALSE.toString());
	}
}