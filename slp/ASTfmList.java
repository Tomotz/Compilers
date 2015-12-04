package slp;

import java.util.ArrayList;
import java.util.List;

public class ASTfmList extends ASTNode {

	public final List<ASTNode> lst = new ArrayList<ASTNode>();

	public ASTfmList(ASTNode node) {
		lst.add(node);
	}
	public ASTfmList() {
	}

	public void addNode(ASTNode n) {
		lst.add(n);
	}
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		
	}

	@Override
	public <DownType, UpType> UpType accept(
			PropagatingVisitor<DownType, UpType> visitor, DownType context) {
		return visitor.visit(this, context);
	}

}
