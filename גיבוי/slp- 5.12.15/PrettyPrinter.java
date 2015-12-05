package slp;

import slp.ASTFormalList.Formal;

/** Pretty-prints an SLP AST.
 */
public class PrettyPrinter implements Visitor {
	protected final ASTNode root;

	/** Constructs a printin visitor from an AST.
	 * 
	 * @param root The root of the AST.
	 */
	public PrettyPrinter(ASTNode root) {
		this.root = root;
	}

	/** Prints the AST with the given root.
	 */
	public void print() {
		root.accept(this);
	}
	
	public void visit(ASTStmtList stmts) {
		for (ASTStmt s : stmts.statements) {
			s.accept(this);
			System.out.println();
		}
	}

	public void visit(ASTStmt stmt) {
		throw new UnsupportedOperationException("Unexpected visit of Stmt abstract class");
	}
	
	
	public void visit(ASTAssignStmt stmt) {
		stmt.varExpr.accept(this);
		System.out.print("=");
		stmt.rhs.accept(this);
		System.out.print(";");
	}
	
	public void visit(ASTExpr expr) {
		throw new UnsupportedOperationException("Unexpected visit of Expr abstract class");
	}	
	
	
	public void visit(ASTVarExpr expr) {
		System.out.print(expr.name);
	}
	
	public void visit(ASTNumberExpr expr) {
		System.out.print(expr.value);
	}
	
	public void visit(ASTUnaryOpExpr expr) {
		System.out.print(expr.op);
		expr.operand.accept(this);
	}
	
	public void visit(ASTBinaryOpExpr expr) {
		expr.lhs.accept(this);
		System.out.print(expr.op);
		expr.rhs.accept(this);
	}

	@Override
	public void visit(ASTRoot node) {
		if (node.child != null)
		{
			//System.out.println(node.name);
			node.child.accept(this);
		}
		
	}

	@Override
	public void visit(ASTfmList fmList) {
		for (ASTNode n : fmList.lst) {
			n.accept(this);
			System.out.println();
		}
	}
	@Override
	public void visit(ASTClassList classes) {
		for (ASTClassDecl n : classes.lst) {
			n.accept(this);
			System.out.println();
		}
	}

	@Override
	public void visit(ASTClassDecl cls) {
		System.out.print("decleration of class: ");
		System.out.print(cls.class_id + "\n");
		cls.extend.accept(this);
		cls.fieldmeths.accept(this);
		
	}

	@Override
	public void visit(ASTExtend node) {
		if (node.name != "")
		{
			System.out.print("extend ");
			System.out.print(node.name);
		}
	}

	@Override
	public void visit(ASTField astField) {
		System.out.print("decleration of fields: ");
		astField.ids.accept(this);
		System.out.print("; of type: ");
		System.out.print(astField.type);
	}

	@Override
	public void visit(ASTIdList astIdList) {
		int i=1;
		for (String n : astIdList.lst) {
			System.out.print(n);		
			if (i<astIdList.lst.size())
				System.out.print(", ");
			++i;
				
		}
	}

	@Override
	public void visit(ASTMethod astMethod) {
		if (astMethod.isStatic)
			System.out.print("decleration of static method: ");
		else
			System.out.print("decleration of virtual method: ");
		System.out.print(astMethod.id);
		System.out.print("; of type: ");
		System.out.println(astMethod.type);
		astMethod.formals.accept(this);
		astMethod.stmts.accept(this);
	}

	@Override
	public void visit(ASTStatType astStatType) {
		/*empty*/
		
	}

	@Override
	public void visit(ASTFormalList formals) {
		for (Formal f : formals.lst) {
			System.out.print("Parameter: ");
			System.out.print(f.id);
			System.out.print("; of type: ");
			System.out.println(f.type);
			
		}
		
	}

}