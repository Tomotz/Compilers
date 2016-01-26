

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
      

class IRLexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0, 0
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\12\1\2\1\0\1\12\1\1\22\0\1\13\1\7\1\4"+
    "\1\14\4\7\1\64\1\65\2\7\1\63\1\7\1\67\1\7\12\15"+
    "\1\17\2\7\1\66\3\7\1\22\1\15\1\23\1\45\1\24\1\34"+
    "\1\60\1\15\1\46\1\55\1\15\1\40\1\25\1\50\1\51\1\21"+
    "\1\15\1\62\1\20\1\56\1\15\1\61\1\15\1\52\2\15\1\10"+
    "\1\3\1\11\1\7\1\16\1\7\1\32\1\44\1\47\1\37\1\30"+
    "\1\15\1\41\1\42\1\35\2\15\1\36\1\53\1\6\1\26\1\54"+
    "\1\15\1\31\1\57\1\5\1\43\1\27\2\15\1\33\1\15\4\7"+
    "\uff81\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\0\1\1\2\2\1\1\1\3\1\4\1\5\1\6"+
    "\1\7\1\10\15\3\1\11\1\12\1\13\1\14\1\15"+
    "\1\0\1\16\1\0\1\3\2\6\20\3\1\17\4\3"+
    "\1\20\1\21\2\3\1\22\1\23\1\3\1\24\2\3"+
    "\1\25\1\26\1\3\1\27\1\30\1\31\1\32\1\33"+
    "\1\34\3\3\1\35\1\0\4\3\1\36\1\3\1\37"+
    "\3\3\1\40\6\3\1\41\1\3\1\42\11\3\1\43"+
    "\1\3\1\44\1\3\1\45\2\3\1\46\2\3\1\47"+
    "\10\3\1\50\3\3\1\51\1\52\1\53\1\3\1\54"+
    "\2\3\1\55\1\56";

  private static int [] zzUnpackAction() {
    int [] result = new int[141];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\70\0\160\0\70\0\250\0\340\0\70\0\70"+
    "\0\u0118\0\u0150\0\70\0\u0188\0\u01c0\0\u01f8\0\u0230\0\u0268"+
    "\0\u02a0\0\u02d8\0\u0310\0\u0348\0\u0380\0\u03b8\0\u03f0\0\u0428"+
    "\0\70\0\70\0\70\0\70\0\70\0\u0460\0\70\0\250"+
    "\0\u0498\0\u04d0\0\70\0\u0508\0\u0540\0\u0578\0\u05b0\0\u05e8"+
    "\0\u0620\0\u0658\0\u0690\0\u06c8\0\u0700\0\u0738\0\u0770\0\u07a8"+
    "\0\u07e0\0\u0818\0\u0850\0\u0498\0\u0888\0\u08c0\0\u08f8\0\u0930"+
    "\0\70\0\u0968\0\u09a0\0\u09d8\0\u0498\0\u0498\0\u0a10\0\u0498"+
    "\0\u0a48\0\u0a80\0\u0498\0\u0498\0\u0ab8\0\u0498\0\u0498\0\u0498"+
    "\0\u0498\0\u0498\0\u0498\0\u0af0\0\u0b28\0\u0b60\0\70\0\u0968"+
    "\0\u0b98\0\u0bd0\0\u0c08\0\u0c40\0\u0c78\0\u0cb0\0\u0ce8\0\u0d20"+
    "\0\u0d58\0\u0d90\0\u0498\0\u0dc8\0\u0e00\0\u0e38\0\u0e70\0\u0ea8"+
    "\0\u0ee0\0\u0f18\0\u0f50\0\u0f88\0\u0fc0\0\u0ff8\0\u1030\0\u1068"+
    "\0\u10a0\0\u10d8\0\u1110\0\u1148\0\u1180\0\u0498\0\u11b8\0\u0498"+
    "\0\u11f0\0\u0498\0\u1228\0\u1260\0\u0498\0\u1298\0\u12d0\0\u0498"+
    "\0\u1308\0\u1340\0\u1378\0\u13b0\0\u13e8\0\u1420\0\u1458\0\u1490"+
    "\0\u0498\0\u14c8\0\u1500\0\u1538\0\u0498\0\u0498\0\u0498\0\u1570"+
    "\0\u0498\0\u15a8\0\u15e0\0\u0498\0\u0498";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[141];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\2\1\3\1\4\1\2\1\5\2\6\1\2\1\7"+
    "\1\10\2\4\1\11\1\6\1\12\1\13\1\14\1\6"+
    "\1\15\1\16\1\6\1\17\12\6\1\20\4\6\1\21"+
    "\1\22\1\6\1\23\1\24\1\25\2\6\1\26\3\6"+
    "\1\27\1\30\1\31\1\32\1\33\1\34\1\35\72\0"+
    "\1\4\70\0\1\36\1\37\5\40\1\0\55\40\5\0"+
    "\2\41\6\0\2\41\1\0\43\41\5\0\1\11\1\42"+
    "\1\43\65\11\5\0\2\44\6\0\2\44\1\0\43\44"+
    "\12\0\1\45\1\41\6\0\2\41\1\0\1\41\1\46"+
    "\21\41\1\47\17\41\12\0\1\41\1\50\6\0\2\41"+
    "\1\0\11\41\1\51\5\41\1\52\23\41\12\0\2\41"+
    "\6\0\2\41\1\0\6\41\1\53\34\41\12\0\2\41"+
    "\6\0\2\41\1\0\6\41\1\54\14\41\1\55\17\41"+
    "\12\0\2\41\6\0\2\41\1\0\15\41\1\56\25\41"+
    "\12\0\2\41\6\0\2\41\1\0\10\41\1\57\4\41"+
    "\1\60\25\41\12\0\1\41\1\61\6\0\2\41\1\0"+
    "\43\41\12\0\2\41\6\0\2\41\1\0\6\41\1\62"+
    "\1\41\1\63\32\41\12\0\2\41\6\0\2\41\1\0"+
    "\11\41\1\64\31\41\12\0\2\41\6\0\2\41\1\0"+
    "\6\41\1\65\34\41\12\0\2\41\6\0\2\41\1\0"+
    "\23\41\1\66\17\41\12\0\2\41\6\0\2\41\1\0"+
    "\15\41\1\67\25\41\12\0\2\41\6\0\2\41\1\0"+
    "\10\41\1\70\32\41\10\0\4\40\66\0\2\41\6\0"+
    "\2\41\1\71\43\41\7\0\1\43\72\0\2\44\6\0"+
    "\2\44\1\72\43\44\12\0\2\41\6\0\2\41\1\71"+
    "\12\41\1\73\30\41\12\0\2\41\6\0\2\41\1\71"+
    "\2\41\1\74\40\41\12\0\2\41\6\0\2\41\1\71"+
    "\24\41\1\75\16\41\12\0\2\41\6\0\2\41\1\71"+
    "\17\41\1\76\23\41\12\0\2\41\6\0\2\41\1\71"+
    "\11\41\1\77\31\41\12\0\2\41\6\0\2\41\1\71"+
    "\17\41\1\100\23\41\12\0\2\41\6\0\2\41\1\71"+
    "\33\41\1\101\7\41\12\0\2\41\6\0\2\41\1\71"+
    "\7\41\1\102\7\41\1\103\23\41\12\0\2\41\6\0"+
    "\2\41\1\71\16\41\1\104\24\41\12\0\2\41\6\0"+
    "\2\41\1\71\24\41\1\105\16\41\12\0\2\41\6\0"+
    "\2\41\1\71\27\41\1\106\13\41\12\0\2\41\6\0"+
    "\2\41\1\71\7\41\1\107\33\41\12\0\2\41\6\0"+
    "\2\41\1\71\27\41\1\110\13\41\12\0\1\111\1\41"+
    "\6\0\2\41\1\71\43\41\12\0\2\41\6\0\2\41"+
    "\1\71\21\41\1\112\21\41\12\0\2\41\6\0\2\41"+
    "\1\71\11\41\1\113\31\41\12\0\2\41\6\0\2\41"+
    "\1\71\33\41\1\114\7\41\12\0\2\41\6\0\2\41"+
    "\1\71\11\41\1\115\31\41\12\0\1\116\1\41\6\0"+
    "\2\41\1\71\43\41\15\0\1\117\1\0\2\120\61\0"+
    "\1\121\1\41\6\0\2\41\1\71\43\41\12\0\2\41"+
    "\6\0\2\41\1\71\3\41\1\122\37\41\12\0\2\41"+
    "\6\0\2\41\1\71\12\41\1\123\30\41\12\0\2\41"+
    "\6\0\2\41\1\71\34\41\1\124\6\41\12\0\2\41"+
    "\6\0\2\41\1\71\10\41\1\125\32\41\12\0\2\41"+
    "\6\0\2\41\1\71\11\41\1\126\31\41\12\0\2\41"+
    "\6\0\2\41\1\71\34\41\1\127\6\41\12\0\1\130"+
    "\1\41\6\0\2\41\1\71\43\41\12\0\2\41\6\0"+
    "\2\41\1\71\23\41\1\131\17\41\12\0\2\41\6\0"+
    "\2\41\1\71\15\41\1\132\25\41\12\0\2\41\6\0"+
    "\2\41\1\71\4\41\1\133\36\41\12\0\2\41\6\0"+
    "\2\41\1\71\13\41\1\134\27\41\12\0\2\41\6\0"+
    "\2\41\1\71\12\41\1\135\30\41\12\0\2\41\6\0"+
    "\2\41\1\71\2\41\1\136\11\41\1\137\26\41\12\0"+
    "\2\41\6\0\2\41\1\71\12\41\1\140\30\41\12\0"+
    "\2\41\6\0\2\41\1\71\14\41\1\141\3\41\1\142"+
    "\15\41\1\143\1\41\1\144\2\41\12\0\2\41\6\0"+
    "\2\41\1\71\23\41\1\145\17\41\12\0\2\41\6\0"+
    "\2\41\1\71\11\41\1\146\31\41\12\0\2\41\6\0"+
    "\2\41\1\71\27\41\1\147\13\41\12\0\2\41\6\0"+
    "\2\41\1\71\20\41\1\150\22\41\12\0\2\41\6\0"+
    "\2\41\1\71\11\41\1\151\31\41\12\0\2\41\6\0"+
    "\2\41\1\71\11\41\1\152\31\41\12\0\2\41\6\0"+
    "\2\41\1\71\15\41\1\153\25\41\12\0\2\41\6\0"+
    "\2\41\1\71\11\41\1\154\31\41\12\0\2\41\6\0"+
    "\2\41\1\71\12\41\1\155\30\41\12\0\2\41\6\0"+
    "\2\41\1\71\4\41\1\156\36\41\12\0\2\41\6\0"+
    "\2\41\1\71\11\41\1\157\31\41\12\0\2\41\6\0"+
    "\2\41\1\71\4\41\1\160\36\41\12\0\2\41\6\0"+
    "\2\41\1\71\12\41\1\161\30\41\12\0\1\41\1\162"+
    "\6\0\2\41\1\71\43\41\12\0\2\41\6\0\2\41"+
    "\1\71\3\41\1\163\37\41\12\0\2\41\6\0\2\41"+
    "\1\71\10\41\1\164\32\41\12\0\2\41\6\0\2\41"+
    "\1\71\10\41\1\165\32\41\12\0\2\41\6\0\2\41"+
    "\1\71\11\41\1\166\31\41\12\0\2\41\6\0\2\41"+
    "\1\71\10\41\1\167\32\41\12\0\2\41\6\0\2\41"+
    "\1\71\13\41\1\170\27\41\12\0\2\41\6\0\2\41"+
    "\1\71\16\41\1\171\24\41\12\0\2\41\6\0\2\41"+
    "\1\71\23\41\1\172\17\41\12\0\2\41\6\0\2\41"+
    "\1\71\16\41\1\173\24\41\12\0\2\41\6\0\2\41"+
    "\1\71\12\41\1\174\30\41\12\0\1\41\1\175\6\0"+
    "\2\41\1\71\43\41\12\0\2\41\6\0\2\41\1\71"+
    "\12\41\1\176\30\41\12\0\2\41\6\0\2\41\1\71"+
    "\16\41\1\177\24\41\12\0\2\41\6\0\2\41\1\71"+
    "\37\41\1\200\3\41\12\0\2\41\6\0\2\41\1\71"+
    "\10\41\1\201\32\41\12\0\2\41\6\0\2\41\1\71"+
    "\3\41\1\202\37\41\12\0\2\41\6\0\2\41\1\71"+
    "\16\41\1\203\24\41\12\0\2\41\6\0\2\41\1\71"+
    "\21\41\1\204\21\41\12\0\2\41\6\0\2\41\1\71"+
    "\13\41\1\205\27\41\12\0\2\41\6\0\2\41\1\71"+
    "\17\41\1\206\23\41\12\0\2\41\6\0\2\41\1\71"+
    "\10\41\1\207\32\41\12\0\2\41\6\0\2\41\1\71"+
    "\12\41\1\210\30\41\12\0\2\41\6\0\2\41\1\71"+
    "\16\41\1\211\24\41\12\0\1\212\1\41\6\0\2\41"+
    "\1\71\43\41\12\0\2\41\6\0\2\41\1\71\16\41"+
    "\1\213\24\41\12\0\2\41\6\0\2\41\1\71\22\41"+
    "\1\214\20\41\12\0\2\41\6\0\2\41\1\71\16\41"+
    "\1\215\24\41\5\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5656];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\11\1\1\1\11\2\1\2\11\2\1\1\11"+
    "\15\1\5\11\1\0\1\11\1\0\2\1\1\11\25\1"+
    "\1\11\25\1\1\11\1\0\75\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[141];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */
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


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  IRLexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  IRLexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 162) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 31: 
          { return printToken("Jump",IRsym.JUMP);
          }
        case 47: break;
        case 28: 
          { return printToken("Xor",IRsym.XOR);
          }
        case 48: break;
        case 29: 
          { return printToken(new String(yytext()),	IRsym.DVLABEL);
          }
        case 49: break;
        case 40: 
          { return printToken("JumpTrue",IRsym.JUMPTRUE);
          }
        case 50: break;
        case 38: 
          { return printToken("Compare",IRsym.COMPARE);
          }
        case 51: break;
        case 12: 
          { return printToken("ASSIGN", IRsym.ASSIGN);
          }
        case 52: break;
        case 16: 
          { return printToken(new String(yytext()), IRsym.STRINGLABEL);
          }
        case 53: break;
        case 27: 
          { return printToken("Neg",IRsym.NEG);
          }
        case 54: break;
        case 34: 
          { return printToken("JumpG",IRsym.JUMPG);
          }
        case 55: break;
        case 14: 
          { return printToken(new String(yytext()), IRsym.QUOTE);
          }
        case 56: break;
        case 7: 
          { return printToken("_",IRsym.UNDER);
          }
        case 57: break;
        case 2: 
          { /* just skip what was found, do nothing */
          }
        case 58: break;
        case 5: 
          { return printToken("RB",IRsym.RB);
          }
        case 59: break;
        case 1: 
          { throw new RuntimeException("Error: Invalid token: "+new String(yytext())+" at line " +Integer.toString(yyline+1));
          }
        case 60: break;
        case 6: 
          { return printToken(new String(yytext()), IRsym.COMMENT);
          }
        case 61: break;
        case 45: 
          { return printToken("ArrayLength",IRsym.ARRAYLENGTH);
          }
        case 62: break;
        case 46: 
          { return printToken("VirtualCall",IRsym.VIRTUALCALL);
          }
        case 63: break;
        case 26: 
          { return printToken("Not",IRsym.NOT);
          }
        case 64: break;
        case 20: 
          { return printToken("Add",IRsym.ADD);
          }
        case 65: break;
        case 41: 
          { return printToken("MoveArray", IRsym.MOVEARRAY);
          }
        case 66: break;
        case 33: 
          { return printToken("JumpL",IRsym.JUMPL);
          }
        case 67: break;
        case 39: 
          { return printToken("Library",IRsym.LIBRARY);
          }
        case 68: break;
        case 36: 
          { return printToken("JumpGE",IRsym.JUMPGE);
          }
        case 69: break;
        case 4: 
          { return printToken("LB",IRsym.LB);
          }
        case 70: break;
        case 11: 
          { return printToken("RP",IRsym.RP);
          }
        case 71: break;
        case 43: 
          { return printToken("JumpFalse",IRsym.JUMPFALSE);
          }
        case 72: break;
        case 25: 
          { return printToken("Inc",IRsym.INC);
          }
        case 73: break;
        case 9: 
          { return printToken(",",IRsym.COMMA);
          }
        case 74: break;
        case 8: 
          { return printToken(":",IRsym.COLON);
          }
        case 75: break;
        case 13: 
          { return printToken("DOT",IRsym.DOT);
          }
        case 76: break;
        case 24: 
          { return printToken("Div",IRsym.DIV);
          }
        case 77: break;
        case 42: 
          { return printToken("MoveField",IRsym.MOVEFIELD);
          }
        case 78: break;
        case 19: 
          { return printToken("And",IRsym.AND);
          }
        case 79: break;
        case 15: 
          { return printToken("Or",IRsym.OR);
          }
        case 80: break;
        case 10: 
          { return printToken("LP",IRsym.LP);
          }
        case 81: break;
        case 35: 
          { return printToken("JumpLE",IRsym.JUMPLE);
          }
        case 82: break;
        case 37: 
          { return printToken("Return",IRsym.RETURN);
          }
        case 83: break;
        case 17: 
          { return printToken(new String(yytext()),	IRsym.LABEL);
          }
        case 84: break;
        case 44: 
          { return printToken("StaticCall",IRsym.STATICCALL);
          }
        case 85: break;
        case 23: 
          { return printToken("Dec",IRsym.DEC);
          }
        case 86: break;
        case 32: 
          { return printToken("SPACE", IRsym.SPACE);
          }
        case 87: break;
        case 21: 
          { return printToken("Mod",IRsym.MOD);
          }
        case 88: break;
        case 30: 
          { return printToken("Move", IRsym.MOVE);
          }
        case 89: break;
        case 18: 
          { return printToken("Sub",IRsym.SUB);
          }
        case 90: break;
        case 22: 
          { return printToken("Mul",IRsym.MUL);
          }
        case 91: break;
        case 3: 
          { return printToken(new String(yytext()), IRsym.WORD);
          }
        case 92: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
            switch (zzLexicalState) {
            case YYINITIAL: {
              return printToken("EOF", IRsym.EOF, new String(yytext()));
            }
            case 142: break;
            default:
              { return new java_cup.runtime.Symbol(sym.EOF); }
            }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
