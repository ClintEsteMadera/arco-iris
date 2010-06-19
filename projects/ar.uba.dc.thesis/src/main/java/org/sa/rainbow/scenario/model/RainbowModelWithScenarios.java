package org.sa.rainbow.scenario.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.acmestudio.acme.core.type.IAcmeFloatValue;
import org.acmestudio.acme.core.type.IAcmeIntValue;
import org.acmestudio.acme.element.IAcmeElementInstance;
import org.acmestudio.acme.element.IAcmeSystem;
import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.acme.element.property.IAcmePropertyValue;
import org.acmestudio.acme.model.command.IAcmeCommand;
import org.acmestudio.standalone.resource.StandaloneLanguagePackHelper;
import org.apache.log4j.Level;
import org.sa.rainbow.adaptation.AdaptationManagerWithScenarios;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.util.Pair;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.atam.scenario.ScenariosManager;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class RainbowModelWithScenarios extends RainbowModel {

	private static RainbowLogger logger = RainbowLoggerFactory.logger(RainbowModelWithScenarios.class);

	private final ScenariosManager scenariosManager;

	private boolean isPropertyUpdateAllowed;

	private final Queue<Pair<String, Object>> propertiesToUpdateQueue;

	public RainbowModelWithScenarios(ScenariosManager scenariosManager) {
		super();
		this.scenariosManager = scenariosManager;
		this.scenariosManager.loadScenarios();
		this.isPropertyUpdateAllowed = true;
		this.propertiesToUpdateQueue = new LinkedList<Pair<String, Object>>();
		log(Level.INFO, "Rainbow Model With Scenarios started");
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
				&& !(brokenScenarios = this.scenariosManager.findBrokenScenarios(stimulus)).isEmpty()) {
			/*
			 * pass control to adaptation manager, who will be responsible for turning the flag
			 * (isPropertyUpdateAllowed) on again...
			 */
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
				log(Level.DEBUG, "Upd prop: " + prop.getQualifiedName() + " = " + value.toString());
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
					log(Level.ERROR, "Acme Command execution failed!", e);
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

	public List<String> getStimulus(String property) {
		return this.scenariosManager.getStimulus(property);
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
			log(Level.TRACE, "(iden,val,alpha,avg) == (" + iden + "," + val + "," + alpha + "," + avg + ")");
		// store new/updated exp.avg value
		m_propExpAvg.put(iden, avg);
	}

	public List<Number> getAllInstancesPropertyValues(String systemName, String name, String property) {
		List<Number> propertyValues = new ArrayList<Number>();
		// this is added in order to contemplate the case when the model is disposed.
		if (m_acme == null) {
			return propertyValues;
		}
		IAcmeSystem system = m_acme.getSystem(systemName);
		Set<IAcmeElementInstance<?, ?>> children = new HashSet<IAcmeElementInstance<?, ?>>();
		children.addAll(system.getComponents());
		children.addAll(system.getConnectors());
		children.addAll(system.getPorts());
		children.addAll(system.getRoles());
		for (IAcmeElementInstance<?, ?> child : children) {
			// FIXME CHEQUEAR QUE EL ARTIFACT ESTE HABILITADO!
			// AcmeElementInstance.getProperty("isArchEnabled")
			// seek element with specified type AND specified property
			if (child.declaresType(name) || child.instantiatesType(name)) {
				IAcmeProperty childProp = child.getProperty(property);
				if (childProp != null) {
					if (childProp.getValue() instanceof IAcmeFloatValue) {
						propertyValues.add(((IAcmeFloatValue) childProp.getValue()).getValue());
					} else if (childProp.getValue() instanceof IAcmeIntValue) {
						propertyValues.add(((IAcmeIntValue) childProp.getValue()).getValue());
					} else {
						throw new RuntimeException("The type " + childProp.getValue().getClass().getName()
								+ " is not currently supported");
					}
				}
			}
		}

		return propertyValues;
	}

	/**
	 * Same behavior as super class except that we log the disruption with TRACE level rather than INFO.
	 */
	@Override
	public void markDisruption(double level) {
		m_moreProp.put(PENALTY_KEY + "Disruption", level);
		log(Level.TRACE, "X_X disruption marked: " + level);
	}

	private void log(Level level, String txt, Throwable... t) {
		Oracle.instance().writeManagerPanel(logger, level, txt, t);
	}

}