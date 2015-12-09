package slp;

import java.util.List;

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
		if (expr.name.equals("this")) return env.lastClass.name;
		else 
			throw new RuntimeException("Error!!! should never reach this code. parser at line " + expr.line);
	}
	
	
	public String visit(ASTNumberExpr expr, Environment env) {
		throw new RuntimeException("Error!!! should never reach this code. parser at line " + expr.line);

	}
	
	
	public String visit(ASTUnaryOpExpr expr, Environment env) {
		Operator op = expr.op;
		ASTExpr rhs = expr.operand;
		String rhsType = rhs.accept(this, env);	
		if (op == Operator.MINUS){
			if (rhsType.equals("int")) return "int";
			else throw new RuntimeException("Line " + expr.line + ": Expected an Integer after '-' ");
		}
		if(op == Operator.LNEG){
			if (rhsType.equals("boolean")) return "boolean"; 
			else throw new RuntimeException("Line " + expr.line + ": Expected a Boolean expression after '!' ");
		}
		else
			throw new RuntimeException("Line " + expr.line + ": Encountered unexpected operator " + op);
		//Integer value = expr.operand.accept(this, env);
		// new Integer(- value.intValue());
	}

	public String visit(ASTBinaryOpExpr expr, Environment env) {
		Operator op = expr.op;
		ASTExpr lhs = expr.lhs;
		ASTExpr rhs = expr.rhs;
		String lhsType = lhs.accept(this, env);
		String rhsType = rhs.accept(this, env);
		if (op == Operator.LAND || op == Operator.LOR){
			if (!lhsType.equals("boolean"))
				throw new RuntimeException("Line " + expr.line + ": Expected a Boolean expression before " + op);
			if (!rhsType.equals("boolean"))
				throw new RuntimeException("Line " + expr.line + ": Expected a Boolean expression after " + op);
			else
				return "boolean";
		}
		if (op == Operator.PLUS){
			if (lhsType.equals("string") || lhsType.equals("int")){
				if (lhsType.equals(rhsType))return lhsType;
				else 
					throw new RuntimeException("Line " + expr.line + 
							": Expected operands of same type for the binary operator " + op);
			} else
				throw new RuntimeException("Line " + expr.line + 
						": The binary operator '+' accepts only Integer or String types as operands");
		}
		if (op == Operator.MINUS || op == Operator.DIV || op == Operator.MULTIPLY || op == Operator.MOD){
			if (!(lhsType.equals("int") && rhsType.equals("int")))
				throw new RuntimeException("Line " + expr.line + 
						": The binary operator '" + op + "' accepts only Integer types as operands");
			else
				return "int";
		}
		if (op == Operator.GT || op == Operator.GTE || op == Operator.LT || op == Operator.LTE){
			if (!(lhsType.equals("int") && rhsType.equals("int")))
				throw new RuntimeException("Line " + expr.line + 
						": The binary operator '" + op + "' accepts only Integer types as operands");
			else 
				return "boolean";
		}
		if (op == Operator.EQUAL || op == Operator.NEQUAL){
			if (run_num == 0) return "boolean";
			if (run_num == 1){
				if (lhsType.equals(rhsType) || lhsType.equals("null") || rhsType.equals("null"))
					return "boolean";
				// if reached here - lhsType != rhsType
				if (lhsType.equals("string")|| lhsType.equals("int") || lhsType.equals("boolean") || 
						rhsType.equals("string")|| rhsType.equals("int") || rhsType.equals("boolean"))
					throw new RuntimeException("Line " + expr.line + 
							": Type mismatch for the operands of '" + op + "'");
				//if reached here - lhsType and rhsType are not primitive types
				//
				//check if lhsType extends rhsType:
				icObject lhsClass = env.getObjByName(lhsType);	
				if (!(lhsClass instanceof icClass)) 
					throw new RuntimeException("Line " + expr.line + 
							": Undefined type: " + lhsType);
				String parent = ((icClass)lhsClass).ext;
				icObject parentClass = env.getObjByName(parent);
				if (!(parentClass instanceof icClass)) 
					throw new RuntimeException("Line " + expr.line + 
							": Unexpected error!!! undefined parent class - should have catched this before");
				while (parent != null){
					if (rhsType.equals(parent)) return "boolean";
					else
						parent = ((icClass)parentClass).ext;
				}
				// if reached here - lhsType doesn't extend rhsType
				//
				//check if rhsType extends lhsType
				icObject rhsClass = env.getObjByName(rhsType);	
				if (!(rhsClass instanceof icClass)) 
					throw new RuntimeException("Line " + expr.line + 
							": Undefined type: " + rhsType);
				parent = ((icClass)rhsClass).ext;
				parentClass = env.getObjByName(parent);
				if (!(parentClass instanceof icClass)) 
					throw new RuntimeException("Line " + expr.line + 
							": Unexpected error!!! undefined parent class - should have catched this before");
				while (parent != null){
					if (lhsType.equals(parent)) return "boolean";
					else
						parent = ((icClass)parentClass).ext;
				}
				//if reached here - rhsType doesn't extend lhsType
				throw new RuntimeException("Line " + expr.line + 
						": Type mismatch for operands of '" + op +"'");
			}
			else throw new RuntimeException("Error!!!! should never reach this line of code");
		}
		else
			throw new RuntimeException("Error!!!! should never reach this line of code");

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
		ASTExpr expr = stm.expr;
		String ExprType = expr.accept(this, env);
		if (!ExprType.equals("boolean"))
			throw new RuntimeException("Line " + expr.line + 
					": Expected boolean expression after 'while'");
		stm.stmt.accept(this, env);
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
	public String visit(ASTVirtualCall vc, Environment env) {
		icObject f = env.getObjByName(vc.id);
		//check vc is a valid method:
		if (!(f instanceof icFunction))
			throw new RuntimeException("Line " + vc.line + 
					": The method '" + vc.id + "' has not been declared");
		List<String> formal_params = ((icFunction)f).arg_types;
		List<ASTExpr> exprList = vc.exprList.lst;
		
		//check number of arguments:
		if (formal_params.size() != exprList.size())
			throw new RuntimeException("Line " + vc.line + 
					": Wrong number of arguments for the method '"+ vc.id + "'");
		
		//check argument types
		for (int i = 0; i<formal_params.size(); i++){
			String formal = formal_params.get(i);
			String found = exprList.get(i).accept(this, env);
			if (!formal.equals(found))
				throw new RuntimeException("Line " + vc.line + 
						": Wrong argument types for the method '"+ vc.id + "'");
		}
		return ((icFunction)f).retType;
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
		throw new RuntimeException("error!!! should never reach this part of code");
	}

}