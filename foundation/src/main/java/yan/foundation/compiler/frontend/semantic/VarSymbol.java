package yan.foundation.compiler.frontend.semantic;

import yan.foundation.compiler.frontend.ast.Tree;

public class VarSymbol extends Symbol {
    public Type type;

    public VarSymbol(String name, Tree tree) {
        super(name, tree);
    }

    public VarSymbol(String name, Tree tree, Type type) {
        super(name, tree);
        this.type = type;
    }
}
