/**
 * Created October 31, 2009.
 */
package org.sa.rainbow.model.evaluator;

import org.sa.rainbow.adaptation.AdaptationManager;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.scenario.model.RainbowModelWithScenarios;
import org.sa.rainbow.util.Util;

/**
 * The Rainbow Architectural Evaluator, which performs broken-scenario-triggered evaluation of the architectural model.
 * 
 * @author Jonathan Chiocchio
 * @author Gabriel Tursi
 */
public class ArchEvaluatorWithEscenarios extends ArchEvaluator {

	/** Reference to the Rainbow model */
	private final RainbowModelWithScenarios rainbowModel;

	public ArchEvaluatorWithEscenarios() {
		super();
		/*
		 * We take advantage of *knowing* that the super class uses the same reference (this has to be done this way
		 * since ArchEvaluator does not provide a getter to access the Rainbow Model. We also *know* that the mentioned
		 * Rainbow model is instance of RainbowModelWithScenarios.
		 */
		rainbowModel = (RainbowModelWithScenarios) Oracle.instance().rainbowModel();
	}

	/**
	 * This method asks the Rainbow Model for invocations in runtime on any scenario's stimulus. If so, it triggers the
	 * evaluation of the corresponding response measure(s) of the scenario(s). In the event of finding a broken
	 * scenario, the proper adaptation is triggered.
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#runAction() <br>
	 *      TODO: See if Rainbow's usual behavior should take precedence over our the scenario's specific behavior.
	 */
	@Override
	protected void runAction() {
		super.runAction(); // perform Rainbow's usual behavior
		AdaptationManager adaptationManager = (AdaptationManager) Oracle.instance().adaptationManager();

		if (rainbowModel.hasAnStimulusBeenInvoked() && !adaptationManager.adaptationInProgress()) {
			this.evaluateInvolvedScenariosConstraints();

			if (rainbowModel.isAnyResponseMeasureNotBeingMet()) {
				Oracle.instance().writeEvaluatorPanel(m_logger, "violated!");
			} else {
				Oracle.instance().writeEvaluatorPanel(m_logger, "pass");
			}
		}
		if (rainbowModel.isAnyResponseMeasureNotBeingMet() && !adaptationManager.adaptationInProgress()) {
			log("Detecting response measure not being met!! Triggering adaptation.");
			adaptationManager.triggerAdaptation();
		}
	}

	/**
	 * Makes the rainbow model to evaluate the constraints imposed by several scenario's response measures (those whose
	 * stimulus has been invoked)
	 */
	private void evaluateInvolvedScenariosConstraints() {
		Oracle.instance().writeEvaluatorPanelSL(m_logger, "Stimulus invoked, evaluating corresponding constraints...");
		Util.dataLogger().info(IRainbowHealthProtocol.DATA_CONSTRAINT_BEGIN);

		rainbowModel.evaluateResponseMeasuresConstraints();

		Util.dataLogger().info(IRainbowHealthProtocol.DATA_CONSTRAINT_END);
	}
}
