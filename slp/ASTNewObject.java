package slp;

public class ASTNewObject extends ASTExpr {
	
	public final String c;
	
	public ASTNewObject(String c){
		this.c = c;
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
		return "New object of class " + c;
	}	

}
