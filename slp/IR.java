package slp;

public class IR {
	static String str_table = "";
	static String dispatch_tables = "";
	static String code = "";
	static int temp_counter = 0;
	static int label_num = 0;
	static int str_num = 0;
	static String whLblStrt;   // labels for the use of break and continue in a while loop
	static String whLblEnd;


	static String get_label(String text)
	{
		//if (ICEvaluator.run_num != 0)
		//return "";
		++label_num;
		return (text + "_" + Integer.toString(label_num));
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

	static void class_dec(icClass cls) {
		// add dispatch list name
		dispatch_tables += "_DV_" + cls.name.toUpperCase() + ": [";
		boolean is_first = true;
		for (icFunction element : cls.statFuncs) {
			// should be ordered by offset
			if (!is_first) {
				dispatch_tables += ",";
				is_first = false;
			}
			dispatch_tables += element.label;
		}
		dispatch_tables += "]\n";
	}


	static String arithmetic_op(String src1, String src2, String op)
	{
		add_comment(src1 + op + src2);
		String reg = new_temp();
		add_line("Move " + src1 + "," + reg);
		add_line(op +" " + src2 + "," + reg);
		return reg;

	}

	static String compare_op(String src1, String src2, Operator op){ //returns 1 for true, 0 for false
		add_comment(src1 + op.toString() + src2);
		
		String result = new_temp();
		String temp1 = new_temp();
		String temp2 = new_temp();
		String end = get_label("end");
		
		add_line("Move 0, " + result);
		add_line("Move "+src1+","+ temp1);
		add_line("Move "+src2+","+ temp2);
		add_line("Compare " + temp2 + "," + temp1); // Compare = temp1 - temp2
		
		if (op==Operator.GT) add_line("JumpLTE "+ end);
		if (op==Operator.GTE) add_line("JumpLT "+ end);
		if (op==Operator.LT) add_line("JumpGTE "+ end);
		if (op==Operator.LTE) add_line("JumpLE "+ end);
		if (op==Operator.EQUAL) add_line("JumpFalse "+ end); //assuming JumpFalse means JumpNEQZ
		if (op==Operator.NEQUAL) add_line("JumpTrue "+ end); //assuming JumpTrue means JumpEQZ
		add_line("Move 1,"+result);
		add_line(end); //label _end (if jumped here then result=0) 
		return result;
	}
	
	static String unary_LNEG_op(String src){
		add_comment("!"+src);
		String result = new_temp();
		String temp = new_temp();
		String end_label = get_label("end");
		add_line("Move 0,"+result);
		add_line("Move "+src+","+temp);
		add_line("Compare 0,"+temp);
		add_line("JumpTrue "+end_label);	//jump to end if true (src==0)
		add_line("Move 1, "+result);
		add_line(end_label);
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
		String reg = new_temp();
		add_line("ArrayLength " + src + "," + reg);
		return reg;
	}

	public static void put_label_comment(String cls, String func) {
		add_line("");
		add_line("########## " + cls + "." + func + " ##########");
	}

	public static void put_label(String label) {
		add_line(label + ":");
	}

	static String new_arr(String len, String type) {
		add_comment("new " + type + "[" + len + "]");
		String reg = new_temp();
		add_line("Library __allocateArray(" + len + ")," + reg);
		return reg;
	}

	static String new_obj(String len, String type) {
		add_comment("new " + type + "()");
		String reg = new_temp();
		add_line("Library __allocateObject(" + len + ")," + reg);
		return reg;
	}

	// should probably use only the second case (new object should be allocated in astNew...)
	static String move(String objName, String ir_rep, int allType) {

		if (allType == 0) {
			add_comment("Assigning new object of type " + objName);
			add_line("MoveField _DV_Foo" + objName + "," + ir_rep + ".0");
		} else if (allType == 1) {
			add_comment("Assigning new variable of type " + objName);
			add_line("Move " + ir_rep + "," + objName);
		}

		return null;

	}
	
	static String staticCall(String funcName){ 
		add_comment("StaticCall _C_" + funcName +"(");
		
		return funcName;
		
	}

}
