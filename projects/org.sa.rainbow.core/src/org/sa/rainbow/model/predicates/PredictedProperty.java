/**
 * Created September 22, 2008.
 */
package org.sa.rainbow.model.predicates;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.acmestudio.acme.element.IAcmeElement;
import org.acmestudio.acme.environment.error.AcmeError;
import org.acmestudio.acme.model.DefaultAcmeModel;
import org.acmestudio.acme.rule.IAcmeDesignAnalysis;
import org.acmestudio.acme.rule.node.FormalParameterNode;
import org.acmestudio.acme.rule.node.IExpressionNode;
import org.acmestudio.acme.rule.node.IExternalAnalysisExpressionNode;
import org.acmestudio.acme.rule.node.TypeReferenceNode;
import org.acmestudio.acme.rule.node.feedback.AcmeExpressionEvaluationException;

/**
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 *
 * Implementation of an external analysis for querying a predicted property
 * from the target system via prediction gauges.
 */
public class PredictedProperty implements IAcmeDesignAnalysis {

    private TypeReferenceNode m_resultType;
    private ArrayList<FormalParameterNode> m_formalParameterList;

    /**
	 * Main constructor.
	 */
	public PredictedProperty() {
        m_resultType = new TypeReferenceNode (null);
        m_resultType.setBasicTypeRef (DefaultAcmeModel.defaultFloatType());
        m_formalParameterList = new ArrayList<FormalParameterNode> ();
        FormalParameterNode p = new FormalParameterNode (null);
        p.setParameterName ("p");
        TypeReferenceNode node = new TypeReferenceNode (null);
        p.setTypeReference (node);
        node.setBasicTypeRef (DefaultAcmeModel.defaultAnyType());
        m_formalParameterList.add (p);
        FormalParameterNode dur = new FormalParameterNode (null);
        dur.setParameterName ("dur");
        node = new TypeReferenceNode (null);
        dur.setTypeReference (node);
        node.setBasicTypeRef (DefaultAcmeModel.defaultIntType());
        m_formalParameterList.add (dur);
	}

	/* (non-Javadoc)
	 * @see org.acmestudio.acme.rule.IAcmeDesignAnalysis#getFormalParameters()
	 */
	public List<FormalParameterNode> getFormalParameters() {
		return m_formalParameterList;
	}

	/* (non-Javadoc)
	 * @see org.acmestudio.acme.rule.IAcmeDesignAnalysis#getResultTypeReference()
	 */
	public TypeReferenceNode getResultTypeReference() {
		return m_resultType;
	}

	/* (non-Javadoc)
	 * @see org.acmestudio.acme.rule.IAcmeDesignAnalysis#generateAnalysisResultTree(org.acmestudio.acme.element.IAcmeElement, java.util.List, java.util.Stack)
	 */
	public IExternalAnalysisExpressionNode generateAnalysisResultTree(
			IAcmeElement context, List<IExpressionNode> parameters, Stack<AcmeError> errorStack)
			throws AcmeExpressionEvaluationException {
        if (parameters.size () == 0) {
            AcmeError error = new AcmeError (context, "Cannot execute the predictedProperty() analysis with zero arguments");
            errorStack.push (error);
            throw new AcmeExpressionEvaluationException ();
        }
        if (parameters.size () != 2) {
            AcmeError error = new AcmeError (context, "Usage: predictedProperty (p : Property, dur : int)");
            errorStack.push (error);
            throw new AcmeExpressionEvaluationException ();
        }
        PredictedPropertyAnalysisNode result = new PredictedPropertyAnalysisNode ();
        return result;
	}

	/* (non-Javadoc)
	 * @see org.acmestudio.acme.rule.IAcmeDesignAnalysis#getExpression()
	 */
	public IExpressionNode getExpression() {  // no expression to return
		return null;
	}

	/* (non-Javadoc)
	 * @see org.acmestudio.acme.rule.IAcmeDesignAnalysis#getAnalysisType()
	 */
	public DesignAnalysisType getAnalysisType() {
		return DesignAnalysisType.EXTERNAL;
	}

	/* (non-Javadoc)
	 * @see org.acmestudio.acme.rule.IAcmeDesignAnalysis#getExternalAnalysisKey()
	 */
	public String getExternalAnalysisKey() {
		return PredictedPropertyAnalysisNode.class.getName();
	}

}
