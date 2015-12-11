
package slp;
import java.util.ArrayList;
import java.util.List;

import slp.icObject;


public class icClass extends icObject {
	List<icFunction> statScope = new ArrayList<icFunction>();  // list of names for static methods
	List <icObject> instScope = new ArrayList<icObject>(); // list of names for dynamic methods and fields
	String ext;  // the class type that the method extends (null if the class is a base class)


	
	public icClass(String name, int scope) {
		super(name, scope);
	}
	
	
	void addObject(icObject o, Environment d, boolean isStatic){
		if (this.hasObject(o.name, d))
		{ //there is already an object with this name in current scope
			throw new RuntimeException(
					"multiple declerations of object: " + o.name);
		}
		if (ICEvaluator.run_num == 1 && ext != null && ext != "")
		{
			icClass father = (icClass)d.getObjByName(ext);
			if (father.hasObject(o.name, d))
			{ //father class already has an object with that name
				throw new RuntimeException(
						"multiple declerations of object: " + o.name);
			}
		}
		if (isStatic)
			statScope.add((icFunction)o);
		else
			instScope.add(o);
	}
	
	/*returns true if class already has an object with the given object name.
	 * otherwise returns 1
	 */
	Boolean hasObject(String objectName, Environment env)
	{
		return getObject(objectName, env) != null ;
	}

	icObject getObject(String objectName, Environment env)
	{
		icClass cur = this;
		while (cur != null)
		{
			for (icObject o : cur.instScope) {
				if (o.name.equals(objectName)){
					return o;
				}
			}
			for (icFunction f : cur.statScope) {
				if (f.name.equals(objectName)){
					return f;
				}
			}
			cur = (icClass)env.getObjByName(cur.ext);
		}
		return null;
	}

	@Override
	public VarType getAssignType() {
		return new VarType(this.name);
	}
	
	
	public boolean checkIfSubType(icObject parent, Environment env){
		icClass cur = this;
		while (cur != null){
			if (cur.getName().equals(parent.getName())){
				return true;
			}
			cur = (icClass) env.getObjByName(cur.ext);
		}
		return false;		
	}


	public VarType getFieldType(String id, Environment env) {
		return getObject(id, env).getAssignType();
	}
	

}
