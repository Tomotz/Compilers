package slp;
 
public class ASTWhileStmt  extends ASTStmt {
 
	public final ASTStmt stmt; 
	public final ASTExpr expr;
	
	public ASTWhileStmt(ASTExpr expr, ASTStmt stmt, int line) {
		this.stmt = stmt;
		this.expr = expr;
		this.line = line;
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
