package yan.examples.yan;

import yan.skeleton.compiler.frontend.ast.Position;
import yan.skeleton.compiler.frontend.ast.Tree;
import yan.skeleton.compiler.frontend.ast.TreeNode;

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

    public static class Function extends YanTreeNode {

        public Function(Position pos) {
            super(pos);
        }

        @Override
        public <R> R accept(YanVisitor<R> visitor) {
            return visitor.visit(this);
        }
    }


}