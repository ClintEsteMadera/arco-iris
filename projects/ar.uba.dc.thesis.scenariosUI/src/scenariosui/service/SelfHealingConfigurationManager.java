package scenariosui.service;

import java.util.Collections;
import java.util.List;

import ar.uba.dc.thesis.atam.scenario.model.Artifact;
import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingConfiguration;
import ar.uba.dc.thesis.atam.scenario.persist.SelfHealingScenarioPersister;
import ar.uba.dc.thesis.common.Identifiable;
import ar.uba.dc.thesis.common.ThesisPojo;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

import commons.query.BaseSearchCriteria;
import commons.utils.IdGenerator;

/**
 * This class is responsible for controlling the persistence of a {@link Self Healing Configuration}.
 */
public final class SelfHealingConfigurationManager {

	private static final SelfHealingConfigurationManager INSTANCE = new SelfHealingConfigurationManager();

	private final SelfHealingScenarioPersister persister = new SelfHealingScenarioPersister();

	private String xmlFilePath;

	private SelfHealingConfiguration selfHealingConfiguration;

	private IdGenerator idGenerator;

	/**
	 * This class is not meant to be instantiated from the outside
	 */
	private SelfHealingConfigurationManager() {
		super();
	}

	public static SelfHealingConfigurationManager getInstance() {
		return INSTANCE;
	}

	public void createNewSelfHealingConfiguration(String filePathWhereToWriteTheXMLTo) {
		this.selfHealingConfiguration = new SelfHealingConfiguration();
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

	public List<SelfHealingScenario> getScenarios(BaseSearchCriteria<SelfHealingScenario> criteria) {
		return this.getElement(this.getCurrentSelfHealingConfiguration().getScenarios(), criteria);
	}

	public List<Environment> getEnvironments(BaseSearchCriteria<Environment> criteria) {
		return this.getElement(this.getCurrentSelfHealingConfiguration().getEnvironments(), criteria);
	}

	public List<Artifact> getArtifacts(BaseSearchCriteria<Artifact> criteria) {
		return this.getElement(this.getCurrentSelfHealingConfiguration().getArtifacts(), criteria);
	}

	private <T extends ThesisPojo> List<T> getElement(List<T> elements, BaseSearchCriteria<T> criteria) {
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
