package yan.foundation.compiler.frontend.semantic;

import java.util.Map;

public interface Scope {
    String getScopeName();

    Scope getEnclosingScope();

    Map<String, Symbol> getMembers();

    default void define(Symbol sym) {
        getMembers().put(sym.name, sym);
        sym.scope = this; // track the scope in each symbol
    }

    default Symbol resolve(String name) {
        Symbol s = getMembers().get(name);
        if (s != null) return s;
        // if not here, check any enclosing scope
        if (getEnclosingScope() != null) return getEnclosingScope().resolve(name);
        return null; // not found
    }

    /**
     * Get the symbol in the current scope by its name
     *
     * @param name name
     * @return the founded symbol, null if not founded
     */
    default Symbol find(String name) {
        return getMembers().get(name);
    }
}
