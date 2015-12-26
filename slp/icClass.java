
package slp;
import java.util.ArrayList;
import java.util.List;

import slp.icObject;


public class icClass extends icObject {
	List<icFunction> statFuncs = new ArrayList<icFunction>();  // list of names for static methods
	List <icFunction> instFuncs = new ArrayList<icFunction>(); // list of names for dynamic methods
	List <icVariable> instVars = new ArrayList<icVariable>(); // list of names for dynamic fields
	icClass ext = null;  // the class type that the method extends (null if the class is a base class)
	int size = 0; //number of fields in class
	public String dv;
	public int num_funcs = 0; //number of virtual funcs

	
	public icClass(String name, int scope, icClass ext) {
		super(name, scope);
		this.ext = ext;
		if (ext == null){
			this.size = 0;
		}
		else{
			this.size = ext.size;
		}
	}
	

	void addFunc(icFunction f, Environment d, boolean isStatic){
		if (ICEvaluator.run_num == 0)
		{
			if (this.hasObject(f.name, d) && ICEvaluator.run_num == 0 )
			{ //there is already an object with this name in current scope
				ICEvaluator.error("multiple declerations of object: " + f.name, null);
			}
			if (!isStatic)
			{
				f.offset = this.num_funcs;
				this.num_funcs ++;
			}
			if (ext != null)
			{
				icObject overriden = ext.getObject(f.name, d);
				if (overriden != null);
				{ //father class already has an object with that name.
					if (isStatic || !(overriden instanceof icFunction))
					{
						ICEvaluator.error("multiple declerations of object: " + f.name, null);
					}		
					else
					{
						f.offset = overriden.offset;
						this.num_funcs--;
					}
				}
			}
			f.label = "_" + d.lastClass.name + "_" + f.name;
			if (isStatic)
			{
				statFuncs.add(f);
			}
			else
			{
				instFuncs.add(f);
			}
		}
		else
		{
			f = (icFunction)d.lastClass.getObject(f.name, d);
			IR.put_label_comment(d.lastClass.name, f.name);
			IR.put_label(f.label);
		}
	}

	void addVar(icVariable v, Environment d){
		if (this.hasObject(v.name, d) && ICEvaluator.run_num == 0 )
		{ //there is already an object with this name in current scope
			ICEvaluator.error("multiple declerations of object: " + v.name, null);
		}
		if (ICEvaluator.run_num == 1 && ext != null)
		{
			if (ext.hasObject(v.name, d))
			{ //father class already has an object with that name
				ICEvaluator.error("multiple declerations of object: " + v.name, null);
			}
		}
		if (ICEvaluator.run_num == 0){
			v.offset = this.size;
			this.size++;
			instVars.add(v);
		}
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
			for (icObject o : cur.instFuncs) {
				if (o.name.equals(objectName)){
					return o;
				}
			}
			for (icFunction f : cur.statFuncs) {
				if (f.name.equals(objectName)){
					return f;
				}
			}
			for (icVariable v : cur.instVars) {
				if (v.name.equals(objectName)){
					return v;
				}
			}
			cur = cur.ext;
		}
		return null;
	}

	@Override
	public VarType getAssignType() {
		return new VarType(this.name, this.name);
	}
	
	
	public boolean checkIfSubType(icObject parent, Environment env){
		icClass cur = this;
		while (cur != null){
			if (cur.getName().equals(parent.getName())){
				return true;
			}
			cur = cur.ext;
		}
		return false;		
	}


	public VarType getFieldType(String id, Environment env) {
		return getObject(id, env).getAssignType();
	}
	

}
