
package slp;

import java.util.ArrayList;
import java.util.List;

import slp.icObject;


public class icFunction extends icObject {

	String retType;
	List<String> varType = new ArrayList<String>();
	List<String> methods = new ArrayList<String>();
	
	
	public icFunction(String name, String retType, int scope) {
		super(name, scope);
	}

}
