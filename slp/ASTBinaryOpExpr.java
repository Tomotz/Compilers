package slp;

/** An AST node for binary expressions.
 */
public class ASTBinaryOpExpr extends ASTExpr {
	public final ASTExpr lhs;
	public final ASTExpr rhs;
	public final Operator op;
	
	public ASTBinaryOpExpr(ASTExpr lhs, ASTExpr rhs, Operator op) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
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
		return lhs.toString() + op + rhs.toString();
	}	
}