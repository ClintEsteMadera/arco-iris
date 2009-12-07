/*
 * Licencia de Caja de Valores S.A., Versi�n 1.0
 *
 * Copyright (c) 2006 Caja de Valores S.A.
 * 25 de Mayo 362, Ciudad Aut�noma de Buenos Aires, Rep�blica Argentina
 * Todos los derechos reservados.
 *
 * Este software es informaci�n confidencial y propietaria de Caja de Valores S.A. ("Informaci�n
 * Confidencial"). Usted no divulgar� tal Informaci�n Confidencial y la usar� solamente de acuerdo a
 * los t�rminos del acuerdo de licencia que posee con Caja de Valores S.A.
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