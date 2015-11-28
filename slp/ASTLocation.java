package slp;

public class ASTLocation extends ASTExpr {
	
	/*
	 * location ::= ID:id {: RESULT =  new ASTLocation(id); :} 
		| expr:e DOT ID:id {: RESULT = new ASTLocation(e,id); :}
		| expr:e1 LB expr:e2 RB {: RESULT =  new ASTLocation(e1,e2); :}
		
	 * */

	public String id = null;
	public ASTExpr e1 = null;
	public ASTExpr e2 = null;
	
	public ASTLocation(String id){
		this.id = id;
	}
	
	public ASTLocation(ASTExpr e1, String id){ 
		this.id = id;
		this.e1 = e1;
	}
	
	public ASTLocation(ASTExpr e1, ASTExpr e2){
		this.e1 = e1;
		this.e2 = e2;
		
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
