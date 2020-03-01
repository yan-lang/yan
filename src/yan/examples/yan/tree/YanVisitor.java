package yan.examples.yan.tree;

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

    default R visit(YanTree.Var var) { return visitOthers(var); }

    default R visit(YanTree.Binary binary) { return visitOthers(binary); }

}
