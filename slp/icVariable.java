package slp;


public class icVariable extends icObject {
	VarType type;
	public int offset;//offset of current var in class. 

	public icVariable(String name, int scope, VarType type) {
		super(name, scope);
		this.type = type;
	}
	
	@Override
	public VarType getAssignType() {
		return this.type;
	}
	
}
