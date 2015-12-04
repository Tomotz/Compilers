package slp;

public class ASTRetExp extends ASTStmt {

public final ASTExpr exp;
	
	public ASTRetExp(ASTExpr exp) {
		this.exp = exp;
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

	
