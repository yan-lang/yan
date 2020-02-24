package yan.examples.yan.tree;

import yan.skeleton.compiler.frontend.ast.Position;
import yan.skeleton.compiler.frontend.ast.Tree;

import java.util.List;

public class YanTree extends Tree {
    public static class Program extends YanTreeNode {
        public List<Function> functions;

        public Program(Position pos, List<Function> functions) {
            super(pos);
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
        public Function(Position pos) {
            super(pos);
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static class Var extends YanTreeNode {
        public Var(Position pos) {
            super(pos);
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    public static abstract class Expression extends YanTreeNode {
        public Expression(Position pos) {
            super(pos);
        }
    }

    public static class Binary extends Expression {
        public Binary(Position pos) {
            super(pos);
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }


}