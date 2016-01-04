package slp;

import java.util.*;

import java_cup.runtime.Symbol;

public class Asm {
	static String code = "";
	private static int temp_counter;

	static final String fp = "$fp";
	static final String zero = "$zero";
	static final String ret = "$a0";
	static String compLef = null;
	static String compRig = null;
	static final int IMM = 0;
	static final int REG = 1;
	static final int MEM = 2;
	static final int SRC = 0;
	static final int DST = 1;

	static final boolean DEBUG = true;

	static void add_line(String content) {
		if (DEBUG) System.out.println(content);
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
	public static int getSingleOpType(String value)
	{
		if (value.startsWith("R"))
		{
			return REG;
		}
		if (isInteger(value,10))
		{
			return IMM;
		}
		return MEM;
	}
	
	static int[] getOpTypes(String[] ops)
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
	private static String new_temp() {
		++temp_counter;
		return "RR" + Integer.toString(temp_counter - 1);
	}

	//get a variable name and returns its offset on the stack
	private static String getVarOffset(String var) {
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
			//leave place for 'this, ra, fp'
			return Integer.toString(4*Integer.parseInt(arg_parts[1])+3*4); 
		}
		if (var.equals("this"))
		{
			return "8";
		}
		throw new RuntimeException("bad var name: " + var);
		
	}
	

