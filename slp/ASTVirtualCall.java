package slp;

public class ASTVirtualCall extends ASTExpr {
	
	public String id;
	public ASTExprList exprList;
	public ASTExpr expr = null;
	
	public ASTVirtualCall(String id, ASTExprList exprList){
		this.id = id;
		this.exprList = exprList;
	}
	
	public ASTVirtualCall(ASTExpr expr, String id, ASTExprList exprList){
		this.id = id;
		this.exprList = exprList;
		this.expr = expr;
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
