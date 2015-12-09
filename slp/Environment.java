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

	/*
	 * TODO: if there is already an object with the same name in the same scope,
	 * should throw an error!
	 */
	public void add(icObject obj) {
		String name = obj.getName();
		Queue<icObject> expSet = varToValue.get(name);
		if (expSet != null) {
			for (icObject icObj : expSet) {
				if (icObj.getScope() == obj.getScope()) {
					throw new RuntimeException("Duplicate items with same name: " + name);
				}
			}
		}
		
		update(name,obj);

	}

	/* TODO: */
	public icObject getObjByName(String name) {
		Queue<icObject> expSet =  varToValue.get(name);
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
		expSet.offer(obj);

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
	public Integer get(ASTVarExpr v) {
		if (!varToValue.containsKey(v.name)) {
			throw new RuntimeException("Attempt to access uninitialized variable: " + v.name);
		}
		return varToValue.get(v.name);
	}
	*/
	

	public void destroyScope(int scope) {
		for (Entry<String, Queue<icObject>> entry : varToValue.entrySet()){
			Queue<icObject> expSet = entry.getValue();
			icObject obj = expSet.peek();
			if (obj.getScope() == scope){
				expSet.poll();
			}
		}

	}

	public void validateType(String type) {
		if (!type.equals("int") || !type.equals("string") || !type.equals("boolean")){
			icObject obj = getObjByName(type);
			if (obj == null){
				throw new RuntimeException(type + "cannot be resolved to a type");
			}
		}
	}

}