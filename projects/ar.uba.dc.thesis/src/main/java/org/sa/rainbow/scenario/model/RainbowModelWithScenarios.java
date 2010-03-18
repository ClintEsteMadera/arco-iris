package org.sa.rainbow.scenario.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.acmestudio.acme.element.IAcmeComponent;
import org.acmestudio.acme.element.IAcmeSystem;
import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.acme.element.property.IAcmePropertyValue;
import org.acmestudio.acme.environment.error.AcmeError;
import org.acmestudio.acme.model.command.IAcmeCommand;
import org.acmestudio.acme.type.verification.SimpleModelTypeChecker;
import org.acmestudio.acme.type.verification.TypeCheckingState;
import org.acmestudio.basicmodel.element.AcmeComponentType;
import org.acmestudio.basicmodel.element.AcmeDesignRule;
import org.acmestudio.basicmodel.model.AcmeModel;
import org.acmestudio.standalone.resource.StandaloneLanguagePackHelper;
import org.sa.rainbow.adaptation.AdaptationManagerWithScenarios;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.util.Pair;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.rainbow.constraint.Constraint;
import ar.uba.dc.thesis.repository.SelfHealingScenarioRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class RainbowModelWithScenarios extends RainbowModel {

	private static RainbowLogger logger = RainbowLoggerFactory.logger(RainbowModelWithScenarios.class);

	/** Holds a list of Scenarios keyed by their corresponding stimulus name */
	private Map<String, List<SelfHealingScenario>> scenariosMap;

	/**
	 * Holds a list of Stimulus keyed by the property involved in the Response Measure Used for simulation only
	 */
	private Map<String, List<String>> stimulusPerProperty;

	private boolean isPropertyUpdateAllowed;

	private final Queue<Pair<String, Object>> propertiesToUpdateQueue;

	public RainbowModelWithScenarios() {
		super();
		Collection<SelfHealingScenario> enabledScenarios = this.loadScenarios();
		this.addAcmeRulesToScenarios(enabledScenarios);
		this.isPropertyUpdateAllowed = true;
		this.propertiesToUpdateQueue = new LinkedList<Pair<String, Object>>();
		Oracle.instance().writeManagerPanel(logger, "Rainbow Model With Scenarios started");
	}

	/**
	 * Updates one property as a result of a stimulus invocation.
	 * 
	 * @param type
	 *            the name of the property to update.
	 * @param value
	 *            the value to put on the property.
	 * @param stimulus
	 *            the stimulus who caused the update.
	 */
	public void updateProperty(String type, Object value, String stimulus) {
		this.updateProperty(type, value);
		this.isPropertyUpdateAllowed = false;
		AdaptationManagerWithScenarios adaptationManager = (AdaptationManagerWithScenarios) Oracle.instance()
				.adaptationManager();
		List<SelfHealingScenario> brokenScenarios;
		// done this way in order to avoid the same else block twice
		if (!adaptationManager.adaptationInProgress()
				&& !(brokenScenarios = this.findBrokenScenarios(stimulus)).isEmpty()) {
			// pass control to adaptation manager, it will be responsible for turning the flag (isPropertyUpdateAllowed)
			// on again...
			adaptationManager.triggerAdaptation(brokenScenarios);
		} else {
			// Let everyone update properties since we are not doing anything now...
			this.isPropertyUpdateAllowed = true;
		}
	}

	/**
	 * Overrides superclass' behavior in order to avoid subsequent properties updates when a stimulus has been invoked.
	 */
	@Override
	public synchronized void updateProperty(String property, Object value) {
		if (this.isPropertyUpdateAllowed) {
			if (m_acme == null)
				return;
			Object obj = m_acme.findNamedObject(m_acme, property);
			if (obj instanceof IAcmeProperty) {
				IAcmeProperty prop = (IAcmeProperty) obj;
				if (logger.isDebugEnabled())
					logger.debug("Upd prop: " + prop.getQualifiedName() + " = " + value.toString());
				try {
					IAcmePropertyValue pVal = StandaloneLanguagePackHelper.defaultLanguageHelper()
							.propertyValueFromString(value.toString(), null);
					IAcmeCommand<?> cmd = m_acmeSys.getCommandFactory().propertyValueSetCommand(prop, pVal);
					cmd.execute();

					// We specifically do not want to turn this flag on, since we do not want Rainbow's usual behavior
					// to interfere with our way of doing things.
					// m_propChanged = true;

					if (pVal.getType() != null) {
						if (pVal.getType().getName().equals("float") || pVal.getType().getName().equals("int")) {
							// update exponential average
							updateExponentialAverage(property, Double.parseDouble(value.toString()));
						}
					}
				} catch (Exception e) {
					logger.error("Acme Command execution failed!", e);
				}
			}
		} else {
			this.propertiesToUpdateQueue.add(new Pair<String, Object>(property, value));
		}
	}

	public boolean isPropertyUpdateAllowed() {
		return isPropertyUpdateAllowed;
	}

	public void setPropertyUpdateAllowed(boolean isPropertyUpdateAllowed) {
		this.isPropertyUpdateAllowed = isPropertyUpdateAllowed;
	}

	public List<String> getStimulusPerProperty(String property) {
		if (property.contains(".")) {
			// takes the name of the property only
			property = property.replaceFirst(".*\\.", "");
		}
		List<String> stimulusPerProperty = this.stimulusPerProperty.get(property);
		return stimulusPerProperty == null ? Collections.<String> emptyList() : stimulusPerProperty;
	}

	/**
	 * Same behavior as superclass, we copied and pasted this method since it is private.<br>
	 * {@inheritDoc}
	 */
	private void updateExponentialAverage(String iden, double val) {
		double avg = 0.0;
		// retrieve the exponential alpha
		double alpha = Double.parseDouble(Rainbow.property(Rainbow.PROPKEY_MODEL_ALPHA));
		if (m_propExpAvg.containsKey(iden)) {
			avg = m_propExpAvg.get(iden);
			// compute the new exponential average value
			avg = (1 - alpha) * avg + alpha * val;
		} else { // assign first value as the starting point for average
			avg = val;
		}
		if (logger.isTraceEnabled())
			logger.trace("(iden,val,alpha,avg) == (" + iden + "," + val + "," + alpha + "," + avg + ")");
		// store new/updated exp.avg value
		m_propExpAvg.put(iden, avg);
	}

	private List<SelfHealingScenario> findBrokenScenarios(String stimulus) {
		List<SelfHealingScenario> brokenScenarios = new ArrayList<SelfHealingScenario>();
		List<SelfHealingScenario> scenariosWithStimulus = this.scenariosMap.get(stimulus);

		Stack<AcmeError> collectedErrors = new Stack<AcmeError>();
		for (SelfHealingScenario scenario : scenariosWithStimulus) {
			if (this.isBroken(scenario, collectedErrors)) {
				this.logErrors(collectedErrors, scenario);
				brokenScenarios.add(scenario);
			} else {
				Oracle.instance().writeEvaluatorPanel(logger, scenario.getName() + " pass");
			}
		}
		return brokenScenarios;
	}

	// TODO mover isBroken a SelfHealingScenario?
	private boolean isBroken(SelfHealingScenario scenario, Stack<AcmeError> collectedErrors) {
		AcmeDesignRule rule = scenario.getResponseMeasure().getConstraint().getAcmeDesignRule();
		@SuppressWarnings("unused")
		Set<AcmeDesignRule> envRules = this.collectEnvironmentRules(scenario);

		// FIXME chequear las condiciones del Environment tambien (envRules)
		SimpleModelTypeChecker typeChecker = new SimpleModelTypeChecker();
		typeChecker.registerModel(this.getAcmeModel());

		TypeCheckingState typeCheckingState = typeChecker.typecheckDesignRuleExpression(rule, rule
				.getDesignRuleExpression(), collectedErrors);

		return !typeCheckingState.typechecks();
	}

	private Set<AcmeDesignRule> collectEnvironmentRules(SelfHealingScenario scenario) {
		Set<AcmeDesignRule> envRules = new HashSet<AcmeDesignRule>();
		for (Constraint constraint : scenario.getEnvironment().getConditions()) {
			envRules.add(constraint.getAcmeDesignRule());
		}
		return envRules;
	}

	private void logErrors(Stack<AcmeError> collectedErrors, SelfHealingScenario scenario) {
		Oracle.instance().writeEvaluatorPanel(logger, scenario.getName() + " broken!");
		while (!collectedErrors.isEmpty()) {
			Oracle.instance().writeEvaluatorPanel(logger, collectedErrors.pop().toString());
		}
	}

	/**
	 * This method retrieves the enabled scenarios provided by
	 * {@link SelfHealingScenarioRepository#getEnabledScenarios()} and puts them on a map keyed by its stimulus.
	 */
	private Collection<SelfHealingScenario> loadScenarios() {
		Collection<SelfHealingScenario> scenarios = SelfHealingScenarioRepository.getEnabledScenarios();

		this.scenariosMap = new HashMap<String, List<SelfHealingScenario>>();
		this.stimulusPerProperty = new HashMap<String, List<String>>();

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

	private void loadStimulusPerProperty(SelfHealingScenario currentScenario, String stimulus) {
		List<String> stimulusPerScenarioList;
		String responseMeasureProperty = currentScenario.getResponseMeasure().getConstraint().getProperty();
		if (this.stimulusPerProperty.containsKey(responseMeasureProperty)) {
			stimulusPerScenarioList = this.stimulusPerProperty.get(responseMeasureProperty);
		} else {
			stimulusPerScenarioList = new ArrayList<String>();
			stimulusPerProperty.put(responseMeasureProperty, stimulusPerScenarioList);
		}
		stimulusPerScenarioList.add(stimulus);
	}

	/**
	 * This method instructs each constraint present on each scenario to create an Acme Rule object. This rule object is
	 * stored on each Constraint object.
	 */
	private void addAcmeRulesToScenarios(Collection<SelfHealingScenario> enabledScenarios) {
		for (SelfHealingScenario scenario : enabledScenarios) {
			Constraint constraint = scenario.getResponseMeasure().getConstraint();
			Artifact artifact = scenario.getArtifact();

			final Set<IAcmeComponent> acmeComponents = this.getAcmeComponents(artifact);
			// for now, the rule name is just the scenario's name
			final String ruleName = scenario.getName();
			for (IAcmeComponent anAcmeComponent : acmeComponents) {
				constraint.createAndAddAcmeRule(ruleName, (AcmeModel) this.m_acme, anAcmeComponent);
			}
		}
	}

	private Set<IAcmeComponent> getAcmeComponents(Artifact artifact) {
		Set<IAcmeComponent> result = new HashSet<IAcmeComponent>();
		String artifactName = artifact.getName();
		for (IAcmeSystem aSystem : m_acme.getSystems()) {
			for (IAcmeComponent aComponent : aSystem.getComponents()) {
				String componentType = ((AcmeComponentType) aComponent.getInstantiatedTypes().iterator().next()
						.getTarget()).getName();
				if (artifactName.equals(componentType)) {
					result.add(aComponent);
				}
			}
		}
		if (result.isEmpty()) {
			throw new RuntimeException("Could not find component of type " + artifactName + " in the ACME model");
		}
		return result;
	}

}