package ar.uba.dc.thesis.atam.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Level;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.atam.scenario.model.Environment;
import ar.uba.dc.thesis.repository.SelfHealingConfigurationRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class SelfHealingConfigurationManager {

	/** Holds a list of Scenarios keyed by their corresponding stimulus name */
	private Map<String, List<SelfHealingScenario>> scenariosMap;

	/**
	 * Holds a list of Stimulus keyed by the property involved in the Response Measure Used for simulation only
	 */
	private Map<String, List<String>> stimulusByPropertyMap;

	private final SelfHealingConfigurationRepository selfHealingConfigurationRepository;

	private int maxPriority;

	private static RainbowLogger logger = RainbowLoggerFactory.logger(SelfHealingConfigurationManager.class);

	/**
	 * Reserved keyword for scenario's stimulus.
	 */
	private static final String ANY_STIMULUS_KEYWORD = "ANY";

	/**
	 * Constructor
	 * 
	 * @param selfHealingConfigurationRepository
	 * @param acmeModel
	 */
	public SelfHealingConfigurationManager(SelfHealingConfigurationRepository selfHealingConfigurationRepository) {
		super();
		this.selfHealingConfigurationRepository = selfHealingConfigurationRepository;
		this.loadScenarios();
	}

	public List<String> getStimulus(String qualifiedPropertyNAme) {
		List<String> stimulusPerProperty = this.stimulusByPropertyMap.get(qualifiedPropertyNAme);
		return stimulusPerProperty == null ? new ArrayList<String>(0) : stimulusPerProperty;
	}

	public List<SelfHealingScenario> getScenarios(String stimulus) {
		List<SelfHealingScenario> result = new ArrayList<SelfHealingScenario>(this.scenariosMap.get(stimulus));
		if (!ANY_STIMULUS_KEYWORD.equals(stimulus) && this.scenariosMap.get(ANY_STIMULUS_KEYWORD) != null) {
			result.addAll(this.scenariosMap.get(ANY_STIMULUS_KEYWORD));
		}
		return result;
	}

	public List<SelfHealingScenario> findBrokenScenarios(String stimulus) {
		log(Level.INFO, "Finding broken scenarios...");

		List<SelfHealingScenario> brokenScenarios = new ArrayList<SelfHealingScenario>();
		List<SelfHealingScenario> scenariosWithStimulus = this.getScenarios(stimulus);

		for (SelfHealingScenario scenario : scenariosWithStimulus) {
			log(Level.INFO, "Is scenario " + scenario.getName() + " broken?");
			String scenarioStatus = " --> PASSED";
			if (Oracle.instance().defaultScenarioBrokenDetector().isBroken(scenario)) {
				scenarioStatus = " --> BROKEN";
				brokenScenarios.add(scenario);
			}
			log(Level.INFO, scenario.getName() + scenarioStatus + "!");
		}
		log(Level.INFO, "END Finding broken scenarios!");
		return brokenScenarios;
	}

	public Collection<SelfHealingScenario> getEnabledScenarios() {
		return this.selfHealingConfigurationRepository.getEnabledScenarios();
	}

	public int getMaxPriority() {
		return maxPriority;
	}

	public Collection<Environment> getAllNonDefaultEnvironments() {
		return this.selfHealingConfigurationRepository.getAllNonDefaultEnvironments();
	}

	public Environment getDefaultEnvironment() {
		return this.selfHealingConfigurationRepository.getDefaultEnvironment();
	}

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEvaluatorPanel(logger, level, txt, t);
	}

	/**
	 * This method retrieves the enabled scenarios provided by
	 * {@link SelfHealingConfigurationRepository#getEnabledScenarios()} and puts them on a map keyed by its stimulus.
	 */
	private Collection<SelfHealingScenario> loadScenarios() {
		Collection<SelfHealingScenario> scenarios = this.selfHealingConfigurationRepository.getEnabledScenarios();

		this.scenariosMap = new HashMap<String, List<SelfHealingScenario>>();
		this.stimulusByPropertyMap = new HashMap<String, List<String>>();

		for (SelfHealingScenario currentScenario : scenarios) {
			String stimulus = currentScenario.getStimulus();
			List<SelfHealingScenario> scenarioList;

			loadStimulusPerProperty(currentScenario, stimulus);

			if (this.scenariosMap.containsKey(stimulus)) {
				scenarioList = this.scenariosMap.get(stimulus);
			} else {
				scenarioList = new ArrayList<SelfHealingScenario>();
				this.scenariosMap.put(stimulus, scenarioList);
			}
			scenarioList.add(currentScenario);

			this.maxPriority = Math.max(currentScenario.getPriority(), this.maxPriority);

			// if the user does not specify any repair strategy, we consider all available ones
			if (CollectionUtils.isEmpty(currentScenario.getRepairStrategies())) {
				String[] allRepairStrategies = Oracle.instance().stitchLoader().getAllStrategyNames().toArray(
						new String[] {});
				currentScenario.addRepairStrategy(allRepairStrategies);
			}
		}

		return scenarios;
	}

	private void loadStimulusPerProperty(SelfHealingScenario currentScenario, String stimulus) {
		List<String> stimulusPerScenarioList;
		String responseMeasureProperty = currentScenario.getResponseMeasure().getConstraint()
				.getFullyQualifiedPropertyName();

		if (this.stimulusByPropertyMap.containsKey(responseMeasureProperty)) {
			stimulusPerScenarioList = this.stimulusByPropertyMap.get(responseMeasureProperty);
		} else {
			stimulusPerScenarioList = new ArrayList<String>();
			stimulusByPropertyMap.put(responseMeasureProperty, stimulusPerScenarioList);
		}
		stimulusPerScenarioList.add(stimulus);
	}

}
