/**
 * Created March 15, 2006 
 */
package org.sa.rainbow.stitch.visitor;

import java.util.List;
import java.util.Stack;

import org.sa.rainbow.stitch.Ohana;
import org.sa.rainbow.stitch.core.Expression;
import org.sa.rainbow.stitch.core.IScope;
import org.sa.rainbow.stitch.core.Import;
import org.sa.rainbow.stitch.core.StitchScript;
import org.sa.rainbow.stitch.core.Strategy;
import org.sa.rainbow.stitch.core.Tactic;
import org.sa.rainbow.stitch.error.StitchProblemHandler;
import org.sa.rainbow.stitch.parser.StitchTreeWalker;


/**
 * The main purpose of this class is to serve as the factory for generating
 * the treewalker behaviors.
 * 
 * History:
 * - "Maquette," taken from sculpting, denotes a small clay model created to as
 * a guide to a larger sculpture.  This class serves that purpose, and provides
 * the intermediate data to form a parsed unit of adaptation script, as well
 * as utility functions.
 * - As of July 5, 2006, class renamed to Stitch, due to Stitch & Lilo theme,
 * and the fact that Stitch is "all-capable" to appear anywhere and has the 
 * weaponry necessary to evaluate a script.  :-)
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class Stitch {

	public static final int SCOPER_PASS = 0;
	public static final int TYPECHECKER_PASS = 1;
	public static final int EVALUATOR_PASS = 2;
	public static final int NUM_PASS = 3;
	public static final Stitch NULL_STITCH = new Stitch(null, null);

	public StitchTreeWalker walker = null;
	public String path = null;
	public StitchScript script = null;
	public IScope scope = null;
	public Expression expr = null;  // expression "currently" in use for evaluation
	public StitchProblemHandler stitchProblemHandler = null;  //ALI: ADDED; SWC: made public

	private ILiloBehavior[] m_behaviors = null;
	private Stack<IScope> m_scopeStack = null;
	private Stack<Expression> m_exprStack = null;

	private boolean m_keepRunning = true;  // flag used to cancel execution


	/**
	 * Protected constructor used by static instance method to instantiate. 
	 */
	protected Stitch (String scriptPath, StitchProblemHandler handler) {
		// [From ALI] incorporated stitchProblemHandler into tree walker
		stitchProblemHandler = handler;
		walker = new StitchTreeWalker();
		walker.setStitchProblemHandler(handler);

		path = scriptPath;

		m_behaviors = new ILiloBehavior[NUM_PASS];
		m_scopeStack = new Stack<IScope>();
		m_exprStack = new Stack<Expression>();
	}

	/**
	 * Instantiates a new Stitch object to aid adaptation script parsing.
	 * Path to the script should be supplied.
	 * @param scriptPath  script file path used as key to store stitch
	 * @return  new instance of Stitch
	 */
	public static Stitch newInstance (String scriptPath, StitchProblemHandler stitchProblemHandler) {
		Stitch stitch = Ohana.instance().findStitch(scriptPath);
		if (stitch == null) {
			stitch = new Stitch(scriptPath, stitchProblemHandler);
			Ohana.instance().storeStitch(scriptPath, stitch);
		}
		return stitch;
	}

	/**
	 * @return list of stored stitches
	 * @deprecated Use {@link Ohana.instance().listStitches()} instead.
	 */
	public static List<Stitch> list () {
		return Ohana.instance().listStitches();
	}

	/**
	 * @deprecated Use {@link Ohana.instance().findStitch()} instead.
	 */
	public static Stitch find (String key) {
		return Ohana.instance().findStitch(key);
	}

	/**
	 * @deprecated Use {@link Ohana.instance().storeStitch()} instead.
	 */
	public static Stitch store (String key, Stitch m) {
		return Ohana.instance().storeStitch(key, m);
	}

	/**
	 * @deprecated Use {@link Ohana.instance().removeStitch()} instead.
	 */
	public static Stitch remove (String key) {
		return Ohana.instance().removeStitch(key);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String str = "\n********\n"
			+ "  The stitch holds " + script.toString() + "\n"
			+ "  Base scope " + (script.parent() == null ? "NULL" : script.parent().toString()) + "\n"
			+ "  Imports [";
		for (Import i : script.imports) {
			str += "\n    " + i.toString();
		}
		str += "\n  ]\n  Tactics [";
		for (Tactic t : script.tactics) {
			str += "\n\t" + t.toString();
		}
		str += "\n  ]\n  Strategies [";
		for (Strategy s : script.strategies) {
			str += "\n\t" + s.toString();
		}
		str += "\n  ]\n********";
		str += "\n========\n" + script.toStringTree() + "\n========";
		return str;
	}

	/**
	 * This dispose method should only be called when cleaning up for application
	 * shutdown, not during, for example, building (reconciling) cycles.
	 */
	public void dispose () {
		walker = null;
		m_behaviors = null;
	}

	/**
	 * Instantiates a new IStitchBehavior for adaptation script parsing.
	 * @param phase  the phase of behavior needed
	 * @return IStitchBehavior  new instance of IStitchBehavior
	 */
	public ILiloBehavior getBehavior (int phase) {
		if (m_behaviors == null) return null;  // probably disposed

		ILiloBehavior beh = m_behaviors[phase];
		if (beh == null) {
			switch (phase) {
			case SCOPER_PASS:
				//ALI: MODIFIED
				beh = new LiloScopeEstablisher(this);
				break;
			case TYPECHECKER_PASS:
				beh = null;
				break;
			case EVALUATOR_PASS:
				//ALI: MODIFIED
				beh = new StitchScriptEvaluator(this);
				break;
			}
			m_behaviors[phase] = beh;
		}
		return beh;
	}

	/**
	 * Pushes current scope in scope stack and set supplied scope as current
	 * scope.
	 * @param s  new scope to set as current scope
	 */
	public void pushScope (IScope s) {
		m_scopeStack.push(scope);
		scope = s;
	}

	/**
	 * Pops last scope from stack and set as current scope.
	 * @return IScope  what was previously the current scope
	 */
	public IScope popScope () {
		IScope prev = scope;
		scope = m_scopeStack.pop();
		return prev;
	}

	/**
	 * Stores the current expression and reset it for evaluation.
	 */
	public void pushExpression () {
		m_exprStack.push(expr);
		expr = null;
	}

	/**
	 * Pops the current expression and restore previous expression.
	 */
	public void popExpression () {
		expr = m_exprStack.pop();
	}

	/**
	 * Returns whether evaluation has been canceled, which is also <code>true</code>
	 * if the Ohana central instance has been disposed. 
	 * @return boolean  <code>true</code> if operation was canceled or Ohana got disposed,
	 *                  <code>false</code> otherwise.
	 */
	public boolean isCanceled () {
		return !m_keepRunning || Ohana.isDisposed();
	}

	/**
	 * Cancels the evaluation operation, which should eventually interrupt execution.
	 */
	public void cancel () {
		m_keepRunning = false;
	}

	public Tactic findTactic (String iden) {
		Object o = script.lookup(iden);
		if (o instanceof Tactic) {
			return (Tactic )o;
		} else {
			return null;
		}
	}

}
