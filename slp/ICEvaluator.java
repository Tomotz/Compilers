package slp;

/**
 * Evaluates straight line programs.
 */
public class ICEvaluator implements PropagatingVisitor<Environment, String> {
	protected ASTNode root;
	static Boolean IS_DEBUG = true;
	static int run_num = 0;

	/**
	 * Constructs an SLP interpreter for the given AST.
	 * 
	 * @param root
	 *            An SLP AST node.
	 */
	public ICEvaluator(ASTNode root) {
		this.root = root;
	}

	/**
	 * Interprets the AST passed to the constructor.
	 */
	public void evaluate() {
		Environment env = new Environment();
		// TODO: add Library!!!!!!!!!!
		if (IS_DEBUG)
			System.out.println("starting first itteration");
		root.accept(this, env);
		++run_num;
		if (IS_DEBUG)
			System.out.println("starting second itteration");
		root.accept(this, env);
	}

	private void error(String str, ASTNode n) {
		throw new RuntimeException(str + " at line: " + Integer.toString(n.line));
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

	// if object is array its range is expected to be checked before
	// handle basic variables
	public String visit(ASTAssignStmt stmt, Environment env) {

		int flag = 0;
		String varExpr = stmt.varExpr.accept(this, env);
		icObject var = env.getObjByName(varExpr);
		String rhs = stmt.rhs.accept(this, env);
		icObject value = env.getObjByName(rhs);

		// value is a number or a string
		if (value == null) {
			if (rhs.equals("integer") || rhs.equals("string") || rhs.equals("boolean")) {
				flag = 1;
			} else if (utility.isInteger(varExpr) && var.getAssignType().equals("integer")) {
				flag = 1;
			} else if ((rhs.equals("true") || rhs.equals("false")) && var.getAssignType().equals("boolean")) {
				flag = 1;
			}

		}

		else if (var instanceof icClass) {
			if (((icClass) var).checkIfSubType(value, var, env) == true) {
				flag = 1;
			}
		} else {
			if (var.getAssignType().equals(value.getAssignType())) {
				flag = 1;
			}

			if (flag == 0) {
				throw new UnsupportedOperationException(
						"Type mismatch: cannot convert from" + value.getAssignType() + "to" + var.getAssignType());
			} else {
				// assign value to variable
			}
		}
		// Integer expressionValue = rhs.accept(this, env);
		// ASTVarExpr var = stmt.varExpr;
		// env.update(var, expressionValue);
		return null;
	}

	public String visit(ASTExpr expr, Environment env) {
		throw new UnsupportedOperationException("Unexpected visit of Expr!");
	}

	public String visit(ASTVarExpr expr, Environment env) {
		return null;// env.get(expr);
	}

	public String visit(ASTNumberExpr expr, Environment env) {
		return null;// new Integer(expr.value);
		// return expr.value; also works in Java 1.5 because of auto-boxing
	}

	public String visit(ASTUnaryOpExpr expr, Environment env) {
		Operator op = expr.op;
		if (op != Operator.MINUS)
			throw new RuntimeException("Encountered unexpected operator " + op);
		// Integer value = expr.operand.accept(this, env);
		return null;// new Integer(- value.intValue());
	}

	public String visit(ASTBinaryOpExpr expr, Environment env) {
		/*
		 * Integer lhsValue = expr.lhs.accept(this, env); int lhsInt =
		 * lhsValue.intValue(); Integer rhsValue = expr.rhs.accept(this, env);
		 * int rhsInt = rhsValue.intValue(); int result; switch (expr.op) { case
		 * DIV: if (rhsInt == 0) throw new RuntimeException(
		 * "Attempt to divide by zero: " + expr); result = lhsInt / rhsInt;
		 * break; case MINUS: result = lhsInt - rhsInt; break; case MULTIPLY:
		 * result = lhsInt * rhsInt; break; case PLUS: result = lhsInt + rhsInt;
		 * break; case LT: result = lhsInt < rhsInt ? 1 : 0; break; case GT:
		 * result = lhsInt > rhsInt ? 1 : 0; break; case LTE: result = lhsInt <=
		 * rhsInt ? 1 : 0; break; case GTE: result = lhsInt >= rhsInt ? 1 : 0;
		 * break; case LAND: result = (lhsInt!=0 && rhsInt!=0) ? 1 : 0; break;
		 * case LOR: result = (lhsInt!=0 || rhsInt!=0) ? 1 : 0; break; default:
		 * throw new RuntimeException("Encountered unexpected operator type: " +
		 * expr.op); }
		 */
		return null;// new Integer(result);
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
		/* should not be called. do nothing */
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
		/* should not be called. do nothing */
		return null;
	}

	@Override
	public String visit(ASTfmList fmList, Environment d) {
		/* should not be called. do nothing */
		return null;
	}

	@Override
	public String visit(ASTRoot root, Environment d) {
		root.child.accept(this, d);
		return null;
	}

	@Override
	public String visit(ASTFormalList astFormalList, Environment d) {
		/* should not be called. do nothing */
		return null;
	}

	@Override
	public String visit(ASTStatType astStatType, Environment d) {
		/* should not be called. do nothing */
		return null;
	}

	@Override
	public String visit(ASTMethod meth, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting method: " + meth.id);
		String classType = d.lastClass.name;
		icFunction func = new icFunction(meth.id, ASTNode.scope, meth.type, classType, meth.isStatic);
		d.lastFunc = func;
		if (meth.isStatic)
			d.lastClass.addObject(func, d, true);
		else
			d.lastClass.addObject(func, d, false);
		d.add(func);

		++ASTNode.scope;
		for (Formal formal : meth.formals.lst) {
			d.lastFunc.arg_types.add(formal.type);
			icVariable v = new icVariable(formal.id, ASTNode.scope, formal.type);
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
		if (run_num == 1 && cls.extend.name != null && cls.extend.name != "") {
			icObject father = d.getObjByName(cls.extend.name);
			if (!(father instanceof icClass)) {
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
		else {
			error("tried accessing length field of non array expression: " + e, expr);
		}
		return null;
	}

	@Override
	public String visit(ASTNewArray expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting new array");
		String index_type = expr.expr.accept(this, d);
		if ("int" != index_type)
			error("bad indexer type. expected int, got: " + index_type, expr);
		if (run_num == 1) {
			d.validateType(expr.type);
		}
		return expr.type;
	}

	@Override
	public String visit(ASTNewObject expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting new object");
		if (run_num == 1) {
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
	public String visit(ASTAssignFormals stmt, Environment env) {
		String type = stmt.form.type;
		String id = stmt.form.id;
		String rhs = stmt.rhs.accept(this,env);
		icVariable var = new icVariable(id,ASTNode.scope,type);
		env.add(var);
		if (var != null){
			//
		}
		return null;
	}

	@Override
	public String visit(ASTWhileStmt stm, Environment env) {
		return null;
		
	}

	@Override
	public String visit(ASTVarStmt stmt, Environment env) {
		if (env.loopScope == false) {
			throw new UnsupportedOperationException(stmt.name + "cannot be used outside of a loop");
		}
		return null;
	}

	@Override
	// handle non-simple objects
	public String visit(ASTRetExp expr, Environment env) {
		String rExpr = expr.exp.accept(this, env);
		icObject retExp = env.getObjByName(rExpr);
		if (!env.lastFunc.getAssignType().equals(retExp.getAssignType())) {
			throw new UnsupportedOperationException("Type mismatch: cannot convert from" + retExp.getAssignType() + "to"
					+ env.lastFunc.getAssignType());
		}
		return null;
	}

	@Override
	public String visit(ASTIfElseStmt stmt, Environment env) {
		String cond = stmt.expr.accept(this, env);
		String ifStmt = stmt.stmt.accept(this, env);
		icObject body = env.getObjByName(ifStmt);
		if (!cond.equals("boolean")) {
			throw new UnsupportedOperationException("Type mismatch: cannot convert from" + cond + "to" + "boolean");
		}

		return null;
	}

	@Override
	public String visit(ASTCallStmt stmt, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(ASTLocation expr, Environment env) {
		String exp1 = expr.e1.accept(this, env);
		String exp2 = expr.e2.accept(this, env);
		String id = expr.id;
		icObject ident = env.getObjByName(id);
		
		if(expr.type == 0){
			if (ident == null){
				throw new UnsupportedOperationException(id + "cannot be resolved!");
			}
			return id;
		}
		else if(expr.type ==1){
			
			if (ident == null){
				throw new UnsupportedOperationException(id + "cannot be resolved!");
			}
			
			// e1 is a name of a class
			if (ident instanceof icClass ){
				if(((icClass) ident).hasObject(exp1) == false) {
					throw new UnsupportedOperationException(exp1 + "cannot be resolved or is not a field");
				}
				else{
					return ((icClass) ident).lastSubObject.name;
				}
			}
			
		}
		else if (expr.type == 2){
			icVariable var =  (icVariable) env.getObjByName(exp1);
			icObject inVar =  env.getObjByName(exp2);
			
			
			if (var == null){                           // the variable doesn't exist
				throw new UnsupportedOperationException(exp1 + "cannot be resolved to a variable");
			}
			
			if (var.isArray == false){                  // the variable is not an array
				throw new UnsupportedOperationException("The type of the expression must be an array type but it resolved to" + var.getAssignType());
			}
			
			
			if (inVar == null){
				if (!exp2.equals("integer") || !utility.isInteger(exp2)){   // checking if ext2 has a value or a representation of an integer
				throw new UnsupportedOperationException(id + "cannot be resolved to a variable");
				}
			}
			
			if (!inVar.getAssignType().equals("integer")){  // checking if ext is a name of variable of type integer
				throw new UnsupportedOperationException("Type mismatch: cannot convert from" + inVar.getAssignType() +  "to int");
			}
			
			return exp1;
			
			
		}
		return null;
	}

	@Override
	public String visit(ASTVirtualCall expr, Environment d) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public String visit(ASTStaticCall expr, Environment env) {
		String expLst = expr.exprList.accept(this, env);
		
		String classId = expr.classId;
		icClass className = (icClass) env.getObjByName(classId);
		String funcId = expr.id;
		
		if (className == null){                           // the class doesn't exist
			throw new UnsupportedOperationException(className + "cannot be resolved to a variable");
		}
		
		if (!className.hasObject(funcId)){
			throw new UnsupportedOperationException(funcId + "cannot be resolved or is not a function");
		}
		icFunction func = (icFunction) env.getObjByName(funcId);
		
		if (expr.exprList.lst.size() != func.getNumofArg()){
			throw new UnsupportedOperationException("The method" + funcId + "in the type" +  classId + "is not applicable for the arguments");
		}
		for (int i =0; i<expLst.length();i++){
			// have to figure a solution to check if the cariables are valid
		}
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