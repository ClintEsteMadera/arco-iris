/**
 * Created September 22, 2008.
 */
package org.sa.rainbow.model.predicates;

import java.util.List;
import java.util.Stack;

import org.acmestudio.acme.core.IAcmeType;
import org.acmestudio.acme.element.property.IAcmeProperty;
import org.acmestudio.acme.environment.error.AcmeError;
import org.acmestudio.acme.rule.node.IExternalAnalysisExpressionNode;
import org.acmestudio.acme.rule.node.feedback.AcmeExpressionEvaluationException;
import org.acmestudio.acme.rule.node.feedback.ExternalAnalysisError;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.monitor.TargetSystem.StatType;
import org.sa.rainbow.util.RainbowLogger;
import org.sa.rainbow.util.RainbowLoggerFactory;

/**
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 *
 * The Acme external analysis node of the external analysis for querying
 * predicted property on the target system.
 */
public class PredictedPropertyAnalysisNode implements
		IExternalAnalysisExpressionNode {

	private RainbowLogger m_logger = null;

	/**
	 * Default (empty) constructor.
	 */
	public PredictedPropertyAnalysisNode() {
		m_logger = RainbowLoggerFactory.logger(getClass());
	}

	/* (non-Javadoc)
	 * @see org.acmestudio.acme.rule.node.IExternalAnalysisExpressionNode#evaluate(org.acmestudio.acme.core.IAcmeType, java.util.List, java.util.Stack)
	 */
	public Object evaluate(IAcmeType requestedType, List<Object> arguments,
			Stack<AcmeError> errorStack) throws AcmeExpressionEvaluationException {

		if (arguments.size () == 0) {
            AcmeError error = new AcmeError ();
            error.setMessageText("Cannot execute the predictedProperty() analysis with zero arguments");
            errorStack.push (error);
            throw new AcmeExpressionEvaluationException ();
        }
        if (arguments.size () != 2) {
            AcmeError error = new AcmeError ();
            error.setMessageText("Usage: predictedProperty (p : Property, dur : int) where <dur> is milliseconds in the future");
            errorStack.push (error);
            throw new AcmeExpressionEvaluationException ();
        }

        Object o1 = arguments.get (0);
        IAcmeProperty property = null;
        if (o1 instanceof IAcmeProperty) {
            property = (IAcmeProperty )o1;
        }
        Object o2 = arguments.get(1);
        Integer intval = null;
        if (o2 instanceof Integer) {
            intval = (Integer )o2;
        }
        if (property != null && intval != null) {
        	// fetch predicted future value of designated property at specified future duration (ms)
        	Object valObj = Oracle.instance().targetSystem().predictProperty(property.getQualifiedName(), intval.longValue(), StatType.SINGLE);
        	if (m_logger.isDebugEnabled()) m_logger.debug(property.getQualifiedName() + " [single] at now+" + intval + "ms = " + valObj);
        	if (valObj instanceof Double) {
            	return ((Double )valObj).floatValue();
        	}
        }

        AcmeError error = new ExternalAnalysisError ();
        error.setMessageText ("Error in predictedProperty analysis.");
        errorStack.push (error);
        throw new AcmeExpressionEvaluationException ();
	}

}
