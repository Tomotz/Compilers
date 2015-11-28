package slp;

public class ASTElseStmt  extends ASTStmt {
	public final ASTStmt stmt; 
	
	public ASTElseStmt(ASTStmt stmt) {
		this.stmt = stmt;
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
