package slp;

public class ASTCallStmt extends ASTStmt {

	public final ASTExpr call; 
	
	public ASTCallStmt( ASTExpr call) {
		this.call = call;
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
