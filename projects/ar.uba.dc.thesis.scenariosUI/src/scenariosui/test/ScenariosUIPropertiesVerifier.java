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
 * $Id: EmesPropertiesVerifier.java,v 1.8 2008/02/29 20:36:20 cvschioc Exp $
 */

package scenariosui.test;

import java.util.ResourceBundle;

import scenariosui.properties.ScenariosUILabels;
import scenariosui.properties.UniqueTableIdentifier;

import commons.properties.Messages;
import commons.test.PropertiesVerifier;

/**
 * Verifica que todas las propiedades (incluyendo las commons, definidas en la librer�a) existan en ambos sentidos; esto
 * es, que todos los labels en los archivos properties est�n definidos en las respectivas clases Enumeradas y que las
 * constantes definidas en estas �ltimas, est�n definidas en los properties.
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
					+ " est�n sincronizados con sus respectivos archivos '.properties'");
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