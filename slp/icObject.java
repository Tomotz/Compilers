package slp;

public abstract class icObject {
	
	String name;
	int scope;
	
	abstract public VarType getAssignType();
	
	public icObject(String name, int scope)
	{
		this.name = name;
		this.scope = scope;
			
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getScope(){
		return this.scope;
	}

}
