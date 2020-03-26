package yan.lang.predefine;

public interface YanVisitor<R> {
    /* 默认的处理方法 */
    default R visitOthers(YanTree node) {
        // 什么都不做
        return null;
    }

    default R visit(YanTree.Program program) {
        return visitOthers(program);
    }

    default R visit(YanTree.VarDef varDef) {
        return visitOthers(varDef);
    }

    default R visit(YanTree.If ifStmt) {
        return visitOthers(ifStmt);
    }

    default R visit(YanTree.ExprStmt exprStmt) {
        return visitOthers(exprStmt);
    }

    default R visit(YanTree.Block block) {
        return visitOthers(block);
    }

    default R visit(YanTree.Print print) {
        return visitOthers(print);
    }

    default R visit(YanTree.Empty empty) {
        return visitOthers(empty);
    }

    default R visit(YanTree.Assign assign) {
        return visitOthers(assign);
    }

    default R visit(YanTree.Binary binary) {
        return visitOthers(binary);
    }

    default R visit(YanTree.Identifier identifier) {
        return visitOthers(identifier);
    }

    default R visit(YanTree.IntConst intConst) {
        return visitOthers(intConst);
    }

    default R visit(YanTree.BoolConst boolConst) {
        return visitOthers(boolConst);
    }
}
