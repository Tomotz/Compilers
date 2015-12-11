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
		if (IS_DEBUG)
			System.out.println("accepting ASTStmtList at line: " + stmts.line);
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
		if (IS_DEBUG)
			System.out.println("accepting ASTAssignStmt at line: " + stmt.line);

		int flag = 0;
		String varExpr = utility.trimArray(stmt.varExpr.accept(this, env));
		icObject var = env.getObjByName(varExpr);
		String rhs = utility.trimArray(stmt.rhs.accept(this, env));
		icObject value = env.getObjByName(rhs);

		// value is a number or a string
		if (value == null) {
			if (rhs.equals(varExpr)) {
				flag = 1;
				}
			}
		
		else if (var instanceof icClass) {
			// if the value is a son of a father class
			if (((icClass) var).checkIfSubType(value, var, env) == true) {
				flag = 1;
			}
		}


		if (flag == 0) {
			throw new UnsupportedOperationException(
					"Type mismatch: cannot convert from" + value.getAssignType() + "to" + var.getAssignType());
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
		if (IS_DEBUG)
			System.out.println("accepting ASTVarExpr at line: " + expr.line);

		if (expr.name.equals("this"))
			return env.lastClass.name;
		else
			throw new RuntimeException("Error!!! should never reach this code. parser at line " + expr.line);
	}

	public String visit(ASTNumberExpr expr, Environment env) {
		throw new RuntimeException("Error!!! should never reach this code. parser at line " + expr.line);

	}

	public String visit(ASTUnaryOpExpr expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTUnaryOpExpr at line: " + expr.line);
		Operator op = expr.op;
		ASTExpr rhs = expr.operand;
		String rhsType = rhs.accept(this, env);
		if (op == Operator.MINUS) {
			if (rhsType.equals("int"))
				return "int";
			else
				throw new RuntimeException("Line " + expr.line + ": Expected an Integer after '-' ");
		}
		if (op == Operator.LNEG) {
			if (rhsType.equals("boolean"))
				return "boolean";
			else
				throw new RuntimeException("Line " + expr.line + ": Expected a Boolean expression after '!' ");
		} else
			throw new RuntimeException("Line " + expr.line + ": Encountered unexpected operator " + op);
		// Integer value = expr.operand.accept(this, env);
		// new Integer(- value.intValue());
	}

	public String visit(ASTBinaryOpExpr expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTBinaryOpExpr at line: " + expr.line);
		Operator op = expr.op;
		ASTExpr lhs = expr.lhs;
		ASTExpr rhs = expr.rhs;
		String lhsType = lhs.accept(this, env);
		String rhsType = rhs.accept(this, env);
		if (op == Operator.LAND || op == Operator.LOR) {
			if (!lhsType.equals("boolean"))
				throw new RuntimeException("Line " + expr.line + ": Expected a Boolean expression before " + op);
			if (!rhsType.equals("boolean"))
				throw new RuntimeException("Line " + expr.line + ": Expected a Boolean expression after " + op);
			else
				return "boolean";
		}
		if (op == Operator.PLUS) {
			System.out.println("check: lhs " + lhsType + " rhs " + rhsType);
			if (lhsType.equals("string") || lhsType.equals("int")) {
				if (lhsType.equals(rhsType))
					return lhsType;
				else
					throw new RuntimeException(
							"Line " + expr.line + ": Expected operands of same type for the binary operator " + op);
			} else
				throw new RuntimeException("Line " + expr.line
						+ ": The binary operator '+' accepts only Integer or String types as operands");
		}
		if (op == Operator.MINUS || op == Operator.DIV || op == Operator.MULTIPLY || op == Operator.MOD) {
			if (!(lhsType.equals("int") && rhsType.equals("int")))
				throw new RuntimeException("Line " + expr.line + ": The binary operator '" + op
						+ "' accepts only Integer types as operands");
			else
				return "int";
		}
		if (op == Operator.GT || op == Operator.GTE || op == Operator.LT || op == Operator.LTE) {
			if (!(lhsType.equals("int") && rhsType.equals("int")))
				throw new RuntimeException("Line " + expr.line + ": The binary operator '" + op
						+ "' accepts only Integer types as operands");
			else
				return "boolean";
		}
		if (op == Operator.EQUAL || op == Operator.NEQUAL) {
			if (run_num == 0)
				return "boolean";
			if (run_num == 1) {
				if (lhsType.equals(rhsType) || lhsType.equals("null") || rhsType.equals("null"))
					return "boolean";
				// if reached here - lhsType != rhsType
				if (lhsType.equals("string") || lhsType.equals("int") || lhsType.equals("boolean")
						|| rhsType.equals("string") || rhsType.equals("int") || rhsType.equals("boolean"))
					throw new RuntimeException(
							"Line " + expr.line + ": Type mismatch for the operands of '" + op + "'");
				// if reached here - lhsType and rhsType are not primitive types
				//
				// check if lhsType extends rhsType:
				icObject lhsClass = env.getObjByName(lhsType);
				if (!(lhsClass instanceof icClass))
					throw new RuntimeException("Line " + expr.line + ": Undefined type: " + lhsType);
				String parent = ((icClass) lhsClass).ext;
				icObject parentClass = env.getObjByName(parent);
				if (!(parentClass instanceof icClass))
					throw new RuntimeException("Line " + expr.line
							+ ": Unexpected error!!! undefined parent class - should have catched this before");
				while (parent != null) {
					if (rhsType.equals(parent))
						return "boolean";
					else
						parent = ((icClass) parentClass).ext;
				}
				// if reached here - lhsType doesn't extend rhsType
				//
				// check if rhsType extends lhsType
				icObject rhsClass = env.getObjByName(rhsType);
				if (!(rhsClass instanceof icClass))
					throw new RuntimeException("Line " + expr.line + ": Undefined type: " + rhsType);
				parent = ((icClass) rhsClass).ext;
				parentClass = env.getObjByName(parent);
				if (!(parentClass instanceof icClass))
					throw new RuntimeException("Line " + expr.line
							+ ": Unexpected error!!! undefined parent class - should have catched this before");
				while (parent != null) {
					if (lhsType.equals(parent))
						return "boolean";
					else
						parent = ((icClass) parentClass).ext;
				}
				// if reached here - rhsType doesn't extend lhsType
				throw new RuntimeException("Line " + expr.line + ": Type mismatch for operands of '" + op + "'");
			} else
				throw new RuntimeException("Error!!!! should never reach this line of code");
		} else
			throw new RuntimeException("Error!!!! should never reach this line of code");

	}

	@Override
	public String visit(ASTClassList astClassList, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting classList at line: " + astClassList.line);
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

			System.out.println("accepting fields: " + astField.ids.lst + " at line: " + astField.line);

		for (String field : astField.ids.lst) {
			System.out.println("field type: " + astField.type);
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
		if (IS_DEBUG)
			System.out.println("accepting root at line: " + root.line);
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

			System.out.println("accepting method: " + meth.id + " at line: " + meth.line);

		String classType = d.lastClass.name;
		icFunction func = new icFunction(meth.id, ASTNode.scope, meth.type, meth.isStatic);
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
	public String visit(ASTClassDecl cls, Environment d) {
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
	public String visit(ASTDotLength expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting expr.length at line: " + expr.line);
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
			System.out.println("accepting new array at line: " + expr.line);
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
			System.out.println("accepting new object at line: " + expr.line);
		if (run_num == 1) {
			d.validateType(expr.type);
		}
		return expr.type;
	}

	@Override
	public String visit(ASTElseStmt elseStmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting elseStmt at line: " + elseStmt.line);

		++ASTNode.scope;
		elseStmt.stmt.accept(this, env);
		env.destroyScope(ASTNode.scope);
		--ASTNode.scope;

		return null;
	}

	@Override
	public String visit(ASTAssignFormals stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTAssignFormals at line: " + stmt.line);
		String type = stmt.form.type;
		String id = stmt.form.id;
		System.out.println("type: " + type + " id: " + id);

		String rhs = null;
		int flag = 0;

		// defining a new variable
		icObject var = env.getObjByName(id);
		/*
		 * if (var != null) { // cant define an identifier multiple times in the
		 * same scope if (var.getScope() == ASTNode.scope) { throw new
		 * RuntimeException("Duplicate local variable " + id); } }
		 */

		// validating the the declared variable has been defined
		if (run_num == 1) {
			if (!type.equals("int") && !type.equals("string") && !type.equals("boolean")) {
				icObject tpe = env.getObjByName(type);
				if (tpe == null) {
					throw new RuntimeException("Line " + stmt.line + ": " + type + " cannot be resolved to a type");
				}
			}

			// case of assignment
			if (stmt.rhs != null) {
				rhs = stmt.rhs.accept(this, env);
				System.out.println("rhs: " + rhs);
				icObject val = env.getObjByName(rhs);

				// checking for type equality
				if (val instanceof icClass) {
					// if type of value is sub-type of the variable type
					if (((icClass) val).checkIfSubType(val, var, env) == true) {
						flag = 1;
					}
					// type equality of basic types
				} else if (rhs != null && !type.equals(rhs)) {
					flag = 1;
				}
			}
		}

		if (flag == 1) {
			throw new RuntimeException(
					"Line " + stmt.line + ": Type mismatch: cannot convert from " + type + " to " + rhs);
		}
		if (run_num == 0) {
			icVariable newVar = new icVariable(id, ASTNode.scope, type);
			env.add(newVar);
		}

		return type;
	}

	@Override
	public String visit(ASTWhileStmt stm, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTWhileStmt at line: " + stm.line);
		ASTExpr expr = stm.expr;

		int nestFlag;
		String ExprType = expr.accept(this, env);
		if (!ExprType.equals("boolean"))
			throw new RuntimeException("Line " + expr.line + ": Expected boolean expression after 'while'");
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
	public String visit(ASTVarStmt stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTVarStmt at line: " + stmt.line);
		if (env.loopScope == false) {
			throw new UnsupportedOperationException(stmt.name + "cannot be used outside of a loop");
		}
		return null;
	}

	@Override
	// handle non-simple objects
	public String visit(ASTRetExp expr, Environment env) {
		String rExpr = expr.exp.accept(this, env);
		icObject func = env.lastFunc;
		String funcRet = func.getAssignType();
		System.out.println("return check: " + rExpr);
		icObject retExp = env.getObjByName(rExpr);
		if (retExp == null) {
			if (!funcRet.equals(rExpr)) {
				throw new UnsupportedOperationException("Type mismatch: cannot convert from" + retExp.getAssignType()
						+ "to" + env.lastFunc.getAssignType());
			}
		} else {
			// assuming retExp is some sort of class type - need to fix
			if (((icClass) retExp).checkIfSubType(retExp, func, env)) {

			}
		}
		return null;
	}

	@Override
	public String visit(ASTIfElseStmt stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTIfElseStmt at line: " + stmt.line);
		String cond = stmt.expr.accept(this, env);


		if (!cond.equals("boolean")) {
			throw new UnsupportedOperationException("Type mismatch: cannot convert from" + cond + "to" + "boolean");
		}
		++ASTNode.scope;
		String ifStmt = stmt.stmt.accept(this, env);
		env.destroyScope(ASTNode.scope);

		--ASTNode.scope;
		return null;
	}

	@Override
	public String visit(ASTCallStmt stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTCallStmt at line: " + stmt.line);

		String call = stmt.call.accept(this, env);
		return call;
	}

	@Override
	public String visit(ASTLocation expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTLocation at line: " + expr.line);

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
				throw new UnsupportedOperationException(id + " cannot be resolved! ");
			}
			System.out.println("return 1: " + ident.getAssignType());
			return ident.getAssignType();
		}
		exp1 = expr.e1.accept(this, env);
		
		if (expr.type == 1) {
			System.out.println("entered loop");
			icObject clss = env.getObjByName(exp1);
			// checking if the class has been declared
			
			if (clss == null) {

				throw new UnsupportedOperationException(id + "cannot be resolved!");
			}
			
			// e1 is a name of a class
			if (clss instanceof icClass) {
				// checking if exp1 is a valid field inside of class
				if (((icClass) clss).hasObject(id) == false) {
					throw new UnsupportedOperationException(exp1 + "cannot be resolved or is not a field");
				} else {
					
					String res = ((icClass) clss).lastSubObject.getAssignType();
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
				throw new UnsupportedOperationException(
						"The type of the expression must be an array type but it resolved to" + exp1);
			}

			if (!exp2.equals("int")) { // checking if the parameter inside the
										// array is an integer

				throw new UnsupportedOperationException("Type mismatch: cannot convert from" + exp2 + "to int");
			}
			System.out.println("returning exp1:" + exp1.indexOf("[")); // need
																					// to
																					// fix

			return exp1.substring(0, exp1.indexOf("["));

		}
		System.out.println("returning null");

		return "null";
	}

	@Override
	public String visit(ASTVirtualCall vc, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTVirtualCall at line: " + vc.line);
		icObject f = env.getObjByName(vc.id);
		// check vc is a valid method:
		if (!(f instanceof icFunction))
			throw new RuntimeException("Line " + vc.line + ": The method '" + vc.id + "' has not been declared");
		List<String> formal_params = ((icFunction) f).arg_types;
		List<ASTExpr> exprList = vc.exprList.lst;

		// check number of arguments:
		if (formal_params.size() != exprList.size())
			throw new RuntimeException(
					"Line " + vc.line + ": Wrong number of arguments for the method '" + vc.id + "'");

		// check argument types
		for (int i = 0; i < formal_params.size(); i++) {
			String formal = formal_params.get(i);
			String found = exprList.get(i).accept(this, env);
			if (!formal.equals(found))
				throw new RuntimeException("Line " + vc.line + ": Wrong argument types for the method '" + vc.id + "'");
		}
		System.out.println("virtual call check ended");
		return ((icFunction) f).retType;
	}

	@Override
	public String visit(ASTStaticCall expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTStaticCall at line: " + expr.line);
		/*
		 * String expLst = expr.exprList.accept(this, env);
		 */

		List<ASTExpr> expLst = expr.exprList.lst;
		String exp;
		String funcArg;
		String classId = expr.classId;

		icClass className = (icClass) env.getObjByName(classId);
		String funcId = expr.id;

		if (className == null) { // the class doesn't exist
			throw new UnsupportedOperationException(
					"Line " + expr.line + className + " cannot be resolved to a variable");
		}

		if (!className.hasObject(funcId)) {
			throw new UnsupportedOperationException(
					"Line " + expr.line + ": " + funcId + " cannot be resolved or is not a function");
		}
		icFunction func = (icFunction) env.getObjByName(funcId);

		if (expLst.size() != func.getNumofArg()) {
			throw new UnsupportedOperationException("Line " + expr.line + "The method " + funcId + "in the type"
					+ classId + " is not applicable for the arguments");
		}
		for (int i = 0; i < expr.exprList.lst.size(); i++) {
			exp = expLst.get(i).accept(this, env);
			funcArg = func.arg_types.get(i);
			if (!funcArg.equals(exp)) {
				throw new RuntimeException(
						"Line " + expr.line + ": Wrong argument types for the method '" + classId + "'");
			}
		}
		System.out.println("return static " + func.getAssignType());
		return func.getAssignType();

	}

	@Override
	public String visit(ASTLiteral expr, Environment d) {
		System.out.println("accepting ASTLiteral at line: " + expr.line);
		System.out.println(expr.s);
		String lit = expr.s;
		if (lit.equals("true") || lit.equals("false")) {
			return "boolean";
		} else if (lit.equals("null")) {
			return "null";
		} else if (lit.equals("int")) {
			return "int";
		} else {
			return "string";
		}

	}

	@Override
	public String visit(ASTExprList expr, Environment d) {
		throw new RuntimeException("error!!! should never reach this part of code");
	}

}