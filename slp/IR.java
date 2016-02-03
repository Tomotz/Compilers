package slp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IR {
	static String str_table = "";
	static String dispatch_tables = "";
	static String code = "";
	static int temp_counter = 0;
	static int label_num = 0;
	static int str_num = 0;
	static String whLblStrt;   // labels for the use of break and continue in a while loop
	static String whLblEnd;
	static String runtime_error_label;
	static String str_error_nullref;
	static String str_error_div0;
	static String str_error_array_bound;
	static String str_error_checksize;

	static void init_str_table()
	{
		str_error_nullref = add_str("\"null ref error\"");
		str_error_div0 = add_str("\"divide by zero error\"");
		str_error_array_bound = add_str("\"array index out of cound\"");
		str_error_checksize = add_str("\"negetive array size\"");
	}
	
	static void add_file_comment(int line_num)
	{
		if (ICEvaluator.run_num != 1)
			return;
		try {
		Main.txtFile2.seek(0);
		for(int i = 0; i < line_num-1; ++i)
			Main.txtFile2.readLine();
		String content = Main.txtFile2.readLine();
		add_comment("line " +Integer.toString(line_num) + ": " +content);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	static String get_label(String text)
	{
		//if (ICEvaluator.run_num != 0)
		//	return "";

		++label_num;
		return ("_" + text + "_" + Integer.toString(label_num));
	}

	static String new_str() {
		if (ICEvaluator.run_num != 1)
			return "";
		++str_num;
		return "str" + Integer.toString(str_num - 1);
	}

	static void add_line(String content) {
		if (ICEvaluator.run_num != 1)
			return;
		code += content + "\n";
	}

	static void add_comment(String content) {
		add_line("#" + content);
	}

	static String new_temp() {
		if (ICEvaluator.run_num != 1)
			return "";
		++temp_counter;
		return "R" + Integer.toString(temp_counter - 1);
	}

	public static String add_str(String value) {
		if (ICEvaluator.run_num != 1)
			return "";
		String str_id = new_str();
		str_table += str_id + ": " + value + "\n";
		return str_id;

	}

	//creates a class dispatch vector
	static void class_dec(icClass cls) {
		// add dispatch list name
		cls.dv = "_DV_" + cls.name.toUpperCase();
		if (ICEvaluator.run_num == 0)
			return;
		dispatch_tables += cls.dv + ": [";
		int prev_dv_len = dispatch_tables.length();
		class_dv(cls, new ArrayList<icFunction>());
	    if (dispatch_tables.length() > prev_dv_len)
	    { //remove last comma
	    	dispatch_tables = dispatch_tables.substring(0, dispatch_tables.length()-1);
	    }
		dispatch_tables += "]\n";
	}
	
	//fills a class dispatch vector. does so recursively
	static List<String> class_dv(icClass cls, List<icFunction> usedUp)
	{
		List<String> usedDown= new ArrayList<String>();
		if (cls.ext != null)
		{
			List<icFunction> up_copy = new ArrayList<icFunction>(usedUp);
			up_copy.addAll(cls.instFuncs);
			usedDown = class_dv(cls.ext, up_copy);
		}
		for (icFunction element : cls.instFuncs) {
			// should be ordered by offset
			boolean is_filtered = false;
			for (String func_name : usedDown) {
				if (func_name.equals(element.name))
				{
					is_filtered = true;
					break;
				}
			}
			if (is_filtered)
				continue;
			usedDown.add(element.name);
			boolean is_up = false;
			for (icFunction up: usedUp)
			{
				if (up.name.equals(element.name))
				{
					is_up = true;
					dispatch_tables += up.label + ",";					
					break;
				}
			}
			if (is_up)
				continue;
			dispatch_tables += element.label + ",";
		}
		return usedDown;
	}

	static String arithmetic_op(String src1, String src2, String op)
	{
		add_comment(src1 + " " + op + " " + src2);

		String reg = new_temp();
		add_line("Move " + src1 + "," + reg);
		if (op == "Div")
		{
			String div0_label = get_label("div0");
			add_line("Compare 0," + reg);
			add_line("JumpTrue " + div0_label);
			add_line("Library __println(" + str_error_div0 + "),Rdummy" );
			add_line("Jump " + runtime_error_label);
			add_line(div0_label + ":");
			
		}
		add_line(op +" " + src2 + "," + reg);
		return reg;

	}

	static String compare_op(String src1, String src2, Operator op, Environment env){ //returns 1 for true, 0 for false

		add_comment(src1 + op.toString() + src2);
		
		String result = new_temp();
		String temp1 = new_temp();
		String end = get_label("endComp");
		
		add_line("Move 0, " + result);

		
		add_line("Move "+src1+","+ temp1);
		
		/*
		move(temp1, src1,env);
		*/
		add_line("Compare " + src2 + "," + temp1); // Compare = temp1 - temp2
		
		if (op==Operator.GT) add_line("JumpLE "+ end);
		if (op==Operator.GTE) add_line("JumpL "+ end);
		if (op==Operator.LT) add_line("JumpGE "+ end);
		if (op==Operator.LTE) add_line("JumpG "+ end);
		if (op==Operator.EQUAL) add_line("JumpFalse "+ end); //assuming JumpFalse means JumpNEQZ
		if (op==Operator.NEQUAL) add_line("JumpTrue "+ end); //assuming JumpTrue means JumpEQZ
		add_line("Move 1,"+result);
		add_line(end+":"); //label _end (if jumped here then result=0) 
		return result;
	}

	static String unary_LNEG_op(String src){
		if (ICEvaluator.run_num == 0)
			return null;
		add_comment("!"+src);
		String result = new_temp();
		String temp = new_temp();
		String end_label = get_label("endLneg");
		add_line("Move 0,"+result);
		add_line("Move "+src+","+temp);
		add_line("Compare 0,"+temp);
		add_line("JumpTrue "+end_label);	//jump to end if true (src==0)
		add_line("Move 1, "+result);
		add_line(end_label+":");
		return result;
	}

	static String evaluate_int(int src)
	{
		return Integer.toString(src);

	}

	static String op_add_string(String src1, String src2) {
		add_comment(src1 + "+" + src2);
		String reg = new_temp();
		add_line("Library __stringCat(" + src1 + "," + src2 + ")," + reg);
		return reg;
	}

	static String dot_len(String src) {
		add_comment(src + ".length");
		check_null_ref(src);
		String reg1 = new_temp();
		add_line("Move " + src + "," + reg1);
		String reg2 = new_temp();
		add_line("ArrayLength " + reg1 + "," + reg2);
		return reg2;
	}

	public static void check_null_ref(String reg) {
		String nullref_label = get_label("nullref");
		add_line("Compare 0," + reg);
		add_line("JumpFalse " + nullref_label);
		add_line("Library __println(" + str_error_nullref + "),Rdummy" );
		add_line("Jump " + runtime_error_label);
		add_line(nullref_label + ":");
	}

	public static void put_label_comment(String cls, String func) {
		//add_line("");
		add_line("########## " + cls + "." + func + " ##########");
	}
	

	public static void put_label(String label) {
		add_line(label + ":");
	}


	static String new_arr(String len, String type) {
		add_comment("new " + type + "[" + len + "]");
		String reg1 = new_temp();
		add_line("Move " + len + "," + reg1);
		add_line("Add 1," + reg1);
		add_line("Mul 4," + reg1);
		//check size(reg1)
		
		String checksize_label = get_label("checksize");
		add_line("Compare 0," + reg1);
		add_line("JumpG " + checksize_label);
		add_line("Library __println(" + str_error_checksize + "),Rdummy" );
		add_line("Jump " + runtime_error_label);
		add_line(checksize_label + ":");

		String reg = new_temp();
		add_line("Library __allocateArray(" + reg1 + ")," + reg);
		
		//init array
		add_line("MoveArray " + len + "," + reg + "[0]");

		init_allocated(len, reg);
		
		
		return reg;
	}

	private static void init_allocated(String len, String start_reg) 
	{
		String temp = new_temp();
		String init_loop_label = get_label("init_loop");

		add_line("Move " + len + "," + temp);
		add_line(init_loop_label + ":");
		add_line("MoveArray 0," + start_reg + "[" + temp + "]");
		add_line("Sub 1," + temp);
		add_line("Compare 0," + temp);
		add_line("JumpLE " + init_loop_label);
	}

	static String new_obj(String len, String type, String class_dv) {
		add_comment("new " + type + "()");
		String reg = new_temp();
		add_line("Library __allocateObject(" + len + ")," + reg);
		add_line("MoveField " + class_dv + "," + reg + ".0");
		init_allocated(len, reg);
		return reg;
	}

	
	// assigns ir_rep to objName. 
	//op_type - if 0, will load value from ir_rep and store it in a new var. if 1, will load ir_rep to a temp.
	static String move(String objName, String ir_rep, int op_type,  Environment env) {
		if (op_type !=0){
			add_comment("Assigning object " + ir_rep + " to "  + objName);
		}
		else{
			add_comment("load object " + ir_rep);
		}
		
		String temp = null;
		
		// case of string
		if (op_type == 2){
			temp = new_temp();
			add_line("Move " + ir_rep + "," + temp);
			add_line("Move " + temp + "," + objName);
			return null;
		}
		else{
			add_line("Move " + ir_rep + "," + objName);
		}

		return null;

	}
	

	//if src is null we assume its not an assign stmt
	static String location_expr_dot_id(String expr, String id, String src, boolean is_this)
	{
		String temp_expr = IR.new_temp();
		IR.add_line("Move " + expr + "," + temp_expr);
		check_null_ref(temp_expr);
		String temp_id = id;
		if (src==null)
		{//non assign
			String out_reg = IR.new_temp();
			IR.add_line("MoveField " + temp_expr + "." + temp_id + "," + out_reg);
			return out_reg;
		}
		else 
		{//assign
			IR.add_line("MoveField "+ src + "," + temp_expr + "." + temp_id );
			return null;
		}
	}

	//if src is null we assume its not an assign stmt
	static String location_id(String id, String src)
	{
		String temp_id = IR.new_temp();
		if (src==null)
		{//non assign
			IR.add_line("Move " + id + "," + temp_id);
			return temp_id;
		}
		else 
		{//assign
			//IR.add_line("Move "+ src + ","  + temp_id );
			IR.add_line("Move "+ src + ","  + id );
			return null;
		}
	}

	//if src is null we assume its not an assign stmt
	static String location_arr(String arr, String index, String src)
	{

		//String temp_arr = IR.new_temp();
		//IR.add_line("Move " + arr + "," + temp_arr);
		check_null_ref(arr);
		String temp_index = IR.new_temp();
		IR.add_line("Move " + index + "," + temp_index);

		//check array access
		String temp = IR.new_temp();

		
		add_line("#__checkArrayAccess(" + arr + "," + temp_index + ")");
		add_line("MoveArray " + arr + "[0]," + temp);

		String arr_index_err_label = get_label("arr_err_index");
		String arr_index_success_label = get_label("arr__success_index");
		add_line("Compare 0," + temp_index);
		add_line("JumpLE " + arr_index_err_label);
		add_line("Compare " + temp_index + "," + temp);
		add_line("JumpG " + arr_index_success_label);
		add_line(arr_index_err_label + ":");
		add_line("Library __println(" + str_error_array_bound + "),Rdummy" );
		add_line("Jump " + runtime_error_label);
		add_line(arr_index_success_label + ":");
		
		
		if (src==null)
		{//non assign
			String out_reg = IR.new_temp();
			IR.add_line("MoveArray " + arr + "[" + temp_index + "]," + out_reg);
			return out_reg;
		}
		else 
		{//assign
			IR.add_line("MoveArray " + src + "," + arr + "[" + temp_index + "]");
			return null;
		}
	}
	
	
	
	static String staticCall(String funcName, String className, String arguments, int irLbFlg){ 
		String fName = "";
		/*
		add_comment("StaticCall _C_" + funcName +"(" +arguments);
		*/
		if (irLbFlg ==0){
			fName = "StaticCall _" + className + "_";
		
		}
		else if(irLbFlg ==1){
			fName = "Library __";
		}
		add_line(fName + funcName +"(" +arguments);
		return funcName;
		
	}

	public static void runtime_error() {
		add_line(runtime_error_label + ":");
		add_line("Library __exit(0),Rdummy");
	}

}
