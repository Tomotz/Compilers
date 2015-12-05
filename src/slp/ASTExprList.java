package slp;

import java.util.ArrayList;
import java.util.List;

public class ASTExprList extends ASTExpr {

	public final List<ASTExpr> lst = new ArrayList<ASTExpr>();

	public ASTExprList(ASTExpr node, int line) {
		lst.add(node);
		this.line = line;
	}
	public ASTExprList(int line) {
		this.line = line;
	}

	public void add(ASTExpr n) {
		lst.add(n);
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
