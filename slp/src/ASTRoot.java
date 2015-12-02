package slp;

/*a generic node in the ast tree*/
public class ASTRoot extends ASTNode{
	public final String name;
	public final ASTNode child;

	public ASTRoot(String name, ASTNode child) {
		this.name = name;
		this.child = child;
	}
	public ASTRoot() {
		this.name = "";
		this.child = null;
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
