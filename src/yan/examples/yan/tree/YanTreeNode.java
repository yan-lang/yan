package yan.examples.yan.tree;

import yan.skeleton.compiler.frontend.ast.TreeNode;

public abstract class YanTreeNode extends TreeNode {
    public abstract <R> R accept(YanVisitor<R> visitor);
}
