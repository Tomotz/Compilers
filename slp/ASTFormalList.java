package slp;

import java.util.ArrayList;
import java.util.List;

public class ASTFormalList extends ASTStmt {
	public final List<Formal> lst = new ArrayList<Formal>();

	public ASTFormalList(String type, String id) {
		lst.add(new Formal(type, id));
	}
	public ASTFormalList() {
	}

	public void addNode(String type, String id) {
		lst.add(new Formal(type, id));
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
