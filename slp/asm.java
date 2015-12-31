package slp;

import java.util.Dictionary;
import java.util.HashMap;

public class Asm {
	static String code = "";
	private static int temp_counter;

	static final String fp = "$fp";
	static final String zero = "$zero";
	static final String ret = "$a0";
	static final int IMM = 0;
	static final int REG = 1;
	static final int MEM = 2;
	static final int SRC = 0;
	static final int DST = 1;

	

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
	
	//returns an unused temporary
	private String new_temp() {
		++temp_counter;
		return "RR" + Integer.toString(temp_counter - 1);
	}

	//get a variable name and returns its offset on the stack
	private String getVarOffset(String var) {
		//try parsing as var
		String[] var_parts = var.split("_var_");
		if (var_parts.length > 1)
		{
			return Integer.toString(-4*Integer.parseInt(var_parts[1])); 
		}
		//try parsing as arg
		String[] arg_parts = var.split("_arg_");
		if (arg_parts.length > 1)
		{
			//leave place for this, ra, fp
			return Integer.toString(4*Integer.parseInt(var_parts[1])+3*4); 
		}
		throw new RuntimeException("bad var name: " + var);
		
	}
	

	public void move(String[] ops)
	{
		//lui and ori??
		if (ops.length != 2)
		{
			throw new RuntimeException("move: wrong number of arguments\n" + ops);
		}
		int[] opTypes = getOpTypes(ops);

		String temp_src = new_temp();
		if (opTypes[SRC] == MEM)
		{
			add_line("lw " + temp_src + ", " + getVarOffset(ops[SRC]) + "(" + fp + ")");
			opTypes[SRC] = REG;
			ops[SRC] = temp_src;
		}
		String temp_dst = ops[DST];
		if (opTypes[DST] == MEM)
		{
			temp_dst = new_temp();
		}
		if (opTypes[SRC] == IMM)
		{
			add_line("addi " + temp_dst + ", " + zero + ", " + ops[SRC]);
		}
		else
		{ //SRC is REG
			add_line("move " + temp_dst + ", " + ops[SRC]);
			
		}
		if (opTypes[DST] == MEM)
		{
			add_line("sw " + ops[DST] + ", " + getVarOffset(temp_dst) + "(" + fp + ")");
		}
	}

	HashMap<String, String> arith_imm_insts = new HashMap<String, String>() {{ put("Add", "addi"); put("Or", "ori"); 
	put("Xor", "xori"); put("And", "andi"); put("Sub", "addi"); }};
	HashMap<String, String> arith_reg_insts = new HashMap<String, String>() {{ put("Add", "add"); put("Or", "or"); 
	put("Xor", "xor"); put("And", "and"); put("Sub", "sub"); }};
	public void arithmetic_op(String Instruction, String[] ops)
	{
		if (ops.length != 2)
		{
			throw new RuntimeException(Instruction + ": wrong number of arguments\n" + ops);
		}
		int[] opTypes = getOpTypes(ops);
		String temp_src = new_temp();
		if (opTypes[SRC] == MEM)
		{
			add_line("lw " + temp_src + ", " + getVarOffset(ops[SRC]) + "(" + fp + ")");
			opTypes[SRC] = REG;
			ops[SRC] = temp_src;
		}
		
		if (Instruction.equals("Sub"))
		{
			ops[SRC] = Integer.toString(-Integer.parseInt(ops[SRC]));
		}
		
		String asm_inst = opTypes[SRC] == IMM ? arith_imm_insts.get(Instruction) : arith_reg_insts.get(Instruction);
		add_line(asm_inst + " " + ops[DST] + ", " + ops[DST] + ", " + ops[SRC]);
		
		
		
	}

	
	public void virtual_call(String[] ops)
	{
		final int CLASS = 0;
		final int OFFSET = 1;
		final int CALL_DST = ops.length-1;
		
		for (int i=ops.length-2; i>=2; --i)
		{ //push arguments
			add_line("push " + ops[i]);
		}
		add_line("push this");
		
		String off = Integer.toString(4*Integer.parseInt(ops[OFFSET]));
		String temp = new_temp();
		add_line("lw " + temp + ", 0(" + ops[CLASS] + ")"); //get the DV
		add_line("lw " + temp + ", " + off + "(" + temp + ")"); //get the correct function
		add_line("jalr " + temp); //save return address and call the virtual func
		add_line("move " + ops[CALL_DST] + ", " + ret); //save return address and call the virtual func
	}
	
	public void static_call(String[] ops)
	{
		final int LABEL = 0;
		final int CALL_DST = ops.length-1;
		
		for (int i=ops.length-2; i>=1; --i)
		{ //push arguments
			add_line("push " + ops[i]);
		}
		
		add_line("push 0"); //no 'this' in static call
		
		add_line("jal " + LABEL); //save return address and call the virtual func
		add_line("move " + ops[CALL_DST] + ", " + ret); //save return address and call the virtual func
	}
	

}
