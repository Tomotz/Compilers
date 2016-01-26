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
%class IRLexer

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
	System.out.println();
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
/*
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
*/
/****************************************************/
LineTerminator	= \r|\n|\r\n
InputCharacter	= [^\r\n]
StringCharacter	= (\\\")|(\\\\)|(\\t)|(\\n)|([ !#-Z]) | "[" | "]" | "^" | [_-~]
WhiteSpace		= {LineTerminator} | [ \t\f]
QUOTE			= \" {StringCharacter}* \"
COMMENT			= "#" {InputCharacter}* {LineTerminator}?
WORD			= [A-Za-z_0-9]+
QUOTE			= \" {StringCharacter}* \"
StringLabel		= [A-Za-z0-9]{WORD}":" 
Label			= "_"{WORD}":"
DVLabel			= {Label} [ \t\f]* "["
/******QUOTE************************/
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
 

"SPACE"					{ return printToken("SPACE", IRsym.SPACE); }
"Move"					{ return printToken("Move", IRsym.MOVE); }
"MoveArray"				{ return printToken("MoveArray", IRsym.MOVEARRAY); }
"MoveField"				{ return printToken("MoveField",IRsym.MOVEFIELD); }
"ArrayLength"			{ return printToken("ArrayLength",IRsym.ARRAYLENGTH); }
"Add"					{ return printToken("Add",IRsym.ADD); }
"Sub"					{ return printToken("Sub",IRsym.SUB); }
"Mul"					{ return printToken("Mul",IRsym.MUL); }
"Div"					{ return printToken("Div",IRsym.DIV); }
"Mod"					{ return printToken("Mod",IRsym.MOD); }
"Inc"					{ return printToken("Inc",IRsym.INC); }
"Dec"					{ return printToken("Dec",IRsym.DEC); }
"Neg"					{ return printToken("Neg",IRsym.NEG); }
"Not"					{ return printToken("Not",IRsym.NOT); }
"And"					{ return printToken("And",IRsym.AND); }
"Or"					{ return printToken("Or",IRsym.OR); }
"Xor"					{ return printToken("Xor",IRsym.XOR); }
"Compare"				{ return printToken("Compare",IRsym.COMPARE); }
"Jump"					{ return printToken("Jump",IRsym.JUMP); }
"JumpTrue"				{ return printToken("JumpTrue",IRsym.JUMPTRUE); }
"JumpFalse"				{ return printToken("JumpFalse",IRsym.JUMPFALSE); }
"JumpG"					{ return printToken("JumpG",IRsym.JUMPG); }
"JumpGE"				{ return printToken("JumpGE",IRsym.JUMPGE); }
"JumpL"					{ return printToken("JumpL",IRsym.JUMPL); }
"JumpLE"				{ return printToken("JumpLE",IRsym.JUMPLE); }
"Library"				{ return printToken("Library",IRsym.LIBRARY); }
"StaticCall"			{ return printToken("StaticCall",IRsym.STATICCALL); }
"VirtualCall"			{ return printToken("VirtualCall",IRsym.VIRTUALCALL); }
"Return"				{ return printToken("Return",IRsym.RETURN); }
","						{ return printToken(",",IRsym.COMMA); }
":"						{ return printToken(":",IRsym.COLON); }
"_"						{ return printToken("_",IRsym.UNDER); }
"["						{ return printToken("LB",IRsym.LB);}
"("						{ return printToken("LP",IRsym.LP);}
"]"						{ return printToken("RB",IRsym.RB);}
")"						{ return printToken("RP",IRsym.RP);}
"="						{ return printToken("ASSIGN", IRsym.ASSIGN);}
"."						{ return printToken("DOT",IRsym.DOT);}
 

{StringLabel}		{
						return printToken(new String(yytext()), IRsym.STRINGLABEL);
					}  

{DVLabel}			{ return printToken(new String(yytext()),	IRsym.DVLABEL); }

{Label}		{
						return printToken(new String(yytext()),	IRsym.LABEL);
					} 


{QUOTE}				{ return printToken(new String(yytext()), IRsym.QUOTE); }
					
{WORD}				{ return printToken(new String(yytext()), IRsym.WORD); }
					
{WhiteSpace}		{ /* just skip what was found, do nothing */ }  
					
{COMMENT}			{ return printToken(new String(yytext()), IRsym.COMMENT); }


.|\n				{ throw new RuntimeException("Error: Invalid token: "+new String(yytext())+" at line " +Integer.toString(yyline+1)); }


<<EOF>>             {
						return printToken("EOF", IRsym.EOF, new String(yytext()));
					}


}


