package slp;

//----------------------------------------------------
// The following code was generated by CUP v0.11a beta 20060608
// Sat Jan 02 12:15:19 IST 2016
//----------------------------------------------------

import java_cup.runtime.*;

/** CUP v0.11a beta 20060608 generated parser.
  * @version Sat Jan 02 12:15:19 IST 2016
  */
public class IR_CUPCup extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public IR_CUPCup() {super();}

  /** Constructor which sets the default scanner. */
  public IR_CUPCup(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public IR_CUPCup(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\015\000\002\002\004\000\002\002\004\000\002\002" +
    "\003\000\002\007\002\000\002\003\005\000\002\004\005" +
    "\000\002\004\003\000\002\005\005\000\002\005\005\000" +
    "\002\005\003\000\002\006\005\000\002\006\003\000\002" +
    "\006\003" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\025\000\010\056\012\064\010\065\004\001\002\000" +
    "\014\057\ufff5\060\ufff5\061\ufff5\062\ufff5\063\ufff5\001\002" +
    "\000\014\057\ufff8\060\ufff8\061\ufff8\062\ufff8\063\ufff8\001" +
    "\002\000\012\002\uffff\056\uffff\064\uffff\065\uffff\001\002" +
    "\000\014\057\ufffb\060\ufffb\061\ufffb\062\020\063\017\001" +
    "\002\000\014\057\ufff6\060\ufff6\061\ufff6\062\ufff6\063\ufff6" +
    "\001\002\000\012\002\027\056\012\064\010\065\004\001" +
    "\002\000\010\056\012\064\010\065\004\001\002\000\006" +
    "\060\ufffe\061\015\001\002\000\004\060\023\001\002\000" +
    "\010\056\012\064\010\065\004\001\002\000\014\057\ufffc" +
    "\060\ufffc\061\ufffc\062\020\063\017\001\002\000\010\056" +
    "\012\064\010\065\004\001\002\000\010\056\012\064\010" +
    "\065\004\001\002\000\014\057\ufffa\060\ufffa\061\ufffa\062" +
    "\ufffa\063\ufffa\001\002\000\014\057\ufff9\060\ufff9\061\ufff9" +
    "\062\ufff9\063\ufff9\001\002\000\012\002\ufffd\056\ufffd\064" +
    "\ufffd\065\ufffd\001\002\000\006\057\025\061\015\001\002" +
    "\000\014\057\ufff7\060\ufff7\061\ufff7\062\ufff7\063\ufff7\001" +
    "\002\000\012\002\001\056\001\064\001\065\001\001\002" +
    "\000\004\002\000\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\025\000\014\002\010\003\005\004\012\005\006\006" +
    "\004\001\001\000\002\001\001\000\002\001\001\000\002" +
    "\001\001\000\002\001\001\000\002\001\001\000\012\003" +
    "\025\004\012\005\006\006\004\001\001\000\010\004\023" +
    "\005\006\006\004\001\001\000\004\007\013\001\001\000" +
    "\002\001\001\000\006\005\015\006\004\001\001\000\002" +
    "\001\001\000\004\006\021\001\001\000\004\006\020\001" +
    "\001\000\002\001\001\000\002\001\001\000\002\001\001" +
    "\000\002\001\001\000\002\001\001\000\002\001\001\000" +
    "\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$IR_CUPCup$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$IR_CUPCup$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$IR_CUPCup$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}



    
    /* Change the method report_error so it will display the line and
       column of where the error occurred in the input as well as the
       reason for the error which is passed into the method in the
       String 'message'. */
    public void report_error(String message, Object info) {
   
        /* Create a StringBuilder called 'm' with the string 'Error' in it. */
        StringBuilder m = new StringBuilder("Error");
   
        /* Check if the information passed to the method is the same
           type as the type java_cup.runtime.Symbol. */
        if (info instanceof java_cup.runtime.Symbol) {
            /* Declare a java_cup.runtime.Symbol object 's' with the
               information in the object info that is being typecasted
               as a java_cup.runtime.Symbol object. */
            java_cup.runtime.Symbol s = ((java_cup.runtime.Symbol) info);
   
            /* Check if the line number in the input is greater or
               equal to zero. */
            if (s.left >= 0) {                
                /* Add to the end of the StringBuilder error message
                   the line number of the error in the input. */
                m.append(" in line "+(s.left+1));   
                /* Check if the column number in the input is greater
                   or equal to zero. */
                if (s.right >= 0)                    
                    /* Add to the end of the StringBuilder error message
                       the column number of the error in the input. */
                    m.append(", column "+(s.right+1));
            }
        }
   
        /* Add to the end of the StringBuilder error message created in
           this method the message that was passed into this method. */
        m.append(" : "+message);
   
        /* Print the contents of the StringBuilder 'm', which contains
           an error message, out on a line. */
        System.err.println(m);
    }
   
    /* Change the method report_fatal_error so when it reports a fatal
       error it will display the line and column number of where the
       fatal error occurred in the input as well as the reason for the
       fatal error which is passed into the method in the object
       'message' and then exit.*/
    public void report_fatal_error(String message, Object info) {
        report_error(message, info);
        System.exit(1);
    }

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$IR_CUPCup$actions {
  private final IR_CUPCup parser;

  /** Constructor */
  CUP$IR_CUPCup$actions(IR_CUPCup parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$IR_CUPCup$do_action(
    int                        CUP$IR_CUPCup$act_num,
    java_cup.runtime.lr_parser CUP$IR_CUPCup$parser,
    java.util.Stack            CUP$IR_CUPCup$stack,
    int                        CUP$IR_CUPCup$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$IR_CUPCup$result;

      /* select the action based on the action number */
      switch (CUP$IR_CUPCup$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 12: // term ::= ID 
            {
              Integer RESULT =null;
		int ileft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).left;
		int iright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).right;
		String i = (String)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.peek()).value;
		 RESULT = i; 
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("term",4, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 11: // term ::= NUMBER 
            {
              Integer RESULT =null;
		int nleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).left;
		int nright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).right;
		Integer n = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.peek()).value;
		 RESULT = n; 
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("term",4, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 10: // term ::= LPAREN expr RPAREN 
            {
              Integer RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-1)).right;
		Integer e = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-1)).value;
		 RESULT = e; 
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("term",4, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 9: // factor ::= term 
            {
              Integer RESULT =null;
		int tleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).left;
		int tright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).right;
		Integer t = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.peek()).value;
		 RESULT = new Integer(t.intValue());                
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("factor",3, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 8: // factor ::= factor DIVIDE term 
            {
              Integer RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).right;
		Integer f = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).value;
		int tleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).left;
		int tright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).right;
		Integer t = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.peek()).value;
		 RESULT = new Integer(f.intValue() / t.intValue()); 
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("factor",3, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 7: // factor ::= factor TIMES term 
            {
              Integer RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).left;
		int fright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).right;
		Integer f = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).value;
		int tleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).left;
		int tright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).right;
		Integer t = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.peek()).value;
		 RESULT = new Integer(f.intValue() * t.intValue()); 
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("factor",3, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 6: // expr ::= factor 
            {
              Integer RESULT =null;
		int fleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).right;
		Integer f = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.peek()).value;
		 RESULT = new Integer(f.intValue());                
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("expr",2, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 5: // expr ::= expr PLUS factor 
            {
              Integer RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).right;
		Integer e = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).value;
		int fleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).left;
		int fright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).right;
		Integer f = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.peek()).value;
		 RESULT = new Integer(e.intValue() + f.intValue()); 
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("expr",2, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 4: // expr_part ::= expr NT$0 SEMICOLON 
            {
              Object RESULT =null;
              // propagate RESULT from NT$0
                RESULT = (Object) ((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-1)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).right;
		Integer e = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)).value;

              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("expr_part",1, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-2)), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 3: // NT$0 ::= 
            {
              Object RESULT =null;
		int eleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).left;
		int eright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()).right;
		Integer e = (Integer)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.peek()).value;
 System.out.println(" = " + e); 
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("NT$0",5, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 2: // expr_list ::= expr_part 
            {
              Object RESULT =null;

              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("expr_list",0, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= expr_list EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-1)).value;
		RESULT = start_val;
              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-1)), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$IR_CUPCup$parser.done_parsing();
          return CUP$IR_CUPCup$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // expr_list ::= expr_list expr_part 
            {
              Object RESULT =null;

              CUP$IR_CUPCup$result = parser.getSymbolFactory().newSymbol("expr_list",0, ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.elementAt(CUP$IR_CUPCup$top-1)), ((java_cup.runtime.Symbol)CUP$IR_CUPCup$stack.peek()), RESULT);
            }
          return CUP$IR_CUPCup$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

