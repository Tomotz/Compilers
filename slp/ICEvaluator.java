package slp;


/** Evaluates straight line programs.
 */
public class ICEvaluator implements PropagatingVisitor<Environment, String> {
	protected ASTNode root;
	static Boolean IS_DEBUG = true;
	static int run_num = 0;

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
		//TODO: add Library!!!!!!!!!!
		if (IS_DEBUG)
			System.out.println("starting first itteration");
		root.accept(this, env);
		++run_num;
		if (IS_DEBUG)
			System.out.println("starting second itteration");
		root.accept(this, env);
	}

	private void error(String str, ASTNode n) {
		throw new RuntimeException(str + " at line: " +
			Integer.toString(n.line));
	}
	
	public String visit(ASTStmtList stmts, Environment env) {
		for (ASTNode st : stmts.statements) {
			st.accept(this, env);
		}
		return null;
	}

	public String visit(ASTStmt stmt, Environment env) {
		throw new UnsupportedOperationException("Unexpected visit of Stmt!");
	}
	
	// have to deal with arrays
	public String visit(ASTAssignStmt stmt, Environment env) {
		icObject father;
		int flag =0;
		String varExpr = stmt.varExpr.accept(this, env);
		icObject var = env.getObjByName(varExpr);
		String rhs = stmt.rhs.accept(this, env);
		icObject value = env.getObjByName(rhs);
		
		if (var instanceof icClass){
			father = value;
			while (father != null){
				if (father.getName().equals(var.getName())){
					flag =1;
					break;
				}
				father =  env.getObjByName(father.getAssignType());
			}
		}
		else if (var.getAssignType().equals(value.getAssignType())){
			flag =1;
		}
		
		if (flag ==0){
			throw new UnsupportedOperationException("Type mismatch: cannot convert from" + value.getAssignType() + "to" + var.getAssignType());
		}
		else{
			// assign value to variable
		}
		
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
		if (IS_DEBUG)
			System.out.println("accepting classList");
		for (ASTClassDecl cls : astClassList.lst) {
			cls.accept(this, d);
		}
		return null;
	}

	@Override
	public String visit(ASTIdList astIdList, Environment d) {
		/*should not be called. do nothing*/
		return null;
	}

	@Override
	public String visit(ASTField astField, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting fields: " + astField.ids.lst);
		for (String field : astField.ids.lst) {
			icVariable o = new icVariable(field, ASTNode.scope, astField.type);
			d.add(o);
			d.lastClass.addObject(o, d, false);
		}
		return null;
	}

	@Override
	public String visit(ASTExtend astExtend, Environment d) {
		/*should not be called. do nothing*/
		return null;
	}

	@Override
	public String visit(ASTfmList fmList, Environment d) {
		/*should not be called. do nothing*/
		return null;
	}

	@Override
	public String visit(ASTRoot root, Environment d) {
		root.child.accept(this, d);
		return null;
	}

	@Override
	public String visit(ASTFormalList astFormalList, Environment d) {
		/*should not be called. do nothing*/
		return null;
	}

	@Override
	public String visit(ASTStatType astStatType, Environment d) {
		/*should not be called. do nothing*/
		return null;
	}

	@Override
	public String visit(ASTMethod meth, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting method: " + meth.id);
		String classType = d.lastClass.name;
		icFunction func = new icFunction(meth.id, ASTNode.scope, meth.type, 
				classType, meth.isStatic);
		d.lastFunc = func;
		if (meth.isStatic)
			d.lastClass.addObject(func, d, true);
		else
			d.lastClass.addObject(func, d, false);
		d.add(func);
			
		++ASTNode.scope;
		for (Formal formal : meth.formals.lst)
		{
			d.lastFunc.arg_types.add(formal.type);
			icVariable v = new icVariable(formal.id,
					ASTNode.scope, formal.type);
			d.add(v);
		}
		
		for (ASTStmt s : meth.stmts.statements) {
			s.accept(this, d);
		}
		d.destroyScope(ASTNode.scope);
		--ASTNode.scope;
		return null;
	}

	@Override
	public String visit(ASTClassDecl cls, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting classDecl: " + cls.class_id);
		icClass c = new icClass(cls.class_id, ASTNode.scope);
		c.ext = cls.extend.name;
		if (run_num == 1 && cls.extend.name != null && cls.extend.name != "")
		{
			icObject father = d.getObjByName(cls.extend.name);
			if (!(father instanceof icClass))
			{
				error("unknown parent class", cls.extend);
			}
		}
		d.add(c);
		d.lastClass = c;
		++ASTNode.scope;
		for (ASTNode fm : cls.fieldmeths.lst) {
			fm.accept(this, d);
		}
		d.destroyScope(ASTNode.scope);
		--ASTNode.scope;
		return null;
	}


	@Override
	public String visit(ASTDotLength expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting expr.length");
		String e = expr.e.accept(this, d);
		if (e.endsWith("[]"))
			return "int";
		else
		{
			error("tried accessing length field of non array expression: "
					+e, expr);
		}
		return null;
	}

	@Override
	public String visit(ASTNewArray expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting new array");
		String index_type = expr.expr.accept(this, d);
		if ("int" != index_type)
			error("bad indexer type. expected int, got: " + index_type, 
					expr);
		if (run_num == 1)
		{
			d.validateType(expr.type);
		}
		return expr.type;
	}

	@Override
	public String visit(ASTNewObject expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting new object");
		if (run_num == 1)
		{
			d.validateType(expr.type);
		}
		return expr.type;
	}

	@Override
	public String visit(ASTElseStmt stmt, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTAssignFormals stmt, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTWhileStmt stmt, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTVarStmt stmt, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTRetExp expr, Environment env) {
		String rExpr = expr.exp.accept(this, env);
		icObject retExp = env.getObjByName(rExpr);
		if (!env.lastFunc.getAssignType().equals(retExp.getAssignType())){
			throw new UnsupportedOperationException("Type mismatch: cannot convert from" + retExp.getAssignType() + "to" + env.lastFunc.getAssignType());
		}
		return null;
	}

	@Override
	public String visit(ASTIfElseStmt stmt, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTCallStmt stmt, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTLocation expr, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTVirtualCall expr, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTStaticCall expr, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTLiteral expr, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTExprList expr, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}