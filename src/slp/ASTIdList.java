package slp;

import java.util.ArrayList;
import java.util.List;

public class ASTIdList extends ASTNode {

	public final List<String> lst = new ArrayList<String>();

	public ASTIdList(String node, int line) {
		lst.add(node);
		this.line = line;
	}

	public void addNode(String n) {
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
