package slp;

import java_cup.runtime.Symbol;

/** Adds line number and name information to scanner symbols.
 */
public class Token extends Symbol {
	private final int line;
	private final String name;

	public Token(int line, String name, int id, Object value) {
		super(id, value);
		this.name = name;
		this.line = line + 1;
	}


	public Token(int line, String name, int id) {
		super(id, null);
		this.name = name;
		this.line = line + 1;
	}

	public Token(int m_type, int line, int column, String token_name, Object value) {
		super(m_type, line, column, value);
		this.name = token_name;
		this.line = line + 1;
	}
	public Token(int m_type, int line, int column, String token_name) {
		super(m_type, line, column);
		this.name = token_name;
		this.line = line + 1;
	}
	
	public String toString() {
		String val = value != null ? "(" + value + ")" : "";
		return name +  val;
	}
	
	public int getLine() {
		return line;
	}
}