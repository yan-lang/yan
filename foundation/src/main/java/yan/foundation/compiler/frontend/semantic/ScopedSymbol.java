package yan.foundation.compiler.frontend.semantic;

import yan.foundation.compiler.frontend.ast.Tree;

public abstract class ScopedSymbol extends Symbol implements Scope {
    Scope enclosingScope;

    public ScopedSymbol(String name, Tree tree, Scope enclosingScope) {
        super(name, tree);
        this.enclosingScope = enclosingScope;
    }

    public Symbol resolveType(String name) { return resolve(name); }

    public Scope getEnclosingScope() { return enclosingScope; }

    public String getScopeName() { return name; }

}
