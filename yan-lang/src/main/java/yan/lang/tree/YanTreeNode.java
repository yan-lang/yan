package yan.lang.tree;

import yan.foundation.compiler.frontend.ast.TreeNode;

public abstract class YanTreeNode extends TreeNode {
    public abstract <R> R accept(YanVisitor<R> visitor);
}
