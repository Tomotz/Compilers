package slp;

import java.util.*;
import java.util.Map.Entry;

/**
 * Represents a state during the evaluation of a program.
 */
public class Environment {
	/**
	 * Maps the names of variables to integer values. The same variable may
	 * appear in different VarExpr objects. We use the name of the variable as a
	 * way of ensuring we a consistent mapping for each variable.
	 */

	private Map<String, Queue<icObject>> varToValue = new HashMap<String, Queue<icObject>>();
	public icClass lastClass = null;
	public icFunction lastFunc = null;
	public boolean loopScope = false;

	public void add(icObject obj) {
		String name = obj.getName();
		Queue<icObject> expSet = varToValue.get(name);
		if (expSet != null) {
			for (icObject icObj : expSet) {
				/*
				if (icObj.getScope() == obj.getScope()) {
					throw new RuntimeException("Duplicate items with same name: " + name);
					
				}
				*/

			}
		}

		update(name, obj);

	}

	public icObject getObjByName(String name) {
		Queue<icObject> expSet = varToValue.get(name);
		if (expSet == null)
			return null;
		return expSet.peek();
	}

	/**
	 * Updates the value of a variable.
	 * 
	 * @param v
	 *            A variable expression.
	 * @param newValue
	 *            The updated value.
	 */
	public void update(String name, icObject obj) {
		Queue<icObject> expSet = varToValue.get(name);
		if (expSet == null) { // if the set is not initialized create it
			expSet = new ArrayDeque<icObject>();
			varToValue.put(name, expSet);
		}
		
		varToValue.get(name).offer(obj);

	}

	/**
	 * Retrieves the value of the given variable. If the variable has not been
	 * initialized an exception is thrown.
	 * 
	 * @param v
	 *            A variable expression.
	 * @return The value of the given variable in this state.
	 */
	/*
	 * public Integer get(ASTVarExpr v) { if (!varToValue.containsKey(v.name)) {
	 * throw new RuntimeException("Attempt to access uninitialized variable: " +
	 * v.name); } return varToValue.get(v.name); }
	 */

	public void destroyScope(int scope) {
	
		for (Entry<String, Queue<icObject>> entry : varToValue.entrySet()) {

			icObject obj = entry.getValue().peek();
			/*
			System.out.println("entry: " + entry);
			*/
			if (obj == null) {
				continue;
			}
			/*
			System.out.println("checking: " + obj.name + " scope " + obj.getScope());
			 */
			if (obj.getScope() == scope) {
				/*
				System.out.println("destroying: " + obj.name);
				/*
				 entry.getValue().poll();
				 */

			}
		}

	}

	public void validateType(String type) {
		if (!type.equals("int") && !type.equals("string") && !type.equals("boolean")) {
			icObject obj = getObjByName(type);
			if (obj == null) {
				throw new RuntimeException(type + " cannot be resolved to a type");
			}
		}
	}
	

	public Environment() {// add library functions
		icClass library = new icClass("Library", 0);

		icFunction f = new icFunction("println", 0, "void", true);
		f.arg_types.add("string");
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("print", 0, "void", true);
		f.arg_types.add("string");
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("printi", 0, "void", true);
		f.arg_types.add("int");
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("printb", 0, "void", true);
		f.arg_types.add("boolean");
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("readi", 0, "int", true);
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("readln", 0, "string", true);
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("eof", 0, "boolean", true);
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("stoi", 0, "int", true);
		f.arg_types.add("string");
		f.arg_types.add("int");
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("itos", 0, "string", true);
		f.arg_types.add("int");
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("stoa", 0, "int[]", true);
		f.arg_types.add("string");
		library.addObject(f, this, true);
		add(f);


		f = new icFunction("atos", 0, "string", true);
		f.arg_types.add("int[]");
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("random", 0, "int", true);
		f.arg_types.add("int");
		library.addObject(f, this, true);
		add(f);

		f = new icFunction("time", 0, "int", true);
		library.addObject(f, this, true);
		add(f);


		f = new icFunction("exit", 0, "void", true);
		f.arg_types.add("int");
		library.addObject(f, this, true);
		add(f);
		add(library);

	}
}