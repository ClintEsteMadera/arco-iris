package scenariosui.service;

import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingScenarioPersister;

/**
 * This class is responsible for controlling the creation, update and opening of a Self Healing Configuration.
 */
public final class ScenariosUIController {

	private static SelfHealingScenarioPersister persister = new SelfHealingScenarioPersister();

	private static String xmlFilePath;

	private static SelfHealingConfiguration selfHealingConfiguration;

	private static long id = 0L;

	/**
	 * This class is not intended to be instantiated
	 */
	private ScenariosUIController() {
		super();
	}

	public static void newSelfHealingConfiguration(String aFilePath) {
		SelfHealingConfiguration config = new SelfHealingConfiguration();
		persister.saveToFile(config, aFilePath);

		// we only assign these fields if the saving process ended without exceptions.
		xmlFilePath = aFilePath;
		selfHealingConfiguration = config;
	}

	public static void openSelfHealingConfiguration(String aFilePath) {
		selfHealingConfiguration = persister.readFromFile(aFilePath);
		xmlFilePath = aFilePath;
	}

	public static void saveSelfHealingConfiguration() {
		persister.saveToFile(selfHealingConfiguration, xmlFilePath);
	}

	public static SelfHealingConfiguration getCurrentSelfHealingConfiguration() {
		return selfHealingConfiguration;
	}

	public static boolean isThereAConfigurationOpen() {
		return xmlFilePath != null;
	}

	/**
	 * Provides unique Long number to be used as identifier for objects.
	 */
	public static synchronized Long getNextId() {
		return id++;
	}
}
