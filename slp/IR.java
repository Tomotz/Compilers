package slp;

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
		class_dv(cls, new ArrayList<icFunction>());
	    if (dispatch_tables.length() > 0)
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
		add_line(op +" " + src2 + "," + reg);
		return reg;

	}

	static String compare_op(String src1, String src2, Operator op){ //returns 1 for true, 0 for false

		add_comment(src1 + op.toString() + src2);
		
		String result = new_temp();
		String temp1 = new_temp();
		String end = get_label("end");
		
		add_line("Move 0, " + result);
		
		add_line("Move "+src1+","+ temp1);
		
		if (op==Operator.GT) add_line("JumpLE "+ end);
		if (op==Operator.GTE) add_line("JumpL "+ end);
		if (op==Operator.LT) add_line("JumpGE "+ end);
		if (op==Operator.LTE) add_line("JumpL "+ end);
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
		String end_label = get_label("end");
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
		add_line("#__checkNullRef("+src+")");//TODO - remove comment
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
		add_line("#__checkSize("+len+")");//TODO - remove comment
		String reg = new_temp();
		add_line("Library __allocateArray(" + len + ")," + reg);
		return reg;
	}

	static String new_obj(String len, String type, String class_dv) {
		add_comment("new " + type + "()");
		String reg = new_temp();
		add_line("Library __allocateObject(" + len + ")," + reg);
		add_line("MoveField " + class_dv + "," + reg + ".0");
		return reg;
	}

	// should probably use only the second case (new object should be allocated in astNew...)
	static String move(String objName, String ir_rep, Environment env) {
		add_comment("Assigning object " + objName);
		/*
		System.out.println("val " + ir_rep + " var " + objName);
		*/
		int varIndex = 0;
		int valIndex = 0;
		int tmpFlag = 0; 
		String valName = null;
		String varName = null;
		String field = null;
		String offset = null;
		String temp = null;
		
		// value is a field 
		if ((valIndex= ir_rep.indexOf('.')) != -1){
			field = ir_rep.substring(0,valIndex);
			offset = ir_rep.substring(valIndex+1);
			
			if ((valIndex = offset.indexOf('[')) != -1){
				String offsetIn = offset.substring(0,valIndex);
				String arrayIn = offset.substring(valIndex+1,offset.length()-1);
				temp = new_temp();
				add_line("Move " + field + "," + temp);
				String temp2 = new_temp();
				add_line("MoveField " + temp + "." + offsetIn + "," + temp2);
				temp = new_temp();
				add_line("Move " + arrayIn + "," + temp);
				ir_rep = new_temp();
				add_line("MoveArray " + temp2 + "[" + temp + "]" + "," + ir_rep);
			}
			else{
				temp = new_temp();
				add_line("Move " + field + "," + temp);
				ir_rep = new_temp();
				add_line("MoveField " + temp + "." + offset + "," + ir_rep);
				tmpFlag = 1;
			}
		}
		
		else if ((valIndex = ir_rep.indexOf('[')) != -1){
			field = ir_rep.substring(0,valIndex);
			offset = ir_rep.substring(valIndex+1,ir_rep.length()-1);
			
			temp = new_temp();
			add_line("Move " + field + "," + temp);
			
			ir_rep = new_temp();
			add_line("MoveArray " + temp + "[" + offset + "]" + "," + ir_rep);
			tmpFlag = 1;
		}
		else if ((valIndex = ir_rep.indexOf('_')) != -1){
			valName = ir_rep.substring(0,valIndex);
		}
		
		
		if ((varIndex = objName.indexOf('.')) != -1){
			field = objName.substring(0,varIndex);
			offset = objName.substring(varIndex+1);
			
			temp = new_temp();
			add_line("Move " + field + "," + temp);
			field = temp;
			
			if (tmpFlag != 1){
				temp = new_temp();
				add_line("Move " + ir_rep + "," + temp);
				ir_rep = temp;
			}
			add_line("MoveField " + ir_rep + "," + field + "." + offset);
			return null;
		}
		else if((varIndex = objName.indexOf('['))!=-1){
			field = objName.substring(0,varIndex);
			offset = objName.substring(varIndex+1,objName.length()-1);
			
			temp = new_temp();
			add_line("Move " + field + "," + temp);
			field = temp;
			
			if (tmpFlag != 1){
				temp = new_temp();
				add_line("Move " + ir_rep + "," + temp);
				ir_rep = temp;
			}
			add_line("MoveArray " + ir_rep + "," + field + "[" + offset + "]");
			return null;
		}
		else if ((varIndex = objName.indexOf('_')) != -1){
			varName = objName.substring(0,varIndex);
			}
		
		if ((env.getObjByName(varName) != null) && (env.getObjByName(valName) != null)){
				temp = new_temp();
				add_line("Move " + ir_rep + "," + temp);
				add_line("Move " + temp + "," + objName);
			}
		else{
			add_line("Move " + ir_rep + "," + objName);
		}

		return null;

	}
	
	static String staticCall(String funcName, String arguments, int irLbFlg){ 
		String fName = "";
		/*
		add_comment("StaticCall _C_" + funcName +"(" +arguments);
		*/
		if (irLbFlg ==0){
			fName = "StaticCall _C_";
		
		}
		else if(irLbFlg ==1){
			fName = "Library __";
		}
		add_line(fName + funcName +"(" +arguments);
		return funcName;
		
	}

}
