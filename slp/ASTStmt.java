package slp;

/** The super class of all AST node for program statements.
 */
public abstract class ASTStmt extends ASTExpr {

	/** Accepts a visitor object as part of the visitor pattern.
	 * @param visitor A visitor.
	 */
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}