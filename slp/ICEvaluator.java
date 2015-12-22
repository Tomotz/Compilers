package slp;

import java.io.FileWriter;
import java.io.IOException;
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
		write_ir();
	}
	
	public void write_ir()
	{
		String file_text = "\n######################\n" +
				"# String literals\n" + 
				IR.str_table + 
				"######################\n\n" +
				"############################################\n" +
				"# Dispatch vectors\n" +
				IR.dispatch_tables +
				"############################################\n\n" + 
				IR.code;
		
		FileWriter IR_file;
		try {
			IR_file = new FileWriter("output.lir");
			IR_file.write(file_text);
			IR_file.close();
		} catch (IOException e) {
			System.out.println("could not open lir file");
			e.printStackTrace();
			return;
		}
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
		if (run_num ==1){
			IR.move(varExpr_type.ir_val, rhs_type.ir_val);
		}
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
		String result_reg;
		if (op == Operator.MINUS) {
			if (rhsType.equals("int")){
				result_reg = IR.arithmetic_op("0",rhsType_type.ir_val,"Sub");
				return new VarType("int",result_reg);
			}
			else
				if (run_num == 1) error ("Expected an Integer after '-' ", expr);
		}
		if (op == Operator.LNEG) {
			if (rhsType.equals("boolean")){
				result_reg = IR.unary_LNEG_op(rhsType_type.ir_val);
				return new VarType("boolean");
			}
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
		String IRop;
		String reg;
		VarType lhsType_type = lhs.accept(this, env);
		
		if (op == Operator.LOR || op == Operator.LAND){
			if (run_num == 0)
				return new VarType("null","");
			String lhs_reg = lhsType_type.ir_val;
			String temp1 = IR.new_temp();
			String temp2 = IR.new_temp();
			String result = IR.new_temp();
			String end_label = IR.get_label("end");
			if (op==Operator.LOR){
				IR.add_comment(lhs_reg+" || ...");
				IR.add_line("Move 1,"+result); //result=1
				IR.add_line("Move "+lhs_reg+","+temp1);
				IR.add_line("Compare 0,"+temp1); //now compare=lhs-0
				IR.add_line("JumpFalse "+end_label); //jump to end_label if lhs != 0
				
				VarType rhsType_type = rhs.accept(this, env);
				String rhs_reg = rhsType_type.ir_val;
				IR.add_line("Move "+rhs_reg+","+temp2);
				IR.add_line("Compare 0,"+temp2); //now compare=rhs-0
				IR.add_line("JumpFalse "+end_label); //jump to end_label if rhs != 0
				
				IR.add_line("Move 0,"+result); // if reached here: the whole expression is false
			}else{ //op==LAND
				IR.add_comment(lhs_reg+" && ...");

				IR.add_line("Move 0,"+result); //result=1
				IR.add_line("Move "+lhs_reg+","+temp1);
				IR.add_line("Compare 0,"+temp1); //now compare=lhs-0
				IR.add_line("JumpTrue "+end_label); //jump to end_label if lhs == 0
				
				VarType rhsType_type = rhs.accept(this, env);
				String rhs_reg = rhsType_type.ir_val;
				IR.add_line("Move "+rhs_reg+","+temp2);
				IR.add_line("Compare 0,"+temp2); //now compare=rhs-0
				IR.add_line("JumpTrue "+end_label); //jump to end_label if rhs == 0
				
				IR.add_line("Move 1,"+result); // if reached here: the whole expression is true
			}

			IR.add_line(end_label); //control will jump here if expression is true
			return new VarType("boolean", result);
		}
		
		VarType rhsType_type = rhs.accept(this, env);
		//System.out.println(expr.line);
		if (run_num == 0)
			return new VarType("null", "");
		if (rhsType_type.num_arrays != 0 || lhsType_type.num_arrays != 0 )
		{
			error("cannot evaluate binary op on array type", expr);
		}
		String rhsType = rhsType_type.type;
		String lhsType = lhsType_type.type;

		if (op == Operator.PLUS) {
			if (IS_DEBUG)
				System.out.println("check: lhs " + lhsType + " rhs " + rhsType);
			if (lhsType.equals("string") || lhsType.equals("int")) {
				if (lhsType.equals(rhsType) && run_num==1)
				{
					if (lhsType.equals("int")) 
						reg = IR.arithmetic_op(lhsType_type.ir_val, rhsType_type.ir_val, "Add");
					else 
						reg = IR.op_add_string(lhsType_type.ir_val, rhsType_type.ir_val);
					return new VarType(lhsType, reg);
				}
				else{

					if (run_num == 1) error("Expected operands of same type for the binary operator " + op +
							" got lhs: " + lhsType + ", rhs: " + rhsType, expr);
					else return new VarType("null", "");
				}
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
			else{
				if (op == Operator.MINUS)
					IRop = "Sub";
				else if (op == Operator.DIV)
					IRop = "Div";
				else if (op == Operator.MULTIPLY)
					IRop = "Mul";
				else if (op == Operator.MOD)
					IRop = "Mod";
				else
					IRop = "";
				reg = IR.arithmetic_op(lhsType_type.ir_val, rhsType_type.ir_val, IRop);
				return new VarType("int", reg);
			}
		}
		if (op == Operator.GT || op == Operator.GTE || op == Operator.LT || op == Operator.LTE)
		{
			if (!(lhsType.equals("int") && rhsType.equals("int"))){
				if (run_num == 1) error("The binary operator '" + op
						+ "' accepts only Integer types as operands. got lhs: " +
						lhsType + ". rhs: " + rhsType, expr);
			}
			else{
				reg = IR.compare_op(lhsType_type.ir_val, rhsType_type.ir_val, op);
				return new VarType("boolean",reg);
			}
		}
		if (op == Operator.EQUAL || op == Operator.NEQUAL) {
			if (run_num == 0)
				return new VarType("boolean","");
			if (run_num == 1) {
				if (lhsType.equals(rhsType) || lhsType.equals("null") || rhsType.equals("null")){
					reg = IR.compare_op(lhsType_type.ir_val, rhsType_type.ir_val, op);
					return new VarType("boolean",reg);
				}
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

				icClass parent = ((icClass) lhsClass).ext;
				while (parent != null) {
					if (rhsType.equals(parent.name)){
						reg = IR.compare_op(lhsType_type.ir_val, rhsType_type.ir_val, op);
						return new VarType("boolean",reg);
					}
					else
						parent = parent.ext;
				}
				// if reached here - lhsType doesn't extend rhsType
				//
				// check if rhsType extends lhsType
				icObject rhsClass = env.getObjByName(rhsType);
				if (!(rhsClass instanceof icClass))
					error("Undefined type: " + rhsType, expr);
				parent = ((icClass) rhsClass).ext;

				while (parent != null) {
					if (lhsType.equals(parent)){
						reg = IR.compare_op(lhsType_type.ir_val, rhsType_type.ir_val, op);
						return new VarType("boolean",reg);
					}
					else
						parent = parent.ext;
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
		return new VarType("null",""); // default value for rum_num == 0

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
			
			d.lastClass.addVar(o, d);
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
		d.lastClass.addFunc(func, d, meth.isStatic);
		d.add(func);

		++ASTNode.scope;
		for (Formal formal : meth.formals.lst) {
			formal.type.ir_val = formal.id;
			d.lastFunc.arg_types.add(formal.type);
			icVariable v = new icVariable(formal.id, ASTNode.scope, formal.type);
			if (IS_DEBUG)
				System.out.println("adding new variable: " + v.name + " scope " + v.scope);
			d.add(v);
		}

		for (ASTStmt s : meth.stmts.statements) {
			s.accept(this, d);
		}
		
		// making sure the IR interpreter will exit after main
		if (func.name.equals("main")){
			IR.add_line("Library exit(0),Rdummy");
		}
		
		d.destroyScope(ASTNode.scope);
		--ASTNode.scope;
		return null;
	}

	@Override
	public VarType visit(ASTClassDecl cls, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting classDecl: " + cls.class_id + " at line: " + cls.line);
		if (run_num == 0)
		{
			icObject father = null;
			if (cls.extend.name != null && cls.extend.name != "") 
			{
				father = d.getObjByName(cls.extend.name);
				if (!(father instanceof icClass))
				{
					error("unknown parent class", cls.extend);
				}
			}
			icClass c = new icClass(cls.class_id, ASTNode.scope, (icClass)father);
			d.add(c);
			d.lastClass = c;
		}
		else
		{
			// in the second run-through we want the class that contains all the methods and fields scanned in the first run
			d.lastClass = (icClass) d.getObjByName(cls.class_id);
		}
		
		++ASTNode.scope;
		for (ASTNode fm : cls.fieldmeths.lst) {
			fm.accept(this, d);
		}
		d.destroyScope(ASTNode.scope);
		--ASTNode.scope;
		IR.class_dec(d.lastClass);
		return null;
	}

	@Override
	public VarType visit(ASTDotLength expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting expr.length at line: " + expr.line);
		VarType e = expr.e.accept(this, d);
		if (e.num_arrays != 0)
		{
			return new VarType("int", IR.dot_len(e.ir_val));
		}
		else 
		{
			error("tried accessing length field of non array expression: " + e, expr);
		}
		return null;
	}

	@Override
	public VarType visit(ASTNewArray expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting new array at line: " + expr.line);
		VarType index = expr.expr.accept(this, d);
		if ("int" != index.type || index.num_arrays != 0)
			error("bad indexer type. expected int, got: " + index, expr);
		String ir_rep = "";
		if (run_num == 1) {
			if (!(d.validateType(new VarType(expr.type)))) {
				error("unknown type: " + expr.type, expr);
			}
			ir_rep = IR.new_arr(index.ir_val, expr.type);
		}
		return new VarType(expr.type + "[]", ir_rep);
	}

	@Override
	public VarType visit(ASTNewObject expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting new object at line: " + expr.line);
		String ir_rep = "";
		if (run_num == 1) {
			d.validateType(new VarType(expr.type));
			icClass type_class = (icClass)d.getObjByName(expr.type);
			ir_rep = IR.new_obj(Integer.toString((type_class.fSize + 1)*4), expr.type, type_class.dv);
		}
		return new VarType(expr.type, ir_rep);
	}

	@Override
	public VarType visit(ASTElseStmt elseStmt, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting elseStmt at line: " + elseStmt.line);

		++ASTNode.scope;
		elseStmt.stmt.accept(this, env);
		System.out.println("else after stmt evaluation" );
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
			} else if (!(type_classo instanceof icClass)){
				error(type + " is not a class name", stmt);
			}
			
			if (!(type_classo instanceof icClass)){
				type.ir_val = id;
			}
			
			// case of assignment
			if (stmt.rhs != null) {
				rhs = stmt.rhs.accept(this, env);
				validateAssign(type, rhs, stmt, env);

				IR.move(id, rhs.ir_val);
			}
		}

		return type;
	}

	@Override
	public VarType visit(ASTWhileStmt stm, Environment env) {
		
		String whlStrt;
		String whlEnd;
		
		if (IS_DEBUG)
			System.out.println("accepting ASTWhileStmt at line: " + stm.line);
		
		ASTExpr expr = stm.expr;

		int nestFlag;
		whlStrt = IR.get_label("startWhile");;
		whlEnd = IR.get_label("endWhile");
		IR.whLblEnd = whlEnd;
		IR.whLblStrt = whlStrt;
		
		IR.add_line(whlStrt);
		
		VarType exprType = expr.accept(this, env);
		if (!exprType.type.equals("boolean") || exprType.num_arrays != 0)
			error("Expected boolean expression after 'while'", expr);
		
		IR.add_line("Compare 0," + exprType.ir_val);
		IR.add_line("JumpLE " + whlEnd );
		
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
		
		IR.add_line("Jump " + whlStrt );
		IR.add_line(whlEnd);
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
		
		if (stmt.name.equals("continue")){
			IR.add_line("jump " + IR.whLblStrt );
		}
		else if(stmt.name.equals("break")){
			IR.add_line("jump " + IR.whLblEnd );
		}
		
		return null;
	}

	@Override
	// handle non-simple objects
	public VarType visit(ASTRetExp expr, Environment env) {
		if (IS_DEBUG)
			System.out.println("accepting ASTRetExp at line: " + expr.line);
		VarType rExpr;
		if (expr.exp == null){
			 rExpr = new VarType("null");
		}
		else{
			 rExpr = expr.exp.accept(this, env);
		}
		VarType funcRet = env.lastFunc.getAssignType();
		validateAssign(funcRet, rExpr, expr, env);
		IR.add_line("Return " + rExpr.ir_val);
		return null;
	}

	@Override
	public VarType visit(ASTIfElseStmt stmt, Environment env) {
		String ifTrueLabel; 
		String ifFalseLabel;
		if (IS_DEBUG)
			System.out.println("accepting ASTIfElseStmt at line: " + stmt.line);
		VarType cond = stmt.expr.accept(this, env);
		
		
		if (!cond.type.equals("boolean") || cond.num_arrays != 0) {

			error("Type mismatch: cannot convert from " + cond + " to " +
						"boolean ", stmt);
		}
		
		IR.add_line("Compare 0," + cond.ir_val);
		ifTrueLabel = IR.get_label("_trueIfCond");
		
		IR.add_line("jumpFalse" + ifTrueLabel);
		ifFalseLabel = IR.get_label("_falseIfCond");
		IR.add_line(ifFalseLabel);
		IR.add_line(ifTrueLabel);
		
		++ASTNode.scope;
		stmt.stmt.accept(this, env);

		env.destroyScope(ASTNode.scope);
		--ASTNode.scope;
		
		if (stmt.elsestmt != null){
			IR.add_line(ifFalseLabel);
			stmt.elsestmt.accept(this,env);
		}
		else{
			IR.add_line(ifFalseLabel);
		}
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
		VarType res = null;
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
			
			if (ident instanceof icFunction){
				error(id + " cannot be resolved! ", expr);
			}
			
			if (IS_DEBUG)
				System.out.println("return 1: " + ident.getAssignType());
			if (run_num == 1) return ident.getAssignType();
			/*
			else return new VarType(ident.getAssignType().type,0,id);
			*/
		}

		exp1 = expr.e1.accept(this, env);
		if (expr.type == 1) {
			if (IS_DEBUG)
				System.out.println("entered loop");
			
			icObject clss = env.getObjByName(exp1.type);
			if (run_num == 1) {
				
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
						 res = ((icClass) clss).getFieldType(id, env);
						if (IS_DEBUG)
							System.out.println("return 2 " + res);
						if (run_num == 1) return res;
					}
				}
				else
				{
					error(exp1.type + " is not a class", expr);					
				}
			} else
				res = ((icClass) clss).getFieldType(id, env);
				/*
				return new VarType(res.type,0,id);
				*/

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

			VarType out_type = new VarType(exp1.type, exp1.num_arrays - 1,exp1.ir_val);
			if (IS_DEBUG)
				System.out.println("returning exp1: " + out_type); // need
			return out_type;
		}
		if (IS_DEBUG)
			System.out.println("returning null");
		/*
		return new VarType(exp1.type,exp1.num_arrays - 1,exp1.ir_val);
		*/
		return new VarType("null");
	}

	@Override
	public VarType visit(ASTVirtualCall vc, Environment env) {
		if (run_num != 1)
			return new VarType("null");
		
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
		int irLbFlg = 0;  // this flag is 1 if the function is a library function
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
		
		if (classId.equals("Library")){
			irLbFlg =1;
		}
		
		icFunction func = (icFunction) className.getObject(funcId, env);
		
		if (IS_DEBUG)
			System.out.println("static func " + func.name);
		if (expLst.size() != func.getNumofArg()) {
			error("The method " + funcId + "in the type" + classId + " is not applicable for the arguments", expr);
		}
		String args = "";
		for (int i = 0; i < expr.exprList.lst.size(); i++) {
			exp = expLst.get(i).accept(this, env);
			funcArg = func.arg_types.get(i);
			
			if (irLbFlg ==0){
			args += funcArg.ir_val + "=" + exp.ir_val;
			}
			else{
				args += exp.ir_val;
			}
			
			if (i!= expr.exprList.lst.size() - 1)
				args += ",";
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
		String reg = IR.new_temp();
		args += "),"+ reg;
		
		
		
		IR.staticCall(funcId, args,irLbFlg);
		if (IS_DEBUG)
			System.out.println("return static " + func.getAssignType());
		func.getAssignType().ir_val = reg;
		return func.getAssignType();

	}

	@Override
	public VarType visit(ASTLiteral expr, Environment d) {
		if (IS_DEBUG)
			System.out.println("accepting ASTLiteral at line: " + expr.line);
		if (IS_DEBUG)
			System.out.println(expr.literalType);
		String lit = expr.getType();
		if (lit.equals("true"))
		{
			return new VarType("boolean", "1");
		} 
		else if (lit.equals("false"))
		{
			return new VarType("boolean", "0");
		}
		else if (lit.equals("null")) 
		{
			return new VarType("null", "0");
		} 
		else if (lit.equals("int")) 
		{
			return new VarType("int", expr.value);
		}
		else 
		{
			String str_name = IR.add_str(expr.value);
			return new VarType("string", str_name);
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