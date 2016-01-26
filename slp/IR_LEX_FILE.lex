 /***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/***************************/
<<<<<<< Updated upstream
/* AUTHOR: OREN ISH SHALOM */
/***************************/
=======
/* AUTHOR: OREN ISH SHALOM */ 
/***************************/ 
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
}  
=======
}   
>>>>>>> Stashed changes

public int getLineNumber(){
	return yyline+1;
} 

<<<<<<< Updated upstream
private Token printToken(String token_name, int token_sym) {
	/*printLineNumber(); 
	System.out.print(token_name); */
	return symbol(token_sym, token_name);
}

private Token printToken(String token_name, int token_sym, Object value) {
	/*printLineNumber(); 
	System.out.print(token_name); */
	return symbol(token_sym, token_name, value);
=======
private Token printToken(String token_name, int token_IR_CUPSym) {
	/*printLineNumber(); 
	System.out.print(token_name); */
	return symbol(token_IR_CUPSym, token_name);
}

private Token printToken(String token_name, int token_IR_CUPSym, Object value) {
	/*printLineNumber(); 
	System.out.print(token_name); */
	return symbol(token_IR_CUPSym, token_name, value);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    /* Create a new java_cup.runtime.Symbol with information about the 
=======
    /* Create a new java_cup.runtime.IR_CUPSymbol with information about the 
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
COMMENT			= "#" {InputCharacter}* {LineTerminator}?
WORD			= [A-Za-z_0-9]+
QUOTE			= \" {StringCharacter}* \"
StringLabel		= [A-Za-z0-9]{WORD}":" 
Label			= "_"{WORD}":"
DVLabel			= {Label} [ \t\f]* "["
=======
COMMENT			= "#" {InputCharacter}* {LineTerminator}
WORD			= [A-Za-z_0-9]+
QUOTE			= \" {StringCharacter}* \"
StringLabel		= {WORD}":" 
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
 

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
=======


"Move"					{ return printToken("Move", IR_CUPSym.MOVE); }
"MoveArray"				{ return printToken("MoveArray", IR_CUPSym.MOVEARRAY); }
"MoveField"				{ return printToken("MoveField",IR_CUPSym.MOVEFIELD); }
"ArrayLength"			{ return printToken("ArrayLength",IR_CUPSym.ARRAYLENGTH); }
"Add"					{ return printToken("Add",IR_CUPSym.ADD); }
"Sub"					{ return printToken("Sub",IR_CUPSym.SUB); }
"Mul"					{ return printToken("Mul",IR_CUPSym.MUL); }
"Div"					{ return printToken("Div",IR_CUPSym.DIV); }
"Mod"					{ return printToken("Mod",IR_CUPSym.MOD); }
"Inc"					{ return printToken("Inc",IR_CUPSym.INC); }
"Dec"					{ return printToken("Dec",IR_CUPSym.DEC); }
"Neg"					{ return printToken("Neg",IR_CUPSym.NEG); }
"Not"					{ return printToken("Not",IR_CUPSym.NOT); }
"And"					{ return printToken("And",IR_CUPSym.AND); }
"Or"					{ return printToken("Or",IR_CUPSym.OR); }
"Xor"					{ return printToken("Xor",IR_CUPSym.XOR); }
"Compare"				{ return printToken("Compare",IR_CUPSym.COMPARE); }
"Jump"					{ return printToken("Jump",IR_CUPSym.JUMP); }
"JumpTrue"				{ return printToken("JumpTrue",IR_CUPSym.JUMPTRUE); }
"JumpFalse"				{ return printToken("JumpFalse",IR_CUPSym.JUMPFALSE); }
"JumpG"					{ return printToken("JumpG",IR_CUPSym.JUMPG); }
"JumpGE"				{ return printToken("JumpGE",IR_CUPSym.JUMPGE); }
"JumpL"					{ return printToken("JumpL",IR_CUPSym.JUMPL); }
"JumpLE"				{ return printToken("JumpLE",IR_CUPSym.JUMPLE); }
"Library"				{ return printToken("Library",IR_CUPSym.LIBRARY); }
"StaticCall"			{ return printToken("StaticCall",IR_CUPSym.STATICCALL); }
"VirtualCall"			{ return printToken("VirtualCall",IR_CUPSym.VIRTUALCALL); }
"Return"				{ return printToken("Return",IR_CUPSym.RETURN); }
","						{ return printToken(",",IR_CUPSym.COMMA); }
":"						{ return printToken(":",IR_CUPSym.COLON); }
"_"						{ return printToken("_",IR_CUPSym.UNDER); }
"["						{ return printToken("LB",IR_CUPSym.LB);}
"("						{ return printToken("LP",IR_CUPSym.LP);}
"]"						{ return printToken("RB",IR_CUPSym.RB);}
")"						{ return printToken("RP",IR_CUPSym.RP);}
"="						{ return printToken("ASSIGN", IR_CUPSym.ASSIGN);}
"."						{ return printToken("DOT",IR_CUPSym.DOT);}


{StringLabel}		{
						return printToken("StringLabel(" + new String(yytext()) + ")", 
							IR_CUPSym.STRINGLABEL);
					}  

{QUOTE}				{ return printToken("Quote("+new String(yytext()) + ")", IR_CUPSym.QUOTE); }
					
{WORD}				{ return printToken("WORD("+new String(yytext()) + ")", IR_CUPSym.WORD); }
					
{WhiteSpace}		{ /* just skip what was found, do nothing */ }  


					
{COMMENT}			{  }
>>>>>>> Stashed changes


.|\n				{ throw new RuntimeException("Error: Invalid token: "+new String(yytext())+" at line " +Integer.toString(yyline+1)); }


<<EOF>>             {
<<<<<<< Updated upstream
						return printToken("EOF", IRsym.EOF, new String(yytext()));
=======
						return printToken("EOF", IR_CUPSym.EOF, new String(yytext()));
>>>>>>> Stashed changes
					}


}


