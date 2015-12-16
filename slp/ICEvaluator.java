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
		env.run_num = 0;
		if (IS_DEBUG)
			System.out.println("starting first itteration");
		root.accept(this, env);
		++run_num;
		++env.run_num;
		if (IS_DEBUG)
			System.out.println("starting second itteration");
		root.accept(this, env);
	}

	public static void error(String str, ASTNode n) {

		if (run_num == 0) return;
		
		if (n != null)
		{
			throw new RuntimeException("\nLine " + n.line + ": " + str);
			//System.out.println("\nLine " + n.line + ": " + str);
		}
		else
		{
			throw new RuntimeException("\n" + str);
			//System.out.println(str);
		}

	}

	public void validateAssign(VarType lhs, VarType rhs, ASTNode curNode, Environment env) {
		if (run_num != 1)
			return;
		if (lhs.type == "void") {
			if (rhs.type != null && rhs.type != "null")
				error("Type mismatch: cannot convert from " + rhs.type + " to " + lhs.type, curNode);
			else
				return;
		}
		if (rhs.type == "void") {
			if (lhs.type != null && lhs.type != "null")
				error("Type mismatch: cannot convert from " + lhs.type + " to " + rhs.type, curNode);
			else
				return;
		}
		if (lhs.num_arrays != rhs.num_arrays)
			error("Type mismatch: cannot convert from " + rhs + " to " + lhs, curNode);
		icObject rhsClass = env.getObjByName(rhs.type);
		if (rhsClass == null) {
			error("cannot find class: " + rhs.type, curNode);
		}
		if (!(rhsClass instanceof icClass)) {
			error(rhs.type + "is not a class", curNode);

		}
		icObject lhsClass = env.getObjByName(lhs.type);

		if (lhsClass == null) {
			error("cannot find class: " + lhs.type, curNode);
		}
		if (!(lhsClass instanceof icClass)) {
			error(lhs.type + "is not a class", curNode);

		}
		if (!((icClass) rhsClass).checkIfSubType(lhsClass, env)) {
			error("Type mismatch: cannot convert from " + rhs.type + " to " + lhs.type, curNode);
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

		VarType varExpr_type = stmt.varExpr.accept(this, env);
		VarType rhs_type = stmt.rhs.accept(this, env);
		validateAssign(varExpr_type, rhs_type, stmt, env);
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
			if (run_num == 1) error ("cannot evaluate unary op on array type", expr);

		}
		String rhsType = rhsType_type.type;
		if (op == Operator.MINUS) {
			if (rhsType.equals("int"))
				return new VarType("int");
			else
				if (run_num == 1) error ("Expected an Integer after '-' ", expr);
		}
		if (op == Operator.LNEG) {
			if (rhsType.equals("boolean"))
				return new VarType("boolean");
			else
				if (run_num == 1) error("Expected a Boolean expression after '!' ", expr);
		} else
			if (run_num == 1) error("Encountered unexpected operator " + op, expr);
		// Integer value = expr.operand.accept(this, env);
		// new Integer(- value.intValue());
		return new VarType("int");
	}

	public VarType visit(ASTBinaryOpExpr expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTBinaryOpExpr at line: " + expr.line);
		Operator op = expr.op;
		ASTExpr lhs = expr.lhs;
		ASTExpr rhs = expr.rhs;
		VarType lhsType_type = lhs.accept(this, env);
		VarType rhsType_type = rhs.accept(this, env);
		if (run_num == 0)
			return new VarType("null","");
		if (rhsType_type.num_arrays != 0 || lhsType_type.num_arrays != 0 )
		{
			error("cannot evaluate binary op on array type", expr);
		}
		String rhsType = rhsType_type.type;
		String lhsType = lhsType_type.type;
		
		if (op == Operator.LAND || op == Operator.LOR) 
		{
			if ((!lhsType.equals("boolean")) && run_num == 1)
				error("Expected a Boolean expression before " + op, expr);
			if ((!rhsType.equals("boolean")) && run_num == 1)
				error("Expected a Boolean expression after " + op, expr);
			else
				return new VarType("boolean");
		}
		if (op == Operator.PLUS) {
			if (IS_DEBUG)
				System.out.println("check: lhs " + lhsType + " rhs " + rhsType);
			if (lhsType.equals("string") || lhsType.equals("int")) {
				if (lhsType.equals(rhsType))
				{
					String reg = IR.op_add(lhsType_type.ir_val, rhsType_type.ir_val);
					return new VarType(lhsType, reg);
				}
				else
					if (run_num == 1) error("Expected operands of same type for the binary operator " + op +
							" got lhs: " + lhsType + ", rhs: " + rhsType, expr);
			} 
			else
				if (run_num == 1) error("The binary operator '+' accepts only Integer or String types as operands "+
						"got lhs: " + lhsType + ". rhs: " + rhsType, expr);
		}
		if (op == Operator.MINUS || op == Operator.DIV || op == Operator.MULTIPLY || op == Operator.MOD)
		{
			if (!(lhsType.equals("int") && rhsType.equals("int"))){
				if (run_num == 1) error("The binary operator '" + op
						+ "' accepts only Integer types as operands. got lhs: " 
						+ lhsType + ". rhs: " + rhsType, expr);
			}
			else
				return new VarType("int");
		}
		if (op == Operator.GT || op == Operator.GTE || op == Operator.LT || op == Operator.LTE)
		{
			if (!(lhsType.equals("int") && rhsType.equals("int"))){
				if (run_num == 1) error("The binary operator '" + op
						+ "' accepts only Integer types as operands. got lhs: " +
						lhsType + ". rhs: " + rhsType, expr);
			}
			else
				return new VarType("boolean");
		}
		if (op == Operator.EQUAL || op == Operator.NEQUAL) {
			if (run_num == 0)
				return new VarType("boolean");
			if (run_num == 1) {
				if (lhsType.equals(rhsType) || lhsType.equals("null") || rhsType.equals("null"))
					return new VarType("boolean");
				// if reached here - lhsType != rhsType
				if (lhsType.equals("string") || lhsType.equals("int") || lhsType.equals("boolean")
						|| rhsType.equals("string") || rhsType.equals("int") || rhsType.equals("boolean"))
					error("Type mismatch for the operands of '" + op + "'. got lhs: " + lhsType + ". rhs: " + rhsType,
							expr);
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
				while (parent != null) {
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
				while (parent != null) {
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
				if (run_num == 1) error("Error!!!! should never reach this line of code", expr);
		}
		else
			if (run_num == 1) error("Error!!!! should never reach this line of code", expr);
		return new VarType("null"); // default value for rum_num == 0

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
			icVariable o = new icVariable(field, ASTNode.scope, new VarType(astField.type));
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
			System.out.println("accepting method: " + meth.id + " at line: " + meth.line + " scope: " + ASTNode.scope);

		icFunction func = new icFunction(meth.id, ASTNode.scope, new VarType(meth.type), meth.isStatic);
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
		if (run_num == 0){
		d.lastClass = c;
		}
		else{
			// in the second run-through we want the class that contains all the methods and fields scanned in the first run
			d.lastClass = (icClass) d.getObjByName(c.name);
		}
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
			if (!(d.validateType(new VarType(expr.type)))) {
				error("unknown type: " + expr.type, expr);
			}
		}
		return new VarType(expr.type + "[]");
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

		VarType rhs = null;

		// defining a new variable
		icVariable var = new icVariable(id, ASTNode.scope, type);
		env.add(var);

		// validating the the declared variable has been defined
		if (run_num == 1) {
			icObject type_classo = env.getObjByName(type.type);
			if (type_classo == null) {
				error(type + " cannot be resolved to a type", stmt);
			} else if (!(type_classo instanceof icClass))
				error(type + " is not a class name", stmt);

			// case of assignment
			if (stmt.rhs != null) {
				rhs = stmt.rhs.accept(this, env);
				validateAssign(type, rhs, stmt, env);
			}
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
		VarType funcRet = env.lastFunc.getAssignType();
		validateAssign(funcRet, rExpr, expr, env);
		return null;
	}

	@Override
	public VarType visit(ASTIfElseStmt stmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTIfElseStmt at line: " + stmt.line);
		VarType cond = stmt.expr.accept(this, env);

		if (!cond.type.equals("boolean") || cond.num_arrays != 0) {

			error("Type mismatch: cannot convert from " + cond + " to " +
						"boolean ", stmt);
		}
		++ASTNode.scope;
		stmt.stmt.accept(this, env);

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
			System.out.println("id: " + expr.id + " type " + expr.type);

		String id = expr.id;
		VarType exp1;
		VarType exp2;
		icObject ident = env.getObjByName(id);
		if (ident == null)
		{
			ident = env.lastClass.getObject(id, env);
		}

		if (run_num ==0){
			return new VarType("null");
		}
		// only a variable name
		if (expr.type == 0)
			{
			// checking if the variables has been declared
			if (ident == null) {
				
				if (run_num ==0){
					return new VarType("null");
					
				}
				else{
				ident = env.lastClass.getObject(id, env);
					
					if (ident == null){
					error(id + " cannot be resolved! ", expr);
					}
				}
				
			}
			if (IS_DEBUG)
				System.out.println("return 1: " + ident.getAssignType());
			return ident.getAssignType();
		}

		exp1 = expr.e1.accept(this, env);
		if (expr.type == 1) {
			if (IS_DEBUG)
				System.out.println("entered loop");
			if (run_num == 1) {
				icObject clss = env.getObjByName(exp1.type);
				// checking if the class has been declared

				if (clss == null) {
					error(exp1.type + " cannot be resolved to a type", expr);
				}

				// e1 is a name of a class
				if (clss instanceof icClass) {
					// checking if exp1 is a valid field inside of class
					if (((icClass) clss).hasObject(id, env) == false) {
						error(id + " cannot be resolved or is not a field of class " + exp1, expr);
					} else {
						VarType res = ((icClass) clss).getFieldType(id, env);
						if (IS_DEBUG)
							System.out.println("return 2 " + res);
						return res;
					}
				}
			} else
				return exp1;

		}
		// e1 is an array
		if (expr.type == 2) {
			exp2 = expr.e2.accept(this, env);

			if (exp1.num_arrays == 0) { // the variable is not an array
				error("The type of the expression must be an array type but it resolved to" + exp1, expr);
			}

			if (!exp2.isBaseType("int")) { // checking if the parameter inside
											// the array is an integer
				error("Type mismatch: cannot convert from " + exp2 + " to int", expr);
			}

			VarType out_type = new VarType(exp1.type, exp1.num_arrays - 1);
			if (IS_DEBUG)
				System.out.println("returning exp1: " + out_type); // need
			return out_type;
		}
		if (IS_DEBUG)
			System.out.println("returning null");

		return new VarType("null");
	}

	@Override
	public VarType visit(ASTVirtualCall vc, Environment env) {
		if (run_num != 1)
			return null;
		if (IS_DEBUG)
			System.out.println("accepting ASTVirtualCall at line: " + vc.line);
		icObject f = env.getObjByName(vc.id);
		
		// using a method inside a class directly
		if (vc.expr == null) {
			if (IS_DEBUG)
			System.out.println("searching in class: " + env.lastClass.name );
			
			f = env.lastClass.getObject(vc.id, env);
		}
		
		else{
			VarType obj = vc.expr.accept(this, env);
			
			f = ((icClass) env.getObjByName(obj.type)).getObject(vc.id, env);
		}
		
		if (IS_DEBUG)
		System.out.println("func name: " + f.name);
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
			if (formal.num_arrays != found.num_arrays) {
				error("Wrong argument types for the method '" + vc.id + "'. expected: " + formal + ", got: " + found,
						vc);
			}
			if (run_num == 1 && !formal.type.equals(found.type)) {
				icObject argClass = env.getObjByName(formal.type);
				icObject expClass = env.getObjByName(found.type);
				if (!(argClass instanceof icClass))
					error("Undefined type: " + argClass, vc);
				if (!(expClass instanceof icClass))
					error("Undefined type: " + expClass, vc);
				if (!((icClass) expClass).checkIfSubType(argClass, env)) {
					error("Wrong argument types for the method '" + vc.id + "'. expected: " + formal + ", got: "
							+ found, vc);
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

		if (!className.hasObject(funcId, env)) {
			error(funcId + " cannot be resolved or is not a function", expr);
		}
		icFunction func = (icFunction) className.getObject(funcId, env);
		if (IS_DEBUG)
			System.out.println("static func " + func.name);
		if (expLst.size() != func.getNumofArg()) {
			error("The method " + funcId + "in the type" + classId + " is not applicable for the arguments", expr);
		}
		for (int i = 0; i < expr.exprList.lst.size(); i++) {
			exp = expLst.get(i).accept(this, env);
			funcArg = func.arg_types.get(i);
			if (exp.num_arrays != funcArg.num_arrays) {
				error("Wrong argument types for the method '" + func.name + "'. expected: " + funcArg + ", got: " + exp,
						expr);
			}

			if (run_num == 1 && !funcArg.type.equals(exp.type)) {
				icObject argClass = env.getObjByName(funcArg.type);
				icObject expClass = env.getObjByName(exp.type);
				if (!(argClass instanceof icClass))
					error("Undefined type: " + argClass, expr);
				if (!(expClass instanceof icClass))
					error("Undefined type: " + expClass, expr);
				if (!((icClass) expClass).checkIfSubType(argClass, env)) {
					error("Wrong argument types for the method '" + func.name + "'. expected: " + funcArg + ", got: "
							+ exp, expr);
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

	@Override
	public VarType visit(ASTScope sc, Environment d) {
		ASTNode.scope++;
		VarType out = sc.s.accept(this, d);
		d.destroyScope(ASTNode.scope);
		ASTNode.scope--;
		
		return out;
	}

}