/**
 * Created October 31, 2009.
 */
package org.sa.rainbow.model.evaluator;

import org.sa.rainbow.adaptation.AdaptationManager;
import org.sa.rainbow.core.AbstractRainbowRunnable;
import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.model.Model;
import org.sa.rainbow.util.Util;

/**
 * The Rainbow Architectural Evaluator, which performs broken-scenario-triggered evaluation of the architectural model.
 * 
 * @author Jonathan Chiocchio
 * @author Gabriel Tursi
 */
public class ArchEvaluatorWithEscenarios extends AbstractRainbowRunnable {

	public static final String NAME = "Rainbow Architecture Evaluator - QAW Scenarios centry";

	private Model rainbowModel;

	public ArchEvaluatorWithEscenarios() {
		super(NAME);

		this.rainbowModel = Oracle.instance().rainbowModel();
		String evaluationPeriod = Rainbow.property(Rainbow.PROPKEY_MODEL_EVAL_PERIOD);
		if (evaluationPeriod != null) {
			setSleepTime(Long.parseLong(evaluationPeriod));
		} else { // default to using the long sleep value
			setSleepTime(IRainbowRunnable.LONG_SLEEP_TIME);
		}
	}

	/**
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose() {
		this.rainbowModel = null;
	}

	/**
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#log(java.lang.String)
	 */
	@Override
	protected void log(String txt) {
		Oracle.instance().writeEvaluatorPanel(m_logger, txt);
	}

	/**
	 * This method asks to the Rainbow Model whether or not some scenario's stimulus was invoked in runtime. If so, it
	 * triggers the evaluation of the corresponding scenario's response measure. In the event of finding a broken
	 * scenario, the proper adaptation is triggered.
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#runAction()
	 */
	@Override
	protected void runAction() {
		AdaptationManager adaptationManager = (AdaptationManager) Oracle.instance().adaptationManager();
		if (rainbowModel.hasPropertyChanged()) {
			rainbowModel.clearPropertyChanged();
			// evaluate constraints only if adaptation not already taking place
			if (!adaptationManager.adaptationInProgress()) {
				Oracle.instance().writeEvaluatorPanelSL(m_logger, "Prop changed, eval constraints...");
				// evaluate model for conformance to constraints
				Util.dataLogger().info(IRainbowHealthProtocol.DATA_CONSTRAINT_BEGIN);
				rainbowModel.evaluateConstraints();
				Util.dataLogger().info(IRainbowHealthProtocol.DATA_CONSTRAINT_END);
				if (rainbowModel.isConstraintViolated()) {
					Oracle.instance().writeEvaluatorPanel(m_logger, "violated!");
				} else {
					Oracle.instance().writeEvaluatorPanel(m_logger, "pass");
				}
			}
		}

		// trigger adaptation if any violation
		// independent if branch allows for adaptation even if no property update occurred
		if (rainbowModel.isConstraintViolated() && !adaptationManager.adaptationInProgress()) {
			log("Detecting constraint violation!! Triggering adaptation.");
			adaptationManager.triggerAdaptation();
		}
	}

}
