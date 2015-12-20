package slp;

public class IR {
	static String str_table = "";
	static String dispatch_tables = "";
	static String code = "";
	static int temp_counter = 0;
	static int label_num = 0;
	static int str_num = 0;

	static String get_label(String text) {
		if (ICEvaluator.run_num != 0)
			return "";
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

	static String op_add(String src1, String src2) {
		add_comment(src1 + "+" + src2);
		String reg = new_temp();
		add_line("Move " + src2 + "," + reg);
		add_line("Add " + src1 + "," + reg);
		return reg;

	}

	static String evaluate_int(int src) {
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
