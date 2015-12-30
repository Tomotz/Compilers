package slp;

public class Asm {
	static String code = "";
	private static int temp_counter;

	static final String fp = "$fp";
	static final String zero = "$zero";
	static final int IMM = 0;
	static final int REG = 1;
	static final int MEM = 2;
	static final int DST = 2;
	static final int TARGET = 1;
	static final int SRC = 0;

	

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

		String temp_src = new_temp();
		if (opTypes[SRC] == MEM)
		{
			add_line("lw " + temp_src + ", " + getVarOffset(ops[SRC]) + "(" + fp + ")");
			opTypes[SRC] = REG;
			ops[SRC] = temp_src;
		}
		String temp_dst = ops[TARGET];
		if (opTypes[TARGET] == MEM)
		{
			temp_dst = new_temp();
		}
		
		if (opTypes[SRC] == IMM)
		{
			add_line("addi " + temp_dst + ", " + zero + ", " + ops[SRC]);
		}
		else
		{ //SRC is REG
			add_line("move " +temp_dst + ", " + ops[SRC]);
			
		}
		
		if (opTypes[TARGET] == MEM)
		{
			add_line("sw " + ops[TARGET] + ", " + getVarOffset(temp_dst) + "(" + fp + ")");
		}
		
		
	}

	//returns an unused temporary
	private String new_temp() {
		++temp_counter;
		return "RR" + Integer.toString(temp_counter - 1);
	}

	//get a variable name and returns its offset on the stack
	private String getVarOffset(String var) {
		//TODO!!!!
		return "";
	}

}
