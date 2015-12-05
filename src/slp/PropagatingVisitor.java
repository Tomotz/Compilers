package slp;

/** An interface for a propagating AST visitor.
 * The visitor passes down objects of type <code>DownType</code>
 * and propagates up objects of type <code>UpType</code>.
 */
public interface PropagatingVisitor<DownType,UpType> {
	public UpType visit(ASTFormalList astFormalList, DownType d);
	public UpType visit(ASTStatType astStatType, DownType d);
	public UpType visit(ASTMethod astMethod, DownType d);
	public UpType visit(ASTClassDecl astClassDecl, DownType d);
	public UpType visit(ASTClassList astClassList, DownType d);
	public UpType visit(ASTIdList astIdList, DownType d);
	public UpType visit(ASTField astField, DownType d);
	public UpType visit(ASTExtend astExtend, DownType d);
	public UpType visit(ASTfmList astGenList, DownType d);
	public UpType visit(ASTStmtList stmts, DownType d);
	public UpType visit(ASTRoot astGenNode, DownType d);
	public UpType visit(ASTStmt stmt, DownType d);
	public UpType visit(ASTAssignStmt stmt, DownType d);
	public UpType visit(ASTExpr expr, DownType d);
	public UpType visit(ASTVarExpr expr, DownType d);
	public UpType visit(ASTNumberExpr expr, DownType d);
	public UpType visit(ASTUnaryOpExpr expr, DownType d);
	public UpType visit(ASTBinaryOpExpr expr, DownType d);
	public UpType visit(ASTDotLength expr, DownType d);
	public UpType visit(ASTNewArray expr, DownType d);
	public UpType visit(ASTNewObject expr, DownType d);
	public UpType visit(ASTElseStmt stmt, DownType d);	
	public UpType visit(ASTAssignFormals stmt, DownType d);
	public UpType visit(ASTWhileStmt stmt, DownType d);
	public UpType visit(ASTVarStmt stmt, DownType d);
	public UpType visit(ASTRetExp expr, DownType d);
	public UpType visit(ASTIfElseStmt stmt, DownType d);
	public UpType visit(ASTCallStmt stmt, DownType d);
	public UpType visit(ASTLocation expr, DownType d);
	public UpType visit(ASTVirtualCall expr, DownType d);
	public UpType visit(ASTStaticCall expr, DownType d);
	public UpType visit(ASTLiteral expr, DownType d);
	public UpType visit(ASTExprList expr, DownType d);
	
}