package yan.foundation.compiler.frontend.ast;

import yan.foundation.compiler.frontend.semantic.symbol.Scope;
import yan.foundation.compiler.frontend.semantic.symbol.Symbol;
import yan.foundation.compiler.frontend.semantic.symbol.Type;

/**
 * {@code TreeNode}是语法树节点的基类，提供常见的数据结构与方法。
 * 当你在定义特定语言语法树时，树中的节点都应继承该类。
 */
public abstract class Tree {
    // 语法分析时需要初始化
    public Range range;

    // 建立符号表时需要初始化
    public Symbol symbol;
    public Scope scope;

    // 类型检查时初始化
    public Type evalType;

    public <T> T setRange(Range range) {
        this.range = range;
        return (T) this;
    }

    public <T> T setRange(int from, int to) {
        return setRange(new Range(from, to));
    }

    public void accept(ReflectiveVisitor v) {
        v.dispatch(this);
    }
}
