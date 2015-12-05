package slp;

public class ASTElseStmt  extends ASTStmt {
	public final ASTStmt stmt; 
	
	public ASTElseStmt(ASTStmt stmt, int line) {
		this.stmt = stmt;
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
