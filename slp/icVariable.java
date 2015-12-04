package slp;

public class icVariable extends icObject {
	String kind; // basic type of variable (i.e. int ,String...)
	String type; // 'object type' if the variable has one 
	boolean isArray; // states whether the regular array or an array
	int arrSize = 0;
	//int size;
	// int value;
	
	public icVariable(String name, int scope, String kind, String type, boolean isArray) {
		super(name, scope);
		this.kind = kind;
		this.type = type;
		this.isArray = isArray;
		
	}
	
	public icVariable(String name, int scope, String kind, String type, boolean isArray, int arrSize) {
		super(name, scope);
		this.kind = kind;
		this.type = type;
		this.isArray = isArray;
		this.arrSize = arrSize;
	}
}
