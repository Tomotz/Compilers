package slp;

public class ASTRetExp extends ASTStmt {

public final ASTExpr exp;
	
	public ASTRetExp(ASTExpr exp, int line) {
		this.exp = exp;
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

	
