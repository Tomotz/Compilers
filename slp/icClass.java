
package slp;
import java.util.ArrayList;
import java.util.List;

import slp.icObject;


public class icClass extends icObject {
	List<String> statMethod = new ArrayList<String>();
	
	public icClass(String name, int scope) {
		super(name, scope);
	}

}
