package slp;

public class ASTStaticCall extends ASTExpr {
	public String classId;
	public String id;
	public ASTExprList exprList;
	public ASTStaticCall(String classId, String id, ASTExprList exprList){
		this.classId = classId;
		this.id = id;
		this.exprList = exprList;
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
