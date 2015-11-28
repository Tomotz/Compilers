package slp;

public class ASTDotLength extends ASTExpr {
	public final ASTExpr e;
	
	public ASTDotLength(ASTExpr e){
		this.e = e;
	}
	@Override
	public void accept(Visitor visitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString() {
		return "Length of " + e.toString();
	}	

}
