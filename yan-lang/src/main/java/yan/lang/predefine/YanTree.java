package yan.lang.predefine;

import yan.foundation.compiler.frontend.ast.ReflectiveVisitor;
import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.compiler.frontend.semantic.v1.Scope;
import yan.foundation.compiler.frontend.semantic.v1.Symbol;
import yan.foundation.compiler.frontend.semantic.v1.Type;
import yan.foundation.compiler.frontend.semantic.v1.symbol.MethodSymbol;
import yan.foundation.compiler.frontend.semantic.v1.symbol.VarSymbol;

import java.util.List;

public abstract class YanTree extends Tree {

    /**
     * Program is the top level node of abstract parse tree.
     * <p>A {@code Program} can only contain a list of definitions, which includes
     * the definitions of class, function, and global variable.</p>
     * <p>Other statements are now allowed at the top level,</p>
     * <p>
     * Grammar:
     * <pre>
     *     Program ::= Decl*
     * </pre>
     * <p>
     * Example of a valid program:
     * <pre>{@code
     *     func add(x: int, y: int) -> int {
     *       return x + y
     *     }
     *     class Note: AbstractNote {
     *       var content: string
     *     }
     *     var a = 10
     *     func main() {
     *       var b = 10
     *     }
     * }</pre>
     */
    public static class Program extends YanTree {
        public List<YanTree> defs;

        public Scope scope;

        public Program(List<YanTree> defs) { this.defs = defs; }

    }

    public static abstract class Stmt extends YanTree {
    }

    /**
     * Variable definition must be initialized. Type of variable could be ignored
     * and compiler will infer type from init expression.
     *
     * <p>Grammar:</p>
     * <pre>
     *     'var' Id (':' Type)? '=' Expr
     * </pre>
     * <p>
     * Example:
     * <pre>
     *     var a = 10
     *     var a:int = 10
     * </pre>
     */
    public static class VarDef extends Stmt {
        public Identifier id;
        public Expr init;

        // A var type could also be a class type, so we just treat var type as identifier,
        // and check it while performing semantic analysis.
        public Identifier varType;

        public VarSymbol symbol;

        public VarDef(Identifier id, Expr init, Identifier varType) {
            this.id = id;
            this.init = init;
            this.varType = varType;
        }

    }

    /**
     * Class definition
     *
     * <p>Example: </p>
     * <pre>
     *     class Apple : Fruit {
     *         var size: int = 0
     *         func eat() {
     *
     *         }
     *     }
     * </pre>
     */
    public static class ClassDef extends YanTree {
        public Identifier id;
        public Identifier superClass;
        public List<YanTree> defs;

        public ClassDef(Identifier id, Identifier superClass, List<YanTree> defs) {
            this.id = id;
            this.superClass = superClass;
            this.defs = defs;
        }

    }

    /**
     * Method definitions include global methods and class methods
     * (static class method is not supported yet).
     * <p>Example:</p>
     * <pre>{@code
     *    func add(x:int, y:int) -> int {
     *        return x + y
     *    }
     * }</pre>
     */
    public static class FuncDef extends YanTree {
        public Identifier id;
        public Identifier retType;
        public List<VarDef> params;
        public Block body;

        public MethodSymbol symbol;

        public FuncDef(Identifier id, Identifier retType, List<VarDef> params, Block body) {
            this.id = id;
            this.retType = retType;
            this.params = params;
            this.body = body;
        }
    }

    /**
     * Return statement (expression is optional)
     * <pre>
     *     return [expr]
     * </pre>
     */
    public static class Return extends Stmt {
        public Expr expr;

        /**
         * The corresponding function of this return,
         * it should be populated by Parser or ControlStructureAnalyzer.
         */
        public FuncDef func;

        public Return(Expr expr) {
            this.expr = expr;
        }
    }

    /**
     * If statement only allow {@code if} and {@code else},
     * {@code else if} is not allowed for now.
     */
    public static class If extends Stmt {
        public Expr condition;
        public Stmt ifBody;
        public Stmt elseBody;

        public If(Expr condition, Block ifBody, Block elseBody) {
            this.condition = condition;
            this.ifBody = ifBody;
            this.elseBody = elseBody;
        }

    }

    /**
     * While statement.
     * <pre>
     *     while (condition) {
     *         body
     *     }
     * </pre>
     */
    public static class While extends Stmt {
        public Expr condition;
        public Stmt body;

        public While(Expr condition, Stmt body) {
            this.condition = condition;
            this.body = body;
        }


    }

    /**
     * Continue statement is only allowed to appear in loop body.
     */
    public static class Continue extends Stmt {
        public While attachedLoop;
    }

    /**
     * Break statement is only allowed to appear in loop body.
     */
    public static class Break extends Stmt {
        public While attachedLoop;
    }

