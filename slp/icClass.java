
package slp;
import java.util.ArrayList;
import java.util.List;

import slp.icObject;


public class icClass extends icObject {
	
	List<icFunction> statScope = new ArrayList<icFunction>();  // list of names for static methods
	List <icObject> instScope = new ArrayList<icObject>(); // list of names for dynamic methods and fields
	String ext;  // the class that the method extends (or null)

	
	public icClass(String name, int scope) {
		super(name, scope);
	}
	
}
