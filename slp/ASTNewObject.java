package slp;

public class ASTNewObject extends ASTExpr {
	
	public final String c;
	
	public ASTNewObject(String c){
		this.c = c;
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
		return "New object of class " + c;
	}	

}
