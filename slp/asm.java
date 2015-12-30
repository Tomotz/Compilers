package slp;

public class asm {
	static final int IMM = 0;
	static final int REG = 1;
	static final int MEM = 2;

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
	public int getOp(String value)
	{
		if (value.startsWith("R"))
		{
			return REG;
		}
		if (isInteger(value, ))
		
	}

}
