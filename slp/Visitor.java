package slp;

/** An interface for AST visitors.
 */
public interface Visitor {
	public void visit(ASTScope stmts);
	public void visit(ASTStmtList stmts);
	public void visit(ASTAssignStmt stmt);
	public void visit(ASTVarExpr expr);
	public void visit(ASTNumberExpr expr);
	public void visit(ASTUnaryOpExpr expr);
	public void visit(ASTBinaryOpExpr expr);
	public void visit(ASTRoot node);
	public void visit(ASTfmList astGenList);
	public void visit(ASTClassDecl cls);
	public void visit(ASTExtend node);
	public void visit(ASTField astField);
	public void visit(ASTIdList astIdList);
	public void visit(ASTClassList astClassList);
	public void visit(ASTMethod astMethod);
	public void visit(ASTStatType astStatType);
	public void visit(ASTFormalList astFormalList);
	public void visit(ASTElseStmt astElseStmt);
	public void visit(ASTAssignFormals astAssignFormals);
	public void visit(ASTWhileStmt astWhileStmt);
	public void visit(ASTVarStmt astVarStmt);
	public void visit(ASTRetExp astRetExp);
	public void visit(ASTIfElseStmt astIfElseStmt);
	public void visit(ASTCallStmt astCallStmt);
	public void visit(ASTLocation astLocation);
	public void visit(ASTVirtualCall astVirtualCall);
	public void visit(ASTStaticCall astStaticCall);
	public void visit(ASTNewObject astNewObject);
	public void visit(ASTNewArray astNewArray);
	public void visit(ASTLiteral astLiteral);
	public void visit(ASTExprList astExprList);
	public void visit(ASTDotLength astDotLength);
}