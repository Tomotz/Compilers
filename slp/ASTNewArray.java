package slp;

public class ASTNewArray extends ASTExpr {
	public final String type;
	public final ASTExpr expr;
	
	public ASTNewArray(String type, ASTExpr expr, int line){
		this.type = type;
		this.expr = expr;
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
	
	
	public String toString() {
		return "New array of type "+ type + "and length:" + expr.toString();
	}	

}
