/* The following code was generated by JFlex 1.4.3 on 14:20 28/11/15 */

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
      

class Lexer implements java_cup.runtime.Scanner {

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
    "\11\0\1\12\1\2\1\0\1\12\1\1\22\0\1\13\1\60\1\4"+
    "\2\7\1\64\1\52\1\7\1\53\1\67\1\22\1\65\1\35\1\63"+
    "\1\40\1\21\1\14\11\15\1\7\1\70\1\62\1\23\1\50\2\7"+
    "\1\44\3\17\1\47\1\43\5\17\1\45\5\17\1\72\1\46\1\71"+
    "\1\73\5\17\1\10\1\3\1\11\1\7\1\20\1\7\1\30\1\24"+
    "\1\33\1\42\1\27\1\51\1\55\1\56\1\36\1\16\1\32\1\26"+
    "\1\16\1\6\1\25\2\16\1\31\1\34\1\5\1\37\1\74\1\57"+
    "\1\41\2\16\1\54\1\61\1\66\1\7\uff81\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\1\1\1\2\2\3\1\2\2\4\1\5\1\6\2\1"+
    "\1\4\1\7\1\10\1\11\1\12\6\4\1\13\1\4"+
    "\1\14\1\7\1\15\1\2\1\16\1\17\1\4\1\20"+
    "\1\2\1\21\1\22\1\23\1\24\1\25\1\26\1\27"+
    "\1\7\1\4\1\0\1\30\1\0\3\4\1\2\1\31"+
    "\1\32\1\33\1\34\12\4\1\35\1\7\1\36\1\37"+
    "\1\4\1\40\1\41\1\42\1\7\2\4\1\43\1\4"+
    "\2\31\2\0\12\4\1\44\1\7\1\4\1\7\1\4"+
    "\1\45\1\46\3\4\1\47\6\4\1\7\1\4\1\50"+
    "\1\51\1\4\1\52\4\4\1\53\2\4\1\54\1\55"+
    "\1\4\1\56\1\4\1\57\1\4\1\60\1\61\1\62"+
    "\1\63\1\4\1\64";

