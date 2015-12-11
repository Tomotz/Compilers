package slp;

import java.util.List;

/**
 * Evaluates straight line programs.
 */
public class ICEvaluator implements PropagatingVisitor<Environment, VarType> {
	protected ASTNode root;
	static Boolean IS_DEBUG = false;
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
		if (IS_DEBUG)
			System.out.println("starting first itteration");
		root.accept(this, env);
		++run_num;
		if (IS_DEBUG)
			System.out.println("starting second itteration");
		root.accept(this, env);
	}

	private void error(String str, ASTNode n) {
		if (n != null)
		{
			throw new RuntimeException("\nLine " + n.line + ": " + str);
			//System.out.println("\nLine " + n.line + ": " + str);
		}
		else
		{
			throw new RuntimeException(str);
			//System.out.println(str);
		}
			
	}

	public VarType visit(ASTStmtList stmts, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTStmtList at line: " + stmts.line);
		for (ASTNode st : stmts.statements) {
			st.accept(this, env);
		}
		return null;
	}

	public VarType visit(ASTStmt stmt, Environment env) {
		error("Unexpected visit of Stmt!", stmt);
		return null;
	}

	// if object is array its range is expected to be checked before
	// handle basic variables
	public VarType visit(ASTAssignStmt stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTAssignStmt at line: " + stmt.line);

		int flag = 0;
		VarType varExpr_type = stmt.varExpr.accept(this, env);
		String varExpr = varExpr_type.type;
		icObject var = env.getObjByName(varExpr);
		VarType rhs_type = stmt.rhs.accept(this, env);
		String rhs = rhs_type.type;
		icObject value = env.getObjByName(rhs);
		if (varExpr_type.num_arrays != rhs_type.num_arrays)
		{
			error("Type mismatch: cannot convert from" + rhs_type + 
					"to" + varExpr_type,	stmt);			
		}

		// value is a number or a string
		if (value == null) {
			if (rhs.equals(varExpr)) {
				flag = 1;
				}
			}
		
		else if (var instanceof icClass) {
			// if the value is a son of a father class
			if (((icClass) var).checkIfSubType(var, env) == true) {
				flag = 1;
			}
		}


		if (flag == 0) {
			error("Type mismatch: cannot convert from" + value.getAssignType() + 
					"to" + var.getAssignType(),	stmt);
		}

		// Integer expressionValue = rhs.accept(this, env);
		// ASTVarExpr var = stmt.varExpr;
		// env.update(var, expressionValue);
		return null;
	}

	public VarType visit(ASTExpr expr, Environment env) {
		error("Unexpected visit of Expr!", expr);
		return null;
	}

	public VarType visit(ASTVarExpr expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTVarExpr at line: " + expr.line);

		if (expr.name.equals("this"))
			return new VarType(env.lastClass.name);
		else
			error("Error!!! should never reach this code", expr);
		return null;
	}

	public VarType visit(ASTNumberExpr expr, Environment env) {
		error("Error!!! should never reach this code", expr);
		return null;
	}

	public VarType visit(ASTUnaryOpExpr expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTUnaryOpExpr at line: " + expr.line);
		Operator op = expr.op;
		ASTExpr rhs = expr.operand;
		VarType rhsType_type = rhs.accept(this, env);
		if (rhsType_type.num_arrays != 0)
		{
			error ("cannot evaluate unary op on array type", expr);
		}
		String rhsType = rhsType_type.type;
		if (op == Operator.MINUS) {
			if (rhsType.equals("int"))
				return new VarType("int");
			else
				error ("Expected an Integer after '-' ", expr);
		}
		if (op == Operator.LNEG) {
			if (rhsType.equals("boolean"))
				return new VarType("boolean");
			else
				error("Expected a Boolean expression after '!' ", expr);
		} else
			error("Encountered unexpected operator " + op, expr);
		// Integer value = expr.operand.accept(this, env);
		// new Integer(- value.intValue());
		return null;
	}

	public VarType visit(ASTBinaryOpExpr expr, Environment env)
	{
		if (IS_DEBUG)
			System.out.println("accepting ASTBinaryOpExpr at line: " + expr.line);
		Operator op = expr.op;
		ASTExpr lhs = expr.lhs;
		ASTExpr rhs = expr.rhs;
		VarType lhsType_type = lhs.accept(this, env);
		VarType rhsType_type = rhs.accept(this, env);
		if (rhsType_type.num_arrays != 0 || lhsType_type.num_arrays != 0 )
		{
			error ("cannot evaluate binary op on array type", expr);
		}
		String rhsType = rhsType_type.type;
		String lhsType = lhsType_type.type;
		
		if (op == Operator.LAND || op == Operator.LOR) 
		{
			if (!lhsType.equals("boolean"))
				error("Expected a Boolean expression before " + op, expr);
			if (!rhsType.equals("boolean"))
				error("Expected a Boolean expression after " + op, expr);
			else
				return new VarType("boolean");
		}
		if (op == Operator.PLUS)
		{
			if (IS_DEBUG)
				System.out.println("check: lhs " + lhsType + " rhs " + rhsType);
			if (lhsType.equals("string") || lhsType.equals("int")) 
			{
				if (lhsType.equals(rhsType))
					return new VarType(lhsType);
				else
					error("Expected operands of same type for the binary operator " + op +
							"got lhs: " + lhsType + ". rhs: " + rhsType, expr);
			} 
			else
				error("The binary operator '+' accepts only Integer or String types as operands"+
						"got lhs: " + lhsType + ". rhs: " + rhsType, expr);
		}
		if (op == Operator.MINUS || op == Operator.DIV || op == Operator.MULTIPLY || op == Operator.MOD)
		{
			if (!(lhsType.equals("int") && rhsType.equals("int")))
				error("The binary operator '" + op
						+ "' accepts only Integer types as operands. got lhs: " 
						+ lhsType + ". rhs: " + rhsType, expr);
			else
				return new VarType("int");
		}
		if (op == Operator.GT || op == Operator.GTE || op == Operator.LT || op == Operator.LTE)
		{
			if (!(lhsType.equals("int") && rhsType.equals("int")))
				error("The binary operator '" + op
						+ "' accepts only Integer types as operands. got lhs: " +
						lhsType + ". rhs: " + rhsType, expr);
			else
				return new VarType("boolean");
		}
		if (op == Operator.EQUAL || op == Operator.NEQUAL) 
		{
			if (run_num == 0)
				return new VarType("boolean");
			if (run_num == 1) 
			{
				if (lhsType.equals(rhsType) || lhsType.equals("null") || rhsType.equals("null"))
					return new VarType("boolean");
				// if reached here - lhsType != rhsType
				if (lhsType.equals("string") || lhsType.equals("int") || lhsType.equals("boolean")
						|| rhsType.equals("string") || rhsType.equals("int") || rhsType.equals("boolean"))
					error("Type mismatch for the operands of '" + op + "'. got lhs: "
							+ lhsType + ". rhs: " + rhsType , expr);
				// if reached here - lhsType and rhsType are not primitive types
				//
				// check if lhsType extends rhsType:
				icObject lhsClass = env.getObjByName(lhsType);
				if (!(lhsClass instanceof icClass))
					error("Undefined type: " + lhsType, expr);
				String parent = ((icClass) lhsClass).ext;
				icObject parentClass = env.getObjByName(parent);
				if (!(parentClass instanceof icClass))
					error("Unexpected error!!! undefined parent class - should have catched this before", expr);
				while (parent != null) 
				{
					if (rhsType.equals(parent))
						return new VarType("boolean");
					else
						parent = ((icClass) parentClass).ext;
				}
				// if reached here - lhsType doesn't extend rhsType
				//
				// check if rhsType extends lhsType
				icObject rhsClass = env.getObjByName(rhsType);
				if (!(rhsClass instanceof icClass))
					error("Undefined type: " + rhsType, expr);
				parent = ((icClass) rhsClass).ext;
				parentClass = env.getObjByName(parent);
				if (!(parentClass instanceof icClass))
					error("Unexpected error!!! undefined parent class - should have catched this before", expr);
				while (parent != null) 
				{
					if (lhsType.equals(parent))
						return new VarType("boolean");
					else
						parent = ((icClass) parentClass).ext;
				}
				// if reached here - rhsType doesn't extend lhsType
				error("Type mismatch for operands of '" + op + "'. got lhs: " + 
				lhsType + ". rhs: " + rhsType, expr);
			} 
			else
				error("Error!!!! should never reach this line of code", expr);
		}
		else
			error("Error!!!! should never reach this line of code", expr);
		return null;
		
		
	}

	@Override
	public VarType visit(ASTClassList astClassList, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting classList at line: " + astClassList.line);
		for (ASTClassDecl cls : astClassList.lst) {
			cls.accept(this, d);
		}
		return null;
	}

	@Override
	public VarType visit(ASTIdList astIdList, Environment d) {
		/* should not be called. do nothing */
		return null;
	}

	@Override
	public VarType visit(ASTField astField, Environment d) {
		if (IS_DEBUG)

			System.out.println("accepting fields: " + astField.ids.lst + " at line: " + astField.line);

		for (String field : astField.ids.lst) {
			if (IS_DEBUG)
				System.out.println("field type: " + astField.type);
			icVariable o = new icVariable(field, ASTNode.scope, 
					new VarType(astField.type));
			d.add(o);
			d.lastClass.addObject(o, d, false);
		}
		return null;
	}

	@Override
	public VarType visit(ASTExtend astExtend, Environment d) {
		/* should not be called. do nothing */
		return null;
	}

	@Override
	public VarType visit(ASTfmList fmList, Environment d) {
		/* should not be called. do nothing */
		return null;
	}

	@Override
	public VarType visit(ASTRoot root, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting root at line: " + root.line);
		root.child.accept(this, d);
		return null;
	}

	@Override
	public VarType visit(ASTFormalList astFormalList, Environment d) {
		/* should not be called. do nothing */
		return null;
	}

	@Override
	public VarType visit(ASTStatType astStatType, Environment d) {
		/* should not be called. do nothing */
		return null;
	}

	@Override
	public VarType visit(ASTMethod meth, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting method: " + meth.id + " at line: " + meth.line);

		icFunction func = new icFunction(meth.id, ASTNode.scope, 
				new VarType(meth.type), meth.isStatic);
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
			if (IS_DEBUG)
				System.out.println("adding new variable: " + v.name + " scope " + v.scope);
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
	public VarType visit(ASTClassDecl cls, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting classDecl: " + cls.class_id + " at line: " + cls.line);

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
	public VarType visit(ASTDotLength expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting expr.length at line: " + expr.line);
		VarType e = expr.e.accept(this, d);
		if (e.num_arrays != 0)
			return new VarType("int");
		else {
			error("tried accessing length field of non array expression: " + e, expr);
		}
		return null;
	}

	@Override
	public VarType visit(ASTNewArray expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting new array at line: " + expr.line);
		VarType index_type = expr.expr.accept(this, d);
		if ("int" != index_type.type || index_type.num_arrays != 0)
			error("bad indexer type. expected int, got: " + index_type, expr);
		if (run_num == 1) {
			if (!(d.validateType(new VarType(expr.type))))
			{
				error("unknown type: " + expr.type, expr);
			}
		}
		return new VarType(expr.type);
	}

	@Override
	public VarType visit(ASTNewObject expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting new object at line: " + expr.line);
		if (run_num == 1) {
			d.validateType(new VarType(expr.type));
		}
		return new VarType(expr.type);
	}

	@Override
	public VarType visit(ASTElseStmt elseStmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting elseStmt at line: " + elseStmt.line);

		++ASTNode.scope;
		elseStmt.stmt.accept(this, env);
		env.destroyScope(ASTNode.scope);
		--ASTNode.scope;

		return null;
	}

	@Override
	public VarType visit(ASTAssignFormals stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTAssignFormals at line: " + stmt.line);
		VarType type = stmt.form.type;
		String id = stmt.form.id;
		if (IS_DEBUG)
			System.out.println("type: " + type + " id: " + id);

		String rhs = null;
		int flag = 0;

		// defining a new variable
		icVariable var = new icVariable(id, ASTNode.scope, type);
		env.add(var);
		/*
		 * if (var != null) { // cant define an identifier multiple times in the
		 * same scope if (var.getScope() == ASTNode.scope) { throw new
		 * RuntimeException("Duplicate local variable " + id); } }
		 */

		// validating the the declared variable has been defined
		if (run_num == 1) {
			icObject type_classo = env.getObjByName(type.type);
			if (type_classo == null) {
				error(type + " cannot be resolved to a type", stmt);
			}
			else if (!(type_classo instanceof icClass))
				error(type +" is not a class name", stmt);
			icClass type_class = (icClass) type_classo;

			// case of assignment
			if (stmt.rhs != null) {
				rhs = stmt.rhs.accept(this, env);
				if (IS_DEBUG)
					System.out.println("rhs: " + rhs);
				if (type_class != null)
				{
					icObject val = env.getObjByName(rhs);
					
					// checking for type equality
					if (val instanceof icClass) {
						// if type of value is sub-type of the variable type
						if (((icClass) val).checkIfSubType(type_class, env) == false) {
							flag = 1;
						}
						// type equality of basic types
					} 
				}
				else if (rhs != null && !type.equals(rhs)) {
					flag = 1;
				}
			}
		}

		if (flag == 1) {
			error("Type mismatch: cannot convert from " + rhs + " to " + type, stmt);
		}
		if (run_num == 0) {
			icVariable newVar = new icVariable(id, ASTNode.scope, type);
			env.add(newVar);
		}

		return type;
	}

	@Override
	public VarType visit(ASTWhileStmt stm, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTWhileStmt at line: " + stm.line);
		ASTExpr expr = stm.expr;

		int nestFlag;
		VarType exprType = expr.accept(this, env);
		if (!exprType.type.equals("boolean") || exprType.num_arrays != 0)
			error("Expected boolean expression after 'while'", expr);
		++ASTNode.scope;
		// while loop might be nested
		if (env.loopScope == true) {
			nestFlag = 0;
		} else {
			env.loopScope = true;
			nestFlag = 1;
		}
		stm.stmt.accept(this, env);
		if (nestFlag == 1) {
			env.loopScope = false;
		}
		env.destroyScope(ASTNode.scope);

		--ASTNode.scope;
		return null;

	}

	@Override
	public VarType visit(ASTVarStmt stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTVarStmt at line: " + stmt.line);
		if (env.loopScope == false) {
			error(stmt.name + "cannot be used outside of a loop", stmt);
		}
		return null;
	}

	@Override
	// handle non-simple objects
	public VarType visit(ASTRetExp expr, Environment env) {
		VarType rExpr = expr.exp.accept(this, env);
		icObject func = env.lastFunc;
		VarType funcRet = func.getAssignType();
		if (funcRet.num_arrays != rExpr.num_arrays)
			error("Type mismatch: cannot convert from" + rExpr
					+ "to" + funcRet, expr);
		if (IS_DEBUG)
			System.out.println("return check: " + rExpr);
		icObject retExp = env.getObjByName(rExpr.type);
		if (retExp == null) {
			if (!funcRet.type.equals(rExpr.type)) {
				error("Type mismatch: cannot convert from" + retExp.getAssignType()
						+ "to" + env.lastFunc.getAssignType(), expr);
			}
		} else {
			// assuming retExp is some sort of class type - need to fix
			if (((icClass) retExp).checkIfSubType(func, env)) {

			}
		}
		return null;
	}

	@Override
	public VarType visit(ASTIfElseStmt stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTIfElseStmt at line: " + stmt.line);
		VarType cond = stmt.expr.accept(this, env);


		if (!cond.type.equals("boolean") || cond.num_arrays != 0) {
			error("Type mismatch: cannot convert from" + cond + "to" +
						"boolean", stmt);
		}
		++ASTNode.scope;
		VarType ifStmt = stmt.stmt.accept(this, env);
		
		env.destroyScope(ASTNode.scope);
		--ASTNode.scope;
		return null;
	}

	@Override
	public VarType visit(ASTCallStmt stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTCallStmt at line: " + stmt.line);

		return stmt.call.accept(this, env);
	}

	@Override
	public VarType visit(ASTLocation expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTLocation at line: " + expr.line);

		if (IS_DEBUG)
			System.out.println("id: " + expr.id + " type " + expr.type );
		int length;

		String id = expr.id;
		String exp1;
		String exp2;
		icObject ident = env.getObjByName(id);

		// only a variable name
		if (expr.type == 0) {
			// checking if the variables has been declared
			if (ident == null) {
				error(id + " cannot be resolved! ", expr);
			}
			if (IS_DEBUG)
				System.out.println("return 1: " + ident.getAssignType());
			return ident.getAssignType();
		}
		exp1 = expr.e1.accept(this, env);
		
		if (expr.type == 1) {
			if (IS_DEBUG)
				System.out.println("entered loop");
			icObject clss = env.getObjByName(exp1);
			// checking if the class has been declared
			
			if (clss == null) {
				error(id + " cannot be resolved!", expr);
			}
			
			// e1 is a name of a class
			if (clss instanceof icClass) {
				// checking if exp1 is a valid field inside of class
				if (((icClass) clss).hasObject(id) == false) {
					error(id + " cannot be resolved or is not a field of class " + exp1, expr);
				} else {
					
					VarType res = ((icClass) clss).lastSubObject.getAssignType();
					if (IS_DEBUG)
						System.out.println("return 2 " + res);
					return res;
				}
			}

		}
		exp2 = expr.e2.accept(this, env);
		// e1 is an array
		if (expr.type == 2) {

			length = exp1.length();
			if (!(exp1.charAt(length - 1) == ']')) { // the variable is not an
														// array
				error("The type of the expression must be an array type but it resolved to" + exp1, expr);
			}

			if (!exp2.equals("int")) { // checking if the parameter inside the
										// array is an integer

				error("Type mismatch: cannot convert from" + exp2 + "to int", expr);
			}
			if (IS_DEBUG)
				System.out.println("returning exp1:" + exp1.indexOf("[")); // need
								
			return exp1.substring(0, exp1.indexOf("["));
		}
		if (IS_DEBUG)
			System.out.println("returning null");

		return new VarType("null");
	}

	@Override
	public VarType visit(ASTVirtualCall vc, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTVirtualCall at line: " + vc.line);
		icObject f = env.getObjByName(vc.id);
		// check vc is a valid method:
		if (!(f instanceof icFunction))
			error("The method '" + vc.id + "' has not been declared", vc);
		List<VarType> formal_params = ((icFunction) f).arg_types;
		List<ASTExpr> exprList = vc.exprList.lst;

		// check number of arguments:
		if (formal_params.size() != exprList.size())
			error("Wrong number of arguments for the method '" + vc.id + "'", vc);

		// check argument types
		for (int i = 0; i < formal_params.size(); i++) {
			VarType formal = formal_params.get(i);
			VarType found = exprList.get(i).accept(this, env);
			if (formal.num_arrays != found.num_arrays)
			{
				error("Wrong argument types for the method '" + vc.id + 
						"'. expected: " + formal + ", got: " + found, vc);				
			}
			if (run_num==1 && !formal.type.equals(found.type)) 
			{
				icObject argClass = env.getObjByName(formal.type);
				icObject expClass = env.getObjByName(found.type);
				if (!(argClass instanceof icClass))
					error("Undefined type: " + argClass, vc);
				if (!(expClass instanceof icClass))
					error("Undefined type: " + expClass, vc);
				if (!((icClass)expClass).checkIfSubType( argClass, env))
				{
					error("Wrong argument types for the method '" + vc.id + 
							"'. expected: " + formal + ", got: " + found, vc);	
				}
			}
			
			
		}
		if (IS_DEBUG)
			System.out.println("virtual call check ended");
		return ((icFunction) f).retType;
	}

	@Override
	public VarType visit(ASTStaticCall expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTStaticCall at line: " + expr.line);
		/*
		 * String expLst = expr.exprList.accept(this, env);
		 */

		List<ASTExpr> expLst = expr.exprList.lst;
		VarType exp;
		VarType funcArg;
		String classId = expr.classId;

		icClass className = (icClass) env.getObjByName(classId);
		String funcId = expr.id;

		if (className == null) { // the class doesn't exist
			error(className + " cannot be resolved to a variable", expr);
		}

		if (!className.hasObject(funcId)) {
			error(funcId + " cannot be resolved or is not a function", expr);
		}
		icFunction func = (icFunction) env.getObjByName(funcId);

		if (expLst.size() != func.getNumofArg()) {
			error("The method " + funcId + "in the type"
					+ classId + " is not applicable for the arguments", expr);
		}
		for (int i = 0; i < expr.exprList.lst.size(); i++) {
			exp = expLst.get(i).accept(this, env);
			funcArg = func.arg_types.get(i);
			if (exp.num_arrays != funcArg.num_arrays)
			{
				error("Wrong argument types for the method '" + func.name + 
						"'. expected: " + funcArg + ", got: " + exp, expr);				
			}
			
			if (run_num==1 &&!funcArg.type.equals(exp.type)) 
			{
				icObject argClass = env.getObjByName(funcArg.type);
				icObject expClass = env.getObjByName(exp.type);
				if (!(argClass instanceof icClass))
					error("Undefined type: " + argClass, expr);
				if (!(expClass instanceof icClass))
					error("Undefined type: " + expClass, expr);
				if (!((icClass)expClass).checkIfSubType( argClass, env))
				{
					error("Wrong argument types for the method '" + func.name + 
							"'. expected: " + funcArg + ", got: " + exp, expr);	
				}
			}
		}
		if (IS_DEBUG)
			System.out.println("return static " + func.getAssignType());
		return func.getAssignType();

	}

	@Override
	public VarType visit(ASTLiteral expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting ASTLiteral at line: " + expr.line);
		if (IS_DEBUG)
			System.out.println(expr.s);
		String lit = expr.s;
		if (lit.equals("true") || lit.equals("false")) {
			return new VarType("boolean");
		} else if (lit.equals("null")) {
			return new VarType("null");
		} else if (lit.equals("int")) {
			return new VarType("int");
		} else {
			return new VarType("string");
		}

	}

	@Override
	public VarType visit(ASTExprList expr, Environment d) {
		error("error!!! should never reach this part of code", expr);
		return null;
	}


}