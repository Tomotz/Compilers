package slp;

public class utility {
	public static boolean isInteger(String str){
		int length = str.length();
		
		for (int i =0 ; i<length;i++){
			if(i ==0 && str.charAt(0) == '-'){
				if(length ==1){
					return false;
				}
				else{ continue;}
			}
			if(!Character.isDigit(str.charAt(i))){
				return false;
			}
			
		}
		
		return true;
		
	}
}
