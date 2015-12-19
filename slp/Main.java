package slp;

import java.io.*;

import java_cup.runtime.*;

/** The entry point of the SLP (Straight Line Program) application.
 *
 */
public class Main {
	private static boolean printtokens = false;
	
	/** Reads an SLP and pretty-prints it.
	 * 
	 * @param args Should be the name of the file containing an SLP.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		try {
			String libic_path = "libic.sig";
			if (args.length == 0) {
				System.out.println("Error: Missing input file argument!");
				printUsage();
				System.exit(-1);
			}
			if (args.length >= 3) {
				if (args[1].equals("-L")) {
					libic_path = args[2];
				}
				else {
					printUsage();
					System.exit(-1);
				}
				if (args.length >= 4)
				{
					if (args[3].equals("-printtokens")) {
						printtokens = true;
					}
					else {
						printUsage();
						System.exit(-1);
					}
				}
			}
			
			// Parse the input file
			FileReader txtFile = new FileReader(args[0]);
			Lexer scanner = new Lexer(txtFile);
			Parser parser = new Parser(scanner);
			parser.printTokens = printtokens;
			
			Symbol parseSymbol = parser.parse();
			System.out.println("Parsed " + args[0] + " successfully!");
			ASTRoot root = (ASTRoot) parseSymbol.value;
			
			
			// Pretty-print the program to System.out
			PrettyPrinter printer = new PrettyPrinter(root);
			printer.print();
			
			// Interpret the program
			
			ICEvaluator evaluator = new ICEvaluator(root);
			evaluator.evaluate();
			
			System.out.print(IR.str_table+IR.dispatch_tables+IR.code);
		} catch (FileNotFoundException e) {//(Exception e) {
			System.out.print(e);
		}
	}
	
	/** Prints usage information about this application to System.out
	 */
	public static void printUsage() {
		System.out.println("Usage: <slp file> [-L <libic file> [-printtokens]]");
	}
}