package org.sa.rainbow.scenario.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.acme.element.property.IAcmePropertyValue;
import org.acmestudio.acme.model.command.IAcmeCommand;
import org.acmestudio.basicmodel.element.AcmeComponent;
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
import ar.uba.dc.thesis.rainbow.Constraint;
import ar.uba.dc.thesis.repository.SelfHealingScenarioRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class RainbowModelWithScenarios extends RainbowModel {

	private static RainbowLogger logger = RainbowLoggerFactory.logger(RainbowModelWithScenarios.class);

	/** Holds a list of Scenarios keyed by their corresponding stimulus name */
	private Map<String, List<SelfHealingScenario>> scenariosMap;

	private boolean isPropertyUpdateAllowed;

	private final Queue<Pair<String, Object>> propertiesToUpdateQueue;

	public RainbowModelWithScenarios() {
		super();
		Collection<SelfHealingScenario> enabledScenarios = this.loadScenarios();
		this.addAcmeRulesToScenarios(enabledScenarios);
		this.isPropertyUpdateAllowed = true;
		this.propertiesToUpdateQueue = new LinkedList<Pair<String, Object>>();
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

		for (SelfHealingScenario scenario : scenariosWithStimulus) {
			if (this.isBroken(scenario)) {
				Oracle.instance().writeEvaluatorPanel(logger, scenario.getName() + " broken!");
				brokenScenarios.add(scenario);
			} else {
				Oracle.instance().writeEvaluatorPanel(logger, "pass");
			}
		}
		return brokenScenarios;
	}

	/**
	 * TODO: Falta implementar este método!!!
	 */
	private boolean isBroken(SelfHealingScenario scenario) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This method retrieves the enabled scenarios provided by
	 * {@link SelfHealingScenarioRepository#getEnabledScenarios()} and puts them on a map keyed by its stimulus.
	 */
	private Collection<SelfHealingScenario> loadScenarios() {
		Collection<SelfHealingScenario> scenarios = SelfHealingScenarioRepository.getEnabledScenarios();

		this.scenariosMap = new HashMap<String, List<SelfHealingScenario>>();
		for (SelfHealingScenario currentScenario : scenarios) {
			String stimulus = currentScenario.getStimulus();
			List<SelfHealingScenario> scenarioList;

			if (this.scenariosMap.containsKey(stimulus)) {
				scenarioList = this.scenariosMap.get(stimulus);
			} else {
				scenarioList = new ArrayList<SelfHealingScenario>();
			}
			scenarioList.add(currentScenario);
		}
		return scenarios;
	}

	/**
	 * This method instructs each constraint present on each scenario to create an Acme Rule object. This rule object is
	 * stored on each Constraint object.
	 */
	private void addAcmeRulesToScenarios(Collection<SelfHealingScenario> enabledScenarios) {
		for (SelfHealingScenario scenario : enabledScenarios) {
			Constraint constraint = scenario.getResponseMeasure().getConstraint();
			Artifact artifact = scenario.getArtifact();

			final AcmeComponent acmeComponent = this.getAcmeComponent(artifact);
			// for now, the rule name is just the scenario's name
			final String ruleName = scenario.getName();
			constraint.createAndAddAcmeRule(ruleName, (AcmeModel) this.m_acme, acmeComponent);
		}
	}

	private AcmeComponent getAcmeComponent(Artifact artifact) {
		final AcmeComponent component = (AcmeComponent) this.m_acme.getSystem(artifact.getSystemName()).getComponent(
				artifact.getName());
		if (component == null) {
			throw new RuntimeException("Could not find component " + artifact.getName() + " in the ACME model");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Found component: " + artifact.getName());
		}
		return component;
	}
}