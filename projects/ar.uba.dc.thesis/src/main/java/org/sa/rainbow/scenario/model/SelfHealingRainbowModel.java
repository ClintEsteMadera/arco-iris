package org.sa.rainbow.scenario.model;

import java.util.Arrays;

import org.acmestudio.acme.core.exception.AcmeIllegalNameException;
import org.acmestudio.acme.core.globals.ExpressionOperator;
import org.acmestudio.acme.element.IAcmeDesignRule.DesignRuleType;
import org.acmestudio.acme.rule.node.IExpressionNode;
import org.acmestudio.acme.rule.node.NumericBinaryRelationalExpressionNode;
import org.acmestudio.acme.rule.node.NumericLiteralNode;
import org.acmestudio.acme.rule.node.ReferenceNode;
import org.acmestudio.basicmodel.element.AcmeComponent;
import org.acmestudio.basicmodel.element.AcmeDesignRule;
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

public class SelfHealingRainbowModel extends RainbowModel {

	public SelfHealingRainbowModel() {
		super();
		addScenariosConstraintsToAcmeModel();
	}

	/**
	 * Take the scenarios data to augment the Acme model.
	 */
	private void addScenariosConstraintsToAcmeModel() {
		// TODO estos 5 datos deberian leerse del ScenarioSpec
		final String systemName = "ZNewsSys";
		final String artifactName = "c1";
		final int maxResponseTime = 5;
		final String artifactProperty = "experRespTime";
		final ExpressionOperator operator = ExpressionOperator.GREATER_OR_EQUAL_OP;

		// TODO el nombre de la regla lo seteamos nosotros tomando el nombre del scenario?
		final String ruleName = "clientePerformanceRule";

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
		final IExpressionNode second = new NumericLiteralNode(maxResponseTime, client1.getContext());
		expression.setSecondChild(second);
		createdRule.setDesignRuleExpression(expression);
	}

	private static final RainbowLogger logger = RainbowLoggerFactory.logger(SelfHealingRainbowModel.class);
}
