package yan.skeleton.compiler.frontend.ast;

/**
 * {@code TreeNode}是语法树节点的基类，提供常见的数据结构与方法。
 * 当你在定义特定语言语法树时，树中的节点都应继承该类。
 */
public abstract class TreeNode {
    public final Position pos;

    public TreeNode(Position pos) {
        this.pos = pos;
    }

}
