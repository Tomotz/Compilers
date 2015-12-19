package slp;

public class VarType {
	String type; //basic type of variable (i.e. int ,String...) or 'object type' 
	int num_arrays = 0; // the number of array dimentions in this variable. 0 means not an array
	String ir_val; //value of variable as shown in IR code

	public VarType(String Type, String ir_val)
	{
		this.type = Type.replace("[]", "");
		this.ir_val = ir_val;
		setArray(Type);
	}

	//TODO - delete this constructor!!!! no one should use it!
	public VarType(String Type)
	{
		this.type = Type.replace("[]", "");
		this.ir_val = null;
		setArray(Type);
	}
	//TODO - delete this constructor!!!! no one should use it!
	public VarType(String baseType, int num_arrays)
	{
		this.ir_val = null;
		this.type = baseType;
		this.num_arrays = num_arrays;
	}

	public VarType(String baseType, int num_arrays, String ir_val)
	{
		this.ir_val = ir_val;
		this.type = baseType;
		this.num_arrays = num_arrays;
	}
	
	public void setArray(String id){
		int count = id.length() - id.replace("[", "").length();
		this.num_arrays = count;
	}
	
	@Override
	public String toString()
	{
		String out = this.type;
		for (int i = 0; i < num_arrays; i++) {
			out = out + "[]";
		}
		return out;
	}

	public boolean isBaseType(String baseType) {
		return (this.num_arrays == 0 && this.type.equals(baseType));
	}
}
