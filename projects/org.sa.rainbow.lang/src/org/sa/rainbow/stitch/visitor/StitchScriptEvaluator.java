/**
 * Created April 5, 2006.
 */
package org.sa.rainbow.stitch.visitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.acmestudio.acme.core.type.IAcmeBooleanValue;
import org.acmestudio.acme.element.IAcmeElement;
import org.acmestudio.acme.element.property.IAcmeProperty;
import org.sa.rainbow.stitch.Ohana;
import org.sa.rainbow.stitch.core.Expression;
import org.sa.rainbow.stitch.core.MyDouble;
import org.sa.rainbow.stitch.core.MyInteger;
import org.sa.rainbow.stitch.core.MyNumber;
import org.sa.rainbow.stitch.core.Statement;
import org.sa.rainbow.stitch.core.Var;
import org.sa.rainbow.stitch.model.ModelOperator;
import org.sa.rainbow.stitch.parser.StitchLexerTokenTypes;
import org.sa.rainbow.stitch.parser.StitchTreeWalkerTokenTypes;
import org.sa.rainbow.stitch.util.Tool;

import antlr.collections.AST;


/**
 * Process Pass 3 of Tree walking:  evaluation.
 * 
 * @author Shang-Wen Cheng (zensoul@cs.cmu.edu)
 */
public class StitchScriptEvaluator extends AbstractLiloBehavior implements ILiloBehavior {

	private static final int LOP = Expression.LOP;
	private static final int ROP = Expression.ROP;

