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
	
	public ASTLiteral(String s, int i, int line){
		this.literalType=i;
		this.s = s;
		this.line = line;
	}


	/** Accepts a visitor object as part of the visitor pattern.
	 * @param visitor A visitor.
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	/** Accepts a propagating visitor parameterized by two types.
	 * 
	 * @param <DownType> The type of the object holding the context.
	 * @param <UpType> The type of the result object.
	 * @param visitor A propagating visitor.
	 * @param context An object holding context information.
	 * @return The result of visiting this node.
	 */
	@Override
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType context) {
		return visitor.visit(this, context);
	}
	
}
