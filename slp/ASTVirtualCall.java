package slp;

public class ASTVirtualCall extends ASTStmt {
	
	/*
	 * 
	 * virtualCall ::= expr:e1 DOT ID:i1 LP expr_list:e2 RP
		{: Parser.print_rule("virtualCall -> 'e1.i1(e2)' for i1="+i1); 
		RESULT = new ASTVirtualCall(e1,i1,e2);
		:}
		 | ID:i1 LP expr_list:e2 RP
		{: Parser.print_rule("virtualCall -> 'i1(e2)' for i1="+i1);
		RESULT = new ASTVirtualCall(i1,e2);
		 :}
	 * 
	 * */
	
	public String id;
	public ASTExprList exprList;
	public ASTExpr expr = null;
	public int type = 0;
	
	public ASTVirtualCall(String id, ASTExprList exprList){
		this.id = id;
		this.exprList = exprList;
		this.type = 0;
	}
	
	public ASTVirtualCall(ASTExpr expr, String id, ASTExprList exprList){
		this.id = id;
		this.exprList = exprList;
		this.expr = expr;
		this.type = 1;
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
