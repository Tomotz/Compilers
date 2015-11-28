package slp;

import java.io.IOException;

/** Evaluates straight line programs.
 */
public class SLPEvaluator implements PropagatingVisitor<Environment, Integer> {
	protected ASTNode root;

	/** Constructs an SLP interpreter for the given AST.
	 * 
	 * @param root An SLP AST node.
	 */
	public SLPEvaluator(ASTNode root) {
		this.root = root;
	}
	
	/** Interprets the AST passed to the constructor.
	 */
	public void evaluate() {
		Environment env = new Environment();
		root.accept(this, env);
	}
	
	public Integer visit(ASTStmtList stmts, Environment env) {
		for (ASTStmt st : stmts.statements) {
			st.accept(this, env);
		}
		return null;
	}

	public Integer visit(ASTStmt stmt, Environment env) {
		throw new UnsupportedOperationException("Unexpected visit of Stmt!");
	}


	public Integer visit(ASTAssignStmt stmt, Environment env) {
		ASTExpr rhs = stmt.rhs;
		Integer expressionValue = rhs.accept(this, env);
		ASTVarExpr var = stmt.varExpr;
		env.update(var, expressionValue);
		return null;
	}

	public Integer visit(ASTExpr expr, Environment env) {
		throw new UnsupportedOperationException("Unexpected visit of Expr!");
	}

	public Integer visit(ASTVarExpr expr, Environment env) {
		return env.get(expr);
	}

	public Integer visit(ASTNumberExpr expr, Environment env) {
		return new Integer(expr.value);		
		// return expr.value; also works in Java 1.5 because of auto-boxing
	}

	public Integer visit(ASTUnaryOpExpr expr, Environment env) {
		Operator op = expr.op;
		if (op != Operator.MINUS)
			throw new RuntimeException("Encountered unexpected operator " + op);
		Integer value = expr.operand.accept(this, env);
		return new Integer(- value.intValue());
	}

	public Integer visit(ASTBinaryOpExpr expr, Environment env) {
		Integer lhsValue = expr.lhs.accept(this, env);
		int lhsInt = lhsValue.intValue();
		Integer rhsValue = expr.rhs.accept(this, env);
		int rhsInt = rhsValue.intValue();
		int result;
		switch (expr.op) {
		case DIV:
			if (rhsInt == 0)
				throw new RuntimeException("Attempt to divide by zero: " + expr);
			result = lhsInt / rhsInt;
			break;
		case MINUS:
			result = lhsInt - rhsInt;
			break;
		case MULT:
			result = lhsInt * rhsInt;
			break;
		case PLUS:
			result = lhsInt + rhsInt;
			break;
		case LT:
			result = lhsInt < rhsInt ? 1 : 0;
			break;
		case GT:
			result = lhsInt > rhsInt ? 1 : 0;
			break;
		case LE:
			result = lhsInt <= rhsInt ? 1 : 0;
			break;
		case GE:
			result = lhsInt >= rhsInt ? 1 : 0;
			break;
		case LAND:
			result = (lhsInt!=0 && rhsInt!=0) ? 1 : 0;
			break;
		case LOR:
			result = (lhsInt!=0 || rhsInt!=0) ? 1 : 0;
			break;
		default:
			throw new RuntimeException("Encountered unexpected operator type: " + expr.op);
		}
		return new Integer(result);
	}

	@Override
	public Integer visit(ASTClassList astClassList, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visit(ASTIdList astIdList, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visit(ASTField astField, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visit(ASTExtend astExtend, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visit(ASTfmList astGenList, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer visit(ASTRoot astGenNode, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}
}