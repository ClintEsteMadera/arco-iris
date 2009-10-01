/**
 * Created April 7, 2006.
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
 * The Rainbow Architectural Evaluator, which performs change-triggered
 * evaluation of the architectural model.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 * @history * [2009.03.04] Removed beacon for model evaluation, set sleep period instead.
 */
public class ArchEvaluator extends AbstractRainbowRunnable {

	public static final String NAME = "Rainbow Architecture Evaluator";

	/** Reference to the Rainbow model */
	private Model m_model = null;

	/**
	 * Default Constructor.
	 */
	public ArchEvaluator () {
		super(NAME);

		m_model = Oracle.instance().rainbowModel();
		String per = Rainbow.property(Rainbow.PROPKEY_MODEL_EVAL_PERIOD);
		if (per != null) {
			setSleepTime(Long.parseLong(per));
		} else {  // default to using the long sleep value
			setSleepTime(IRainbowRunnable.LONG_SLEEP_TIME);
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose () {
		m_model = null;
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#log(java.lang.String)
	 */
	@Override
	protected void log (String txt) {
		Oracle.instance().writeEvaluatorPanel(m_logger, txt);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#runAction()
	 */
	@Override
	protected void runAction () {
		AdaptationManager am = (AdaptationManager )Oracle.instance().adaptationManager();
		if (m_model.hasPropertyChanged()) {
			m_model.clearPropertyChanged();
			// evaluate constraints only if adaptation not already taking place
			if (!am.adaptationInProgress()) {
				Oracle.instance().writeEvaluatorPanelSL(m_logger, "Prop changed, eval constraints...");
				// evaluate model for conformance to constraints
				Util.dataLogger().info(IRainbowHealthProtocol.DATA_CONSTRAINT_BEGIN);
				m_model.evaluateConstraints();
				Util.dataLogger().info(IRainbowHealthProtocol.DATA_CONSTRAINT_END);
				if (m_model.isConstraintViolated()) {
					Oracle.instance().writeEvaluatorPanel(m_logger, "violated!");
				} else {
					Oracle.instance().writeEvaluatorPanel(m_logger, "pass");
				}
			}
		}

		// trigger adaptation if any violation
		// independent if branch allows for adaptation even if no property update occurred
		if (m_model.isConstraintViolated() && !am.adaptationInProgress()) {
			log("Detecting constraint violation!! Triggering adaptation.");
			am.triggerAdaptation();
		}
	}

}
