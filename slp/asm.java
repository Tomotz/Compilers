package slp;

import java.util.*;

import java_cup.runtime.Symbol;

public class asm 
{
	static String code = "";
	private static int temp_counter;

	static final String fp = "$fp";
	static final String sp = "$sp";
	static final String zero = "$zero";
	static final String ret_addr = "$ra";
	static final String ret_val = "$v0";
	static String compLef = null;
	static String compRig = null;
	static final int IMM = 0;
	static final int REG = 1;
	static final int MEM = 2;
	static final int SRC = 0;
	static final int DST = 1;

	static final boolean DEBUG = true;

	static void add_line(String content) {
		//if (DEBUG) 
			//System.out.println(content);
		code += content + "\n";
	}
	
	static List<List<Integer>> next_lines = new ArrayList<List<Integer>>();
	static Map<String, Integer> label_lines = new HashMap<String, Integer>();
	static Map<Integer, List<String>> reg_use = new HashMap<Integer, List<String>>();
	static Map<Integer, String> reg_def = new HashMap<Integer, String>();
	static List<List<String>> in = new ArrayList<List<String>>();
	static List<List<String>> out = new ArrayList<List<String>>();
	static Map<String, Set<String>> bump_graph = new HashMap<String, Set<String>>();
	static Map<String, String> reg_aloc = new HashMap<String, String>();
	static Set<String> all_regs = new HashSet<String>();
	
	//run register allocation algorithm
	static void reg_algo()
	{
		get_label_lines();
		prepare_usage();
		get_dataflow();
		build_graph();
		allocate_regs();
		fix_code();
		//if (DEBUG)
			//System.out.println(code);
	}

	//replace the temporaries in code with correct registers
	private static void fix_code()
	{
		//probably doesn't work :)
		String seperator = "( | \\,|\\, |\\,|\\(|\\)|$|\\n)";
		for (int i=0; i<2; ++i)
		{ //loop because regex cant catch same var twice
			for (String reg : reg_aloc.keySet()) {
				String pat = seperator +"(" + reg + ")" + seperator;
				code = code.replaceAll(pat, "$1\\" + reg_aloc.get(reg) + "$3");
				
			}
		}
	}

	//build the reg alloc dict
	private static void allocate_regs() {
		for (String temp : all_regs) 
		{
			if (!bump_graph.containsKey(temp))
			{
				String allocated_reg = "$t0";
				reg_aloc.put(temp, allocated_reg);	
				continue;
			}
			Set<String> banned_regs = new HashSet<String>();
			for (String neighbor : bump_graph.get(temp))
			{
				if (reg_aloc.containsKey(neighbor))
					banned_regs.add(reg_aloc.get(neighbor));				
			}
			boolean is_alloced = false;
			for (int i=0; i<8; ++i)
			{
				String allocated_reg = "$t" + Integer.toString(i);
				if (banned_regs.contains(allocated_reg))
					continue;
				is_alloced = true;
				reg_aloc.put(temp, allocated_reg);				
			}
			if (!is_alloced)
				throw new RuntimeException("cannot allocate temp: " + temp + " not enough regs in pool");
				
		}		
	}

	//build the interference graph
	private static void build_graph() 
	{
		for (List<String> line : in)
		{
			for (int i=0; i<line.size();++i)
			{
				for (int j=i+1; j<line.size();++j)
				{
					String temp0 = line.get(i);
					String temp1 = line.get(j);
					if (temp0 == temp1)
						continue;
					if (bump_graph.containsKey(temp0))
						bump_graph.get(temp0).add(temp1);
					else
					{
						Set<String> h = new HashSet<String>();
						h.add(temp1);
						bump_graph.put(temp0, h);
					}
					if (bump_graph.containsKey(temp1))
						bump_graph.get(temp1).add(temp0);
					else
					{
						Set<String> h = new HashSet<String>();
						h.add(temp0);
						bump_graph.put(temp1, h);
					}
				}	
			}
		}
		
	}