	public static void move(String[] ops)
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
			add_line("sw " + ops[SRC] + ", " + getVarOffset(ops[DST]) + "(" + fp + ")");
		}
	}

	@SuppressWarnings("serial")
	HashMap<String, String> arith_imm_insts = new HashMap<String, String>() {{ put("Add", "addi"); put("Or", "ori"); 
		put("Xor", "xori"); put("And", "andi"); put("Sub", "addi"); }};
	@SuppressWarnings("serial")
	HashMap<String, String> arith_reg_insts = new HashMap<String, String>() {{ put("Add", "add"); put("Or", "or"); 
		put("Xor", "xor"); put("And", "and"); put("Sub", "sub"); }};
	public void arithmetic_op(String Instruction, String[] ops)
	{
		if (ops.length != 2)
		{
			throw new RuntimeException(Instruction + ": wrong number of arguments\n" + ops);
		}
		int[] opTypes = getOpTypes(ops);
		if (opTypes[SRC] == MEM)
		{
			String temp_src = new_temp();
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

	@SuppressWarnings("serial")
	HashMap<String, String> mul_insts = new HashMap<String, String>() {{ put("Mul", "mult"); put("Div", "div"); 
		put("Mod", "div"); }};
	public void mult_op(String Instruction, String[] ops)
	{
		if (ops.length != 2)
		{
			throw new RuntimeException(Instruction + ": wrong number of arguments\n" + ops);
		}
		int[] opTypes = getOpTypes(ops);
		if (opTypes[SRC] == MEM)
		{
			String temp_src = new_temp();
			add_line("lw " + temp_src + ", " + getVarOffset(ops[SRC]) + "(" + fp + ")");
			opTypes[SRC] = REG;
			ops[SRC] = temp_src;
		}
		if (opTypes[SRC] == IMM)
		{
			String temp_src = new_temp();
			add_line("addi " + temp_src + ", " + zero +", " + getVarOffset(ops[SRC]));
			opTypes[SRC] = REG;
			ops[SRC] = temp_src;
		}
		
		
		String asm_inst = mul_insts.get(Instruction);
		add_line(asm_inst + " " + ops[DST] + ", " + ops[SRC]);	
		if (Instruction.equals("Mod"))
			add_line("mfhi " + " " + ops[DST]);		
		else
			add_line("mflo " + " " + ops[DST]);	
			
	}
	
	public static void virtual_call(String[] ops)
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
	
	public static void static_call(String[] ops)
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
	
	public static void compare(String fReg, String sReg){
		if (isInteger(fReg,10)){
			add_line("slti $t," + sReg+ "," + fReg); 
		}
		else{
			add_line("slt $t," + sReg+ "," + fReg); 
		}
	}
	
	public static void ret(String rValue){
		add_line("jr " + ret);
	}
	
	public static void jump(String label){
		add_line("j " + label);
	}
	
	public static void JumpGE(String label){
		add_line("bge " + compRig + "," + compLef + "," + label);
	}
	
	public static void JumpG(String label){
		add_line("bgt " + compRig + "," + compLef + "," + label);
	}
	
	public static void JumpLE(String label){
		add_line("ble " + compRig + "," + compLef + "," + label);
	}
	
	public static void JumpL(String label){
		add_line("blt " + compRig + "," + compLef + "," + label);
	}
	
	public static void JumpFalse(String label){
		add_line("bne " + compRig + "," + compLef + "," + label);
	}
	
	public static void JumpTrue(String label){
		add_line("beq " + compRig + "," + compLef + "," + label);
	}
	
	public static void LirToMips(IRLexer lexer) throws Exception{
		
		Symbol token = lexer.next_token();
		Symbol nextToken;
		List<String> resultList;
		while (token.sym != sym.EOF)
		{
			String result="";
			//System.out.println(token.toString());
			switch(token.sym){
				case IRsym.STRINGLABEL:
					if (DEBUG) 
						result = "(string label:)";
					nextToken = lexer.next_token();
					result += token.toString() + " .asciiz " + nextToken.toString();
					add_line(result);
					break;
				case IRsym.LABEL:
					if (DEBUG) 
						result = "(label:)";
					result += token.toString();
					add_line(result);
					break;
				case IRsym.DVLABEL:
					if (DEBUG) 
						result = "(DVLabel:)";
					result += token.toString();
					result = result.substring(0, result.length()-1) + ".word ";
					nextToken = lexer.next_token();
					while(nextToken.sym != IRsym.RB){
						if (nextToken.sym != IRsym.COMMA) 
							result += nextToken.toString();
						nextToken = lexer.next_token();
					}	
					add_line(result);
					break;
				case IRsym.MOVE:
					
					String op1 = lexer.next_token().toString();
					lexer.next_token();
					String op2 = lexer.next_token().toString();
					String[] ops = {op1,op2};
					move(ops);
					break;
					
				case IRsym.VIRTUALCALL:
					if (DEBUG) 
						System.out.println("(virtualCall:)");
					resultList = new ArrayList<String>();
					resultList.add(lexer.next_token().toString()); // object of the virtual call
					lexer.next_token(); // DOT
					resultList.add(lexer.next_token().toString());	// offset
					lexer.next_token();	// LP
					nextToken = lexer.next_token();
					while (nextToken.sym != IRsym.RP){
						if (nextToken.sym == IRsym.COMMA) 
							nextToken = lexer.next_token();	// ignore commas. now nextToken is formal param
						nextToken = lexer.next_token();	// now nextToken is "="
						nextToken = lexer.next_token(); // now nextToken is the actual arg
						resultList.add(nextToken.toString());
						nextToken = lexer.next_token();
					}
					lexer.next_token();	// ignore comma
					resultList.add(lexer.next_token().toString());	// result reg
					virtual_call(resultList.toArray(new String[0]));
 					break;
 					
				case IRsym.STATICCALL:
					if (DEBUG) 
						System.out.println("(staticCall:)");
					resultList = new ArrayList<String>();
					resultList.add(lexer.next_token().toString());	// _class_method
					lexer.next_token();	// LP
					
					token = lexer.next_token();
					while(token.sym != IRsym.RP){
						if (token.sym == IRsym.COMMA) lexer.next_token();	// ignore commas
						lexer.next_token();	// ignore formal parameter and "="
						token=lexer.next_token(); // ignore "="
						resultList.add(token.toString());	// arg of the virtual call
						token = lexer.next_token();
					}
					lexer.next_token();	// ignore comma
					resultList.add(lexer.next_token().toString());	// result reg
					static_call(resultList.toArray(new String[0]));
					break;
				case IRsym.JUMP:
					if (DEBUG) 
						System.out.println("(jump:)");
					String label = lexer.next_token().toString();
					jump(label);
				case IRsym.COMPARE:
					if (DEBUG) 
						System.out.println("(compare:)");
					compLef = lexer.next_token().toString();
					token=lexer.next_token();
					compRig = lexer.next_token().toString();
					/*
					String fReg = lexer.next_token().toString();
					token=lexer.next_token(); // ignore ","
					String sReg = lexer.next_token().toString();
					compare(fReg,sReg);
					*/
				case IRsym.RETURN:
					if (DEBUG) 
						System.out.println("(return:)");
					String rValue = lexer.next_token().toString();
				case IRsym.SUB:	
					/*
					if (DEBUG) 
						System.out.println("(sub:)");
					String lValue = lexer.next_token().toString();
					lexer.next_token().toString();
					rValue = lexer.next_token().toString();
					String[] oper = {lValue,rValue};
					System.out.println("sub " + lValue + " " + rValue);
					Asm func = new Asm();
					 func.arithmetic_op("sub", oper);
					 */
				default: break;
			}			
			token = lexer.next_token();
			
		}
	}
}
