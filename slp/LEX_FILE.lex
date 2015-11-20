/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/***************************/
/* AUTHOR: OREN ISH SHALOM */
/***************************/

/*************/
/* USER CODE */
/*************/
   
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/
      
%%
   
/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column
/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup
   
/****************/
/* DECLARATIONS */
/****************/
/*************************************************************************

****/   
/* Code between %{ and %}, both of which must be at the beginning of a 

line, */
/* will be copied letter to letter into the Lexer class code.              

  */
  
%{
private void printLineNumber(){
	System.out.println();
	System.out.print(yyline+1);
	System.out.print(": ");
}
%}
/* Here you declare member variables and functions that are used inside 

the  */
/* scanner actions.                                                        

  */  
/*************************************************************************

****/   
%{   
    

/*************************************************************************

********/
    /* Create a new java_cup.runtime.Symbol with information about the 

current token */
    

/*************************************************************************

********/
    private Token symbol(int type)               {return new Token(type, 

yyline, yycolumn);}
    private Token symbol(int type, Object value) {return new Token(type, 

yyline, yycolumn, value);}
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	= \r|\n|\r\n
InputCharacter	= [^\r\n]
StringCharacter	= (\\\")|(\\\\)|(\\t)|(\\n)|([ !#-Z]) | "[" | "]" | "^" | [_-~]
WhiteSpace		= {LineTerminator} | [ \t\f]
INTEGER			= 0* | [1-9][0-9]*
IDENTIFIER		= [a-z][A-Za-z_0-9]*
illegalInt       = 0+[1-9]+
CLASS_ID		= [A-Z][A-Za-z_0-9]* 
COMMENT			= "/*" ~"*/" | "//" {InputCharacter}* {LineTerminator}?
QUOTE			= \" {StringCharacter}* \"
/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/
   
/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/
   
<YYINITIAL> {

"="					{ printLineNumber(); System.out.print("ASSIGN"); return symbol(sym.ASSIGN);}
"boolean"			{ printLineNumber(); System.out.print("BOOLEAN"); return symbol(sym.BOOLEAN);}
"break"				{ printLineNumber(); System.out.print("BREAK"); return symbol(sym.BREAK);}
"class"				{ printLineNumber(); System.out.print("CLASS"); return symbol(sym.CLASS);}
","					{ printLineNumber(); System.out.print("COMMA"); return symbol(sym.COMMA);}
"continue"			{ printLineNumber(); System.out.print("CONTINUE"); return symbol(sym.CONTINUE);}
"/"					{ printLineNumber(); System.out.print("DIVIDE");    return symbol(sym.DIVIDE);}
"."					{ printLineNumber(); System.out.print("DOT"); return symbol(sym.DOT);}
"=="				{ printLineNumber(); System.out.print("EQUAL"); return symbol(sym.EQUAL);}
"extends"			{ printLineNumber(); System.out.print("EXTENDS"); return symbol(sym.EXTENDS);}
"else"				{ printLineNumber(); System.out.print("ELSE"); return symbol(sym.ELSE);}
"false"				{ printLineNumber(); System.out.print("FALSE"); return symbol(sym.FALSE);}
">"					{ printLineNumber(); System.out.print("GT"); return symbol(sym.GT);}
">="				{ printLineNumber(); System.out.print("GTE"); return symbol(sym.GTE);}
"if"				{ printLineNumber(); System.out.print("IF"); return symbol(sym.IF);}
"int"				{ printLineNumber(); System.out.print("INT"); return symbol(sym.INT);}
"&&"				{ printLineNumber(); System.out.print("LAND"); return symbol(sym.LAND);}
"["					{ printLineNumber(); System.out.print("LB"); return symbol(sym.LB);}
"("					{ printLineNumber(); System.out.print("LP"); return symbol(sym.LP);}
"{"					{ printLineNumber(); System.out.print("LCBR"); return symbol(sym.LCBR);}
"length"			{ printLineNumber(); System.out.print("LENGTH"); return symbol(sym.LENGTH);}
"new"				{ printLineNumber(); System.out.print("NEW"); return symbol(sym.NEW);}
"!"					{ printLineNumber(); System.out.print("LNEG"); return symbol(sym.LNEG);}
"||"				{ printLineNumber(); System.out.print("LOR"); return symbol(sym.LOR);}
"<"					{ printLineNumber(); System.out.print("LT"); return symbol(sym.LT);}
"<="				{ printLineNumber(); System.out.print("LTE"); return symbol(sym.LTE);}
"-"					{ printLineNumber(); System.out.print("MINUS"); return symbol(sym.MINUS);}
"%"					{ printLineNumber(); System.out.print("MOD"); return symbol(sym.MOD);}
"*"					{ printLineNumber(); System.out.print("MULTIPLY"); return symbol(sym.MULTIPLY);}
"!="				{ printLineNumber(); System.out.print("NEQUAL"); return symbol(sym.NEQUAL);}
"null"				{ printLineNumber(); System.out.print("NULL"); return symbol(sym.NULL);}
"+"					{ printLineNumber(); System.out.print("PLUS"); return symbol(sym.PLUS);}
"]"					{ printLineNumber(); System.out.print("RB"); return symbol(sym.RB);}
"}"					{ printLineNumber(); System.out.print("RCBR"); return symbol(sym.RCBR);}
"return"			{ printLineNumber(); System.out.print("RETURN"); return symbol(sym.RETURN);}
")"					{ printLineNumber(); System.out.print("RP"); return symbol(sym.RP);}
";"					{ printLineNumber(); System.out.print("SEMI"); return symbol(sym.SEMI);}
"static"			{ printLineNumber(); System.out.print("STATIC"); return symbol(sym.STATIC);}
"string"			{ printLineNumber(); System.out.print("STRING"); return symbol(sym.STRING);}
"this"				{ printLineNumber(); System.out.print("THIS"); return symbol(sym.THIS);}
"true"				{ printLineNumber(); System.out.print("TRUE"); return symbol(sym.TRUE);}
"void"				{ printLineNumber(); System.out.print("VOID"); return symbol(sym.VOID);}
"while"				{ printLineNumber(); System.out.print("WHILE"); return symbol(sym.WHILE);}
"/*" 				{ throw new RuntimeException("Error: unclosed comment at line "+Integer.toString(yyline+1)); }
"*/"				{ throw new RuntimeException("Error: closed comment without an opening at line "+Integer.toString(yyline+1)); }


{INTEGER}			{
						printLineNumber();
						System.out.print("INTEGER(");
						System.out.print(yytext());
						System.out.print(")");
						return symbol(sym.INTEGER, new Integer(yytext()));
					}   
{IDENTIFIER}		{
						printLineNumber();
						System.out.print("ID(");
						System.out.print(yytext());
						System.out.print(")");
						return symbol(sym.ID, new String(yytext()));
					}
						
{CLASS_ID}			{
						printLineNumber();
						System.out.print("CLASS_ID(");
						System.out.print(yytext());
						System.out.print(")");
						return symbol(sym.CLASS_ID, new String(yytext()));
					}	
					
{WhiteSpace}		{ /* just skip what was found, do nothing */ }  

{QUOTE}				{
						printLineNumber();
						System.out.print("QUOTE(");
						System.out.print(yytext());
						System.out.print(")");
						return symbol(sym.QUOTE, new String(yytext()));
					} 
					
{COMMENT}			{ /* skip */ }


.|\n				{ throw new RuntimeException("Error: Invalid token: "+new String(yytext())+" at line " +Integer.toString(yyline+1)); }

{illegalInt}        { throw new RuntimeException("Error: Invalid token: "+new String(yytext())+" at line " +Integer.toString(yyline+1));  }


<<EOF>>             {
						printLineNumber();
						System.out.print("EOF");
						return symbol(sym.EOF, new String(yytext()));
					}


}


