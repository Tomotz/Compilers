package slp;

/** An enumeration containing all the keyword types in the IC language.
 */
public enum Keyword 
{
	CLASS	,
	EXTENDS	,
	STATIC	,
	VOID	,
	INT	,
	BOOLEAN	,
	STRING	,
	RETURN	,
	IF	,
	ELSE	,
	WHILE	,
	BREAK	,
	CONTINUE	,
	THIS	,
	NEW	,
	LENGTH	,
	TRUE	,
	FALSE	,
	NULL	;

	/** Prints the keywords in the same way it appears in the program.
	 */
	public String toString()
	{
		switch (this)
		{
		case CLASS: return "class";
		case EXTENDS: return "extends";
		case STATIC: return "static";
		case VOID: return "void";
		case INT: return "int";
		case BOOLEAN: return "boolean";
		case STRING: return "string";
		case RETURN: return "return";
		case IF: return "if";
		case ELSE: return "else";
		case WHILE: return "while";
		case BREAK: return "break";
		case CONTINUE: return "continue";
		case THIS: return "this";
		case NEW: return "new";
		case LENGTH: return "length";
		case TRUE: return "TRUE";
		case FALSE: return "FALSE";
		case NULL: return "null";
	
	
		default: throw new RuntimeException("Unexpted value: " + this.name());
		}
	
	}
}
