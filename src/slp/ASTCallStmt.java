package slp;

public class ASTCallStmt extends ASTStmt {

	public final ASTExpr call; 
	
	public ASTCallStmt( ASTExpr call, int line) {
		this.call = call;
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
