package slp;

/** An enumeration containing all the operation types in the SLP language.
 */
public enum Operator {
MINUS, PLUS, MULTIPLY, DIV, LT, GT, LTE, GTE, EQUAL, NEQUAL, LAND, LOR, MOD, LNEG;
	
	/** Prints the operator in the same way it appears in the program.
	 */
	public String toString() {
		switch (this) {
		case MINUS: return "-";
		case PLUS: return "+";
		case MULTIPLY: return "*";
		case DIV: return "/";
		case LT: return "<";
		case GT: return ">";
		case LTE: return "<=";
		case GTE: return ">=";
		case LAND: return "&&";
		case LOR: return "||";
		case EQUAL: return "==";
		case NEQUAL: return "!=";
		case MOD: return "%";
		case LNEG: return "!";
		default: throw new RuntimeException("Unexpted value: " + this.name());
		}
	}
}