package slp;

public class ASTLiteral extends ASTExpr {

/*
 * 
 * literal ::= INTEGER:l {: RESULT = new ASTLiteral(l,0) :} 
		| QUOTE:l {: RESULT = new ASTLiteral(l,1); :}
		| TRUE:l {: RESULT = new ASTLiteral(l,2); :} 
		| FALSE:l {: RESULT = new ASTLiteral(l,3); :}
		| NULL:l {: RESULT = new ASTLiteral(l,4); :}
		
 * */	
	public int literalType = 0;
	public String s = null;
	
	public ASTLiteral(String s, int i){
		this.literalType=i;
		this.s = s;
			
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
