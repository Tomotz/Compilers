package slp;

public class ASTLocation extends ASTExpr {
	
	/*
	 * location ::= ID:id {: RESULT =  new ASTLocation(id); :}
		| expr:e DOT ID:id {: RESULT = new ASTLocation(e,id); :}
		| expr:e1 LB expr:e2 RB {: RESULT =  new ASTLocation(e1,e2); :}
		
	 * */
	public int type = 0;
	public String id = null;
	public ASTExpr e1 = null;
	public ASTExpr e2 = null;
	
	public ASTLocation(String id, int line){
		this.id = id;
		this.type = 0;
		this.line = line;
	}
	
	public ASTLocation(ASTExpr e1, String id, int line){ 
		this.id = id;
		this.e1 = e1;
		this.type = 1;
		this.line = line;
	}
	
	public ASTLocation(ASTExpr e1, ASTExpr e2, int line){
		this.e1 = e1;
		this.e2 = e2;
		this.type = 2;
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
