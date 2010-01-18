package org.sa.rainbow.scenario.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.acmestudio.acme.type.IAcmeTypeChecker;
import org.acmestudio.acme.type.verification.SynchronousTypeChecker;
import org.acmestudio.basicmodel.element.AcmeComponent;
import org.acmestudio.basicmodel.model.AcmeModel;
import org.acmestudio.standalone.environment.StandaloneEnvironment;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.Stimulus;
import ar.uba.dc.thesis.rainbow.Constraint;
import ar.uba.dc.thesis.repository.SelfHealingScenarioRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class RainbowModelWithScenarios extends RainbowModel {

	private static RainbowLogger logger = RainbowLoggerFactory.logger(RainbowModelWithScenarios.class);

	/** We maintain another reference to the same object since superclass' is private */
	private final StandaloneEnvironment m_acmeEnv = StandaloneEnvironment.instance();

	private Map<Stimulus, List<SelfHealingScenario>> scenariosMap;

	private Stimulus lastStimulusInvoked;

	private boolean thereIsSomeResponseMeasureNotBeingMet;

	public RainbowModelWithScenarios() {
		super();
		Collection<SelfHealingScenario> enabledScenarios = this.loadScenarios();
		this.addAcmeRulesToScenarios(enabledScenarios);
	}

	/**
	 * TODO: Falta implementar este método!!!
	 */
	@Override
	public void evaluateResponseMeasuresConstraints() {
		if (m_acmeEnv == null)
			return;

		IAcmeTypeChecker typechecker = m_acmeEnv.getTypeChecker();
		if (typechecker instanceof SynchronousTypeChecker) {
			SynchronousTypeChecker synchChecker = (SynchronousTypeChecker) typechecker;
			synchChecker.typecheckAllModelsNow();
			m_constraintViolated = !synchChecker.typechecks(m_acmeSys);
			if (m_constraintViolated) {
				Set<?> errors = m_acmeEnv.getAllRegisteredErrors();
				Oracle.instance().writeEvaluatorPanel(logger, errors.toString());
			}
		}
	}

	public boolean hasAnStimulusBeenInvoked() {
		return this.lastStimulusInvoked != null;
	}

	public boolean isAnyResponseMeasureNotBeingMet() {
		return this.thereIsSomeResponseMeasureNotBeingMet;
	}

	/**
	 * This method retrieves the enabled scenarios provided by
	 * {@link SelfHealingScenarioRepository#getEnabledScenarios()} and puts them on a map keyed by its stimulus.
	 */
	private Collection<SelfHealingScenario> loadScenarios() {
		Collection<SelfHealingScenario> scenarios = SelfHealingScenarioRepository.getEnabledScenarios();

		this.scenariosMap = new HashMap<Stimulus, List<SelfHealingScenario>>();
		for (SelfHealingScenario currentScenario : scenarios) {
			Stimulus stimulus = currentScenario.getStimulus();
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