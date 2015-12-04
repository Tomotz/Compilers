package slp;

public class ASTField extends ASTNode {
	public final String type;
	ASTIdList ids;
	
	public ASTField(String type, ASTIdList ids) {
		this.type = type;
		this.ids = ids;
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
