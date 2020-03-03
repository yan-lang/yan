package yan.examples.yan.tree;

import yan.skeleton.compiler.frontend.ast.Tree;

import javax.swing.text.Position;
import java.util.List;

public class YanTree extends Tree {
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