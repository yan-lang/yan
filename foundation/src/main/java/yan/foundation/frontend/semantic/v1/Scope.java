package yan.foundation.frontend.semantic.v1;

import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {
    String name;
    Scope enclosingScope;
    Map<String, Symbol> symbols = new LinkedHashMap<>();

    public Scope(Scope enclosingScope) {
        this("scope", enclosingScope);
    }

    public Scope(String name, Scope enclosingScope) {
        this.name = name;
        this.enclosingScope = enclosingScope;
    }

    public void define(Symbol sym) {
        getMembers().put(sym.name, sym);
    }

    public Symbol resolve(String name) {
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
    public Symbol resolveLocally(String name) {
        return getMembers().get(name);
    }

    public boolean declaredLocally(String name) {
        return getMembers().containsKey(name);
    }

    public String getScopeName() { return name; }

    public Scope getEnclosingScope() { return enclosingScope; }

    public Map<String, Symbol> getMembers() { return symbols; }
}
