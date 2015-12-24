package slp;


public class icVariable extends icObject {
	VarType type;

	public icVariable(String name, int scope, VarType type) {
		super(name, scope);
		this.type = type;
		type.ir_val = name;
	}
	public icVariable(String name, int scope, VarType type, String ir_val) {
		super(name, scope);
		this.type = type;
		type.ir_val = ir_val;
	}
	
	@Override
	public VarType getAssignType() {
		return this.type;
	}
	
}
