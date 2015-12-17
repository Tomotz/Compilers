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
	public String value = null;
	
	public ASTLiteral(String value, int literalType, int line){
		this.literalType=literalType;
		this.value = value;
		this.line = line;
	}
	
	public String getType(){
		switch (this.literalType)
		{
		case 0:
			return "int";
		case 1:
			return "string";
		case 2:
			return "true";
		case 3:
			return "false";
		case 4:
			return "null";
		default:
			return "";
		}
		
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
