package slp;


/** Pretty-prints an SLP AST.
 */
public class PrettyPrinter implements Visitor {
	protected final ASTNode root;

	/** Constructs a printin visitor from an AST.
	 * 
	 * @param root The root of the AST.
	 */
	public PrettyPrinter(ASTNode root) {
		this.root = root;
	}
	
	public void lineStart() {
		for (int i=0;i<ASTNode.indent;++i)
			System.out.print("-");
		System.out.print(" ");
	}
	/** Prints the AST with the given root.
	 */
	public void print() {
		root.accept(this);
	}
	
	public void visit(ASTStmtList stmts) {
		ASTNode.indent++;
		for (ASTStmt s : stmts.statements) {
			lineStart();
			s.accept(this);
			System.out.println();
		}
		ASTNode.indent--;
	}

	
	public void visit(ASTExpr expr) {
		ASTNode.indent++;
		throw new UnsupportedOperationException("Unexpected visit of Expr abstract class");
		//ASTNode.indent--;
	}	
	
	
	public void visit(ASTVarExpr expr) {
		ASTNode.indent++;
		System.out.print(expr.name);
		ASTNode.indent--;
	}
	
	public void visit(ASTNumberExpr expr) {
		ASTNode.indent++;
		System.out.print(expr.value);
		ASTNode.indent--;
	}
	
	public void visit(ASTUnaryOpExpr expr) {
		ASTNode.indent++;
		System.out.print(expr.op);
		expr.operand.accept(this);
		ASTNode.indent--;
	}
	
	public void visit(ASTBinaryOpExpr expr) {
		ASTNode.indent++;
		expr.lhs.accept(this);
		System.out.print(expr.op);
		expr.rhs.accept(this);
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTRoot node) {
		ASTNode.indent++;
		if (node.child != null)
		{
			//System.out.println(node.name);
			node.child.accept(this);
		}
		ASTNode.indent--;
		
	}

	@Override
	public void visit(ASTfmList fmList) {
		ASTNode.indent++;
		for (ASTNode n : fmList.lst) {
			lineStart();
			n.accept(this);
			System.out.println();
		}
		ASTNode.indent--;
	}
	@Override
	public void visit(ASTClassList classes) {
		ASTNode.indent++;
		for (ASTClassDecl n : classes.lst) {
			lineStart();
			n.accept(this);
			System.out.println();
		}
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTClassDecl cls) {
		ASTNode.indent++;
		System.out.print("decleration of class: ");
		System.out.print(cls.class_id + "\n");
		cls.extend.accept(this);
		cls.fieldmeths.accept(this);
		ASTNode.indent--;
		
	}

	@Override
	public void visit(ASTExtend node) {
		ASTNode.indent++;
		if (node.name != "")
		{
			System.out.print("extend ");
			System.out.print(node.name);
		}
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTField astField) {
		ASTNode.indent++;
		System.out.print("decleration of fields: ");
		astField.ids.accept(this);
		System.out.print("; of type: ");
		System.out.print(astField.type);
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTIdList astIdList) {
		ASTNode.indent++;
		int i=1;
		for (String n : astIdList.lst) {
			System.out.print(n);		
			if (i<astIdList.lst.size())
				System.out.print(", ");
			++i;
				
		}
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTMethod astMethod) {
		ASTNode.indent++;
		if (astMethod.isStatic)
			System.out.print("decleration of static method: ");
		else
			System.out.print("decleration of virtual method: ");
		System.out.print(astMethod.id);
		System.out.print("; of type: ");
		System.out.println(astMethod.type);
		astMethod.formals.accept(this);
		astMethod.stmts.accept(this);
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTStatType astStatType) {
		ASTNode.indent++;
		/*empty*/
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTFormalList formals) {
		ASTNode.indent++;
		for (Formal f : formals.lst) {
			lineStart();
			System.out.print("Parameter: ");
			System.out.print(f.id);
			System.out.print("; of type: ");
			System.out.println(f.type);
		}
		ASTNode.indent--;
		
	}
	
	@Override
	public void visit(ASTAssignStmt assign){
		if (assign != null){
			ASTNode.indent++;
			lineStart();
			if (assign.varExpr != null){
				System.out.print("Assignment statement");
				assign.varExpr.accept(this);
			}
			assign.rhs.accept(this);
			ASTNode.indent--;
		}
	}
	
	public void visit(ASTRetExp ret){
		ASTNode.indent++;
		lineStart();
		System.out.print("Return statement, with return value");
		ret.exp.accept(this);	
		ASTNode.indent--;
		}
	
	public void visit(ASTIfElseStmt ifstmt){
		ASTNode.indent++;
		lineStart();
		System.out.print("If statement \n");
		ifstmt.expr.accept(this);
		System.out.print("Block of statements \n");
		ifstmt.stmt.accept(this);
	
		ASTNode.indent--;
	}
	
	public void visit(ASTWhileStmt whl){
		ASTNode.indent++;
		lineStart();
		System.out.print("While statement \n");
		whl.expr.accept(this);
		System.out.print("Block of statements \n");
		whl.stmt.accept(this);
		ASTNode.indent--;
	}
	
	public void visit(ASTVarStmt varStmt){
		ASTNode.indent++;
		lineStart();
		varStmt.toString();
		System.out.print("statement \n");
		ASTNode.indent--;
	}
	
	public void visit(ASTAssignFormals varStmt){
		ASTNode.indent++;
		lineStart();
		
		System.out.print("Declaration of local variable: ");
		System.out.print(varStmt.form.id);
		System.out.println(", with initial value");
		lineStart();
		System.out.print("Primitive data type:");
		System.out.println(varStmt.form.type);
		lineStart();
		if (varStmt.rhs != null){0
			varStmt.rhs.accept(this);
		}
		ASTNode.indent--;
		
	}
	
	public void visit(ASTElseStmt elseStmt){
		ASTNode.indent++;
		lineStart();
		if (elseStmt.stmt != null){
			System.out.print("Else statement \n");
			lineStart();
			System.out.print("Block of statements \n");
			elseStmt.stmt.accept(this);
		}
		ASTNode.indent--;
	}


	@Override
	public void visit(ASTStmt stmt) {
		
	}
	
	
}

