package slp;

/** An interface for a propagating AST visitor.
 * The visitor passes down objects of type <code>DownType</code>
 * and propagates up objects of type <code>UpType</code>.
 */
public interface PropagatingVisitor<DownType,UpType> {
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
}