package org.sa.rainbow.scenario.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.acmestudio.acme.core.exception.AcmeIllegalNameException;
import org.acmestudio.acme.core.globals.ExpressionOperator;
import org.acmestudio.acme.element.IAcmeDesignRule.DesignRuleType;
import org.acmestudio.acme.rule.node.IExpressionNode;
import org.acmestudio.acme.rule.node.NumericBinaryRelationalExpressionNode;
import org.acmestudio.acme.rule.node.NumericLiteralNode;
import org.acmestudio.acme.rule.node.ReferenceNode;
import org.acmestudio.acme.type.IAcmeTypeChecker;
import org.acmestudio.acme.type.verification.SynchronousTypeChecker;
import org.acmestudio.basicmodel.element.AcmeComponent;
import org.acmestudio.basicmodel.element.AcmeDesignRule;
import org.acmestudio.standalone.environment.StandaloneEnvironment;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;
import org.sa.rainbow.util.Util;

import ar.uba.dc.thesis.atam.Artifact;
import ar.uba.dc.thesis.atam.Stimulus;
import ar.uba.dc.thesis.repository.SelfHealingScenarioRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class RainbowModelWithScenarios extends RainbowModel {

	private static final String SCENARIO_SPEC_PATH = "customize.scenario.path";

	private static RainbowLogger logger = RainbowLoggerFactory.logger(RainbowModelWithScenarios.class);

	/** We maintain another reference to the same object since superclass' is private */
	private final StandaloneEnvironment m_acmeEnv = StandaloneEnvironment.instance();

	private Map<Stimulus, List<SelfHealingScenario>> scenariosMap;

	public RainbowModelWithScenarios() {
		super();
		this.loadScenarios();
		this.addScenariosConstraintsToAcmeModel();
	}

	private void loadScenarios() {
		List<SelfHealingScenario> scenarios = this.loadScenariosFromFile();

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
	}

	private List<SelfHealingScenario> loadScenariosFromFile() {
		@SuppressWarnings("unused")
		File scenarioSpec = Util.getRelativeToPath(Rainbow.instance().getTargetPath(), Rainbow
				.property(SCENARIO_SPEC_PATH));
		// TODO: Transformar de XML a una Lista de Escenarios.
		return Collections.emptyList();
	}

	/**
	 * Take the scenarios data to augment the Acme model.
	 */
	private void addScenariosConstraintsToAcmeModel() {
		Collection<SelfHealingScenario> scenarios = SelfHealingScenarioRepository.getEnabledScenarios();
		for (SelfHealingScenario scenario : scenarios) {
			// TODO las siguientes 3 propiedades salen de parsear el ResponseMeasure o tipamos la Constraint para que ya
			// venga cocinado?
			final String artifactProperty = "experRespTime";
			final ExpressionOperator operator = ExpressionOperator.GREATER_OR_EQUAL_OP;
			final int propertyValue = 5;

			// TODO el nombre de la regla lo seteamos nosotros tomando el nombre del scenario?
			final String ruleName = "clientePerformanceRule";
			addScenarioConstraint(scenario.getArtifact(), propertyValue, artifactProperty, operator, ruleName);

		}
	}

	private void addScenarioConstraint(Artifact artifact, final int propertyValue, final String artifactProperty,
			final ExpressionOperator operator, final String ruleName) {
		final AcmeComponent component = (AcmeComponent) m_acme.getSystem(artifact.getSystemName()).getComponent(
				artifact.getName());
		if (component == null) {
			throw new RuntimeException("Could not find component " + artifact.getName() + " in the ACME model");
		}
		logger.debug("Found component: " + artifact.getName());
		AcmeDesignRule createdRule = null;
		try {
			createdRule = component.createDesignRule(ruleName);
		} catch (final AcmeIllegalNameException e) {
			logger.error("Error creating rule from Scenario", e);
			throw new RuntimeException(e);
		}
		// TODO siempre es invariant o puede ser heuristic?
		createdRule.setDesignRuleType(DesignRuleType.INVARIANT);
		final NumericBinaryRelationalExpressionNode expression = new NumericBinaryRelationalExpressionNode(component
				.getContext());
		// TODO le ponemos FLOAT siempre y listo?
		expression.setExpressionType(NumericBinaryRelationalExpressionNode.INT_TYPE);
		final ReferenceNode first = new ReferenceNode(null);
		first.setReference(Arrays.asList("self", artifactProperty));
		expression.setFirstChild(first);
		expression.setOperator(operator);
		final IExpressionNode second = new NumericLiteralNode(propertyValue, component.getContext());
		expression.setSecondChild(second);
		createdRule.setDesignRuleExpression(expression);
	}

	@Override
	public void evaluateConstraints() {
		if (this.isDisposed()) {
			return;
		}

		// por ahora lo uso para ver como tomar el nombre de la constraint que se violo
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
}
