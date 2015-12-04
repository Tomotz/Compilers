package slp;

public class ASTIfElseStmt extends ASTStmt {

	public  ASTStmt elsestmt = null;
	public final ASTStmt stmt; 
	public final ASTExpr expr;
	
	public ASTIfElseStmt( ASTExpr expr, ASTStmt stmt, ASTStmt elsestmt) {
		this.elsestmt = elsestmt;
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
