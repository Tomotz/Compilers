package slp;

public class ASTMethod extends ASTNode {
	public final Boolean isStatic;
	public final String type;
	public final String id;
	public final ASTFormalList formals;
	public final ASTStmtList stmts;
	
	public ASTMethod(Boolean isStatic, String type, String id, 
			ASTFormalList formals, ASTStmtList stmts, int line) {
		this.type = type;
		this.id = id;
		this.isStatic = isStatic;
		this.formals = formals;
		this.stmts = stmts;
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
