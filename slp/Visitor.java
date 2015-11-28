package slp;

/** An interface for AST visitors.
 */
public interface Visitor {
	public void visit(ASTStmtList stmts);
	public void visit(ASTStmt stmt);
	public void visit(ASTAssignStmt stmt);
	public void visit(ASTExpr expr);
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
}