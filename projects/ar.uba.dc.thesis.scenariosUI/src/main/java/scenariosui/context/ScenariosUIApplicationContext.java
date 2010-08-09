package scenariosui.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import scenariosui.service.ScenariosUISystemConfiguration;

import commons.context.BaseApplicationContext;

public class ScenariosUIApplicationContext extends BaseApplicationContext {

	/**
	 * Implementación obligatoria del método de la superclase.
	 * 
	 * @see BaseApplicationContext#getEmesTypes()
	 */
	public static ScenariosUIApplicationContext getInstance() {
		if (scenariosUIContext == null) {
			try {
				scenariosUIContext = new ScenariosUIApplicationContext();
			} catch (Exception ex) {
				log.fatal(ERROR_MSG, ex);
				throw new RuntimeException(ERROR_MSG, ex);
			}
		}
		return scenariosUIContext;
	}

	@Override
	public ScenariosUISystemConfiguration getSystemConfiguration() {
		return systemConfiguration;
	}

	/**
	 * Constructor privado.
	 */
	private ScenariosUIApplicationContext() {
		super();
	}

	private static ScenariosUIApplicationContext scenariosUIContext;

	private static final Log log = LogFactory.getLog(ScenariosUIApplicationContext.class);

	private static final String ERROR_MSG = "No se pudo crear el application emesContext";

	private static ScenariosUISystemConfiguration systemConfiguration = new ScenariosUISystemConfiguration();

	static {
		locations.addAll(systemConfiguration.getServicesDescriptorNames());
	}
}