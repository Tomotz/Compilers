package slp;

public class Formal {
	public final VarType type;
	public final String id;
	public int line;
	
	public Formal(String type, String id, int line){
		this.type = new VarType(type);
		this.id = id;
		this.line = line;
	}
}
