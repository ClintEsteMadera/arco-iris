package ar.uba.dc.thesis.atam.scenario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
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

	private int maxPriority;

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

			this.maxPriority = Math.max(currentScenario.getPriority(), this.maxPriority);
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
		return stimulusPerProperty == null ? new ArrayList<String>(0) : stimulusPerProperty;
	}

	public List<SelfHealingScenario> getScenarios(String stimulus) {
		return this.scenariosMap.get(stimulus);
	}

	public List<SelfHealingScenario> findBrokenScenarios(String stimulus) {
		List<SelfHealingScenario> brokenScenarios = new ArrayList<SelfHealingScenario>();
		List<SelfHealingScenario> scenariosWithStimulus = this.getScenarios(stimulus);

		for (SelfHealingScenario scenario : scenariosWithStimulus) {
			String scenarioStatus = " --> PASSED";
			if (Oracle.instance().defaultScenarioBrokenDetector().isBroken(scenario)) {
				scenarioStatus = " --> BROKEN";
				brokenScenarios.add(scenario);
			}
			log(Level.INFO, scenario.getName() + scenarioStatus + "!");
		}
		return brokenScenarios;
	}

	public Collection<SelfHealingScenario> getEnabledScenarios() {
		return this.selfHealingScenarioRepository.getEnabledScenarios();
	}

	public int getMaxPriority() {
		return maxPriority;
	}

	protected void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeEvaluatorPanel(logger, level, txt, t);
	}
}
