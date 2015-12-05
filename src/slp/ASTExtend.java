package slp;

/*a generic node in the ast tree*/
public class ASTExtend extends ASTNode{
	public final String name;

	public ASTExtend(String name, int line) {
		this.name = name;
		this.line = line;
	}
	public ASTExtend(int line) {
		this.name = "";
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
