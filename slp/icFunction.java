
package slp;

import java.util.ArrayList;
import java.util.List;

import slp.icObject;


public class icFunction extends icObject {

	VarType retType;
	//String classType;              // the father class of the function
	boolean isStatic;              // states whether the function is static
	List<VarType> arg_types = new ArrayList<VarType>();  //  list of function argument types
	String label; //the label of function start
	

	
	public icFunction(String name,int scope, VarType retType,
			boolean isStatic ) {
		super(name, scope);
		this.retType = retType;
		this.isStatic = isStatic;
	}


	@Override
	public VarType getAssignType() {
		return this.retType;
	}
	
	public int getNumofArg(){
		return arg_types.size();
	}

}
