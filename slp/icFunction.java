
package slp;

import java.util.ArrayList;
import java.util.List;

import slp.icObject;


public class icFunction extends icObject {

	String retType;
	String classType;              // the father class of the function
	boolean isStatic;              // states whether the function is static
	List<String> varName = new ArrayList<String>();  //  list of names of variables that the function declares
	int varNum;   // number of variables that the function declares
	

	
	public icFunction(String name,int scope, String retType,String classType,boolean isStatic ) {
		super(name, scope);
		this.retType = retType;
		this.classType = classType;
		this.isStatic = isStatic;
	}

	void addVariable (String name){
		varName.add(name);
		varNum += 1;
	}
}
