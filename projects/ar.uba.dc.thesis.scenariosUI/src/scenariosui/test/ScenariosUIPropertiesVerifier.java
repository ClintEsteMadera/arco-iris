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
 * $Id: EmesPropertiesVerifier.java,v 1.8 2008/02/29 20:36:20 cvschioc Exp $
 */

package scenariosui.test;

import java.util.ResourceBundle;

import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.UniqueTableIdentifier;

import commons.properties.Messages;
import commons.test.PropertiesVerifier;

/**
 * Verifica que todas las propiedades (incluyendo las commons, definidas en la librería) existan en ambos sentidos; esto
 * es, que todos los labels en los archivos properties estén definidos en las respectivas clases Enumeradas y que las
 * constantes definidas en estas últimas, estén definidas en los properties.
 * 
 * @author Jonathan Chiocchio
 * @version $Revision: 1.8 $ $Date: 2008/02/29 20:36:20 $
 */

public class ScenariosUIPropertiesVerifier extends PropertiesVerifier {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		boolean allPropsAreInPropertiesFiles = testAllPropertiesAreDefined();
		boolean allDefinedPropsAreInEnums = testAllDefinedPropsAreInEnums();
		boolean thereAreNoRepetitions = testThereAreNoRepetitions(UniqueTableIdentifier.class);

		if (allPropsAreInPropertiesFiles && allDefinedPropsAreInEnums) {
			System.out.println("* Todos los EnumProperties registrados en este test"
					+ " están sincronizados con sus respectivos archivos '.properties'");
		}
		if (thereAreNoRepetitions) {
			System.out.println("* No hay repeticiones entre constantes de distintos EnumProperties");
		}
	}

	static {
		bundleMap.put(ResourceBundle.getBundle("scenariosUI_labels"), ScenariosUILabels.class);
		bundleMap.put(ResourceBundle.getBundle("messages"), Messages.class);
		bundleMap.put(ResourceBundle.getBundle("tableconstants"), UniqueTableIdentifier.class);
	}
}