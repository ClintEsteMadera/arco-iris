package ar.uba.dc.arcoirisui.service;

import java.io.File;
import java.util.Collections;
import java.util.List;

import ar.uba.dc.arcoirisui.gui.query.RepairStrategySearchCriteria;
import ar.uba.dc.arcoiris.atam.scenario.model.Artifact;
import ar.uba.dc.arcoiris.atam.scenario.model.Environment;
import ar.uba.dc.arcoiris.common.Identifiable;
import ar.uba.dc.arcoiris.selfhealing.SelfHealingScenario;
import ar.uba.dc.arcoiris.selfhealing.StitchLoader;
import ar.uba.dc.arcoiris.selfhealing.StrategyTO;
import ar.uba.dc.arcoiris.selfhealing.config.SelfHealingConfiguration;
import ar.uba.dc.arcoiris.selfhealing.config.persistence.SelfHealingConfigurationPersister;

import commons.query.SearchCriteria;

/**
 * This class is responsible for controlling the persistence of all the concepts involved in this application,
 * especially, instances of {@link Self Healing Configuration}.
 */
public final class ArcoIrisUIManager {

	private static final ArcoIrisUIManager INSTANCE = new ArcoIrisUIManager();

	private final SelfHealingConfigurationPersister persister = new SelfHealingConfigurationPersister();

	private String xmlFilePath;

	private SelfHealingConfiguration selfHealingConfiguration;

	private IdGenerator idGenerator;

	/**
	 * This class is not meant to be instantiated from the outside
	 */
	private ArcoIrisUIManager() {
		super();
	}

	public static ArcoIrisUIManager getInstance() {
		return INSTANCE;
	}

	public void createNewSelfHealingConfiguration(String filePathWhereToWriteTheXMLTo) {
		String fileName = new File(filePathWhereToWriteTheXMLTo).getName();
		String selfHealingConfigDescription = fileName.substring(0, fileName.length() - 4);

		this.selfHealingConfiguration = new SelfHealingConfiguration(selfHealingConfigDescription);
		this.xmlFilePath = filePathWhereToWriteTheXMLTo;
		this.saveSelfHealingConfiguration();
		this.idGenerator = new IdGenerator();
	}

	public void openExistingSelfHealingConfiguration(String filePathToTheXML) {
		this.xmlFilePath = filePathToTheXML;
		this.selfHealingConfiguration = persister.readFromFile(this.xmlFilePath);
		this.idGenerator = new IdGenerator();
	}

	public synchronized void saveSelfHealingConfiguration() {
		persister.saveToFile(this.selfHealingConfiguration, this.xmlFilePath);
	}

	public synchronized void close() {
		this.saveSelfHealingConfiguration();
		this.xmlFilePath = null;
		this.selfHealingConfiguration = null;
		this.idGenerator = null;
	}

	/**
	 * Obtains the next clean id to be used as unique identifier for instances of {@link Identifiable}
	 * 
	 * @param clazz
	 *            the class to provide the unique id for. This is useful in order to contemplate different sequences for
	 *            different classes.
	 * @return an id to be used as unique identifier for instances of {@link Identifiable}
	 */
	public synchronized Long getNextId(Class<? extends Identifiable> clazz) {
		List<? extends Identifiable> identifiables = this.getListOfIdentifiablesAccordingTo(clazz);

		return this.idGenerator.getNextId(clazz, identifiables);
	}

	/**
	 * Return the recently requested id to the pool since it was not used.
	 */
	public synchronized void returnRecentlyRequestedId(Class<? extends Identifiable> clazz) {
		List<? extends Identifiable> identifiables = this.getListOfIdentifiablesAccordingTo(clazz);

		this.idGenerator.returnRecentlyRequestedId(clazz, identifiables);
	}

	private List<? extends Identifiable> getListOfIdentifiablesAccordingTo(Class<? extends Identifiable> clazz) {
		List<? extends Identifiable> identifiables = null;
		if (SelfHealingScenario.class.isAssignableFrom(clazz)) {
			identifiables = this.selfHealingConfiguration.getScenarios();
		} else if (Environment.class.isAssignableFrom(clazz)) {
			identifiables = this.selfHealingConfiguration.getEnvironments();
		} else if (Artifact.class.isAssignableFrom(clazz)) {
			identifiables = this.selfHealingConfiguration.getArtifacts();
		} else {
			throw new RuntimeException("This is what you get when you code using switch statements!!");
		}
		return identifiables;
	}

	public SelfHealingConfiguration getCurrentSelfHealingConfiguration() {
		return this.selfHealingConfiguration;
	}

	public List<SelfHealingScenario> getScenarios(SearchCriteria<SelfHealingScenario> criteria) {
		return this.getElement(this.getCurrentSelfHealingConfiguration().getScenarios(), criteria);
	}

	public List<Environment> getEnvironments(SearchCriteria<Environment> criteria) {
		return this.getElement(this.getCurrentSelfHealingConfiguration().getEnvironments(), criteria);
	}

	public List<Artifact> getArtifacts(SearchCriteria<Artifact> criteria) {
		return this.getElement(this.getCurrentSelfHealingConfiguration().getArtifacts(), criteria);
	}

	public List<StrategyTO> getAllStrategies(RepairStrategySearchCriteria criteria) {
		StitchLoader stitchLoader = new StitchLoader(new File(criteria.getStitchDirectory()), false);
		return stitchLoader.getAllStrategiesTO();
	}

	private <I extends Identifiable> List<I> getElement(List<I> elements, SearchCriteria<I> criteria) {
		if (criteria.getId() == null) {
			return elements;
		} else {
			I environment = this.findElement(elements, criteria.getId());
			return Collections.singletonList(environment);
		}
	}

	private <I extends Identifiable> I findElement(List<I> elements, Long id) {
		for (I element : elements) {
			if (element.getId().equals(id)) {
				return element;
			}
		}
		throw new RuntimeException("Unable to find element with id " + id);
	}
}
