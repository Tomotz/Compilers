package slp;

public class icVariable extends icObject {
	
	String type; //basic type of variable (i.e. int ,String...) or 'object type' 
	boolean isArray; // states whether the regular array or an array
	int arrSize = 0;
	//int size;
	// int value;

	public icVariable(String name, int scope, String type, boolean isArray) {
		super(name, scope);
		this.type = type;
		this.isArray = isArray;
		
	}

	public icVariable(String name, int scope, String type) {
		super(name, scope);
		this.type = type;
		
	}
	
	public icVariable(String name, int scope, String type, boolean isArray, int arrSize) {
		super(name, scope);
		this.type = type;
		this.isArray = isArray;
		this.arrSize = arrSize;
	}

	@Override
	public String getAssignType() {
		return this.type;
	}
}
