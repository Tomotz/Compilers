package slp;

public class ASTStatType extends ASTNode {
	public final Boolean isStatic;
	public final String type;
	public final String id;
	
	public ASTStatType(Boolean isStatic, String type, String id, int line) {
		this.type = type;
		this.id = id;
		this.isStatic = isStatic;
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
