package slp;

import java.io.IOException;

/** Evaluates straight line programs.
 */
public class ICEvaluator implements PropagatingVisitor<Environment, String> {
	protected ASTNode root;

	/** Constructs an SLP interpreter for the given AST.
	 * 
	 * @param root An SLP AST node.
	 */
	public ICEvaluator(ASTNode root) {
		this.root = root;
	}
	
	/** Interprets the AST passed to the constructor.
	 */
	public void evaluate() {
		Environment env = new Environment();
		root.accept(this, env);
	}
	
	public String visit(ASTStmtList stmts, Environment env) {
		for (ASTStmt st : stmts.statements) {
			st.accept(this, env);
		}
		return null;
	}

	public String visit(ASTStmt stmt, Environment env) {
		throw new UnsupportedOperationException("Unexpected visit of Stmt!");
	}


	public String visit(ASTAssignStmt stmt, Environment env) {
		ASTExpr rhs = stmt.rhs;
		//Integer expressionValue = rhs.accept(this, env);
		//ASTVarExpr var = stmt.varExpr;
		//env.update(var, expressionValue);
		return null;
	}

	public String visit(ASTExpr expr, Environment env) {
		throw new UnsupportedOperationException("Unexpected visit of Expr!");
	}

	public String visit(ASTVarExpr expr, Environment env) {
		return null;// env.get(expr);
	}

	public String visit(ASTNumberExpr expr, Environment env) {
		return null;//new Integer(expr.value);		
		// return expr.value; also works in Java 1.5 because of auto-boxing
	}

	public String visit(ASTUnaryOpExpr expr, Environment env) {
		Operator op = expr.op;
		if (op != Operator.MINUS)
			throw new RuntimeException("Encountered unexpected operator " + op);
		//Integer value = expr.operand.accept(this, env);
		return null;// new Integer(- value.intValue());
	}

	public String visit(ASTBinaryOpExpr expr, Environment env) {
		/*Integer lhsValue = expr.lhs.accept(this, env);
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
		case MULTIPLY:
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
		case LTE:
			result = lhsInt <= rhsInt ? 1 : 0;
			break;
		case GTE:
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
		}*/
		return null;//new Integer(result);
	}

	@Override
	public String visit(ASTClassList astClassList, Environment d) {
		for (ASTClassDecl cls : astClassList.lst) {
			cls.accept(this, d);
		}
		return null;
	}

	@Override
	public String visit(ASTIdList astIdList, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTField astField, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTExtend astExtend, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTfmList astGenList, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTRoot root, Environment d) {
		root.child.accept(this, d);
		return null;
	}

	@Override
	public String visit(ASTFormalList astFormalList, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTStatType astStatType, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTMethod astMethod, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTClassDecl astClassDecl, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}
}