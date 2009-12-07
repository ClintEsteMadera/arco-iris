/*
 * Licencia de Caja de Valores S.A., Versión 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Autónoma de Buenos Aires, República Argentina
 * Todos los derechos reservados.
 *
 * Este software es información confidencial y propietaria de Caja de Valores S.A. ("Información
 * Confidencial"). Usted no divulgará tal Información Confidencial y la usará solamente de acuerdo a
 * los términos del acuerdo de licencia que posee con Caja de Valores S.A.
 */

/*
 * $Id: EmesApplicationContext.java,v 1.12 2008/04/23 19:15:34 cvschioc Exp $
 */
package commons.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import commons.service.ScenariosUISystemConfiguration;

/**
 * @author Jonathan Chiocchio
 * @version $Revision: 1.12 $ $Date: 2008/04/23 19:15:34 $
 */
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