package slp;

import java.util.*;

/** Represents a state during the evaluation of a program. 
 */
public class Environment {
	/** Maps the names of variables to integer values.
	 * The same variable may appear in different VarExpr objects.  We use the
	 * name of the variable as a way of ensuring we a consistent mapping
	 * for each variable. 
	 */
	
	private Map<String,Set<icObject>> varToValue = new HashMap<String,Set<icObject>>();
	public icClass lastClass = null;
	public icFunction lastFunc = null;
	
	/*TODO: if there is already an object with the same name in the same scope,
	 * should throw an error!
	 */
	public void add(icObject obj)
	{
		
	}
	
	
	/** Updates the value of a variable.
	 * 
	 * @param v A variable expression.
	 * @param newValue The updated value.
	 */
	public void update(ASTVarExpr v, int newValue) {
		varToValue.put(v.name, new Integer(newValue));
		// Actually, varToValue.put(v, newValue) works as well because of the
		// auto-boxing feature of Java 1.5, which automatically wraps newValue
		// with an Integer object.
	}
	
	/** Retrieves the value of the given variable.
	 * If the variable has not been initialized an exception is thrown.
	 * 
	 * @param v A variable expression.
	 * @return The value of the given variable in this state.
	 */
	public Integer get(ASTVarExpr v) {
		if (!varToValue.containsKey(v.name)) {
			throw new RuntimeException("Attempt to access uninitialized variable: " + v.name);
		}
		return varToValue.get(v.name);
	}


	public void destroyScope(int scope) {
		// TODO Auto-generated method stub
		
	}

}