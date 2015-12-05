package slp;

public class ASTClassDecl extends ASTNode {
	ASTfmList fieldmeths;
	public final String class_id;
	public final ASTExtend extend;
	
	public ASTClassDecl(String class_id, ASTExtend extend, ASTfmList fieldmeths, int line) {
		this.class_id = class_id;
		this.extend = extend;
		this.fieldmeths = fieldmeths;
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

