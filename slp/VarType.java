package slp;

public class VarType {
	String type; //basic type of variable (i.e. int ,String...) or 'object type' 
	int num_arrays = 0; // the number of array dimentions in this variable. 0 means not an array

	
	public VarType(String Type)
	{
		this.type = type.replace("[]", "");
		setArray(type);
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
}
