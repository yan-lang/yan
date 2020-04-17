package yan.foundation.compiler.frontend.ast;

import yan.foundation.compiler.frontend.lex.Token;
import yan.foundation.compiler.frontend.semantic.Scope;
import yan.foundation.compiler.frontend.semantic.Symbol;
import yan.foundation.compiler.frontend.semantic.Type;

/**
 * {@code TreeNode}是语法树节点的基类，提供常见的数据结构与方法。
 * 当你在定义特定语言语法树时，树中的节点都应继承该类。
 */
public abstract class Tree {

    // 语法分析时需要初始化, 分别标记了当前这个语法树的第一个Token和最后一个Token.
    public Token start, end;

    /* 部分语法树需要填symbol和scope */

    // 建立符号表时需要初始化
    public Symbol symbol;
    public Scope scope;

    // 类型检查时初始化
    public Type evalType;


    public Range range() {
        if (start == null || end == null)
            return Range.INVALID;
        return new Range(start.index, end.index);
    }

    public void accept(ReflectiveVisitor v) {
        v.dispatch(this);
    }
}