	//fills in and out lists
	private static void get_dataflow() {
		boolean is_changed;
		for (int line=0; line < next_lines.size(); ++line) {
			//init in and out
			in.add(new ArrayList<String>());
			out.add(new ArrayList<String>());
		}
		do
		{
			is_changed = false;
			for (int line=0; line < next_lines.size(); ++line) { 
				//for each n
				List<String> in_tag = new ArrayList<String>(in.get(line));
				List<String> out_tag = new ArrayList<String>(out.get(line));
				List<String> cur_in;
				//in[n] = use[n]
				if (reg_use.containsKey(line))
					cur_in = new ArrayList<String>(reg_use.get(line));
				else
					cur_in = new ArrayList<String>();
				//in[n] += out[n] - def[n]
				for (String reg : out.get(line)) {
					if (reg_def.containsKey(line) && reg_def.get(line) == reg)
						continue;
					cur_in.add(reg);
				}
				in.set(line, cur_in);
				
				//out[n] = U(in[s])
				List<String> cur_out= new ArrayList<String>();
				List<Integer> next_line = next_lines.get(line);
				for (Integer next : next_line)
				{
					cur_out.addAll(in.get(next));
				}
				out.set(line, cur_out);
				is_changed = !(in_tag.equals(cur_in) && out_tag.equals(cur_out));
				
			}
			
			
		}
		while (is_changed);
	}

	//prepare all reg use and def lists
	private static void prepare_usage() {
		int cur_line = 0;
		for (String line : code.split("\n")) {
			String trimmed = line.trim();
			if (trimmed.equals("") || trimmed.startsWith("#") || trimmed.contains(":"))
				continue; //comment, label or empty line
			
			String[] parts = trimmed.split(" |\\, | \\,|\\,|\\(|\\)");
			String inst = parts[0];
			List<Integer> lines_follow = new ArrayList<Integer>();
			if (inst.equals("j"))
			{
				lines_follow.add(label_lines.get(parts[1]));
			}
			else if (inst.equals("jalr"))
			{
				lines_follow.add(cur_line);
			}
			else if (inst.equals("beq") || inst.equals("bne"))
			{
				reg_usage(parts[1], "use", next_lines.size());
				reg_usage(parts[2], "use", next_lines.size());
				lines_follow.add(label_lines.get(parts[3]));
				lines_follow.add(cur_line);
			}
			else if (inst.equals("jr"))
			{
				//empty on perpose
			}
			else if (inst.equals("add") || inst.equals("sub") || inst.equals("addu")
					|| inst.equals("and") || inst.equals("nor") || inst.equals("or")
					|| inst.equals("sllv") || inst.equals("srav") || inst.equals("srlv")
					|| inst.equals("subu") || inst.equals("xor") || inst.equals("slt")
					|| inst.equals("sltu"))
			{
				reg_usage(parts[1], "def", next_lines.size());
				reg_usage(parts[2], "use", next_lines.size());
				reg_usage(parts[3], "use", next_lines.size());
				lines_follow.add(cur_line);
			}
			else if (inst.equals("addi") || inst.equals("addiu") || inst.equals("andi")
					|| inst.equals("ori") || inst.equals("sll") || inst.equals("sra")
					|| inst.equals("srl") || inst.equals("xori") || inst.equals("sltiu")
					|| inst.equals("slti") || inst.equals("move") || inst.equals("div")
					|| inst.equals("divu") || inst.equals("mult") || inst.equals("multu"))
			{
				reg_usage(parts[1], "def", next_lines.size());
				reg_usage(parts[2], "use", next_lines.size());
				lines_follow.add(cur_line);
			}
			else if (inst.equals("lw"))
			{
				reg_usage(parts[1], "def", next_lines.size());
				reg_usage(parts[3], "use", next_lines.size());
				lines_follow.add(cur_line);
			}
			else if (inst.equals("sw"))
			{
				reg_usage(parts[1], "use", next_lines.size());
				reg_usage(parts[3], "use", next_lines.size());
				lines_follow.add(cur_line);
			}
			else if (inst.equals("li"))
			{
				reg_usage(parts[1], "def", next_lines.size());
				lines_follow.add(cur_line);
			}
			else if (inst.equals("syscall"))
			{
				lines_follow.add(cur_line);
			}
			else
				throw new RuntimeException("unknown instruction: " + inst);
			
			next_lines.add(lines_follow);
			++cur_line;
		}
	}
	
