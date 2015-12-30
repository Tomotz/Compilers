package slp;

public class Asm {
	static String code = "";
	
	static final String sp = "$sp";
	static final int IMM = 0;
	static final int REG = 1;
	static final int MEM = 2;
	static final int DEST_REG = 2;
	static final int TARGET_REG = 1;
	static final int SRC_REG = 0;

	

	static void add_line(String content) {
		code += content + "\n";
	}
	
	public static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	//returns IMM, REG or MEM
	public int getSingleOpType(String value)
	{
		if (value.startsWith("R"))
		{
			return REG;
		}
		if (isInteger(value,10))
		{
			return IMM;
		}
		else
			return MEM;
	}
	
	int[] getOpTypes(String[] ops)
	{
		int[] opTypes = new int[ops.length];
		int i=0;
		for (String s : ops) {
			opTypes[i] = getSingleOpType(s);
			++i;
		}
		return opTypes;
	}
	
	public void move(String operands)
	{
		String[] ops = operands.split(" ");
		if (ops.length != 2)
		{
			throw new RuntimeException("move: wrong number of arguments");
		}
		int[] opTypes = getOpTypes(ops);

		String temp_reg = new_temp();
		if (opTypes[SRC_REG] == MEM)
		{
			add_line("lw " + temp_reg + ", " + getVarOffset(ops[SRC_REG]) + "(" + sp + ")");
		}
		
		
	}

	//returns an unused temporary
	private String new_temp() {
		// TODO Auto-generated method stub
		return null;
	}

	//get a variable name and returns its offset on the stack
	private String getVarOffset(String var) {
		//TODO!!!!
		return "";
	}

}
