package slp;

public class ASTMethod extends ASTNode {
	public final Boolean isStatic;
	public final String type;
	public final String id;
	public final ASTNode formals;
	public final ASTNode stmts;
	
	public ASTMethod(Boolean isStatic, String type, String id, ASTNode formals,
			ASTNode stmts) {
		this.type = type;
		this.id = id;
		this.isStatic = isStatic;
		this.formals = formals;
		this.stmts = stmts;
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
