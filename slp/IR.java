package slp;

public class IR {
	static String str_table = "";
	static String dispatch_tables = "";
	static String code = "";
	static int temp_counter = 0;
	
	static void add_line(String content)
	{
		if (ICEvaluator.run_num == 1)
			code.concat(content + "\n");
	}
	
	static String new_temp()
	{
		++temp_counter;
		return "R" + Integer.toString(temp_counter - 1);
	}

	static String op_add(String src1, String src2)
	{
		add_line("#" + src1 + "+" + src2);
		String reg = new_temp();
		add_line("Move " + src2 + "," + reg);
		add_line("Add " + src1 + "," + reg);
		return reg;
		
	}
	static String evaluate_int(int src)
	{
		return Integer.toString(src);
		
	}
	static String op_add_string(String src1, String src2)
	{
		add_line("#" + src1 + "+" + src2);
		String reg = new_temp();
		add_line("Library __stringCat(" + src1 + "," + src2 + ")," + reg);
		return reg;
	}

}
