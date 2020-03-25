package yan.lang.predefine;

import yan.foundation.compiler.frontend.ast.AbstractTreeNode;
import yan.foundation.compiler.frontend.ast.Tree;

import java.util.List;

public class YanTree extends Tree {

    public static abstract class YanTreeNode extends AbstractTreeNode {
        public abstract <R> R accept(YanVisitor<R> visitor);
    }

    public interface YanVisitor<R> {
        /* 默认的处理方法 */
        default R visitOthers(YanTreeNode node) {
            // 什么都不做
            return null;
        }

        default R visit(YanTree.Program program) {
            return visitOthers(program);
        }

        default R visit(YanTree.VarDef varDef) {
            return visitOthers(varDef);
        }

        default R visit(YanTree.If ifStmt) {
            return visitOthers(ifStmt);
        }

        default R visit(YanTree.ExprStmt exprStmt) {
            return visitOthers(exprStmt);
        }

        default R visit(YanTree.Block block) {
            return visitOthers(block);
        }

        default R visit(YanTree.Print print) {
            return visitOthers(print);
        }

        default R visit(YanTree.Empty empty) {
            return visitOthers(empty);
        }

        default R visit(YanTree.Assign assign) {
            return visitOthers(assign);
        }

        default R visit(YanTree.Binary binary) {
            return visitOthers(binary);
        }

        default R visit(YanTree.Identifier identifier) {
            return visitOthers(identifier);
        }

        default R visit(YanTree.IntConst intConst) {
            return visitOthers(intConst);
        }

        default R visit(YanTree.BoolConst boolConst) {
            return visitOthers(boolConst);
        }
    }

    public static final class Program extends YanTreeNode {
        public final List<Stmt> stmts;

        public Program(List<Stmt> stmts) {
            this.stmts = stmts;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static abstract class Stmt extends YanTreeNode {
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
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
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
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
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
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static final class Block extends Stmt {
        public final List<Stmt> stmts;

        public Block(List<Stmt> stmts) {
            this.stmts = stmts;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public final static class Print extends Stmt {
        public final Expr expr;

        public Print(Expr expr) {
            this.expr = expr;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public final static class Empty extends Stmt {
        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static abstract class Expr extends YanTreeNode {
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
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
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
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static final class BinaryOp extends AbstractTreeNode {
        enum Type {PLUS, MINUS, MULTI, DIV, EXP, EQUAL, LARGE, LESS}

        public final Type type;

        public BinaryOp(Type type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type.toString();
        }
    }

    public static final class Identifier extends Expr {
        public final String name;

        public Identifier(String name) {
            this.name = name;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static final class IntConst extends Expr {
        public final Integer value;

        public IntConst(Integer value) {
            this.value = value;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static final class BoolConst extends Expr {
        public final boolean value;

        public BoolConst(boolean value) {
            this.value = value;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

}