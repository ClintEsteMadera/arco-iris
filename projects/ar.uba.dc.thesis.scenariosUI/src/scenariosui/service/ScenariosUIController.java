package scenariosui.service;

import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingScenarioPersister;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.query.BaseCriteria;

/**
 * This class is responsible for controlling the creation, update and opening of a Self Healing Configuration.
 */
public final class ScenariosUIController {

	private static final ScenariosUIController instance = new ScenariosUIController();

	private SelfHealingScenarioPersister persister = new SelfHealingScenarioPersister();

	private String xmlFilePath;

	private SelfHealingConfiguration selfHealingConfiguration;

	private long id = 0L;

	/**
	 * This class is not intended to be instantiated externally.
	 */
	private ScenariosUIController() {
		super();
	}

	public static synchronized ScenariosUIController getInstance() {
		return instance;
	}

	public synchronized void newSelfHealingConfiguration(String aFilePath) {
		SelfHealingConfiguration config = new SelfHealingConfiguration();
		this.persister.saveToFile(config, aFilePath);
		// we only assign these fields if the saving process ended without exceptions.
		this.xmlFilePath = aFilePath;
		this.selfHealingConfiguration = config;
	}

	public synchronized void openSelfHealingConfiguration(String aFilePath) {
		this.selfHealingConfiguration = this.persister.readFromFile(aFilePath);
		this.xmlFilePath = aFilePath;
		this.id = this.getInitialValueForId();
	}

	public synchronized void saveSelfHealingConfiguration() {
		this.persister.saveToFile(this.selfHealingConfiguration, this.xmlFilePath);
	}

	public synchronized void closeSelfHealingConfiguration() {
		this.saveSelfHealingConfiguration();
		this.selfHealingConfiguration = null;
		this.xmlFilePath = null;
		this.id = 0L;
	}

	public synchronized SelfHealingConfiguration getCurrentSelfHealingConfiguration() {
		return this.selfHealingConfiguration;
	}

	public List<SelfHealingScenario> getScenarios(BaseCriteria<SelfHealingScenario> criteria) {
		return this.getElement(this.getCurrentSelfHealingConfiguration().getScenarios(), criteria);
	}

	public List<Environment> getEnvironments(BaseCriteria<Environment> criteria) {
		return this.getElement(this.getCurrentSelfHealingConfiguration().getEnvironments(), criteria);
	}

	/**
	 * Provides unique Long number to be used as identifier for objects.
	 */
	public synchronized Long getNextId() {
		return this.id++;
	}

	/**
	 * Return the recently requested id to the pool since it was not used.
	 */
	public synchronized void returnRecentlyRequestedId() {
		this.id--;
	}

	private long getInitialValueForId() {
		long initialValue = 0;
		List<SelfHealingScenario> scenarios = this.selfHealingConfiguration.getScenarios();

		if (scenarios != null) {
			for (SelfHealingScenario scenario : scenarios) {
				initialValue = Math.max(initialValue, scenario.getId());
			}
		}
		return initialValue == 0 ? initialValue : initialValue + 1;
	}

	private <T extends ThesisPojo> List<T> getElement(List<T> elements, BaseCriteria<T> criteria) {
		if (criteria.getId() == null) {
			return elements;
		} else {
			T environment = this.findElement(elements, criteria.getId());
			return Collections.singletonList(environment);
		}
	}

	private <T extends ThesisPojo> T findElement(List<T> elements, Long id) {
		for (T element : elements) {
			if (element.getId().equals(id)) {
				return element;
			}
		}
		throw new RuntimeException("Unable to find element with id " + id);
	}
}
