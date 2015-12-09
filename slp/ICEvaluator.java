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
		// TODO
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