  private static int [] zzUnpackAction() {
    int [] result = new int[133];
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
    "\0\0\0\75\0\172\0\75\0\267\0\364\0\u0131\0\75"+
    "\0\75\0\u016e\0\u01ab\0\u01e8\0\u0225\0\u0262\0\u029f\0\u02dc"+
    "\0\u0319\0\u0356\0\u0393\0\u03d0\0\u040d\0\u044a\0\75\0\u0487"+
    "\0\75\0\u04c4\0\u0501\0\u053e\0\75\0\75\0\u057b\0\u05b8"+
    "\0\u05f5\0\u0632\0\75\0\75\0\75\0\75\0\75\0\75"+
    "\0\u066f\0\u06ac\0\u06e9\0\75\0\267\0\u0726\0\u0763\0\u07a0"+
    "\0\u07dd\0\u081a\0\u0857\0\75\0\75\0\u0894\0\u08d1\0\u090e"+
    "\0\u094b\0\u0988\0\u09c5\0\u0a02\0\u0a3f\0\u0a7c\0\u0ab9\0\u01e8"+
    "\0\u0af6\0\75\0\75\0\u0b33\0\75\0\75\0\75\0\u0b70"+
    "\0\u0bad\0\u0bea\0\u01e8\0\u0c27\0\u0c64\0\75\0\u0857\0\u0ca1"+
    "\0\u0cde\0\u0d1b\0\u0d58\0\u0d95\0\u0dd2\0\u0e0f\0\u0e4c\0\u0e89"+
    "\0\u0ec6\0\u0f03\0\u01e8\0\u0f40\0\u0f7d\0\u0fba\0\u0ff7\0\u01e8"+
    "\0\u01e8\0\u1034\0\u1071\0\u10ae\0\u01e8\0\u10eb\0\u1128\0\u1165"+
    "\0\u11a2\0\u11df\0\u121c\0\u1259\0\u1296\0\u0225\0\u01e8\0\u12d3"+
    "\0\u01e8\0\u1310\0\u134d\0\u138a\0\u13c7\0\u01e8\0\u1404\0\u1441"+
    "\0\u0225\0\u01e8\0\u147e\0\u01e8\0\u14bb\0\u01e8\0\u14f8\0\u01e8"+
    "\0\u01e8\0\u01e8\0\u01e8\0\u1535\0\u01e8";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[133];
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
    "\1\2\1\3\1\4\1\2\1\5\1\6\1\7\1\2"+
    "\1\10\1\11\2\4\1\12\1\13\1\14\1\15\1\2"+
    "\1\16\1\17\1\20\1\21\1\14\1\22\1\23\1\14"+
    "\1\24\1\14\1\25\1\26\1\27\1\30\1\14\1\31"+
    "\2\14\1\32\4\15\1\33\1\14\1\34\1\35\1\36"+
    "\2\14\1\37\1\40\1\41\1\42\1\43\1\44\1\45"+
    "\1\46\1\47\1\50\1\51\2\15\1\52\77\0\1\4"+
    "\75\0\1\53\1\54\5\55\1\0\62\55\5\0\2\14"+
    "\5\0\5\14\3\0\11\14\1\0\2\14\1\0\7\14"+
    "\1\0\1\14\3\0\1\14\1\56\1\14\11\0\4\14"+
    "\5\0\2\14\5\0\5\14\3\0\3\14\1\57\5\14"+
    "\1\0\1\14\1\60\1\0\7\14\1\0\1\14\3\0"+
    "\3\14\11\0\4\14\14\0\1\12\1\61\73\0\2\13"+
    "\64\0\2\14\5\0\5\14\3\0\11\14\1\0\2\14"+
    "\1\0\7\14\1\0\1\14\3\0\3\14\11\0\4\14"+
    "\5\0\2\15\5\0\5\15\3\0\11\15\1\0\2\15"+
    "\1\0\7\15\1\0\1\15\3\0\3\15\11\0\4\15"+
    "\21\0\1\62\1\63\73\0\1\64\76\0\1\65\56\0"+
    "\2\14\5\0\5\14\3\0\1\14\1\66\3\14\1\67"+
    "\3\14\1\0\2\14\1\0\7\14\1\0\1\14\3\0"+
    "\3\14\11\0\4\14\5\0\2\14\5\0\5\14\3\0"+
    "\3\14\1\70\5\14\1\0\2\14\1\0\7\14\1\0"+
    "\1\14\3\0\3\14\11\0\4\14\5\0\2\14\5\0"+
    "\5\14\3\0\2\14\1\71\6\14\1\0\2\14\1\0"+
    "\1\72\6\14\1\0\1\14\3\0\3\14\11\0\4\14"+
    "\5\0\2\14\5\0\5\14\3\0\3\14\1\73\5\14"+
    "\1\0\2\14\1\0\7\14\1\0\1\14\3\0\3\14"+
    "\11\0\4\14\5\0\2\14\5\0\5\14\3\0\1\14"+
    "\1\74\1\75\6\14\1\0\2\14\1\0\7\14\1\0"+
    "\1\14\3\0\3\14\11\0\4\14\5\0\1\76\1\14"+
    "\5\0\5\14\3\0\11\14\1\0\2\14\1\0\7\14"+
    "\1\0\1\14\3\0\3\14\11\0\4\14\5\0\1\14"+
    "\1\77\5\0\5\14\3\0\11\14\1\0\2\14\1\0"+
    "\7\14\1\0\1\100\3\0\3\14\11\0\4\14\5\0"+
    "\2\15\5\0\5\15\3\0\11\15\1\0\2\15\1\0"+
    "\3\15\1\101\3\15\1\0\1\15\3\0\3\15\11\0"+
    "\4\15\23\0\1\102\123\0\1\103\27\0\2\14\5\0"+
    "\5\14\3\0\11\14\1\0\2\14\1\0\7\14\1\0"+
    "\1\14\3\0\1\14\1\104\1\14\11\0\4\14\23\0"+
    "\1\105\132\0\1\106\36\0\1\107\56\0\2\15\5\0"+
    "\5\15\3\0\11\15\1\0\2\15\1\0\7\15\1\0"+
    "\1\15\3\0\3\15\11\0\1\15\1\110\2\15\5\0"+
    "\2\14\5\0\5\14\3\0\1\14\1\111\7\14\1\0"+
    "\2\14\1\0\7\14\1\0\1\14\3\0\3\14\11\0"+
    "\4\14\3\0\4\55\73\0\2\14\5\0\5\14\3\0"+
    "\11\14\1\0\1\112\1\14\1\0\7\14\1\0\1\14"+
    "\3\0\3\14\11\0\4\14\5\0\2\14\5\0\5\14"+
    "\3\0\11\14\1\0\2\14\1\0\7\14\1\0\1\14"+
    "\3\0\2\14\1\113\11\0\4\14\5\0\2\14\5\0"+
    "\5\14\3\0\2\14\1\114\6\14\1\0\2\14\1\0"+
    "\7\14\1\0\1\14\3\0\3\14\11\0\4\14\15\0"+
    "\1\61\57\0\1\62\1\115\1\116\72\62\22\117\1\120"+
    "\52\117\5\0\2\14\5\0\5\14\3\0\1\14\1\121"+
    "\7\14\1\0\2\14\1\0\7\14\1\0\1\14\3\0"+
    "\3\14\11\0\4\14\5\0\2\14\5\0\5\14\3\0"+
    "\3\14\1\122\5\14\1\0\2\14\1\0\7\14\1\0"+
    "\1\14\3\0\3\14\11\0\4\14\5\0\1\14\1\123"+
    "\5\0\5\14\3\0\11\14\1\0\2\14\1\0\7\14"+
    "\1\0\1\14\3\0\3\14\11\0\4\14\5\0\2\14"+
    "\5\0\5\14\3\0\10\14\1\124\1\0\2\14\1\0"+
    "\7\14\1\0\1\14\3\0\3\14\11\0\4\14\5\0"+
    "\1\125\1\14\5\0\5\14\3\0\11\14\1\0\2\14"+
    "\1\0\7\14\1\0\1\14\3\0\3\14\11\0\4\14"+
    "\5\0\1\126\1\14\5\0\5\14\3\0\11\14\1\0"+
    "\2\14\1\0\7\14\1\0\1\14\3\0\3\14\11\0"+
    "\4\14\5\0\1\14\1\127\5\0\5\14\3\0\11\14"+
    "\1\0\2\14\1\0\7\14\1\0\1\14\3\0\3\14"+
    "\11\0\4\14\5\0\2\14\5\0\5\14\3\0\4\14"+
    "\1\130\4\14\1\0\2\14\1\0\7\14\1\0\1\14"+
    "\3\0\3\14\11\0\4\14\5\0\2\14\5\0\5\14"+
    "\3\0\4\14\1\131\1\132\3\14\1\0\2\14\1\0"+
    "\7\14\1\0\1\14\3\0\3\14\11\0\4\14\5\0"+
    "\1\133\1\14\5\0\5\14\3\0\11\14\1\0\2\14"+
    "\1\0\7\14\1\0\1\14\3\0\3\14\11\0\4\14"+
    "\5\0\2\15\5\0\5\15\3\0\11\15\1\0\2\15"+
    "\1\0\4\15\1\134\2\15\1\0\1\15\3\0\3\15"+
    "\11\0\4\15\5\0\2\14\5\0\5\14\3\0\11\14"+
    "\1\0\1\135\1\14\1\0\7\14\1\0\1\14\3\0"+
    "\3\14\11\0\4\14\5\0\2\15\5\0\5\15\3\0"+
    "\11\15\1\0\2\15\1\0\7\15\1\0\1\15\3\0"+
    "\3\15\11\0\2\15\1\136\1\15\5\0\2\14\5\0"+
    "\5\14\3\0\11\14\1\0\1\137\1\14\1\0\7\14"+
    "\1\0\1\14\3\0\3\14\11\0\4\14\5\0\2\14"+
    "\5\0\5\14\3\0\10\14\1\140\1\0\2\14\1\0"+
    "\7\14\1\0\1\14\3\0\3\14\11\0\4\14\5\0"+
    "\2\14\5\0\5\14\3\0\2\14\1\141\6\14\1\0"+
    "\2\14\1\0\7\14\1\0\1\14\3\0\3\14\11\0"+
    "\4\14\2\0\1\116\72\0\21\117\1\116\1\120\52\117"+
    "\5\0\2\14\5\0\5\14\3\0\2\14\1\142\6\14"+
    "\1\0\2\14\1\0\7\14\1\0\1\14\3\0\3\14"+
    "\11\0\4\14\5\0\2\14\5\0\5\14\3\0\4\14"+
    "\1\143\4\14\1\0\2\14\1\0\7\14\1\0\1\14"+
    "\3\0\3\14\11\0\4\14\5\0\2\14\5\0\5\14"+
    "\3\0\11\14\1\0\2\14\1\0\7\14\1\0\1\14"+
    "\3\0\1\144\2\14\11\0\4\14\5\0\2\14\5\0"+
    "\5\14\3\0\3\14\1\145\5\14\1\0\2\14\1\0"+
    "\7\14\1\0\1\14\3\0\3\14\11\0\4\14\5\0"+
    "\2\14\5\0\5\14\3\0\3\14\1\146\5\14\1\0"+
    "\2\14\1\0\7\14\1\0\1\14\3\0\3\14\11\0"+
    "\4\14\5\0\2\14\5\0\5\14\3\0\11\14\1\0"+
    "\1\14\1\147\1\0\7\14\1\0\1\14\3\0\3\14"+
    "\11\0\4\14\5\0\1\150\1\14\5\0\5\14\3\0"+
    "\11\14\1\0\2\14\1\0\7\14\1\0\1\14\3\0"+
    "\3\14\11\0\4\14\5\0\2\14\5\0\5\14\3\0"+
    "\10\14\1\151\1\0\2\14\1\0\7\14\1\0\1\14"+
    "\3\0\3\14\11\0\4\14\5\0\1\152\1\14\5\0"+
    "\5\14\3\0\11\14\1\0\2\14\1\0\7\14\1\0"+
    "\1\14\3\0\3\14\11\0\4\14\5\0\2\14\5\0"+
    "\5\14\3\0\11\14\1\0\1\153\1\14\1\0\7\14"+
    "\1\0\1\14\3\0\3\14\11\0\4\14\5\0\2\15"+
    "\5\0\5\15\3\0\11\15\1\0\2\15\1\0\5\15"+
    "\1\154\1\15\1\0\1\15\3\0\3\15\11\0\4\15"+
    "\5\0\2\14\5\0\5\14\3\0\2\14\1\155\6\14"+
    "\1\0\2\14\1\0\7\14\1\0\1\14\3\0\3\14"+
    "\11\0\4\14\5\0\2\15\5\0\5\15\3\0\11\15"+
    "\1\0\2\15\1\0\6\15\1\156\1\0\1\15\3\0"+
    "\3\15\11\0\4\15\5\0\2\14\5\0\5\14\3\0"+
    "\11\14\1\0\2\14\1\0\1\14\1\157\5\14\1\0"+
    "\1\14\3\0\3\14\11\0\4\14\5\0\2\14\5\0"+
    "\5\14\3\0\3\14\1\160\5\14\1\0\2\14\1\0"+
    "\7\14\1\0\1\14\3\0\3\14\11\0\4\14\5\0"+
    "\2\14\5\0\5\14\3\0\6\14\1\161\2\14\1\0"+
    "\2\14\1\0\7\14\1\0\1\14\3\0\3\14\11\0"+
    "\4\14\5\0\1\162\1\14\5\0\5\14\3\0\11\14"+
    "\1\0\2\14\1\0\7\14\1\0\1\14\3\0\3\14"+
    "\11\0\4\14\5\0\1\14\1\163\5\0\5\14\3\0"+
    "\11\14\1\0\2\14\1\0\7\14\1\0\1\14\3\0"+
    "\3\14\11\0\4\14\5\0\2\14\5\0\5\14\3\0"+
    "\5\14\1\164\3\14\1\0\2\14\1\0\7\14\1\0"+
    "\1\14\3\0\3\14\11\0\4\14\5\0\2\14\5\0"+
    "\5\14\3\0\11\14\1\0\1\165\1\14\1\0\7\14"+
    "\1\0\1\14\3\0\3\14\11\0\4\14\5\0\2\14"+
    "\5\0\5\14\3\0\10\14\1\166\1\0\2\14\1\0"+
    "\7\14\1\0\1\14\3\0\3\14\11\0\4\14\5\0"+
    "\2\14\5\0\5\14\3\0\11\14\1\0\1\167\1\14"+
    "\1\0\7\14\1\0\1\14\3\0\3\14\11\0\4\14"+
    "\5\0\1\14\1\170\5\0\5\14\3\0\11\14\1\0"+
    "\2\14\1\0\7\14\1\0\1\14\3\0\3\14\11\0"+
    "\4\14\5\0\2\15\5\0\5\15\3\0\11\15\1\0"+
    "\2\15\1\0\6\15\1\171\1\0\1\15\3\0\3\15"+
    "\11\0\4\15\5\0\2\14\5\0\5\14\3\0\3\14"+
    "\1\172\5\14\1\0\2\14\1\0\7\14\1\0\1\14"+
    "\3\0\3\14\11\0\4\14\5\0\2\14\5\0\5\14"+
    "\3\0\4\14\1\173\4\14\1\0\2\14\1\0\7\14"+
    "\1\0\1\14\3\0\3\14\11\0\4\14\5\0\2\14"+
    "\5\0\5\14\3\0\11\14\1\0\2\14\1\0\7\14"+
    "\1\0\1\14\3\0\1\14\1\174\1\14\11\0\4\14"+
    "\5\0\2\14\5\0\5\14\3\0\11\14\1\0\2\14"+
    "\1\0\1\14\1\175\5\14\1\0\1\14\3\0\3\14"+
    "\11\0\4\14\5\0\1\14\1\176\5\0\5\14\3\0"+
    "\11\14\1\0\2\14\1\0\7\14\1\0\1\14\3\0"+
    "\3\14\11\0\4\14\5\0\1\14\1\177\5\0\5\14"+
    "\3\0\11\14\1\0\2\14\1\0\7\14\1\0\1\14"+
    "\3\0\3\14\11\0\4\14\5\0\2\14\5\0\5\14"+
    "\3\0\7\14\1\200\1\14\1\0\2\14\1\0\7\14"+
    "\1\0\1\14\3\0\3\14\11\0\4\14\5\0\2\14"+
    "\5\0\5\14\3\0\11\14\1\0\2\14\1\0\7\14"+
    "\1\0\1\14\3\0\1\201\2\14\11\0\4\14\5\0"+
    "\1\14\1\202\5\0\5\14\3\0\11\14\1\0\2\14"+
    "\1\0\7\14\1\0\1\14\3\0\3\14\11\0\4\14"+
    "\5\0\2\14\5\0\5\14\3\0\10\14\1\203\1\0"+
    "\2\14\1\0\7\14\1\0\1\14\3\0\3\14\11\0"+
    "\4\14\5\0\2\14\5\0\5\14\3\0\11\14\1\0"+
    "\1\14\1\204\1\0\7\14\1\0\1\14\3\0\3\14"+
    "\11\0\4\14\5\0\2\14\5\0\5\14\3\0\3\14"+
    "\1\205\5\14\1\0\2\14\1\0\7\14\1\0\1\14"+
    "\3\0\3\14\11\0\4\14";

  private static int [] zzUnpackTrans() {
    int [] result = new int[5490];
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
    "\1\1\1\11\1\1\1\11\3\1\2\11\15\1\1\11"+
    "\1\1\1\11\3\1\2\11\4\1\6\11\2\1\1\0"+
    "\1\11\1\0\6\1\2\11\14\1\2\11\1\1\3\11"+
    "\6\1\1\11\2\0\65\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[133];
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
private void printLineNumber(){
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
  Lexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  Lexer(java.io.InputStream in) {
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
    while (i < 154) {
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
        case 25: 
          { /* skip */
          }
        case 53: break;
        case 43: 
          { return printToken("CLASS",sym.CLASS);
          }
        case 54: break;
        case 49: 
          { return printToken("STRING",sym.STRING);
          }
        case 55: break;
        case 16: 
          { return printToken("LNEG",sym.LNEG);
          }
        case 56: break;
        case 4: 
          { return printToken("ID(" + new String(yytext()) + ")", 
							sym.ID, new String(yytext()));
          }
        case 57: break;
        case 35: 
          { return printToken("NEW",sym.NEW);
          }
        case 58: break;
        case 6: 
          { return printToken("RB",sym.RB);
          }
        case 59: break;
        case 33: 
          { return printToken("LOR",sym.LOR);
          }
        case 60: break;
        case 27: 
          { throw new RuntimeException("Error: closed comment without an opening at line "+Integer.toString(yyline+1));
          }
        case 61: break;
        case 31: 
          { return printToken("LAND",sym.LAND);
          }
        case 62: break;
        case 7: 
          { return printToken("CLASS_ID(" + new String(yytext()) + ")", 
							sym.CLASS_ID, new String(yytext()));
          }
        case 63: break;
        case 21: 
          { return printToken("RCBR",sym.RCBR);
          }
        case 64: break;
        case 34: 
          { return printToken("LTE",sym.LTE);
          }
        case 65: break;
        case 17: 
          { return printToken("LT",sym.LT);
          }
        case 66: break;
        case 8: 
          { return printToken("DIVIDE",sym.DIVIDE);
          }
        case 67: break;
        case 3: 
          { /* just skip what was found, do nothing */
          }
        case 68: break;
        case 38: 
          { return printToken("NULL",sym.NULL);
          }
        case 69: break;
        case 2: 
          { throw new RuntimeException("Error: Invalid token: "+new String(yytext())+" at line " +Integer.toString(yyline+1));
          }
        case 70: break;
        case 40: 
          { return printToken("TRUE",sym.TRUE);
          }
        case 71: break;
        case 52: 
          { return printToken("CONTINUE",sym.CONTINUE);
          }
        case 72: break;
        case 15: 
          { return printToken("LCBR",sym.LCBR);
          }
        case 73: break;
        case 45: 
          { return printToken("WHILE",sym.WHILE);
          }
        case 74: break;
        case 12: 
          { return printToken("DOT",sym.DOT);
          }
        case 75: break;
        case 1: 
          { return printToken("INTEGER(" + new String(yytext()) + ")", 
							sym.INTEGER, new Integer(yytext()));
          }
        case 76: break;
        case 32: 
          { return printToken("NEQUAL",sym.NEQUAL);
          }
        case 77: break;
        case 47: 
          { return printToken("RETURN",sym.RETURN);
          }
        case 78: break;
        case 37: 
          { return printToken("THIS",sym.THIS);
          }
        case 79: break;
        case 46: 
          { return printToken("LENGTH",sym.LENGTH);
          }
        case 80: break;
        case 5: 
          { return printToken("LB",sym.LB);
          }
        case 81: break;
        case 13: 
          { return printToken("GT",sym.GT);
          }
        case 82: break;
        case 28: 
          { return printToken("EQUAL",sym.EQUAL);
          }
        case 83: break;
        case 18: 
          { return printToken("MINUS",sym.MINUS);
          }
        case 84: break;
        case 36: 
          { return printToken("INT",sym.INT);
          }
        case 85: break;
        case 20: 
          { return printToken("PLUS",sym.PLUS);
          }
        case 86: break;
        case 9: 
          { return printToken("MULTIPLY",sym.MULTIPLY);
          }
        case 87: break;
        case 48: 
          { return printToken("STATIC",sym.STATIC);
          }
        case 88: break;
        case 22: 
          { return printToken("RP",sym.RP);
          }
        case 89: break;
        case 50: 
          { return printToken("BOOLEAN", sym.BOOLEAN);
          }
        case 90: break;
        case 23: 
          { return printToken("SEMI",sym.SEMI);
          }
        case 91: break;
        case 51: 
          { return printToken("EXTENDS",sym.EXTENDS);
          }
        case 92: break;
        case 19: 
          { return printToken("MOD",sym.MOD);
          }
        case 93: break;
        case 39: 
          { return printToken("ELSE",sym.ELSE);
          }
        case 94: break;
        case 26: 
          { throw new RuntimeException("Error: unclosed comment at line "+Integer.toString(yyline+1));
          }
        case 95: break;
        case 42: 
          { return printToken("BREAK",sym.BREAK);
          }
        case 96: break;
        case 10: 
          { return printToken("ASSIGN", sym.ASSIGN);
          }
        case 97: break;
        case 24: 
          { return printToken("QUOTE(" + new String(yytext()) + ")", 
							sym.QUOTE, new String(yytext()));
          }
        case 98: break;
        case 29: 
          { return printToken("IF",sym.IF);
          }
        case 99: break;
        case 14: 
          { return printToken("LP",sym.LP);
          }
        case 100: break;
        case 30: 
          { return printToken("GTE",sym.GTE);
          }
        case 101: break;
        case 44: 
          { return printToken("FALSE",sym.FALSE);
          }
        case 102: break;
        case 41: 
          { return printToken("VOID",sym.VOID);
          }
        case 103: break;
        case 11: 
          { return printToken("COMMA",sym.COMMA);
          }
        case 104: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
            switch (zzLexicalState) {
            case YYINITIAL: {
              return printToken("EOF", sym.EOF, new String(yytext()));
            }
            case 134: break;
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
