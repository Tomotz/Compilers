
package slp;

import java.util.ArrayList;
import java.util.List;

import slp.icObject;


public class icFunction extends icObject {

	String retType;
	String classType;              // the father class of the function
	boolean isStatic;              // states whether the function is static
	List<String> arg_types = new ArrayList<String>();  //  list of function argument types

	

	
	public icFunction(String name,int scope, String retType,
			String classType, boolean isStatic ) {
		super(name, scope);
		this.retType = retType;
		this.classType = classType;
		this.isStatic = isStatic;
	}

}
