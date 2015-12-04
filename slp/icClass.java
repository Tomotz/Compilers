
package slp;
import java.util.ArrayList;
import java.util.List;

import slp.icObject;


public class icClass extends icObject {
	
	List<String> statScope = new ArrayList<String>();  // list of names for static methods
	List <String> instScope = new ArrayList<String>(); // list of names for dynamic methods and fields
	String ext;  // the class that the method extends (or null)

	
	public icClass(String name, int scope) {
		super(name, scope);
	}
	
	void addStaticObj (String name){
		statScope.add(name);
	}
	
	void addInstObj (String name){
		instScope.add(name);
	}

}