    /**
     * Expression statement
     * <pre>
     *     expression
     * </pre>
     */
    public static class ExprStmt extends Stmt {
        public final Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }

    }

    /**
     * Block statement
     * <pre>
     *     {
     *         body
     *     }
     * </pre>
     */
    public static class Block extends Stmt {
        public final List<Stmt> stmts;

        public Block(List<Stmt> stmts) {
            this.stmts = stmts;
        }

    }

    /**
     * Empty statement is just a empty line.
     */
    public static class Empty extends Stmt {
    }

    /**
     * Super class for all expressions.
     */
    public static abstract class Expr extends YanTree {
        public Type evalType;
    }

    /**
     * We make operator a tree as well to save its location.
     */
    public static class Operator extends YanTree {
        public enum Tag {
            PLUS, MINUS, MULTI, DIV, EXP,
            ASSIGN,
            EQ, NEQ, GT, GTE, LT, LTE,
            REL_OR, REL_AND, REL_NOT;
        }

        public Tag tag;

        public Operator(Tag tag) {
            this.tag = tag;
        }
    }

    /**
     * Super class for all expressions that has a operator.
     */
    public static abstract class OperatorExpr extends Expr {
        public final Operator op;

        public OperatorExpr(Operator op) {
            this.op = op;
        }
    }

    /**
     * Unary expression
     * op expression
     */
    public static class Unary extends OperatorExpr {
        public Expr expr;

        public Unary(Operator op, Expr expr) {
            super(op);
            this.expr = expr;
        }
    }

    /**
     * Binary Expression
     * <pre>
     *     expression op expression
     * </pre>
     */
    public static final class Binary extends OperatorExpr {
        public final Expr left;
        public final Expr right;

        public Binary(Operator op, Expr left, Expr right) {
            super(op);
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Type cast expression
     * <pre>
     *     (type)expression
     * </pre>
     */
    public static class TypeCast extends Expr {
        public Identifier castedType;
        public Expr expr;

        public TypeCast(Identifier castedType, Expr expr) {
            this.castedType = castedType;
            this.expr = expr;
        }
    }

    public static class FunCall extends Expr {
        public Identifier funcName;
        public List<Expr> args;

        public FunCall(Identifier funcName, List<Expr> args) {
            this.funcName = funcName;
            this.args = args;
        }
    }

    /**
     * Identifier
     */
    public static class Identifier extends Expr {
        public final String name;

        public Symbol symbol;

        public Identifier(String name) {
            this.name = name;
        }

    }

    public static class Literal extends Expr {
        public enum Tag {BOOL, INT,}

        public Tag tag;
        public Object value;

        public Literal(Tag tag, Object value) {
            this.tag = tag;
            this.value = value;
        }
    }

    public static class Factory {
        public interface Context {
            int getTokenStartIndex();

            int getTokenEndIndex();
        }

        Context context;

        public Factory(Context context) {
            this.context = context;
        }

        public Program Program(List<YanTree> defs) {
            var tree = new Program(defs);
            return tree;
        }
    }

    //    @formatter:off
    public interface VoidVisitor extends ReflectiveVisitor {
        default void visitOthers(YanTree that) { }
        default void visit(Program that)       { visitOthers(that);}
        default void visit(VarDef that)        { visitOthers(that); }
        default void visit(ClassDef that)      { visitOthers(that); }
        default void visit(FuncDef that)       { visitOthers(that); }
        default void visit(Return that)        { visitOthers(that); }
        default void visit(If that)            { visitOthers(that); }
        default void visit(While that)         { visitOthers(that); }
        default void visit(Continue that)      { visitOthers(that); }
        default void visit(Break that)         { visitOthers(that); }
        default void visit(ExprStmt that)      { visitOthers(that); }
        default void visit(Block that)         { visitOthers(that); }
        default void visit(Empty that)         { visitOthers(that); }
        default void visit(Operator that)      { visitOthers(that); }
        default void visit(Unary that)         { visitOthers(that); }
        default void visit(Binary that)        { visitOthers(that); }
        default void visit(TypeCast that)      { visitOthers(that); }
        default void visit(FunCall that)       { visitOthers(that); }
        default void visit(Identifier that)    { visitOthers(that); }
        default void visit(Literal that)       { visitOthers(that); }
    }

    //@formatter:off
    public interface Visitor<R> {
        default R visitOthers(YanTree that) { return null; }
        default R visit(Program that)       { return visitOthers(that);}
        default R visit(VarDef that)        { return visitOthers(that); }
        default R visit(ClassDef that)      { return visitOthers(that); }
        default R visit(FuncDef that)       { return visitOthers(that); }
        default R visit(Return that)        { return visitOthers(that); }
        default R visit(If that)            { return visitOthers(that); }
        default R visit(While that)         { return visitOthers(that); }
        default R visit(Continue that)      { return visitOthers(that); }
        default R visit(Break that)         { return visitOthers(that); }
        default R visit(ExprStmt that)      { return visitOthers(that); }
        default R visit(Block that)         { return visitOthers(that); }
        default R visit(Empty that)         { return visitOthers(that); }
        default R visit(Operator that)      { return visitOthers(that); }
        default R visit(Unary that)         { return visitOthers(that); }
        default R visit(Binary that)        { return visitOthers(that); }
        default R visit(TypeCast that)      { return visitOthers(that); }
        default R visit(FunCall that)       { return visitOthers(that); }
        default R visit(Identifier that)    { return visitOthers(that); }
        default R visit(Literal that)       { return visitOthers(that); }
    }

}