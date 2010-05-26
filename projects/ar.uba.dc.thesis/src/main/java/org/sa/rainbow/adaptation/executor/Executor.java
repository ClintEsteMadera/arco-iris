/**
 * Created September 2, 2006.
 */
package org.sa.rainbow.adaptation.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.acmestudio.acme.element.IAcmeElementInstance;
import org.sa.rainbow.adaptation.AdaptationManager;
import org.sa.rainbow.adaptation.AdaptationManagerWithScenarios;
import org.sa.rainbow.adaptation.IGenericArchOperators;
import org.sa.rainbow.core.AbstractRainbowRunnable;
import org.sa.rainbow.core.Oracle;
import org.sa.rainbow.core.Rainbow;
import org.sa.rainbow.model.RainbowModel;
import org.sa.rainbow.stitch.core.Strategy;
import org.sa.rainbow.stitch.core.Var;
import org.sa.rainbow.stitch.model.ModelOperator;
import org.sa.rainbow.translator.effectors.IEffector;
import org.sa.rainbow.translator.effectors.IEffector.Outcome;

/**
 * <b>NOTE:</b> This class is literally copied from the original one. We only casted to
 * {@link AdaptationManagerWithScenarios} instead of {@link AdaptationManager}
 * <p>
 * The Strategy Executor serves the role of maintaining the active thread(s) to carry a strategy/ies. This design
 * allows, in the future, for simultaneous execution of multiple strategies if Rainbow should support that ability.
 * <p>
 * Generic architectural operators: - connect (comp1, comp2) - disconnect (comp1, comp2) - start (comp) - shutdown
 * (comp) - changeState (element, prop, val) - execute (element, args)
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class Executor extends AbstractRainbowRunnable implements ModelOperator, IGenericArchOperators {

	public static final String NAME = "Rainbow Strategy Executor";

	/*
	 * A queue of strategies to execute, this could be enriched later with priorities and such...
	 */
	private Queue<Strategy> m_queue = null;
	private Map<Strategy, Object[]> m_args = null;

	/**
	 * @param name
	 */
	public Executor() {
		super(NAME);

		m_queue = new LinkedList<Strategy>();
		m_args = new HashMap<Strategy, Object[]>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.IDisposable#dispose()
	 */
	public void dispose() {
		m_queue.clear();
		m_args.clear();

		// null-out data members
		m_queue = null;
		m_args = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#log(java.lang.String)
	 */
	@Override
	protected void log(String txt) {
		Oracle.instance().writeExecutorPanel(m_logger, txt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.core.AbstractRainbowRunnable#runAction()
	 */
	@Override
	protected void runAction() {
		if (!m_queue.isEmpty()) {
			// retrieve the next strategy in the queue and execute it
			Strategy strategy = m_queue.poll();
			Object[] args = m_args.remove(strategy);
			log("Executing Strategy " + strategy.getName() + "...");
			Strategy.Outcome o = null;
			try {
				// provide var for _dur_
				Var v = new Var();
				v.scope = strategy.stitch().scope;
				v.setType("long");
				v.name = "_dur_";
				if (Rainbow.predictionEnabled()) { // provide future duration
					v.setValue(strategy.estimateAvgTimeCost());
				} else {
					v.setValue(0L);
				}
				strategy.stitch().script.addVar(v.name, v);
				// execute strategy
				o = (Strategy.Outcome) strategy.evaluate(args);
				// cleanup, remove temp var
				strategy.stitch().script.vars().remove(v.name);
			} catch (NullPointerException e) {
				// see if cause of NPE is Rainbow termination
				if (!Rainbow.shouldTerminate()) { // nope, terminate and re-throw
					terminate();
					throw e;
				}
			}
			log(" - Outcome: " + o);
			if (!Rainbow.shouldTerminate()) {
				// TODO: Should we check for evaluation outcome?
				((AdaptationManagerWithScenarios) Oracle.instance().adaptationManager()).markStrategyExecuted(strategy);
			}
		}
	}

	public void enqueueStrategy(Strategy strategy, Object[] args) {
		m_queue.offer(strategy);
		m_args.put(strategy, args);
	}

	public void dequeueStrategy(Strategy strategy) {
		if (m_queue.contains(strategy)) {
			m_queue.remove(strategy);
		}
	}

	Queue<Strategy> _retrieveStrategyQueueForTesting() {
		return m_queue;
	}

	// //////////////////////////////////////
	// ModelOperator method used by Ohana
	// //////////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.stitch.model.ModelOperator#invoke(java.lang.String, java.lang.Object[])
	 */
	public Object invoke(String name, Object[] args) {
		Outcome o = Outcome.UNKNOWN;
		// extract the first argument, args assumed to have at least one entry
		Object element = args[0];
		// check effector name against opMap to determine which generic op to invoke
		String opName = ((RainbowModel) Oracle.instance().rainbowModel()).getGenericOperatorName(name);
		if (opName == null) { // no operator name found, just plain invoke
			String[] strArgs = new String[args.length - 1];
			for (int i = 0; i < strArgs.length; ++i) {
				strArgs[i] = args[i + 1].toString();
			}
			o = execute(name, element, strArgs);
		} else if (opName.equals("start")) {
			String[] strArgs = new String[args.length - 1];
			for (int i = 0; i < strArgs.length; ++i) {
				strArgs[i] = args[i + 1].toString();
			}
			o = start(name, element, strArgs);
		} else if (opName.equals("stop")) {
			String[] strArgs = new String[args.length - 1];
			for (int i = 0; i < strArgs.length; ++i) {
				strArgs[i] = args[i + 1].toString();
			}
			o = stop(name, element, strArgs);
		} else if (opName.equals("changeState")) {
			Map<String, String> statePairs = new HashMap<String, String>();
			for (int i = 1; i < args.length; i += 2) {
				statePairs.put(args[i].toString(), args[i + 1].toString());
			}
			o = changeState(name, element, statePairs);
		} else if (opName.equals("connect")) {
			Object comp2 = args[1];
			if (args.length > 2) {
				String[] strArgs = new String[args.length - 2];
				for (int i = 0; i < strArgs.length; ++i) {
					strArgs[i] = args[i + 2].toString();
				}
				o = connect(name, element, comp2, strArgs);
			}
		} else if (opName.equals("disconnect")) {
			int i = 1;
			Object comp2 = args[i++];
			Object conn = null;
			if (args.length > 2) {
				conn = args[i++];
			}
			String[] strArgs = new String[args.length - i];
			for (int j = 0; j < strArgs.length; ++j) {
				strArgs[j] = args[j + i].toString();
			}
			o = disconnect(name, element, comp2, conn, strArgs);
		}
		return o.name();
	}

	// //////////////////////////////////
	// Generic Architectural Operators
	// //////////////////////////////////

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.adaptation.IGenericArchOperators#start(java.lang.String, java.lang.Object,
	 *      java.lang.String[])
	 */
	public Outcome start(String effName, Object component, String[] optArgs) {
		String target = null;
		if (component instanceof IAcmeElementInstance<?, ?>) {
			target = RainbowModel.getElementLocation((IAcmeElementInstance<?, ?>) component);
		} else {
			target = component.toString();
		}
		// Get a reference to the named effector interface & execute
		IEffector eff = Oracle.instance().targetSystem().getEffector(effName, target);
		IEffector.Outcome r = (eff == null) ? IEffector.Outcome.UNKNOWN : eff.execute(optArgs);
		if (m_logger.isTraceEnabled())
			m_logger.trace(effName + " ('start' Eff) returned " + r);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.adaptation.IGenericArchOperators#stop(java.lang.String, java.lang.Object, java.lang.String[])
	 */
	public Outcome stop(String effName, Object component, String[] optArgs) {
		String target = null;
		if (component instanceof IAcmeElementInstance<?, ?>) {
			target = RainbowModel.getElementLocation((IAcmeElementInstance<?, ?>) component);
		} else {
			target = component.toString();
		}
		// Get a reference to the named effector interface & execute
		IEffector eff = Oracle.instance().targetSystem().getEffector(effName, target);
		IEffector.Outcome r = (eff == null) ? IEffector.Outcome.UNKNOWN : eff.execute(optArgs);
		if (m_logger.isTraceEnabled())
			m_logger.trace(effName + " ('stop' Eff) returned " + r);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.adaptation.IGenericArchOperators#changeState(java.lang.String, java.lang.Object,
	 *      java.util.Map)
	 */
	public Outcome changeState(String effName, Object element, Map<String, String> statePairs) {
		String target = null;
		if (element instanceof IAcmeElementInstance<?, ?>) {
			target = RainbowModel.getElementLocation((IAcmeElementInstance<?, ?>) element);
		} else {
			target = element.toString();
		}
		// form string arguments from key-value pairs of states to change
		List<String> pairList = new ArrayList<String>();
		for (Map.Entry<String, String> pair : statePairs.entrySet()) {
			pairList.add(pair.getKey() + "=" + pair.getValue());
		}
		String[] args = pairList.toArray(new String[0]);
		// Get a reference to the named effector interface & execute
		IEffector eff = Oracle.instance().targetSystem().getEffector(effName, target);
		IEffector.Outcome r = (eff == null) ? IEffector.Outcome.UNKNOWN : eff.execute(args);
		if (m_logger.isTraceEnabled())
			m_logger.trace(effName + " ('changeState' Eff) returned " + r);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.adaptation.IGenericArchOperators#connect(java.lang.String, java.lang.Object,
	 *      java.lang.Object, java.lang.String[])
	 */
	public Outcome connect(String effName, Object initiatingComp, Object targetComp, String[] optArgs) {
		String target = null;
		if (initiatingComp instanceof IAcmeElementInstance<?, ?>) {
			target = RainbowModel.getElementLocation((IAcmeElementInstance<?, ?>) initiatingComp);
		} else {
			target = initiatingComp.toString();
		}
		// include target component's location or name as part of arguments
		List<String> argList = new ArrayList<String>();
		if (targetComp instanceof IAcmeElementInstance<?, ?>) {
			argList.add(RainbowModel.getElementLocation((IAcmeElementInstance<?, ?>) targetComp));
		} else {
			argList.add(targetComp.toString());
		}
		Collections.addAll(argList, optArgs);
		String[] args = argList.toArray(new String[0]);
		// Get a reference to the named effector interface & execute
		IEffector eff = Oracle.instance().targetSystem().getEffector(effName, target);
		IEffector.Outcome r = (eff == null) ? IEffector.Outcome.UNKNOWN : eff.execute(args);
		if (m_logger.isTraceEnabled())
			m_logger.trace(effName + " ('connect' Eff) returned " + r);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.adaptation.IGenericArchOperators#disconnect(java.lang.String, java.lang.Object,
	 *      java.lang.Object, java.lang.Object, java.lang.String[])
	 */
	public Outcome disconnect(String effName, Object comp1, Object comp2, Object conn, String[] optArgs) {
		String target = null;
		if (comp1 instanceof IAcmeElementInstance<?, ?>) {
			target = RainbowModel.getElementLocation((IAcmeElementInstance<?, ?>) comp1);
		} else {
			target = comp1.toString();
		}
		// include target component's location or name as part of arguments
		List<String> argList = new ArrayList<String>();
		if (comp2 instanceof IAcmeElementInstance<?, ?>) {
			argList.add(RainbowModel.getElementLocation((IAcmeElementInstance<?, ?>) comp2));
		} else {
			argList.add(comp2.toString());
		}
		// include connector reference if provided
		if (conn != null) {
			if (conn instanceof IAcmeElementInstance<?, ?>) {
				argList.add(((IAcmeElementInstance<?, ?>) conn).getQualifiedName());
			} else {
				argList.add(conn.toString());
			}
		}
		Collections.addAll(argList, optArgs);
		String[] args = argList.toArray(new String[0]);
		// Get a reference to the named effector interface & execute
		IEffector eff = Oracle.instance().targetSystem().getEffector(effName, target);
		IEffector.Outcome r = (eff == null) ? IEffector.Outcome.UNKNOWN : eff.execute(args);
		if (m_logger.isTraceEnabled())
			m_logger.trace(effName + " ('disconnect' Eff) returned " + r);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sa.rainbow.adaptation.IGenericArchOperators#execute(java.lang.String, java.lang.Object,
	 *      java.lang.String[])
	 */
	public IEffector.Outcome execute(String effName, Object element, String[] args) {
		String target = null;
		if (element instanceof IAcmeElementInstance<?, ?>) {
			target = RainbowModel.getElementLocation((IAcmeElementInstance<?, ?>) element);
		} else {
			target = element.toString();
		}
		// Get a reference to the named effector interface & execute
		IEffector eff = Oracle.instance().targetSystem().getEffector(effName, target);
		IEffector.Outcome r = (eff == null) ? IEffector.Outcome.UNKNOWN : eff.execute(args);
		if (m_logger.isTraceEnabled())
			m_logger.trace(effName + " returned " + r);
		return r;
	}

}
