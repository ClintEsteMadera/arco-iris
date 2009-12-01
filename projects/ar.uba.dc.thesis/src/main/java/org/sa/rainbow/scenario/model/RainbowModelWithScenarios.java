package org.sa.rainbow.scenario.model;

import java.util.Arrays;
import java.util.Collection;
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
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

import ar.uba.dc.thesis.repository.SelfHealingScenarioRepository;
import ar.uba.dc.thesis.selfhealing.SelfHealingScenario;

public class RainbowModelWithScenarios extends RainbowModel {

	// Agregar a RainbowModelWithScenarios la logica para agregar todos los escenarios y un mapa que indexe los
	// escenarios por estímulo.

	private RainbowLogger m_logger = null;
	/** Keep copy of standalone environment, so we know when we've disposed one */
	private StandaloneEnvironment m_acmeEnv = null;

	public RainbowModelWithScenarios() {
		super();
		m_logger = RainbowLoggerFactory.logger(getClass());
		m_acmeEnv = StandaloneEnvironment.instance();
		addScenariosConstraintsToAcmeModel();
	}

	/**
	 * Take the scenarios data to augment the Acme model.
	 */
	private void addScenariosConstraintsToAcmeModel() {
		Collection<SelfHealingScenario> scenarios = SelfHealingScenarioRepository.getEnabledScenarios();
		for (SelfHealingScenario scenario : scenarios) {
			final String systemName = "ZNewsSys";// TODO de donde tomamos el nombre del sistema?
			final String artifactName = scenario.getArtifact().getName();
			// TODO las siguientes 3 propiedades salen de parsear el ResponseMeasure o tipamos la Constraint para que ya
			// venga cocinado?
			final String artifactProperty = "experRespTime";
			final ExpressionOperator operator = ExpressionOperator.GREATER_OR_EQUAL_OP;
			final int propertyValue = 5;

			// TODO el nombre de la regla lo seteamos nosotros tomando el nombre del scenario?
			final String ruleName = "clientePerformanceRule";
			addScenarioConstraint(systemName, artifactName, propertyValue, artifactProperty, operator, ruleName);

		}
	}

	private void addScenarioConstraint(final String systemName, final String artifactName, final int propertyValue,
			final String artifactProperty, final ExpressionOperator operator, final String ruleName) {
		final AcmeComponent client1 = (AcmeComponent) m_acme.getSystem(systemName).getComponent(artifactName);
		if (client1 == null) {
			throw new RuntimeException("No se encontro el componente " + artifactName);
		}
		logger.info("Se encontro el componente " + artifactName);
		AcmeDesignRule createdRule = null;
		try {
			createdRule = client1.createDesignRule(ruleName);
		} catch (final AcmeIllegalNameException e) {
			logger.error("Error creating rule from Scenario", e);
			throw new RuntimeException(e);
		}
		// TODO siempre es invariant o puede ser heuristic?
		createdRule.setDesignRuleType(DesignRuleType.INVARIANT);
		final NumericBinaryRelationalExpressionNode expression = new NumericBinaryRelationalExpressionNode(client1
				.getContext());
		// TODO le ponemos FLOAT siempre y listo?
		expression.setExpressionType(NumericBinaryRelationalExpressionNode.INT_TYPE);
		final ReferenceNode first = new ReferenceNode(null);
		first.setReference(Arrays.asList("self", artifactProperty));
		expression.setFirstChild(first);
		expression.setOperator(operator);
		final IExpressionNode second = new NumericLiteralNode(propertyValue, client1.getContext());
		expression.setSecondChild(second);
		createdRule.setDesignRuleExpression(expression);
	}

	@Override
	public void evaluateConstraints() {
		// por ahora lo uso para ver como tomar el nombre de la constraint que se violo
		if (m_acmeEnv == null) {
			return;
		}

		IAcmeTypeChecker typechecker = m_acmeEnv.getTypeChecker();
		if (typechecker instanceof SynchronousTypeChecker) {
			SynchronousTypeChecker synchChecker = (SynchronousTypeChecker) typechecker;
			synchChecker.typecheckAllModelsNow();
			m_constraintViolated = !synchChecker.typechecks(m_acmeSys);
			if (m_constraintViolated) {
				Set<?> errors = m_acmeEnv.getAllRegisteredErrors();
				Oracle.instance().writeEvaluatorPanel(m_logger, errors.toString());
			}
		}
	}

	private static final RainbowLogger logger = RainbowLoggerFactory.logger(RainbowModelWithScenarios.class);

}
