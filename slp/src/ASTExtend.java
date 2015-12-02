package slp;

/*a generic node in the ast tree*/
public class ASTExtend extends ASTNode{
	public final String name;

	public ASTExtend(String name) {
		this.name = name;
	}
	public ASTExtend() {
		this.name = "";
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
