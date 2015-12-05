package slp;

import java.util.ArrayList;
import java.util.List;

public class ASTfmList extends ASTNode {

	public final List<ASTNode> lst = new ArrayList<ASTNode>();

	public ASTfmList(ASTNode node, int line) {
		lst.add(node);
		this.line = line;
	}
	public ASTfmList(int line) {
		this.line = line;
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
