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
		// TODO Auto-generated method stub

	}

	@Override
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType context) {
		// TODO Auto-generated method stub
		return null;
	}

}
