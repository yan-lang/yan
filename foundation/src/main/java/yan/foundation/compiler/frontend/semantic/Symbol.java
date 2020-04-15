package yan.foundation.compiler.frontend.semantic;

import yan.foundation.compiler.frontend.ast.Tree;

public class Symbol {
    public String name; // All Symbols have a name.
    public Tree tree;   // The corresponding tree for this symbol,
    public Scope scope; // Scope that contains this symbol

    public Symbol(String name, Tree tree) {
        this.name = name;
        this.tree = tree;
    }
}
