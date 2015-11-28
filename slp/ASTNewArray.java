package slp;

public class ASTNewArray extends ASTExpr {
	public final String type;
	public final ASTExpr expr;
	
	public ASTNewArray(String type, ASTExpr expr){
		this.type = type;
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
	
	public String toString() {
		return "New array of type "+ type + "and length:" + expr.toString();
	}	

}
