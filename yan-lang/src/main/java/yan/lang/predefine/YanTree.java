package yan.lang.predefine;

import yan.foundation.compiler.frontend.ast.Tree;

import java.util.List;

public abstract class YanTree extends Tree {

    public abstract <R> R accept(YanVisitor<R> visitor);

    public abstract void accept(Visitor visitor);

    public static final class Program extends YanTree {
        public final List<Stmt> stmts;

        public Program(List<Stmt> stmts) {
            this.stmts = stmts;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public static abstract class Stmt extends YanTree {
    }

    /**
     * Variable Definition
     * <pre>
     *     'var' identifier '=' expression
     * </pre>
     */
    public static final class VarDef extends Stmt {
        public final Identifier identifier;
        public final Expr initializer;

        public VarDef(Identifier identifier, Expr initializer) {
            this.identifier = identifier;
            this.initializer = initializer;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public static final class If extends Stmt {
        public final Expr condition;
        public final Block ifBody;
        public final Block elseBody;

        public If(Expr condition, Block ifBody, Block elseBody) {
            this.condition = condition;
            this.ifBody = ifBody;
            this.elseBody = elseBody;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    /**
     * Expression statement
     * <pre>
     *     expression
     * </pre>
     */
    public static final class ExprStmt extends Stmt {
        public final Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public static final class Block extends Stmt {
        public final List<Stmt> stmts;

        public Block(List<Stmt> stmts) {
            this.stmts = stmts;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public final static class Print extends Stmt {
        public final Expr expr;

        public Print(Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public final static class Empty extends Stmt {
        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public static abstract class Expr extends YanTree {
    }

    /**
     * Assign expression
     * identifier '=' expression
     */
    public static final class Assign extends Expr {
        public final Identifier identifier;
        public final Expr expr;

        public Assign(Identifier identifier, Expr expr) {
            this.identifier = identifier;
            this.expr = expr;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    /**
     * Binary Expression
     * <pre>
     *     expression op expression
     * </pre>
     */
    public static final class Binary extends Expr {
        public final Expr left;
        public final Expr right;
        public final BinaryOp op;

        public Binary(Expr left, BinaryOp op, Expr right) {
            this.left = left;
            this.right = right;
            this.op = op;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public static final class BinaryOp extends Tree {
        enum Type {PLUS, MINUS, MULTI, DIV, EXP, EQUAL, LARGE, LESS}

        public final Type type;

        public BinaryOp(Type type) {
            this.type = type;
        }

        @Override
        public String toString() { return type.toString(); }
    }

    public static final class Identifier extends Expr {
        public final String name;

        public Identifier(String name) {
            this.name = name;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public static final class IntConst extends Expr {
        public final Integer value;

        public IntConst(Integer value) {
            this.value = value;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public static final class BoolConst extends Expr {
        public final boolean value;

        public BoolConst(boolean value) {
            this.value = value;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) { return visitor.visit(this); }

        @Override
        public void accept(Visitor visitor) { visitor.visit(this); }
    }

    public interface Visitor {
        default void visitOthers(YanTree node) { }

        default void visit(Program program) { visitOthers(program);}

        default void visit(VarDef varDef) { visitOthers(varDef); }

        default void visit(If ifStmt) { visitOthers(ifStmt); }

        default void visit(ExprStmt exprStmt) { visitOthers(exprStmt); }

        default void visit(Block block) { visitOthers(block); }

        default void visit(Print print) { visitOthers(print); }

        default void visit(Empty empty) { visitOthers(empty); }

        default void visit(Assign assign) { visitOthers(assign); }

        default void visit(Binary binary) { visitOthers(binary); }

        default void visit(Identifier identifier) { visitOthers(identifier); }

        default void visit(IntConst intConst) { visitOthers(intConst); }

        default void visit(BoolConst boolConst) { visitOthers(boolConst); }
    }
}