	/**
	 * @param stitch  the script molding scaffold
	 */
	protected StitchScriptEvaluator(Stitch stitch) {
		super(stitch);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#createVar(antlr.collections.AST, antlr.collections.AST)
	 */
	@Override
	public void createVar(AST typeAST, AST nameAST) {
		// see if a value was assigned at var creation
		if (scope().expressions().size() > 0) {
			Var v = scope().vars().get(nameAST.getText());
			if (v != null && v.valStmt != null) {
				Expression e = scope().expressions().get(0);
				// make sure expr assocaited with var has AST and no known result
				if (e.ast() != null && e.getResult() == null) {
					e.evaluate(null);
				}
				v.setValue(e.getResult());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#lOp()
	 */
	@Override
	public void lOp() {
		if (checkSkipEval()) return;

		expr().curOp.push(LOP);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#rOp()
	 */
	@Override
	public void rOp() {
		if (checkSkipEval()) return;

		expr().curOp.push(ROP);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#beginExpression()
	 */
	@Override
	public void beginExpression() {
		if (expr() != null) {
			// don't subExpr anymore, just keep depth cnt
			expr().subLevel++;
			return;
		}

		if (scope() instanceof Statement
				&& scope().expressions().size() > ((Statement )scope()).curExprIdx) {
			setExpression(scope().expressions().get(((Statement )scope()).curExprIdx++));
			expr().curExprIdx = 0;
		} else if (scope() instanceof Expression) {
			Expression expr = (Expression )scope();
			if (expr.isComplex()) {
				if (expr.expressions().size() > expr.curExprIdx) {
					setExpression(expr.expressions().get(expr.curExprIdx++));
					expr.subLevel = 0;
				}
			} else {
				setExpression((Expression )scope());
				expr().curExprIdx = 0;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#endExpression()
	 */
	@Override
	public void endExpression() {
		Expression expr = expr();
		if (expr.subLevel > 0) {  // decr dept count first
			expr.subLevel--;
			return;
		} else {  // check quantified expression special case
			if (expr.skipQuanPredicate) {
				expr.skipQuanPredicate = false;
			}
		}

		// transfer any child result
		if (expr.getResult() == null && expr.expressions().size() > 0) {
			// transfer children result up
			Expression sub = expr.expressions().get(0);
			expr.setResult(sub.getResult());
		}

		setExpression(null);  // clear eval expression reference
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#beginQuantifiedExpression()
	 */
	@Override
	public void beginQuantifiedExpression() {
		if (checkSkipEval()) return;

		doBeginComplexExpr();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#doQuantifiedExpression()
	 */
	@SuppressWarnings("unchecked")  // newSet.add statement
	@Override
	public void doQuantifiedExpression() {
		if (checkSkipEval()) return;

		Expression cExpr = (Expression )scope();
		if (cExpr.kind != Expression.Kind.QUANTIFIED) {
			Tool.error("Error! Expected quantified expression not found!!", stitchProblemHandler());
			return;
		}
		// at this point, this quantified expression is ready to be evaluated
		// - there should be at least one quantifier variable
		//   TODO: handle just one quantifier variable for now
		//   TODO: check expr variables (references) against list of vars...
		if (cExpr.vars().size() > 1) {
			Tool.error("Sorry, only one quantified variable is currently supported! " + cExpr, stitchProblemHandler());
			return;
		}
		Var v = (Var )cExpr.vars().values().toArray()[0];
		// - the set expression should have values
		Set set = (Set )cExpr.expressions().get(0).getResult();
		if (set == null) {
			Tool.error("Error! Quantifier set is NULL!" + cExpr.toStringTree(), stitchProblemHandler());
			set = Collections.EMPTY_SET;
		}
		// - the predicate expression should have an AST ready to be evaluated
		Expression expr = cExpr.expressions().get(1);
		// now we retrieve the subset of elements matching the type of the var
		Set subset = new LinkedHashSet();
		if (set.size() > 0) {
			for (Object o : set) {
				if (Tool.typeMatches(v, o) && Tool.isArchEnabled(o)) {
					// type matches AND this object in quantified set is arch enabled
					subset.add(o);
				}
			}
		}

		// now we can evaluate quantified expression by
		// iterating through quantified set elements and collecting results
		int type = cExpr.ast().getType();
		boolean rv = true;
		Set newSet = null;
		if (type == StitchTreeWalkerTokenTypes.EXISTS_UNIQUE
				|| type == StitchTreeWalkerTokenTypes.EXISTS) {
			// need to start EXISTS with false
			rv = false;
		}
		for (Object elem : subset) {
			v.setValue(elem);
			expr.evaluate(null);
			boolean b = (Boolean )expr.getResult();

			switch (type) {
			case StitchTreeWalkerTokenTypes.FORALL:
				rv &= b;
				break;
			case StitchTreeWalkerTokenTypes.EXISTS:
				rv |= b;
				break;
			case StitchTreeWalkerTokenTypes.EXISTS_UNIQUE:
				rv = (rv && !b) || (!rv && b); 
				break;
			case StitchTreeWalkerTokenTypes.SELECT:
				if (newSet == null) {
					newSet = new LinkedHashSet();
				}
				if (b) {  // add element to set
					newSet.add(elem);
				}
				break;
			default:
				Tool.error("Unimplemented quantified expression type?? " + cExpr, stitchProblemHandler());
				break;
			}
		}
		// set predicate skip flag on the expression to be evaluated
		expr.skipQuanPredicate = true;
		// store result
		if (type == StitchTreeWalkerTokenTypes.SELECT) {
			if (newSet == null) {  // create an empty set
				newSet = new LinkedHashSet();
			}
			// store the chosen set
			cExpr.setResult(newSet);
		} else {  // store the boolean result
			cExpr.setResult(rv);
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#endQuantifiedExpression(antlr.collections.AST)
	 */
	@Override
	public void endQuantifiedExpression(AST quanAST) {
		if (checkSkipEval()) return;

		Expression cExpr = doEndComplexExpr();
		expr().setResult(cExpr.getResult());
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#beginMethodCallExpression()
	 */
	@Override
	public void beginMethodCallExpression() {
		if (checkSkipEval()) return;

		doBeginComplexExpr();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#endMethodCallExpression(antlr.collections.AST, antlr.collections.AST)
	 */
	@Override
	public void endMethodCallExpression(AST mcAST, AST idAST) {
		if (checkSkipEval()) return;

		Expression cExpr = doEndComplexExpr();
		Object[] args = new Object[cExpr.expressions().size()];
		int i = 0;
		for (Expression e : cExpr.expressions()) {
			if (e.ast() != null && e.getResult() == null) {
				e.evaluate(null);
			}
			args[i++] = e.getResult();
		}
		Object rv = executeMethod(idAST.getText(), args);
		cExpr.setResult(rv);
		if (rv instanceof Integer) {
			expr().setResult(new MyInteger((Integer )rv));
		} else if (rv instanceof Long) {
			expr().setResult(new MyInteger((Long )rv));
		} else if (rv instanceof Float) {
			expr().setResult(new MyDouble(((Float )rv).doubleValue()));
		} else if (rv instanceof Double) {
			expr().setResult(new MyDouble((Double )rv));
		} else {
			expr().setResult(rv);
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#beginSetExpression()
	 */
	@Override
	public void beginSetExpression() {
		if (checkSkipEval()) return;

		doBeginComplexExpr();
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#endSetExpression(antlr.collections.AST)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void endSetExpression(AST setAST) {
		if (checkSkipEval()) return;

		Expression cExpr = doEndComplexExpr();
		Set set = new LinkedHashSet(); 
		// iterate through elements and make sure they are the same types
		// complain if NOT, or form the proper set if yes
		Var v = new Var();  // declare a Var object to be able to use typeMatches
		for (Expression e : cExpr.expressions()) {
			if (e.getResult() == null) {
				if (v.getValue() != null) {
					// consider this a type mismatch?
					Tool.error("Unexpected null value in a SET!", stitchProblemHandler());
					return;
				}
			} else if (v.getValue() != null && !Tool.typeMatches(v, e.getResult())) {
				// explicit type mismatch
				// TODO: support subtypes?
				Tool.warn("Type mismatch between elements in set: " + e.getResult().getClass() + " vs " + v.getValue().getClass(), stitchProblemHandler());
			} else {
				// store type if type not already set
				if (v.getValue() == null) {
					v.setValue(e.getResult());
					v.setType(e.getResult().getClass().getName());
				}
				set.add(e.getResult());
			}
		}
		cExpr.setResult(set);
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#doExpression(antlr.collections.AST)
	 */
	@Override
	public void doExpression(AST exprAST) {
		if (checkSkipEval()) return;

		if (scope() instanceof Expression) {
			Expression expr = (Expression )scope();
			if (expr.getResult() == null && expr.expressions().size() > 0) {
				// transfer children result up
				Expression sub = expr.expressions().get(0);
				expr.setResult(sub.getResult());
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#doAssignExpression(antlr.collections.AST, antlr.collections.AST)
	 */
	@Override
	public void doAssignExpression(AST opAST, AST lValAST) {
		// Note:  Assignment will only be meaningful as its own statement, so
		//   should not be evaluated as expression elsewhere!

		if (checkSkipEval()) return;
		Expression expr = expr();

		// pop the lOp and rOp for good measures
		expr.lrOps[LOP].pop();
		Object rVal = expr.lrOps[ROP].pop();
		// the lVal can only be an identifier, otherwise, won't make sense!
		Object lObj = expr.lookup(lValAST.getText());
		if (lObj != null && lObj instanceof Var) {
			Var v = (Var )lObj;
			if (Tool.typeMatches(v, rVal)) {
				v.setValue(rVal);
				expr.setResult(rVal);
			} else {
				Tool.error("Assignment expression cannot be evaluated due to mismatched value types! "
						+ v.getType() + " vs. " + rVal.getClass().getName(), stitchProblemHandler());
				return;
			}
		} else {
			// can't find lvalue reference
			Tool.error("Assignment expression cannot be evaluated because lvalue reference cannot be found! "
					+ lValAST.getText(), stitchProblemHandler());
			return;
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#doLogicalExpression(antlr.collections.AST)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doLogicalExpression(AST opAST) {
		if (checkSkipEval()) return;
		Expression expr = expr();

		Boolean lOp = null;
		Boolean rOp = null;
		if (expr.lrOps[LOP].peek() == null
				|| expr.lrOps[ROP].peek() == null) {
			// if either is NULL, result is NULL
			Tool.warn("One logical operand is NULL: "
					+ expr.lrOps[LOP].pop() + ", "
					+ expr.lrOps[ROP].pop(), stitchProblemHandler());
			expr.setResult(null);
			return;
		}

		// deal with IAcmeProperty operands
		if (expr.lrOps[LOP].peek() instanceof IAcmeProperty) {
			IAcmeProperty aProp = (IAcmeProperty )expr.lrOps[LOP].pop();
			if (aProp.getValue() instanceof IAcmeBooleanValue) {
				expr.lrOps[LOP].push(((IAcmeBooleanValue )aProp.getValue()).getValue());
			} else {
				Tool.error("IAcmeProperty does NOT hold the expected Boolean value! "
						+ aProp + " " + opAST.getText() + " "
						+ expr.lrOps[ROP].peek(), stitchProblemHandler());
				return;
			}
		}
		if (expr.lrOps[ROP].peek() instanceof IAcmeProperty) {
			IAcmeProperty aProp = (IAcmeProperty )expr.lrOps[ROP].pop();
			if (aProp.getValue() instanceof IAcmeBooleanValue) {
				expr.lrOps[ROP].push(((IAcmeBooleanValue )aProp.getValue()).getValue());
			} else {
				Tool.error("IAcmeProperty does NOT hold the expected Boolean value! "
						+ expr.lrOps[LOP].peek() + " " + opAST.getText() + " "
						+ aProp, stitchProblemHandler());
				return;
			}
		}
		if (expr.lrOps[LOP].peek() instanceof Boolean
				&& expr.lrOps[ROP].peek() instanceof Boolean) {
			lOp = (Boolean )expr.lrOps[LOP].pop();
			rOp = (Boolean )expr.lrOps[ROP].pop();
		} else {
			Tool.error("Type mismatch or NOT booleans in logical expression! "
					+ expr.lrOps[LOP].peek() + " " + opAST.getText() + " "
					+ expr.lrOps[ROP].peek(), stitchProblemHandler());
			return;
		}

		switch (opAST.getType()) {
		case StitchTreeWalkerTokenTypes.IMPLIES:
			expr.setResult(!lOp || rOp);
			break;
		case StitchTreeWalkerTokenTypes.IFF:
			expr.setResult(lOp == rOp);
			break;
		case StitchTreeWalkerTokenTypes.LOGICAL_OR:
			expr.setResult(lOp || rOp);
			break;
		case StitchTreeWalkerTokenTypes.LOGICAL_AND:
			expr.setResult(lOp && rOp);
			break;
		default:
			debug("Don't know what logical op to do... :'(");
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#doRelationalExpression(antlr.collections.AST)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void doRelationalExpression(AST opAST) {
		if (checkSkipEval()) return;
		Expression expr = expr();

		boolean compareAsNum = false;
		MyNumber lOp = null;
		MyNumber rOp = null;
		Object lObj = null;
		Object rObj = null;
		// deal with null operands
		if (expr.lrOps[LOP].peek() == null
				|| expr.lrOps[ROP].peek() == null) {
			// if either is NULL, result is NULL
			Tool.warn("One relational operand is NULL: "
					+ expr.lrOps[LOP].pop() + ", "
					+ expr.lrOps[ROP].pop() + " ... "
					+ opAST.toStringList(), stitchProblemHandler());
			expr.setResult(null);
			return;
		}
		// deal with IAcmeProperty operands
		if (expr.lrOps[LOP].peek() instanceof IAcmeProperty) {
			expr.lrOps[LOP].push(MyNumber.newNumber((IAcmeProperty )expr.lrOps[LOP].pop()));
		}
		if (expr.lrOps[ROP].peek() instanceof IAcmeProperty) {
			expr.lrOps[ROP].push(MyNumber.newNumber((IAcmeProperty )expr.lrOps[ROP].pop()));
		}
		if (expr.lrOps[LOP].peek() instanceof MyNumber
				&& expr.lrOps[ROP].peek() instanceof MyNumber) {
			// this means all ops is checkable
			compareAsNum = true;
			lOp = (MyNumber )expr.lrOps[LOP].pop();
			rOp = (MyNumber )expr.lrOps[ROP].pop();
		} else if (expr.lrOps[LOP].peek().getClass().equals(expr.lrOps[ROP].peek().getClass())){
			// only EQ and NE will be checked
			lObj = expr.lrOps[LOP].pop();
			rObj = expr.lrOps[ROP].pop();
		} else {  // type check problem!
			Tool.error("Type mismatch in relational expression! "
					+ expr.lrOps[LOP].peek() + " " + opAST.getText() + " "
					+ expr.lrOps[ROP].peek(), stitchProblemHandler());
			return;
		}

		boolean b;
		switch (opAST.getType()) {
		case StitchTreeWalkerTokenTypes.EQ:
			if (compareAsNum) {
				b = lOp.eq(rOp);
				expr.setResult(b);
			} else {
				b = lObj.equals(rObj);
				expr.setResult(b);
			}
			debug("Compare:  " + lOp + " EQ " + rOp + " -> " + b);
			break;
		case StitchTreeWalkerTokenTypes.NE:
			if (compareAsNum) {
				b = lOp.ne(rOp);
				expr.setResult(b);
			} else {
				b = !lObj.equals(rObj);
				expr.setResult(b);
			}
			debug("Compare:  " + lOp + " NE " + rOp + " -> " + b);
			break;
		case StitchTreeWalkerTokenTypes.LE:
			if (compareAsNum) {
				b = lOp.le(rOp);
				expr.setResult(b);
				debug("Compare:  " + lOp + " LE " + rOp + " -> " + b);
			} else {
				Tool.error("LE is not defined for " + lObj + " & " + rObj, stitchProblemHandler());
			}
			break;
		case StitchTreeWalkerTokenTypes.LT:
			if (compareAsNum) {
				b = lOp.lt(rOp);
				expr.setResult(b);
				debug("Compare:  " + lOp + " LT " + rOp + " -> " + b);
			} else {
				Tool.error("LT is not defined for " + lObj + " & " + rObj, stitchProblemHandler());
			}
			break;
		case StitchTreeWalkerTokenTypes.GT:
			if (compareAsNum) {
				b = lOp.gt(rOp);
				expr.setResult(b);
				debug("Compare:  " + lOp + " GT " + rOp + " -> " + b);
			} else {
				Tool.error("GT is not defined for " + lObj + " & " + rObj, stitchProblemHandler());
			}
			break;
		case StitchTreeWalkerTokenTypes.GE:
			if (compareAsNum) {
				b = lOp.ge(rOp);
				expr.setResult(b);
				debug("Compare:  " + lOp + " GE " + rOp + " -> " + b);
			} else {
				Tool.error("GE is not defined for " + lObj + " & " + rObj, stitchProblemHandler());
			}
			break;
		default:
			debug("Don't know what relational op to do... :'(");
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#doArithmeticExpression(antlr.collections.AST)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doArithmeticExpression(AST opAST) {
		if (checkSkipEval()) return;
		Expression expr = expr();

		// support String concatenation
		if (expr.lrOps[LOP].peek() instanceof String
				&& opAST.getType() == StitchTreeWalkerTokenTypes.PLUS) {
			String s1 = (String )expr.lrOps[LOP].pop();
			Object s2 = expr.lrOps[ROP].pop();
			expr.setResult(s1 + s2.toString());
			return;
		}

		MyNumber lOp = null;
		MyNumber rOp = null;
		if (expr.lrOps[LOP].peek() == null
				|| expr.lrOps[ROP].peek() == null) {
			// if either is NULL, result is NULL
			Tool.warn("One arithmetic operand is NULL: "
					+ expr.lrOps[LOP].pop() + ", "
					+ expr.lrOps[ROP].pop() + " ... "
					+ opAST.toStringList(), stitchProblemHandler());
			expr.setResult(null);
			return;
		}

		// deal with IAcmeProperty operands
		if (expr.lrOps[LOP].peek() instanceof IAcmeProperty) {
			expr.lrOps[LOP].push(MyNumber.newNumber((IAcmeProperty )expr.lrOps[LOP].pop()));
		}
		if (expr.lrOps[ROP].peek() instanceof IAcmeProperty) {
			expr.lrOps[ROP].push(MyNumber.newNumber((IAcmeProperty )expr.lrOps[ROP].pop()));
		}
		if (expr.lrOps[LOP].peek() instanceof MyNumber
				&& expr.lrOps[ROP].peek() instanceof MyNumber) {
			lOp = (MyNumber )expr.lrOps[LOP].pop();
			rOp = (MyNumber )expr.lrOps[ROP].pop();
		} else {  // type check problem!
			Tool.error("Types not Numbers in arithmetic expression! "
					+ expr.lrOps[LOP].peek() + " " + opAST.getText() + " "
					+ expr.lrOps[ROP].peek(), stitchProblemHandler());
			return;
		}

		MyNumber mynum = null;
		switch (opAST.getType()) {
		case StitchTreeWalkerTokenTypes.PLUS:
			mynum = lOp.plus(rOp);
			expr.setResult(mynum);
			debug("Add:  " + lOp + " + " + rOp + " -> " + mynum.toJavaNumber());
			break;
		case StitchLexerTokenTypes.MINUS:
			mynum = lOp.minus(rOp);
			expr.setResult(mynum);
			debug("Subtract:  " + lOp + " - " + rOp + " -> " + mynum.toJavaNumber());
			break;
		case StitchLexerTokenTypes.STAR:
			mynum = lOp.times(rOp);
			expr.setResult(mynum);
			debug("Multiply:  " + lOp + " * " + rOp + " -> " + mynum.toJavaNumber());
			break;
		case StitchLexerTokenTypes.SLASH:
			mynum = lOp.dividedBy(rOp);
			expr.setResult(mynum);
			debug("Divide:  " + lOp + " / " + rOp + " -> " + mynum.toJavaNumber());
			break;
		case StitchLexerTokenTypes.MOD:
			mynum = lOp.modulus(rOp);
			expr.setResult(mynum);
			debug("Mod:  " + lOp + " % " + rOp + " -> " + mynum.toJavaNumber());
			break;
		default:
			debug("Don't know what arithmetic op to do... :'(");
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#doUnaryExpression(antlr.collections.AST)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doUnaryExpression(AST opAST) {
		if (checkSkipEval()) return;
		Expression expr = expr();

		// assume operand is number!
		Boolean bOp = null;
		MyNumber op = null;
		if (expr.lrOps[LOP].peek() == null) {
			// if operand NULL, result NULL
			Tool.warn("Unary operand is NULL: "
					+ expr.lrOps[LOP].pop(), stitchProblemHandler());
			expr.setResult(null);
			return;
		}
		// deal with IAcmeProperty operand
		if (expr.lrOps[LOP].peek() instanceof IAcmeProperty) {
			expr.lrOps[LOP].push(MyNumber.newNumber((IAcmeProperty )expr.lrOps[LOP].pop()));
		}
		if (expr.lrOps[LOP].peek() instanceof MyNumber) {
			op = (MyNumber )expr.lrOps[LOP].pop();
		} else if (opAST.getType() == StitchTreeWalkerTokenTypes.LOGICAL_NOT) {
			if (expr.lrOps[LOP].peek() instanceof Boolean) {
				bOp = (Boolean )expr.lrOps[LOP].pop();
			} else {
				Tool.error("Type of logical not operand NOT Boolean! "
						+ expr.lrOps[LOP].peek(), stitchProblemHandler());
				return;
			}
		} else {  // type check problem!
			Tool.error("Type of unary operand NOT Number! "
					+ opAST.getText() + " " + expr.lrOps[LOP].peek(), stitchProblemHandler());
			return;
		}

		switch (opAST.getType()) {
		case StitchTreeWalkerTokenTypes.INCR:
			expr.setResult(op.incr());
			break;
		case StitchTreeWalkerTokenTypes.DECR:
			expr.setResult(op.decr());
			break;
		case StitchTreeWalkerTokenTypes.LOGICAL_NOT:
			expr.setResult(!bOp);
			break;
		case StitchTreeWalkerTokenTypes.UNARY_MINUS:
			expr.setResult(op.negate());
			break;
		case StitchTreeWalkerTokenTypes.UNARY_PLUS:
			expr.setResult(op);
			break;
		case StitchTreeWalkerTokenTypes.POST_INCR:
		case StitchTreeWalkerTokenTypes.POST_DECR:
			Tool.error("Post increment & decrement not implemented", stitchProblemHandler());
			break;
		default:
			debug("Don't know what unary op to do... :'(");
			break;
		}
	}

	/* (non-Javadoc)
	 * @see org.sa.rainbow.stitch.visitor.AbstractLiloBehavior#doIdentifierExpression(antlr.collections.AST, org.sa.rainbow.stitch.core.Expression.Kind)
	 */
	@Override
	public void doIdentifierExpression(AST idAST, Expression.Kind kind) {
		if (checkSkipEval()) return;
		Expression expr = expr();

		if (kind == Expression.Kind.IDENTIFIER) {
			// find identifier, lookup entire string first
			String iden = idAST.getText();
			Object o = scope().lookup(iden);
			if (o == null) {  // break up dot notation
				int dotIdx = iden.indexOf(".");
				if (dotIdx > -1) {  // looking for v.something
					o = scope().lookup(iden.substring(0, dotIdx));
					if (o != null && o instanceof Var) {
						Var v = (Var )o;
						// find idx sub within object's scope
						o = v.scope.lookup(iden.substring(dotIdx+1));
						if (o == null) {
							// treat var as model element and access rest as its attribute
							String dotVal = iden.substring(dotIdx);
							Object val = v.getValue(); 
							if (val instanceof IAcmeElement) {
								IAcmeElement elem = (IAcmeElement )val;
								o = scope().lookup(elem.getQualifiedName() + dotVal);
								if (o == null) {
									// this may mean an invalid reference to element attribute
									Tool.error("Invalid reference '" + iden + "' encountered!", stitchProblemHandler());
								}
							} else {
								o = scope().lookup(v.name + dotVal);
							}
//							if (o != null && o instanceof IAcmeProperty) {
								// get the Acme Property value
//								o = Tool.deriveValue(((IAcmeProperty )o).getValue());
//							}
						}
					}
				}
			}
			if (o == null) {
				// lookup of various combo failed, could indicate invalid reference
				Tool.error("Unresolved reference '" + iden + "'! Perhaps model not accessible?", stitchProblemHandler());
			} else {
				if (o instanceof Var) {
					Var v = (Var )o;
					if (v.getValue() == null) {
						v.computeValue(m_stitch);
					}
					expr.setResult(((Var )o).getValue());
				} else {  // store the object directly
					expr.setResult(o);
				}
			}
		} else if (kind == Expression.Kind.INTEGER) {
			expr.setResult(new MyInteger(Integer.parseInt(idAST.getText())));
		} else if (kind == Expression.Kind.FLOAT) {
			expr.setResult(new MyDouble(Double.parseDouble(idAST.getText())));
		} else if (kind == Expression.Kind.STRING) {
			// strip the double quotes of string literal
			String s = idAST.getText();
			expr.setResult(s.substring(1, s.length()-1));
		} else if (kind == Expression.Kind.CHAR) {
			// strip the single quotes, so char is at index 1
			expr.setResult(new Character(idAST.getText().charAt(1)));
		} else if (kind == Expression.Kind.BOOLEAN) {
			expr.setResult(new Boolean(Boolean.parseBoolean(idAST.getText())));
		} else if (kind == Expression.Kind.NULL) {
			expr.setResult(null);
		}
	}

	protected boolean checkSkipEval () {
		boolean rv = false;
		if (expr() != null) {
			rv = expr().skipQuanPredicate;
		}

		return rv;
	}

	private void doBeginComplexExpr () {
		// get the next expression in list, checking to see if it's complex.
		// if it is, we're in a straight complex expression;
		// if not, check current expr to see if it is complex, in which case,
		// we're within a complex expression already...
		Expression expr = expr();
		Expression cExpr = null;
		if (expr.expressions().size() > 0) {
			Expression nextExpr = expr.expressions().get(expr.curExprIdx++);
			if (nextExpr.isComplex()) {
				cExpr = nextExpr;
			} else if (expr.isComplex()) {
				cExpr = expr;
			}
		} else {
			if (expr.isComplex()) {
				// this SHOULD be the case, by nature of tree walk
				cExpr = expr;
			}
		}

		if (cExpr != null) {
			// reset expr count for descending into element exprs
			cExpr.curExprIdx = 0;
			setExpression(null);
			pushScope(cExpr);
		}
	}

	private Expression doEndComplexExpr () {
		Expression cExpr = (Expression )scope();
		setExpression((Expression )cExpr.parent());
		popScope();
		return cExpr;
	}

	/**
	 * Executes a method assuming Java... can be refactored and extended later
	 * to support methods in other languages.
	 * @param name  the Method name to evaluate
	 * @param args  the arguments to the method
	 */
	@SuppressWarnings("unchecked")  // suppress error on Class.isAssignableFrom()
	private Object executeMethod (String name, Object[] args) {
		Object rv = null;
		Method method = null;

		// break method call name into class and method parts
		int dotIdx = name.lastIndexOf(".");
		String methodClass = null;
		if (dotIdx > -1) {
			methodClass = name.substring(0, dotIdx);
			// mangle any method class renaming
			if (script().renames.containsKey(methodClass)) {  // replace
				methodClass = script().renames.get(methodClass);
			}
			name = name.substring(dotIdx+1);
		}

		// construct the method's formal parameter list
		Class[] params = new Class[args.length];
		// convert all MyNumber to java Number object first
		for (int i=0; i < args.length; ++i) {
			if (args[i] != null && args[i] instanceof MyNumber) {
				args[i] = ((MyNumber )args[i]).toJavaNumber();
			}
		}
		// populate formal parameter list based on args list
		int i = 0;
		for (Object o : args) {
			if (o != null) {
				params[i++] = o.getClass();
			}
		}

		// construct list of classes in which to search for method name, look in imports
		List<Class> classesToSearch = new ArrayList<Class>();
		for (Class opClass : m_stitch.script.ops) {
			// first, see if method class matches the imported method's class
			if (methodClass != null) {
				if (! opClass.getName().endsWith(methodClass)) {
					// not a match, don't waste time searching its methods
					continue;
				}
			}
			// add to list to search
			classesToSearch.add(opClass);
		}
		if (classesToSearch.size() == 0 && methodClass != null) {
			// attempt to load the method class and search it
			try {
				classesToSearch.add(Class.forName(methodClass));
			} catch (ClassNotFoundException e) {
				if (Tool.logger().isInfoEnabled())
					Tool.logger().info("Attempt to load class " + methodClass + " failed while executing method " + name + "!", e);
			}
		}

		// find this name reference in reduced list of classes
		OUTER: for (Class opClass : classesToSearch) {
			// iterate thru list of declared methods for whose name match,
			// and look to see if supplied param is a proper subtype
			for (Method m : opClass.getDeclaredMethods()) {
				if (m.getName().equals(name)) {  // method name matches, check params
					boolean allParamClassOk = true;
					i = 0;  // track param index for positional comparison
					for (Class c : m.getParameterTypes()) {
						boolean matchPrimitive = true;
						if (c.isAssignableFrom(params[i])) {  // good!
						} else if (c.isPrimitive()) {
							matchPrimitive =
								c.equals(int.class) && Integer.class.equals(params[i])
								|| c.equals(short.class) && Short.class.equals(params[i])
								|| c.equals(long.class) && Long.class.equals(params[i])
								|| c.equals(float.class) && Float.class.equals(params[i])
								|| c.equals(double.class) && Double.class.equals(params[i])
								|| c.equals(boolean.class) && Boolean.class.equals(params[i])
								|| c.equals(byte.class) && Byte.class.equals(params[i])
								|| c.equals(char.class) && Character.class.equals(params[i]);
						}
						if (! matchPrimitive && args[i] != null && args[i] instanceof IAcmeProperty) {
							// check if class would match if we extract the IAcmeProperty
							Object vObj = Tool.deriveValue(((IAcmeProperty )args[i]).getValue());
							if (vObj != null) {
								Class vClass = vObj.getClass();
								if (c.isInstance(vObj)) {
									matchPrimitive = true;  // good!
									// change param class and arg
									args[i] = vObj;
									params[i] = vObj.getClass();
								} else if ((c.equals(int.class) || c.equals(long.class))
										&& vClass.equals(MyInteger.class)) {
									matchPrimitive = true;  // good!
									args[i] = ((MyInteger )vObj).intValue();
									params[i] = int.class;
								} else if ((c.equals(float.class) || c.equals(double.class))
										&& vClass.equals(MyDouble.class)) {
									matchPrimitive = true;  // good!
									args[i] = ((MyDouble )vObj).doubleValue();
									params[i] = double.class;
								}
							}
						}
						allParamClassOk &= matchPrimitive;
						++i;
					}
					if (allParamClassOk) {  // found a method!!
						if (Modifier.isStatic(m.getModifiers())) {
							method = m;
							break OUTER;
						} else {
							Tool.error("Applicable method for " + name
									+ " is NOT STATIC; invocation will fail!",
									stitchProblemHandler());
						}
					}
				}
			}
		}
		// check if we should continue to actually invoke method
		if (! m_stitch.isCanceled()) {
			if (method == null) {  // cannot execute anything
				// try method in ModelOperator using name and first argument
				if (args.length > 0) {
					rv = Ohana.instance().modelOperator().invoke(name, args);
				}
				if (rv == null) {
					Tool.error("No applicable method found for " + name, stitchProblemHandler());
				}
			} else {
				try {
					rv = method.invoke(null, args);
				} catch (IllegalAccessException e) {
					debug(e.toString());
				} catch (InvocationTargetException e) {
					scope().setError(true);
					Tool.error("Method invocation failed! " + method.toString(), e, stitchProblemHandler());
					e.printStackTrace();
				}
			}
			// check if OperatorResult, and if result is a failure
			if (rv != null && rv instanceof String) {
				ModelOperator.OperatorResult opResult =
					ModelOperator.OperatorResult.parseEffectorResult((String )rv);
				switch (opResult) {
				case UNKNOWN:
					Tool.error("No effector found corresponding to method '" + name + "'!", stitchProblemHandler());
					break;
				case FAILURE:
					// bad, set state to indicate failure occurred
					scope().setError(true);
					Tool.error("Method invocation did not succeeed! " + name, stitchProblemHandler());
					break;
				}
			}
		}
		return rv;
	}
}
