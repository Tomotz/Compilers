package slp;

import java.util.ArrayList;
import java.util.List;

public class ASTExprList extends ASTExpr {

	public final List<ASTExpr> lst = new ArrayList<ASTExpr>();

	public ASTExprList(ASTExpr node) {
		lst.add(node);
	}
	public ASTExprList() {
	}

	public void add(ASTExpr n) {
		lst.add(n);
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
