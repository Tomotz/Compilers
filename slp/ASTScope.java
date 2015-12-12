package slp;

public class ASTScope extends ASTStmt {
	ASTStmtList s;
	ASTScope(ASTStmtList s, int line)
	{
		this.s = s;
		this.line = line;
		
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
