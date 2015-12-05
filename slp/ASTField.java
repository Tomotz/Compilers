package slp;

public class ASTField extends ASTNode {
	public final String type;
	ASTIdList ids;
	
	public ASTField(String type, ASTIdList ids, int line) {
		this.type = type;
		this.ids = ids;
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
