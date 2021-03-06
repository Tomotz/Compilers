package slp;

import java.util.ArrayList;
import java.util.List;

public class ASTClassList extends ASTNode {

	public final List<ASTClassDecl> lst = new ArrayList<ASTClassDecl>();

	public ASTClassList(ASTClassDecl node, int line) {
		lst.add(node);
		this.line = line;
	}
	public ASTClassList(int line) {
		this.line = line;
	}

	public void addNode(ASTClassDecl n) {
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
