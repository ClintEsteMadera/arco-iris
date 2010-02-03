package org.sa.rainbow.model.evaluator;

import org.sa.rainbow.adaptation.AdaptationManagerWithScenarios;
import org.sa.rainbow.core.AbstractRainbowRunnable;
import org.sa.rainbow.core.IRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.health.IRainbowHealthProtocol;
import org.sa.rainbow.model.Model;
import org.sa.rainbow.util.Util;

/**
 * ArchEvaluatorWithScenarios replaces ArchEvaluator because the last one references directly to AdaptationManager, but
 * its necessary to use AdaptationManagerWithScenarios. This could be avoided by defining an interface
 * (IAdaptationManager) with the following methods: adaptationInProgress and triggerAdaptation, and making Oracle to
 * return the interface instead of the concrete class.
 */
public class ArchEvaluatorWithScenarios extends AbstractRainbowRunnable {

	public static final String NAME = "Rainbow Architecture Evaluator With Scenarios";

	/** Reference to the Rainbow model */
	private Model m_model = null;

	/**
	 * Default Constructor.
	 */
	public ArchEvaluatorWithScenarios() {
		super(NAME);

		m_model = Oracle.instance().rainbowModel();
		String per = Rainbow.property(Rainbow.PROPKEY_MODEL_EVAL_PERIOD);
		if (per != null) {
			setSleepTime(Long.parseLong(per));
		} else { // default to using the long sleep value
			setSleepTime(IRainbowRunnable.LONG_SLEEP_TIME);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose() {
		m_model = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#log(java.lang.String)
	 */
	@Override
	protected void log(String txt) {
		Oracle.instance().writeEvaluatorPanel(m_logger, txt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#runAction()
	 */
	@Override
	protected void runAction() {
		AdaptationManagerWithScenarios am = (AdaptationManagerWithScenarios) Oracle.instance().adaptationManager();
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