package yan.foundation.compiler.frontend.semantic.v1;

import yan.foundation.compiler.frontend.ast.Tree;

public class Symbol {
    public String name;
    public Scope scope;
    public Tree tree;

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, Tree tree) {
        this.name = name;
        this.tree = tree;
    }

    public String kind() {
        return getClass().getSimpleName().toLowerCase();
    }
}
