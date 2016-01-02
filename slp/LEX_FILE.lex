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
/* will be copied letter to letter into the Lexer class code.*/
  
%{
public void printLineNumber(){
	System.out.pr3intln();
	System.out.print(yyline+1);
	System.out.print(": "); 
}  

public int getLineNumber(){
	return yyline+1;
} 

private Token printToken(String token_name, int token_sym) {
	/*printLineNumber(); 
	System.out.print(token_name); */
	return symbol(token_sym, token_name);
}

private Token printToken(String token_name, int token_sym, Object value) {
	/*printLineNumber(); 
	System.out.print(token_name); */
	return symbol(token_sym, token_name, value);
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
    private Token symbol(int type, String token_name)               
   		{return new Token(type, yyline, yycolumn, token_name);}
    private Token symbol(int type, String token_name, Object value) 
    	{return new Token(type, yyline, yycolumn, token_name, value);}
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

"="					{ return printToken("ASSIGN", sym.ASSIGN);}
"boolean"			{ return printToken("BOOLEAN", sym.BOOLEAN);}
"break"				{ return printToken("BREAK",sym.BREAK);}
"class"				{ return printToken("CLASS",sym.CLASS);}
","					{ return printToken("COMMA",sym.COMMA);}
"continue"			{ return printToken("CONTINUE",sym.CONTINUE);}
"/"					{ return printToken("DIVIDE",sym.DIVIDE);}
"."					{ return printToken("DOT",sym.DOT);}
"=="				{ return printToken("EQUAL",sym.EQUAL);}
"extends"			{ return printToken("EXTENDS",sym.EXTENDS);}
"else"				{ return printToken("ELSE",sym.ELSE);}
"false"				{ return printToken("FALSE",sym.FALSE);}  
">"					{ return printToken("GT",sym.GT);}
">="				{ return printToken("GTE",sym.GTE);}
"if"				{ return printToken("IF",sym.IF);}
"int"				{ return printToken("INT",sym.INT);}
"&&"				{ return printToken("LAND",sym.LAND);}
"["					{ return printToken("LB",sym.LB);}
"("					{ return printToken("LP",sym.LP);}
"{"					{ return printToken("LCBR",sym.LCBR);}
"length"			{ return printToken("LENGTH",sym.LENGTH);}
"new"				{ return printToken("NEW",sym.NEW);}
"!"					{ return printToken("LNEG",sym.LNEG);}
"||"				{ return printToken("LOR",sym.LOR);}
"<"					{ return printToken("LT",sym.LT);}
"<="				{ return printToken("LTE",sym.LTE);}
"-"					{ return printToken("MINUS",sym.MINUS);}
"%"					{ return printToken("MOD",sym.MOD);}
"*"					{ return printToken("MULTIPLY",sym.MULTIPLY);}
"!="				{ return printToken("NEQUAL",sym.NEQUAL);}
"null"				{ return printToken("NULL",sym.NULL);}
"+"					{ return printToken("PLUS",sym.PLUS);}
"]"					{ return printToken("RB",sym.RB);}
"}"					{ return printToken("RCBR",sym.RCBR);}
"return"			{ return printToken("RETURN",sym.RETURN);}
")"					{ return printToken("RP",sym.RP);}
";"					{ return printToken("SEMI",sym.SEMI);}
"static"			{ return printToken("STATIC",sym.STATIC);}
"string"			{ return printToken("STRING",sym.STRING);}
"this"				{ return printToken("THIS",sym.THIS);}
"true"				{ return printToken("TRUE",sym.TRUE);}
"void"				{ return printToken("VOID",sym.VOID);}
"while"				{ return printToken("WHILE",sym.WHILE);}
"/*" 				{ throw new RuntimeException("Error: unclosed comment at line "+Integer.toString(yyline+1)); }
"*/"				{ throw new RuntimeException("Error: closed comment without an opening at line "+Integer.toString(yyline+1)); }


{INTEGER}			{
						return printToken("INTEGER(" + new String(yytext()) + ")", 
							sym.INTEGER, new Integer(yytext()));
					}   
{IDENTIFIER}		{
						return printToken("ID(" + new String(yytext()) + ")", 
							sym.ID, new String(yytext()));
					}
						
{CLASS_ID}			{
						return printToken("CLASS_ID(" + new String(yytext()) + ")", 
							sym.CLASS_ID, new String(yytext()));
					}	
					
{WhiteSpace}		{ /* just skip what was found, do nothing */ }  

{QUOTE}				{
						return printToken("QUOTE(" + new String(yytext()) + ")", 
							sym.QUOTE, new String(yytext()));
					} 
					
{COMMENT}			{ /* skip */ }


.|\n				{ throw new RuntimeException("Error: Invalid token: "+new String(yytext())+" at line " +Integer.toString(yyline+1)); }

{illegalInt}        { throw new RuntimeException("Error: Invalid token: "+new String(yytext())+" at line " +Integer.toString(yyline+1));  }


<<EOF>>             {
						return printToken("EOF", sym.EOF, new String(yytext()));
					}


}


