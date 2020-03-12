package yan.lang;

import yan.foundation.compiler.frontend.ast.Tree;
import yan.foundation.compiler.frontend.ast.TreeNode;

import java.util.List;

public class YanTree extends Tree {
    public interface YanVisitor<R> {
        /* 默认的处理方法 */
        default R visitOthers(YanTreeNode node) {
            // 什么都不做
            return null;
        }

        default R visit(YanTree.Program program) {
            return visitOthers(program);
        }

        default R visit(YanTree.Function function) {
            return visitOthers(function);
        }

        default R visit(YanTree.Var var) {
            return visitOthers(var);
        }

        default R visit(YanTree.Binary binary) {
            return visitOthers(binary);
        }

    }

    public static abstract class YanTreeNode extends TreeNode {
        public abstract <R> R accept(YanVisitor<R> visitor);
    }


    public static class Program extends YanTreeNode {
        public List<Function> functions;

        public Program(List<Function> functions) {
            this.functions = functions;
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }


    /**
     * <pre>
     *     func
     * </pre>
     */
    public static class Function extends YanTreeNode {

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Var extends YanTreeNode {

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static abstract class Expression extends YanTreeNode {

    }

    public static class Binary extends Expression {

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }


}