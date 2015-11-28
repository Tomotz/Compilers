package slp;

public class ASTWhileStmt  extends ASTStmt {
 
	public final ASTStmt stmt; 
	public final ASTExpr expr;
	
	public ASTWhileStmt(ASTStmt whStmt ,ASTExpr expr, ASTStmt stmt) {
		this.stmt = stmt;
		this.expr = expr;
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public <DownType, UpType> UpType accept(
		PropagatingVisitor<DownType, UpType> visitor, DownType context) {
			return visitor.visit(this, context);
	}
}
