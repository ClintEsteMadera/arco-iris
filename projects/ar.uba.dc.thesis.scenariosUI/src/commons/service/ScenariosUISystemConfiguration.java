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
 * $Id: EmesSystemConfiguration.java,v 1.1 2008/04/23 19:15:33 cvschioc Exp $
 */
package commons.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import sba.common.core.BaseSystemConfiguration;

public class ScenariosUISystemConfiguration extends BaseSystemConfiguration {

	public List<String> getServicesDescriptorNames() {
		List<String> result = new ArrayList<String>();
		//result.add("spring/service-client/lala-service.xml");

		String backendDescriptors = this.getProperty(BACKEND_DESCRIPTORS);

		if (backendDescriptors != null) {
			StringTokenizer st = new StringTokenizer(backendDescriptors, ",");
			while (st.hasMoreTokens()) {
				result.add(st.nextToken());
			}
		}

		return result;
	}

	private static final long serialVersionUID = 1L;

	/*
	 * NO cambiar el nombre de las siguientes propiedades ya que se usan para el proceso de build
	 */
	private static final String BACKEND_DESCRIPTORS = "BACKEND_DESCRIPTORS";
}