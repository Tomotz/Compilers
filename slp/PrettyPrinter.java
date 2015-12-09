package slp;


/** Pretty-prints an SLP AST.
 */
public class PrettyPrinter implements Visitor {
	protected final ASTNode root;

	void printstr(Object str)
	{
		//System.out.print(str);
	}
	void printstrln(Object str)
	{
		//System.out.println(str);
	}
	void printstrln()
	{
		//System.out.println();
	}
	/** Constructs a printin visitor from an AST.
	 * 
	 * @param root The root of the AST.
	 */
	public PrettyPrinter(ASTNode root) {
		this.root = root;
	}
	
	public void lineStart() {
		for (int i=0;i<ASTNode.indent;++i)
			printstr("-");
		printstr(" ");
	}
	/** Prints the AST with the given root.
	 */
	public void print() {
		root.accept(this);
	}
	
	public void visit(ASTStmtList stmts) {
		ASTNode.indent++;
		for (ASTStmt s : stmts.statements) {
			if (s != null)
				s.accept(this);
		}
		ASTNode.indent--;
	}

	
	
	
	public void visit(ASTVarExpr expr) {
		ASTNode.indent++;
		printstr(expr.name);
		ASTNode.indent--;
	}
	
	public void visit(ASTNumberExpr expr) {
		ASTNode.indent++;
		printstr(expr.value);
		ASTNode.indent--;
	}
	
	public void visit(ASTUnaryOpExpr expr) {
		ASTNode.indent++;
		printstr(expr.op);
		expr.operand.accept(this);
		ASTNode.indent--;
	}
	
	public void visit(ASTBinaryOpExpr expr) {
		ASTNode.indent++;
		lineStart();
		printstr("Logical binary operation: " );
		printstrln(expr.op);
		expr.lhs.accept(this);
		expr.rhs.accept(this);
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTRoot node) {
		ASTNode.indent++;
		if (node.child != null)
		{
			//printstrln(node.name);
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
			printstrln();
		}
		ASTNode.indent--;
	}
	@Override
	public void visit(ASTClassList classes) {
		ASTNode.indent++;
		for (ASTClassDecl n : classes.lst) {
			lineStart();
			n.accept(this);
			printstrln();
		}
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTClassDecl cls) {
		ASTNode.indent++;
		printstr("decleration of class: ");
		printstr(cls.class_id + "\n");
		cls.extend.accept(this);
		cls.fieldmeths.accept(this);
		ASTNode.indent--;
		
	}

