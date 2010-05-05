package org.sa.rainbow.scenario.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.acmestudio.acme.environment.error.AcmeError;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.repository.SelfHealingScenarioRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class ScenariosManager {

	/** Holds a list of Scenarios keyed by their corresponding stimulus name */
	private Map<String, List<SelfHealingScenario>> scenariosMap;

	/**
	 * Holds a list of Stimulus keyed by the property involved in the Response Measure Used for simulation only
	 */
	private Map<String, List<String>> stimulusByPropertyMap;

	private final SelfHealingScenarioRepository selfHealingScenarioRepository;

	private static RainbowLogger logger = RainbowLoggerFactory.logger(ScenariosManager.class);

	/**
	 * Constructor
	 * 
	 * @param selfHealingScenarioRepository
	 * @param acmeModel
	 */
	public ScenariosManager(SelfHealingScenarioRepository selfHealingScenarioRepository) {
		super();
		this.selfHealingScenarioRepository = selfHealingScenarioRepository;
	}

	/**
	 * This method retrieves the enabled scenarios provided by
	 * {@link SelfHealingScenarioRepository#getEnabledScenarios()} and puts them on a map keyed by its stimulus.
	 */
	public Collection<SelfHealingScenario> loadScenarios() {
		Collection<SelfHealingScenario> scenarios = this.selfHealingScenarioRepository.getEnabledScenarios();

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
		}
		return scenarios;
	}

	public void loadStimulusPerProperty(SelfHealingScenario currentScenario, String stimulus) {
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

	public List<String> getStimulus(String qualifiedPropertyNAme) {
		List<String> stimulusPerProperty = this.stimulusByPropertyMap.get(qualifiedPropertyNAme);
		return stimulusPerProperty == null ? Collections.<String> emptyList() : stimulusPerProperty;
	}

	public List<SelfHealingScenario> getScenarios(String stimulus) {
		return this.scenariosMap.get(stimulus);
	}

	public List<SelfHealingScenario> findBrokenScenarios(String stimulus) {
		List<SelfHealingScenario> brokenScenarios = new ArrayList<SelfHealingScenario>();
		List<SelfHealingScenario> scenariosWithStimulus = this.getScenarios(stimulus);

		Stack<AcmeError> collectedErrors = new Stack<AcmeError>();
		for (SelfHealingScenario scenario : scenariosWithStimulus) {
			if (scenario.isBroken(Oracle.instance().rainbowModel())) {
				this.logErrors(collectedErrors, scenario);
				brokenScenarios.add(scenario);
			} else {
				Oracle.instance().writeEvaluatorPanel(logger, scenario.getName() + " pass");
			}
		}
		return brokenScenarios;
	}

	public Collection<SelfHealingScenario> getEnabledScenarios() {
		return this.selfHealingScenarioRepository.getEnabledScenarios();
	}

	private void logErrors(Stack<AcmeError> collectedErrors, SelfHealingScenario scenario) {
		Oracle.instance().writeEvaluatorPanel(logger, scenario.getName() + " broken!");
		while (!collectedErrors.isEmpty()) {
			Oracle.instance().writeEvaluatorPanel(logger, collectedErrors.pop().toString());
		}
	}

}
