package yan.lang;

import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.compiler.frontend.ast.TreeNode;

import java.util.List;

public class YanTree extends Tree {
    public enum BinaryOp {
        PLUS, MINUS, MULTI, DIV, EXP
    }

    public static abstract class YanTreeNode extends TreeNode {
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

        default R visit(YanTree.ExprStmt exprStmt) {
            return visitOthers(exprStmt);
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

    }

    public static class Program extends YanTreeNode {
        public List<Stmt> stmts;

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
    public static class VarDef extends Stmt {
        Identifier identifier;
        Expr initializer;

        public VarDef(Identifier identifier, Expr initializer) {
            this.identifier = identifier;
            this.initializer = initializer;
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
    public static class ExprStmt extends Stmt {
        Expr expr;

        public ExprStmt(Expr expr) {
            this.expr = expr;
        }

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
    public static class Assign extends Expr {
        Identifier identifier;
        Expr expr;

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
    public static class Binary extends Expr {
        Expr left;
        Expr right;
        BinaryOp op;

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

    public static class Identifier extends Expr {
        String name;

        public Identifier(String name) {
            this.name = name;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static class IntConst extends Expr {
        Integer value;

        public IntConst(Integer value) {
            this.value = value;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }
}