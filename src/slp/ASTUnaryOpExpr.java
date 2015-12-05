package slp;

/** An AST node for unary expressions.
 */
public class ASTUnaryOpExpr extends ASTExpr {
	public final Operator op;
	public final ASTExpr operand;
	
	public ASTUnaryOpExpr(ASTExpr operand, Operator op, int line) {
		this.operand = operand;
		this.op = op;
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
		return op + operand.toString();
	}
}