	//searches the code for labels and put their lines in the label_lines dict
	private static void get_label_lines() {
		int line_num = 0;
		for (String line : code.split("\n")) {
			String trimmed = line.trim();
			if (trimmed.equals("") || trimmed.startsWith("#"))
				continue; //comment
			if (!trimmed.contains(":"))
			{
				++line_num;
				continue;//not a label
			}
			label_lines.put(trimmed.replace(":",  ""), line_num);	
		}
		
	}


	//prepare the reg use or def of a single register
	private static void reg_usage(String reg, String usage_type, int line_num)
	{
		if (!reg.startsWith("R"))
			return; //not a temporary name
		if (usage_type == "def")
		{ //only one def per line. no list problems like in use
			reg_def.put(line_num, reg);
			all_regs.add(reg);
			return;
		}
		
		//handle use
		if (reg_use.containsKey(line_num))
			reg_use.get(line_num).add(reg);
		else
		{
			List<String> reg_list = new ArrayList<String>();
			reg_list.add(reg);
			reg_use.put(line_num, reg_list);
			all_regs.add(reg);
		}
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
		return "RA" + Integer.toString(temp_counter - 1);
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
	static HashMap<String, String> arith_imm_insts = new HashMap<String, String>() {{ put("Add", "addi"); put("Or", "ori"); 
		put("Xor", "xori"); put("And", "andi"); put("Sub", "addi"); }};
	@SuppressWarnings("serial")
	static HashMap<String, String> arith_reg_insts = new HashMap<String, String>() {{ put("Add", "add"); put("Or", "or"); 
		put("Xor", "xor"); put("And", "and"); put("Sub", "sub"); }};
	public static void arithmetic_op(String Instruction, String[] ops)
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
	
	public static String get_this()
	{
		String this_off = getVarOffset("this");
		String temp = new_temp();
		add_line("lw " + temp + ", " + this_off + "(" + fp + ")");
		return temp;
	}
	
	public static void virtual_call(String[] ops)
	{
		final int CLASS = 0;
		final int OFFSET = 1;
		final int CALL_DST = ops.length-1;
		
		for (int i=ops.length-2; i>=2; --i)
		{ //push arguments
			push(ops[i]);
		}
		
		push(get_this());
		
		String off = Integer.toString(4*Integer.parseInt(ops[OFFSET]));
		String temp = new_temp();
		add_line("lw " + temp + ", 0(" + ops[CLASS] + ")"); //get the DV
		add_line("lw " + temp + ", " + off + "(" + temp + ")"); //get the correct function
		push(ret_addr); 
		add_line("jalr " + temp); //save return address and call the virtual func
		add_line("move " + ops[CALL_DST] + ", " + ret_val);
	}
	
	public static void push(String reg)
	{
		add_line("addi " + sp + ", " + sp + ", -4");
		add_line("sw " + reg + ", 4("+sp+")");
	}
	
	public static void static_call(String[] ops)
	{
		final int LABEL = 0;
		final int CALL_DST = ops.length-1;
		for (int i=ops.length-2; i>=1; --i)
		{ //push arguments
			push(ops[i]);
		}
		
		push(zero); //no 'this' in static call

		push(ret_addr); 
		add_line("jal " + LABEL); //save return address and call the virtual func
		add_line("move " + ops[CALL_DST] + ", " + ret_val); 
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
		//TODO: rValue can be integer. move work on registers
		add_line("move " + ret_val + ", " + rValue); //save return value in v1
		add_line("jr " + ret_addr);
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
	
	public static void not(String cond){
		add_line("not " + cond + "," + cond);
	}
	
	public static void exit(){
		add_line("li $v0, 10");
		add_line("syscall");
	}
	
	public static void printInt(int d){
		add_line("li $v0, 1");
		add_line("li $a0, " + d);
		add_line("syscall");
	}
	
	public static void printChar(char c){
		add_line("li $v0, 11");
		add_line("li $a0, " + c);   // i'm not sure if to use lb instead
		add_line("syscall");
	}
	
	public static void allocate(int byteSize){
		add_line("li $a0, " + byteSize);
		add_line("mul $a0, $a0, 4");
		add_line("li $v0, 9");
		add_line("syscall");
		add_line("move $t0, $v0");
	}
	
	public static void LirToMips(IRLexer lexer) throws Exception
	{
		boolean DEBUG_TOKENS = false && DEBUG;
		Symbol token = lexer.next_token();
		Symbol nextToken;
		List<String> resultList;
		while (token.sym != sym.EOF)
		{
			String result="";
			switch(token.sym){
				case IRsym.STRINGLABEL:
					if (DEBUG_TOKENS) 
						System.out.println("#(string label:)");
					nextToken = lexer.next_token();
					result += token.toString() + " .asciiz " + nextToken.toString();
					add_line(result);
					break;
				case IRsym.LABEL:
					if (DEBUG_TOKENS) 
						System.out.println("#(label:)");
					result += token.toString();
					add_line(result);
					break;
				case IRsym.DVLABEL:
					if (DEBUG_TOKENS) 
						System.out.println("#(DVLabel:)");
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
					if (DEBUG_TOKENS) 
						System.out.println("#(virtualCall:)");
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
					if (DEBUG_TOKENS) 
						System.out.println("#(staticCall:)");
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
					if (DEBUG_TOKENS) 
						System.out.println("#(jump:)");
					String label = lexer.next_token().toString();
					jump(label);
					break;
				case IRsym.JUMPFALSE:
					if (DEBUG_TOKENS) 
						System.out.println("#(jumpFalse:)");
					String labelf = lexer.next_token().toString();
					JumpFalse(labelf);
					break;
				case IRsym.JUMPTRUE:
					if (DEBUG_TOKENS) 
						System.out.println("#(jumpFalse:)");
					String labelt = lexer.next_token().toString();
					JumpTrue(labelt);
					break;
				case IRsym.COMPARE:
					if (DEBUG_TOKENS) 
						System.out.println("#(compare:)");
					compLef = lexer.next_token().toString();
					token=lexer.next_token();
					compRig = lexer.next_token().toString();
					/*
					String fReg = lexer.next_token().toString();
					token=lexer.next_token(); // ignore ","
					String sReg = lexer.next_token().toString();
					compare(fReg,sReg);
					*/
					break;
				case IRsym.RETURN:
					if (DEBUG_TOKENS) 
						System.out.println("#(return:)");
					String rValue = lexer.next_token().toString();
					ret(rValue);
					break;
				case IRsym.SUB:	
					if (DEBUG_TOKENS) 
						System.out.println("#(sub:)");
					String lValue = lexer.next_token().toString();
					lexer.next_token().toString();
					rValue = lexer.next_token().toString();
					String[] oper = {lValue,rValue};
					
					arithmetic_op("Sub", oper);
					break;
				case IRsym.ADD:	
					if (DEBUG_TOKENS) 
						System.out.println("#(add:)");
					lValue = lexer.next_token().toString();
					lexer.next_token().toString();
					rValue = lexer.next_token().toString();
					String[] oper_a = {lValue,rValue};
					
					arithmetic_op("Add", oper_a);
					break;
				case IRsym.NOT:	
					
					if (DEBUG_TOKENS) 
						System.out.println("#(not:)");
					String nt = lexer.next_token().toString();
					not(nt);
					break;
				case IRsym.COMMENT:
					if (DEBUG_TOKENS) 
						System.out.println("#(comment:)");
					add_line(token.toString());
					break;
				case IRsym.LIBRARY:
					if (DEBUG_TOKENS) 
						System.out.println("#(library:)");
					String libname = lexer.next_token().toString();
					if (libname.equals("__exit"))
					{
						lexer.next_token().toString(); //LP
						lexer.next_token().toString(); //num
						lexer.next_token().toString(); //RP
						lexer.next_token().toString(); //comma
						lexer.next_token().toString(); //out_reg
						exit();
					}
					else if (libname.equals("__printInt"))
					{
						lexer.next_token().toString(); //LP
						String param = lexer.next_token().toString(); //num
						lexer.next_token().toString(); //RP
						lexer.next_token().toString(); //comma
						lexer.next_token().toString(); //out_reg
						printInt(Integer.parseInt(param));						
					}
					else
					{
						if (DEBUG) 
							System.out.println("unknown Lib: " + libname.toString());
						lexer.next_token().toString(); //LP
						lexer.next_token().toString(); //RP?
						lexer.next_token().toString(); //comma?
						lexer.next_token().toString(); //out_reg?
						
					}
					//TODO: other libs
					break;
				default:
					if (DEBUG) 
						System.out.println("bad token: " + token.toString());
					break;
			}	
			
			token = lexer.next_token();
			
		}
		reg_algo();
	}
	
}