	@Override
	public void visit(ASTExtend node) {
		ASTNode.indent++;
		if (node.name != "")
		{
			printstr("extend ");
			printstr(node.name);
		}
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTField astField) {
		ASTNode.indent++;
		printstr(astField.line + ": decleration of fields: ");
		astField.ids.accept(this);
		printstr("; of type: ");
		printstr(astField.type);
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTIdList astIdList) {
		ASTNode.indent++;
		int i=1;
		for (String n : astIdList.lst) {
			printstr(n);		
			if (i<astIdList.lst.size())
				printstr(", ");
			++i;
				
		}
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTMethod astMethod) {
		ASTNode.indent++;
		if (astMethod.isStatic)
			printstr("decleration of static method: ");
		else
			printstr("decleration of virtual method: ");
		printstr(astMethod.id);
		printstr("; of type: ");
		printstrln(astMethod.type);
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
			printstr("Parameter: ");
			printstr(f.id);
			printstr("; of type: ");
			printstrln(f.type);
		}
		ASTNode.indent--;
		
	}
	
	@Override
	public void visit(ASTAssignStmt assign){
		if (assign != null){
			ASTNode.indent++;
			if (assign.varExpr != null){
				lineStart();
				printstrln("Assignment statement");
				assign.varExpr.accept(this);
			}
			if (assign.rhs != null)
				assign.rhs.accept(this);
			ASTNode.indent--;
		}
	}
	
	public void visit(ASTRetExp ret){
		ASTNode.indent++;
		lineStart();
		printstrln("Return statement, with return value");
		ret.exp.accept(this);	
		ASTNode.indent--;
		}
	
	public void visit(ASTIfElseStmt ifstmt){
		ASTNode.indent++;
		lineStart();
		printstrln("If statement");
		ifstmt.expr.accept(this);
		lineStart();
		printstrln("Block of statements");
		ifstmt.stmt.accept(this);
		if (ifstmt.elsestmt != null)
			ifstmt.elsestmt.accept(this);
	
		ASTNode.indent--;
	}
	
	public void visit(ASTWhileStmt whl){
		ASTNode.indent++;
		lineStart();
		printstrln("While statement");
		whl.expr.accept(this);
		lineStart();
		printstrln("Block of statements");
		whl.stmt.accept(this);
		ASTNode.indent--;
	}
	
	public void visit(ASTVarStmt varStmt){
		ASTNode.indent++;
		lineStart();
		varStmt.toString();
		printstr("statement \n");
		ASTNode.indent--;
	}
	
	public void visit(ASTAssignFormals varStmt){
		ASTNode.indent++;

		lineStart();
		printstr("Declaration of local variable: ");
		printstr(varStmt.form.id);
		printstrln(", with initial value");
		lineStart();
		printstr("Primitive data type: ");
		printstrln(varStmt.form.type);
		if (varStmt.rhs != null){
			varStmt.rhs.accept(this);
		}
		ASTNode.indent--;
		
	}
	
	public void visit(ASTElseStmt elseStmt){
		ASTNode.indent++;
		lineStart();
		if (elseStmt.stmt != null){
			printstr("Else statement \n");
			lineStart();
			printstr("Block of statements \n");
			elseStmt.stmt.accept(this);
		}
		ASTNode.indent--;
	}

	public void visit(ASTNewObject obj){
		ASTNode.indent++;
		lineStart();
		printstr("Instantiation of class: ");
		printstrln(obj.type);
		ASTNode.indent--;
	}
	
	public void visit(ASTNewArray arr){
		ASTNode.indent++;
		lineStart();
		printstr("Array allocation of type: ");
		printstrln(arr.type);
		arr.expr.accept(this);
		ASTNode.indent--;
	}
	
	public void visit(ASTExprList eList){
		ASTNode.indent++;
		for (ASTExpr exp : eList.lst){
			exp.accept(this);
		}
	}
	
	@Override
	public void visit(ASTDotLength expr) {
		ASTNode.indent++;
		lineStart();
		printstrln("Reference to object length");
		expr.e.accept(this);
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTLiteral l){
		ASTNode.indent++;
		lineStart();
		switch (l.literalType){
		case 0:
			printstrln("Integer literal: "+l.s);
			break;
		case 1:
			
			printstrln("String literal: "+l.s);
			break;
		case 2:
			printstrln("Boolean literal: "+l.s);
			break;
		case 3:
			printstrln("Boolean literal: "+l.s);
			break;
		case 4:
			printstrln("Null literal");
			break;
		}
		ASTNode.indent--;
	}
	
	public void visit(ASTLocation loc){
		ASTNode.indent++;
		switch (loc.type){
			case 0:
				lineStart();
				printstrln(loc.line + ": Reference to variable: " + loc.id);
				break;
			case 1:
				lineStart();
				printstrln(loc.line + ": Reference to field or function: ");
				loc.e1.accept(this);
				lineStart();
				printstrln(loc.line + ": Reference to variable: " + loc.id);
				break;
			case 2:
				lineStart();
				printstrln(loc.line + ": Reference to array");
				loc.e1.accept(this);
				loc.e2.accept(this);
				break;
		
			
		}
		ASTNode.indent--;
	}
	
	public void visit(ASTStaticCall c){
		ASTNode.indent++;
		lineStart();
		printstrln("Call to static method: "+ c.classId + "." + c.id);
		c.exprList.accept(this);
		ASTNode.indent--;
	}
	
	public void visit(ASTVirtualCall c){
		ASTNode.indent++;
		switch (c.type){
			case 0:
				lineStart();
				printstrln("Call to virtual method: " + c.id);
				c.exprList.accept(this);
				break;
			case 1:
				c.expr.accept(this);
				lineStart();
				printstrln("Call to virtual method: " + c.id);
				c.exprList.accept(this);
		}
		ASTNode.indent--;
	}

	@Override
	public void visit(ASTCallStmt astCallStmt) {
		
	}


	
	
}

