package slp;

import java.util.*;

/**
 * Represents a state during the evaluation of a program.
 */
public class Environment {
	/**
	 * Maps the names of variables to integer values. The same variable may
	 * appear in different VarExpr objects. We use the name of the variable as a
	 * way of ensuring we a consistent mapping for each variable.
	 */

	private Map<String, Stack<icObject>> varToValue = new HashMap<String, Stack<icObject>>();
	private Stack<icObject> popStack = new Stack<icObject>();
	public icClass lastClass = null;
	public icFunction lastFunc = null;
	public boolean loopScope = false;
	public int run_num = 0;

	public void add(icObject obj) {
		if (obj.scope == 0 && run_num == 1)
			return; // should already appear in the table
		String name = obj.getName();
		Stack<icObject> expSet = varToValue.get(name);
		if (expSet == null) {
			expSet = new Stack<icObject>();
			expSet.push(obj);
			varToValue.put(obj.name, expSet);
		}
		else
		{
			icObject last = expSet.peek();
			if (last.scope >= obj.scope)
				ICEvaluator.error("object " + obj.name + " redefenition", null);
			expSet.push(obj);
		}
		if (obj.scope != 0)
			popStack.push(obj);

	}

	public icObject getObjByName(String name) {
		Stack<icObject> expSet = varToValue.get(name);
		if (expSet == null)
			return null;
		return expSet.peek();
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
		while (!popStack.isEmpty() && popStack.peek().scope == scope)
		{	
			
			String name = popStack.pop().name;
			Stack<icObject> expSet = varToValue.get(name);
			expSet.pop();
			if (expSet.isEmpty())
				varToValue.remove(name);
		}
	}

	public boolean validateType(VarType type) {
		icObject obj = getObjByName(type.type);
		return (obj != null);
	}
	

	public Environment() {// add library functions
		icClass baseType = new icClass("int", 0, null);
		this.add(baseType);
		baseType = new icClass("string", 0, null);
		this.add(baseType);
		baseType = new icClass("boolean", 0, null);
		this.add(baseType);
		baseType = new icClass("void", 0, null);
		this.add(baseType);
		baseType = new icClass("null", 0, null);
		this.add(baseType);
		
		icClass library = new icClass("Library", 0, null);
		this.lastClass = library;

		icFunction f = new icFunction("println", 0, new VarType("void", ""), true);
		f.arg_types.add(new VarType("string", ""));
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("print", 0,  new VarType("void", ""), true);
		f.arg_types.add(new VarType("string", ""));
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("printi", 0,  new VarType("void", ""), true);
		f.arg_types.add(new VarType("int", ""));
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("printb", 0,  new VarType("void", ""), true);
		f.arg_types.add(new VarType("boolean", ""));
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("readi", 0,  new VarType("int", ""), true);
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("readln", 0,  new VarType("string", ""), true);
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("eof", 0,  new VarType("boolean", ""), true);
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("stoi", 0,  new VarType("int", ""), true);
		f.arg_types.add(new VarType("string", ""));
		f.arg_types.add(new VarType("int", ""));
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("itos", 0,  new VarType("string", ""), true);
		f.arg_types.add(new VarType("int", ""));
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("stoa", 0,  new VarType("int[]", ""), true);
		f.arg_types.add(new VarType("string", ""));
		library.addFunc(f, this, true);
		add(f);


		f = new icFunction("atos", 0,  new VarType("string", ""), true);
		f.arg_types.add(new VarType("int[]", ""));
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("random", 0,  new VarType("int", ""), true);
		f.arg_types.add(new VarType("int", ""));
		library.addFunc(f, this, true);
		add(f);

		f = new icFunction("time", 0,  new VarType("int", ""), true);
		library.addFunc(f, this, true);
		add(f);


		f = new icFunction("exit", 0,  new VarType("void", ""), true);
		f.arg_types.add(new VarType("int", ""));
		library.addFunc(f, this, true);
		add(f);
		add(library);

	}
}