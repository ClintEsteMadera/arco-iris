package scenariosui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import commons.core.BaseSystemConfiguration;

public class ScenariosUISystemConfiguration extends BaseSystemConfiguration {

	private static final long serialVersionUID = 1L;

	/*
	 * NO cambiar el nombre de las siguientes propiedades ya que se usan para el proceso de build
	 */
	private static final String BACKEND_DESCRIPTORS = "BACKEND_DESCRIPTORS";

	public List<String> getServicesDescriptorNames() {
		List<String> result = new ArrayList<String>();
		// result.add("spring/service-client/lala-service.xml");

		String backendDescriptors = this.getProperty(BACKEND_DESCRIPTORS);

		if (backendDescriptors != null) {
			StringTokenizer st = new StringTokenizer(backendDescriptors, ",");
			while (st.hasMoreTokens()) {
				result.add(st.nextToken());
			}
		}

		return result;
	}
}