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
		bundleMap.put(ResourceBundle.getBundle("uniqueTableIdentifier"), UniqueTableIdentifier.class);
	}
}